import { CommonModule,DatePipe } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { KeycloakService } from "./keycloak.service";
import { ParkingApiService } from "./parking-api.service";
import { Parcheggio, Prenotazione, ProfiloUtente, Veicolo } from "./api.models";

type SezioneDashboard = "prenotazioni" | "profilo";

@Component({
  selector: "app-root",
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe],
  templateUrl: "./app.component.html",
  styleUrl: "./app.component.css",
})
export class AppComponent implements OnInit {
  parcheggi: Parcheggio[] = [];
  veicoli: Veicolo[] = [];
  prenotazioni: Prenotazione[] = [];
  profilo?: ProfiloUtente;
  sezioneAttiva: SezioneDashboard = "prenotazioni";
  fasciaCaricata = false;
  caricamentoDisponibilita = false;
  messaggio = "";
  errore = "";
  readonly orariDisponibili = Array.from(
    { length: 17 },
    (_, i) => `${String(i + 6).padStart(2, "0")}:00`,
  );
  readonly durateDisponibili = Array.from({ length: 12 }, (_, i) => i + 1);
  profiloForm = { codiceFiscale: "", nome: "", cognome: "" };
  veicoloForm = { targa: "", marca: "", modello: "" };
  slotForm = this.defaultSlot();
  prenotazioneForm = { veicoloId: 0 };
  constructor(
    public readonly keycloak: KeycloakService,
    private readonly api: ParkingApiService,
  ) {}
  ngOnInit(): void {
    if (this.keycloak.isLoggedIn()) this.caricaDatiPrivati();
  }
  get parcheggiOrdinati(): Parcheggio[] {
    return [...this.parcheggi].sort((a, b) => a.numero - b.numero);
  }
  get orarioInizioSelezionato(): string {
    return `${this.slotForm.data}T${this.slotForm.oraInizio}:00`;
  }
  get orarioFineSelezionato(): string {
    const d = this.parseLocalDateTime(this.orarioInizioSelezionato);
    d.setHours(d.getHours() + Number(this.slotForm.durataOre));
    return this.formatLocalDateTime(d);
  }
  get riepilogoFascia(): string {
    return `${this.slotForm.data} · ${this.slotForm.oraInizio} - ${this.orarioFineSelezionato.slice(11, 16)}`;
  }
  login(): void {
    this.keycloak.login();
  }
  registrati(): void {
    this.keycloak.register();
  }
  logout(): void {
    this.keycloak.logout();
  }
  cambiaSezione(s: SezioneDashboard): void {
    this.sezioneAttiva = s;
    this.errore = "";
    this.messaggio = "";
  }
  caricaDatiPrivati(): void {
    this.api.getProfilo().subscribe({
      next: (p) => {
        this.profilo = p;
        this.profiloForm = {
          codiceFiscale: p.codiceFiscale,
          nome: p.nome,
          cognome: p.cognome,
        };
        this.caricaVeicoli();
        this.caricaPrenotazioni();
      },
      error: () => {
        this.sezioneAttiva = "profilo";
        this.messaggio = "Completa il tuo profilo ";
      },
    });
  }
  salvaProfilo(): void {
    this.api.salvaProfilo(this.profiloForm).subscribe({
      next: (p) => {
        this.profilo = p;
        this.messaggio = "Profilo salvato.";
        this.caricaVeicoli();
        this.caricaPrenotazioni();
      },
      error: (e) => this.mostraErrore(e),
    });
  }
  caricaVeicoli(): void {
    this.api.getVeicoli().subscribe({
      next: (d) => {
        this.veicoli = d;
        if (d.length && !this.prenotazioneForm.veicoloId)
          this.prenotazioneForm.veicoloId = d[0].id;
      },
      error: (e) => this.mostraErrore(e),
    });
  }
  creaVeicolo(): void {
    this.api.creaVeicolo(this.veicoloForm).subscribe({
      next: () => {
        this.veicoloForm = { targa: "", marca: "", modello: "" };
        this.messaggio = "Veicolo inserito.";
        this.caricaVeicoli();
      },
      error: (e) => this.mostraErrore(e),
    });
  }
  cancellaVeicolo(id: number): void {
    this.api.cancellaVeicolo(id).subscribe({
      next: () => this.caricaVeicoli(),
      error: (e) => this.mostraErrore(e),
    });
  }
  caricaPrenotazioni(): void {
    this.api.getPrenotazioni().subscribe({
      next: (d) => (this.prenotazioni = d),
      error: (e) => this.mostraErrore(e),
    });
  }
  mostraDisponibilita(): void {
    this.caricamentoDisponibilita = true;
    this.api
      .getDisponibilitaParcheggi(
        this.orarioInizioSelezionato,
        this.orarioFineSelezionato,
      )
      .subscribe({
        next: (d) => {
          this.parcheggi = d;
          this.fasciaCaricata = true;
          this.caricamentoDisponibilita = false;
          this.errore = "";
        },
        error: (e) => {
          this.caricamentoDisponibilita = false;
          this.fasciaCaricata = false;
          this.mostraErrore(e);
        },
      });
  }
  prenota(p: Parcheggio): void {
    if (!this.profilo || !this.prenotazioneForm.veicoloId) {
      this.errore = "Completa il profilo e seleziona un veicolo.";
      return;
    }
    this.api
      .creaPrenotazione({
        veicoloId: this.prenotazioneForm.veicoloId,
        parcheggioId: p.id,
        orarioInizio: this.orarioInizioSelezionato,
        orarioFine: this.orarioFineSelezionato,
      })
      .subscribe({
        next: () => {
          this.messaggio = `Posto ${p.numero} prenotato.`;
          this.mostraDisponibilita();
          this.caricaPrenotazioni();
        },
        error: (e) => this.mostraErrore(e),
      });
  }
  cancellaPrenotazione(id: number): void {
    this.api.cancellaPrenotazione(id).subscribe({
      next: () => {
        this.caricaPrenotazioni();
        if (this.fasciaCaricata) this.mostraDisponibilita();
      },
      error: (e) => this.mostraErrore(e),
    });
  }
  aggiornaFascia(): void {
    this.fasciaCaricata = false;
    this.parcheggi = [];
  }
  private mostraErrore(e: unknown): void {
    const x = e as { error?: { message?: string }; message?: string };
    this.errore = x.error?.message || x.message || "Errore";
    this.messaggio = "";
  }
  private defaultSlot() {
    const d = new Date();
    d.setHours(d.getHours() + 1, 0, 0, 0);
    if (d.getHours() < 6) d.setHours(6, 0, 0, 0);
    if (d.getHours() > 22) {
      d.setDate(d.getDate() + 1);
      d.setHours(6, 0, 0, 0);
    }
    return {
      data: this.formatDateOnly(d),
      oraInizio: `${String(d.getHours()).padStart(2, "0")}:00`,
      durataOre: 1,
    };
  }
  private parseLocalDateTime(v: string): Date {
    const [dp, tp] = v.split("T");
    const [y, m, d] = dp.split("-").map(Number);
    const [h, mi, s = 0] = tp.split(":").map(Number);
    return new Date(y, m - 1, d, h, mi, s, 0);
  }
  private formatLocalDateTime(d: Date): string {
    return `${this.formatDateOnly(d)}T${String(d.getHours()).padStart(2, "0")}:00:00`;
  }
  private formatDateOnly(d: Date): string {
    return [
      d.getFullYear(),
      String(d.getMonth() + 1).padStart(2, "0"),
      String(d.getDate()).padStart(2, "0"),
    ].join("-");
  }
}
