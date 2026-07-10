package com.parking.parking_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prenotazione")
@Getter
@Setter
@NoArgsConstructor
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(name = "orario_inizio", nullable = false)
    private LocalDateTime orarioInizio;

    @Column(name = "orario_fine", nullable = false)
    private LocalDateTime orarioFine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veicolo_id", nullable = false)
    private Veicolo veicolo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcheggio_id", nullable = false)
    private Parcheggio parcheggio;
}
