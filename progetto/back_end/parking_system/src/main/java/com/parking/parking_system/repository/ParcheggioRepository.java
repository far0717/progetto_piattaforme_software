package com.parking.parking_system.repository;

import com.parking.parking_system.entity.Parcheggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcheggioRepository extends JpaRepository<Parcheggio, Long> {
    Parcheggio findByNumero(int numero);

    //in JpaRepository esiste in metodo findById però il tipo di ritorno è
    //Optional<Parcheggio>, così ho definito il metodo Parcheggio findParcheggioById(Long id)
    Parcheggio findParcheggioById(Long id);

}
