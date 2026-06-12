package com.parking.parking_system.repository;

import com.parking.parking_system.entity.Parcheggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcheggioRepository extends JpaRepository<Parcheggio, Long> {
    Parcheggio findByNumero(int numero);

    //in JpaRepository esiste in metodo findById però il tipo di ritorno è
    //Optional<Parcheggio>, così ho definito il metodo Parcheggio findParcheggioById(Long id)
    //non usando Optional<Parcheggio> devo poi controllare io a mano che il Parcheggio non sia null
    Parcheggio findParcheggioById(Long id);

    List<Parcheggio> findByDisponibileTrue();

}
