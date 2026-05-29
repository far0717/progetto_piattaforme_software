package com.parking.parking_system.repository;

import com.parking.parking_system.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

/*
estendendo JpaRepository Spring mi da le operazioni CRUD base:save(),findById(),findAll(),deleteById(),delete() ed anche le query avanzate
<Utente,Long> devo indicare l'Entity su cui usare i metodi del UtenteRepository, e devo indicare il tipo della chiave primaria
di Utente annotata con @Id, in questo caso il tipo è Long. JpaRepository<Entity, Id >
Per query complesse devo usare @Query di Spring Data JPA,permette di scrivere query manuali (JPQL(default) o SQL) dentro i repository,
ma per quelle semplici posso sfruttare il Spring Data JPA Query Methods, come per il metodo existsByEmail,
io definisco il metodo seguendo questo formato:<operation>By<property><condition><logicalOperator><property><condition>,
es: findByNomeContainingAndCognomeContainingOrEmailContaining, ma la implementazione la crea Spring e non la mostra.

 */
public interface UtenteRepository extends JpaRepository< Utente, Long > {
    boolean existsByEmail(String email);
}