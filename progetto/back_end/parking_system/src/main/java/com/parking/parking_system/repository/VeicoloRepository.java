package com.parking.parking_system.repository;

import com.parking.parking_system.entity.Utente;
import com.parking.parking_system.entity.Veicolo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VeicoloRepository extends JpaRepository<Veicolo, Long> {
    List<Veicolo> findByUtenteOrderByTargaAsc(Utente utente);
    Optional<Veicolo> findByIdAndUtente(Long id, Utente utente);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Veicolo v where v.id = :id and v.utente = :utente")
    Optional<Veicolo> findByIdAndUtenteForUpdate(@Param("id") Long id, @Param("utente") Utente utente);

    boolean existsByTarga(String targa);
}
