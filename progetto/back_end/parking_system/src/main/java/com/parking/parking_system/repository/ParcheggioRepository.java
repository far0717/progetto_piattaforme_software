package com.parking.parking_system.repository;

import com.parking.parking_system.entity.Parcheggio;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParcheggioRepository extends JpaRepository<Parcheggio, Long> {
    Optional<Parcheggio> findByNumero(int numero);

    //questo lock pessimistico impedisce ad un'altra transazione di modificare
    //il parcheggio che già sta venendo modificato da una transazione
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Parcheggio p where p.id = :id")
    Optional<Parcheggio> findByIdForUpdate(@Param("id") Long id);
    //@Param("id") collega il parametro id al parametro della query :id

    boolean existsByNumero(int numero);

    //trova tutti i parcheggi con disponibile = true e ordinali per numero crescente
    List<Parcheggio> findByDisponibileTrueOrderByNumeroAsc();

    List<Parcheggio> findAllByOrderByNumeroAsc();
}
