package com.parking.parking_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "veicolo")
@Getter
@Setter
@NoArgsConstructor
public class Veicolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 20)
    private String targa;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String marca;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String modello;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @OneToMany(mappedBy = "veicolo")
    private List<Prenotazione> prenotazioni = new ArrayList<>();
}
