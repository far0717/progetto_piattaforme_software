package com.parking.parking_system.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "veicolo")
public class Veicolo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String targa;


    @Column(nullable = false,length = 50)
    private String marca;


    @Column(nullable = false,length = 50)
    private String modello;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    //il veicolo ha un utente, qui definisco la FK
    private Utente utente;

    @OneToMany(mappedBy = "veicolo")
    private List<Prenotazione> prenotazioni;
}
