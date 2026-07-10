package com.parking.parking_system.support.configuration.data;

import com.parking.parking_system.entity.Parcheggio;
import com.parking.parking_system.repository.ParcheggioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    public static final int TOTALE_POSTI = 87;

    private final ParcheggioRepository parcheggioRepository;

    @Bean
    CommandLineRunner initParcheggi() {
        return args -> {
            for (int i = 1; i <= TOTALE_POSTI; i++) {
                if (!parcheggioRepository.existsByNumero(i)) {
                    Parcheggio p = new Parcheggio();
                    p.setNumero(i);
                    p.setDisponibile(true);
                    parcheggioRepository.save(p);
                }
            }
        };
    }
}
