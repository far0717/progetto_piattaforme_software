/*
qui definisco tutte le interfaccie che descrivono le strutture dati scambiate con il BackEnd
, le interfaccie TypeScript non creano oggetti durante l'esecuzione,ma servono solo al
compilatore per controllare che i campi siano corretti
 */

export interface Parcheggio {
  id: number;
  numero: number;
  disponibile: boolean;
}

export interface ProfiloUtenteRequest {
  codiceFiscale: string;
  nome: string;
  cognome: string;
}

export interface ProfiloUtente extends ProfiloUtenteRequest {
  id: number;
  keycloakId: string;
  email: string;
}

export interface VeicoloRequest {
  targa: string;
  marca: string;
  modello: string;
}

export interface Veicolo extends VeicoloRequest {
  id: number;
  utenteId: number;
}

export interface PrenotazioneRequest {
  veicoloId: number;
  parcheggioId: number;
  orarioInizio: string;
  orarioFine: string;
}

export interface Prenotazione {
  id: number;
  data: string;
  orarioInizio: string;
  orarioFine: string;
  utenteId: number;
  utenteEmail: string;
  veicoloId: number;
  targa: string;
  parcheggioId: number;
  numeroParcheggio: number;
}
