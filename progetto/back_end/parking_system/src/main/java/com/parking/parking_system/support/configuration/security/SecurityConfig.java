package com.parking.parking_system.support.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration //questa annotazione indica che la classe ha metodi che creano oggetti Bean che Spring deve poi gestire
public class SecurityConfig {

    @Bean //questa annotazione indica a Spring che il metodo annotato produce un oggetto
    // che Spring deve registrare e gestire nel container IoC(Inversion of Control) come bean
    //IoC vuol dire che non sono più io developer a creare e gestire gli oggetti, ma fa tutto Spring, non faccio più new
    //ma uso alternative come field injection + @Autowired, oppure Lombok che fa Constructor Injection
    public PasswordEncoder passwordEncoder() {
        //BCryptPasswordEncoder restituisce un hash della password, e nel DB salvo l'hash e non la password vera
        return new BCryptPasswordEncoder();
    }
}
