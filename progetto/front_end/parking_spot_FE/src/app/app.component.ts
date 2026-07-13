import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { KeycloakService } from './keycloak.service';
import { ParkingApiService } from './parking-api.service';
import { Prenotazione, ProfiloUtente, Veicolo } from './api.models';

type SezioneDashboard = 'prenotazioni' | 'profilo';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  profilo?: ProfiloUtente;
  veicoli: Veicolo[] = [];
  prenotazioni: Prenotazione[] = [];
  sezioneAttiva: SezioneDashboard = 'prenotazioni';
  messaggio = '';
  errore = '';

  profiloForm = { codiceFiscale: '', nome: '', cognome: '' };
  veicoloForm = { targa: '', marca: '', modello: '' };

  constructor(
    public readonly keycloak: KeycloakService,
    private readonly api: ParkingApiService
  ) {}

  ngOnInit(): void {
    if (this.keycloak.isLoggedIn()) this.caricaDatiPrivati();
  }

  login(): void { this.keycloak.login(); }
  registrati(): void { this.keycloak.register(); }
  logout(): void { this.keycloak.logout(); }

  cambiaSezione(sezione: SezioneDashboard): void {
    this.sezioneAttiva = sezione;
    this.messaggio = '';
    this.errore = '';
  }

  caricaDatiPrivati(): void {
    this.api.getProfilo().subscribe({
      next: profilo => {
        this.profilo = profilo;
        this.profiloForm = { codiceFiscale: profilo.codiceFiscale, nome: profilo.nome, cognome: profilo.cognome };
        this.caricaVeicoli();
        this.caricaPrenotazioni();
      },
      error: () => {
        this.sezioneAttiva = 'profilo';
        this.messaggio = 'Completa il tuo profilo per iniziare.';
      }
    });
  }

  salvaProfilo(): void {
    this.api.salvaProfilo(this.profiloForm).subscribe({
      next: profilo => {
        this.profilo = profilo;
        this.messaggio = 'Profilo salvato.';
        this.caricaVeicoli();
        this.caricaPrenotazioni();
      },
      error: err => this.mostraErrore(err)
    });
  }

  caricaVeicoli(): void {
    this.api.getVeicoli().subscribe({ next: data => this.veicoli = data, error: err => this.mostraErrore(err) });
  }

  creaVeicolo(): void {
    this.api.creaVeicolo(this.veicoloForm).subscribe({
      next: () => {
        this.messaggio = 'Veicolo inserito.';
        this.veicoloForm = { targa: '', marca: '', modello: '' };
        this.caricaVeicoli();
      },
      error: err => this.mostraErrore(err)
    });
  }

  cancellaVeicolo(id: number): void {
    this.api.cancellaVeicolo(id).subscribe({ next: () => this.caricaVeicoli(), error: err => this.mostraErrore(err) });
  }

  caricaPrenotazioni(): void {
    this.api.getPrenotazioni().subscribe({ next: data => this.prenotazioni = data, error: err => this.mostraErrore(err) });
  }

  cancellaPrenotazione(id: number): void {
    this.api.cancellaPrenotazione(id).subscribe({ next: () => this.caricaPrenotazioni(), error: err => this.mostraErrore(err) });
  }

  private mostraErrore(err: unknown): void {
    const erroreHttp = err as { error?: { message?: string }; message?: string };
    this.errore = erroreHttp.error?.message || erroreHttp.message || 'Errore imprevisto';
    this.messaggio = '';
  }
}
