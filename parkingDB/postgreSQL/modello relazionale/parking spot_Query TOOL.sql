

CREATE TABLE utente (
                        id BIGSERIAL PRIMARY KEY,
                        keycloak_id VARCHAR(100) UNIQUE NOT NULL,
                        codice_fiscale VARCHAR(16) UNIQUE NOT NULL,
                        nome VARCHAR(50) NOT NULL,
                        cognome VARCHAR(50) NOT NULL,
                        email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE veicolo (
                         id BIGSERIAL PRIMARY KEY,
                         targa VARCHAR(20) UNIQUE NOT NULL,
                         marca VARCHAR(50) NOT NULL,
                         modello VARCHAR(50) NOT NULL,
                         utente_id BIGINT NOT NULL,
                         FOREIGN KEY (utente_id) REFERENCES utente(id)
);

CREATE TABLE parcheggio (
                            id BIGSERIAL PRIMARY KEY,
                            numero INTEGER UNIQUE NOT NULL,
                            disponibile BOOLEAN DEFAULT TRUE NOT NULL,
                            version BIGINT
);

CREATE TABLE prenotazione (
                              id BIGSERIAL PRIMARY KEY,
                              data DATE NOT NULL,
                              orario_inizio TIMESTAMP NOT NULL,
                              orario_fine TIMESTAMP NOT NULL,
                              utente_id BIGINT NOT NULL,
                              veicolo_id BIGINT NOT NULL,
                              parcheggio_id BIGINT NOT NULL,
                              FOREIGN KEY (utente_id) REFERENCES utente(id),
                              FOREIGN KEY (veicolo_id) REFERENCES veicolo(id),
                              FOREIGN KEY (parcheggio_id) REFERENCES parcheggio(id)
);

CREATE INDEX idx_prenotazione_parcheggio_orari
    ON prenotazione(parcheggio_id, orario_inizio, orario_fine);

CREATE INDEX idx_prenotazione_veicolo_orari
    ON prenotazione(veicolo_id, orario_inizio, orario_fine);
