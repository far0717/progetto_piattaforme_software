package com.parking.parking_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//come prima cosa sono andato su https://start.spring.io/
//da li ho impostato il file Spring Boot e tutti parametri e dipendenze, dal sito ho scaricato un .zip del progetto
//e su quello ho creato Controller,Service,Repository,Entity, però questa classe era già generata
/*
Questa classe è il punto di avvio del programma: quando la eseguo,
Spring Boot si attiva,configura automaticamente tutto il progetto e avvia il server web
annotando la classe con @SpringBootApplication spring configura automaticamente tutto, cerca da solo controllers,
services,repositories,entities.

*/
@SpringBootApplication
public class ParkingSystemApplication {

	public static void main(String[] args) {
		/*
		 * SpringApplication.run(...) avvia l’applicazione Spring Boot, inizializza il contesto Spring e quindi permette
		 * la Dependency Injection tramite i container, poi avvia il server web(Tomcat), fa lo scan delle componenti annotate: @Controller, @Service, @Repository, @Entity
		 * configura automaticamente il progetto in base alle dipendenze, configura la connessione al database (nel mio caso uso PostgreSQL, tramite application.properties)
		 * Alla fine del processo l’applicazione espone gli endpoint REST definiti nei controller,
		 * che possono essere consumati dal Front-end tramite richieste HTTP,userò Angular per il Front-end.
		 */
		SpringApplication.run(ParkingSystemApplication.class, args);
	}

}
