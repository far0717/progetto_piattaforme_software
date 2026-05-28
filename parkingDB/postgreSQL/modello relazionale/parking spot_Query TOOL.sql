-- Ho aggiunto degli id aritificiali per ottimizzare l'uso del DB da Spring,
-- però nel modello ER non li ho inseriti, pk nel modello ER si usano gli identificatori naturali 
-- del dominio applicativo, es. la targa è l'identificativo del veicolo ma io uso un id artificiale
CREATE TABLE utente (
    id BIGSERIAL PRIMARY KEY,
    codice_fiscale VARCHAR(16) UNIQUE NOT NULL,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100) NOT NULL,
    abbonato BOOLEAN DEFAULT FALSE
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
    disponibile BOOLEAN DEFAULT TRUE
);


CREATE TABLE prenotazione (
    id BIGSERIAL PRIMARY KEY,

    data DATE,
    orario_inizio TIMESTAMP,
    orario_fine TIMESTAMP,

    utente_id BIGINT NOT NULL,
    veicolo_id BIGINT NOT NULL,
    parcheggio_id BIGINT NOT NULL,

    FOREIGN KEY (utente_id) REFERENCES utente(id),
    FOREIGN KEY (veicolo_id) REFERENCES veicolo(id),
    FOREIGN KEY (parcheggio_id) REFERENCES parcheggio(id)
);