import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import {
  Parcheggio,
  Prenotazione,
  PrenotazioneRequest,
  ProfiloUtente,
  ProfiloUtenteRequest,
  Veicolo,
  VeicoloRequest,
} from "./api.models";

// servizio singleton
@Injectable({ providedIn: "root" })
export class ParkingApiService {
  private readonly baseUrl = "/api"; //tutti gli endpoint iniziano con /api e grazie al proxy viene inoltrato alla porta 8081
  //pk nel target del proxy.conf.json ho messo 8081

  constructor(private readonly http: HttpClient) {}

  getDisponibilitaParcheggi(
    orarioInizio: string,
    orarioFine: string,
  ): Observable<Parcheggio[]> {
    const params = new HttpParams()
      .set("orarioInizio", orarioInizio)
      .set("orarioFine", orarioFine);

    return this.http.get<Parcheggio[]>(
      `${this.baseUrl}/parcheggi/disponibilita`,
      { params },
    );
  }

  getProfilo(): Observable<ProfiloUtente> {
    return this.http.get<ProfiloUtente>(`${this.baseUrl}/utenti/me`);
  }

  salvaProfilo(request: ProfiloUtenteRequest): Observable<ProfiloUtente> {
    return this.http.post<ProfiloUtente>(`${this.baseUrl}/utenti/me`, request);
  }

  getVeicoli(): Observable<Veicolo[]> {
    return this.http.get<Veicolo[]>(`${this.baseUrl}/veicoli`);
  }

  creaVeicolo(request: VeicoloRequest): Observable<Veicolo> {
    return this.http.post<Veicolo>(`${this.baseUrl}/veicoli`, request);
  }

  cancellaVeicolo(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/veicoli/${id}`);
  }

  getPrenotazioni(): Observable<Prenotazione[]> {
    return this.http.get<Prenotazione[]>(`${this.baseUrl}/prenotazioni`);
  }

  creaPrenotazione(request: PrenotazioneRequest): Observable<Prenotazione> {
    return this.http.post<Prenotazione>(
      `${this.baseUrl}/prenotazioni`,
      request,
    );
  }

  cancellaPrenotazione(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/prenotazioni/${id}`);
  }
}
