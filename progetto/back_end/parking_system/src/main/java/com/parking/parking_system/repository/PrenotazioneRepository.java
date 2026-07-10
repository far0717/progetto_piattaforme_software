package com.parking.parking_system.repository;

import com.parking.parking_system.entity.Parcheggio;
import com.parking.parking_system.entity.Prenotazione;
import com.parking.parking_system.entity.Utente;
import com.parking.parking_system.entity.Veicolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByUtenteOrderByOrarioInizioDesc(Utente utente);
    List<Prenotazione> findAllByOrderByOrarioInizioDesc();
    Optional<Prenotazione> findByIdAndUtente(Long id, Utente utente);
    boolean existsByParcheggio(Parcheggio parcheggio);
    boolean existsByVeicolo(Veicolo veicolo);

    @Query("""
            select case when count(p) > 0 then true else false end
            from Prenotazione p
            where p.parcheggio.id = :parcheggioId
              and p.orarioInizio < :orarioFine
              and p.orarioFine > :orarioInizio
            """)
    boolean existsSovrapposizioneParcheggio(
            @Param("parcheggioId") Long parcheggioId,
            @Param("orarioInizio") LocalDateTime orarioInizio,
            @Param("orarioFine") LocalDateTime orarioFine
    );

    @Query("""
            select case when count(p) > 0 then true else false end
            from Prenotazione p
            where p.veicolo.id = :veicoloId
              and p.orarioInizio < :orarioFine
              and p.orarioFine > :orarioInizio
            """)
    boolean existsSovrapposizioneVeicolo(
            @Param("veicoloId") Long veicoloId,
            @Param("orarioInizio") LocalDateTime orarioInizio,
            @Param("orarioFine") LocalDateTime orarioFine
    );
}
