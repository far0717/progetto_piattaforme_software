package com.parking.parking_system.support.validation;

import com.parking.parking_system.support.eccezioni.OperazioneNonConsentitaException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class PrenotazioneValidator {

    public void validaIntervallo(LocalDateTime orarioInizio, LocalDateTime orarioFine) {
        if (orarioInizio == null || orarioFine == null) {
            throw new OperazioneNonConsentitaException("La fascia oraria è obbligatoria");
        }

        if (orarioInizio.isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new OperazioneNonConsentitaException("L'orario di inizio non può essere nel passato");
        }

        if (!orarioFine.isAfter(orarioInizio)) {
            throw new OperazioneNonConsentitaException(
                    "L'orario di fine deve essere successivo all'orario di inizio");
        }

        if (orarioInizio.getMinute() != 0 || orarioInizio.getSecond() != 0 || orarioInizio.getNano() != 0
                || orarioFine.getMinute() != 0 || orarioFine.getSecond() != 0 || orarioFine.getNano() != 0) {
            throw new OperazioneNonConsentitaException(
                    "Le prenotazioni devono iniziare e finire a orari interi, per esempio 09:00-10:00");
        }

        Duration durata = Duration.between(orarioInizio, orarioFine);
        if (durata.toMinutes() < 60) {
            throw new OperazioneNonConsentitaException("La prenotazione deve durare almeno 1 ora");
        }

        if (durata.toMinutes() % 60 != 0) {
            throw new OperazioneNonConsentitaException("La durata deve essere composta da slot interi di 1 ora");
        }

        if (durata.toHours() > 12) {
            throw new OperazioneNonConsentitaException("La prenotazione non può superare 12 ore");
        }
    }
}
