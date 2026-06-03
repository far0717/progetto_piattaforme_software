package com.parking.parking_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="parcheggio")
public class Parcheggio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name="numero", nullable = false, unique = true)
    @Min(value = 1, message = "Il numero del parcheggio deve essere maggiore di 0")
    private int numero;

    @Column(name="disponibile", nullable = false)
    private boolean disponibile;

    public void occupaParcheggio() {
        this.disponibile = false; }
    public void liberaParcheggio() {
        this.disponibile = true; }
}
