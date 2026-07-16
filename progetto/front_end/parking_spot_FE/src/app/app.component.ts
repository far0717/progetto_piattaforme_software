import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { KeycloakService } from "./keycloak.service";
import { ParkingApiService } from "./parking-api.service";
import { Parcheggio, Prenotazione, ProfiloUtente, Veicolo } from "./api.models";

type SezioneDashboard = "prenotazioni" | "profilo";
type ZonaParcheggio = "A" | "B";
type VistaZona = "tutte" | ZonaParcheggio;
type TipoPosto = "standard" | "disabili" | "scarico merci";

interface PostoMappa extends Parcheggio {
  zona: ZonaParcheggio;
  tipo: TipoPosto;
  prenotabile: boolean;
  titolo: string;
  icona: string;
}

interface PosizionePlanimetria {
  left: number;
  top: number;
  width: number;
  height: number;
  rotation?: number;
}

interface PostoPlanimetria extends PostoMappa, PosizionePlanimetria {}

@Component({
  selector: "app-root",
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: "./app.component.html",
  styleUrl: "./app.component.css",
})
export class AppComponent implements OnInit {
  parcheggi: Parcheggio[] = [];
  veicoli: Veicolo[] = [];
  prenotazioni: Prenotazione[] = [];
  profilo?: ProfiloUtente;

  sezioneAttiva: SezioneDashboard = "prenotazioni";
  vistaZona: VistaZona = "tutte";
  fasciaCaricata = false; //se false mostra il messaggio iniziale e non la mappa
  caricamentoDisponibilita = false; //impedisce click ripetuti mentre la richiesta è in corso

  messaggio = "";
  errore = "";

  readonly orariDisponibili = Array.from(
    { length: 17 },
    (_, i) => `${String(i + 6).padStart(2, "0")}:00`,
  );
  readonly durateDisponibili = Array.from({ length: 12 }, (_, i) => i + 1);
  private readonly postiDisabili = new Set<number>([
    2, 3, 4, 5, 46, 47, 48, 49,
  ]);
  private readonly postiScaricoMerci = new Set<number>([1, 45]);

  profiloForm = {
    codiceFiscale: "",
    nome: "",
    cognome: "",
  };

  veicoloForm = {
    targa: "",
    marca: "",
    modello: "",
  };

  slotForm = this.defaultSlot();

  prenotazioneForm = {
    veicoloId: 0,
  };

  constructor(
    public readonly keycloak: KeycloakService,
    private readonly api: ParkingApiService,
  ) {}

  ngOnInit(): void {
    if (this.keycloak.isLoggedIn()) {
      this.caricaDatiPrivati();
    }
  }

  get parcheggiOrdinati(): Parcheggio[] {
    return [...this.parcheggi].sort((a, b) => a.numero - b.numero);
  }

  get postiMappa(): PostoMappa[] {
    return this.parcheggiOrdinati.map((parcheggio) =>
      this.toPostoMappa(parcheggio),
    );
  }

  get postiZonaA(): PostoMappa[] {
    return this.postiMappa.filter((p) => p.zona === "A");
  }

  get postiZonaB(): PostoMappa[] {
    return this.postiMappa.filter((p) => p.zona === "B");
  }

  get postiPlanimetria(): PostoPlanimetria[] {
    return this.postiMappa
      .map((posto) => ({
        ...posto,
        ...this.getPosizionePlanimetria(posto.numero),
      }))
      .filter((posto) => this.postoVisibile(posto));
  }

  get orarioInizioSelezionato(): string {
    return `${this.slotForm.data}T${this.slotForm.oraInizio}:00`;
  }

  get orarioFineSelezionato(): string {
    const inizio = this.parseLocalDateTime(this.orarioInizioSelezionato);
    inizio.setHours(inizio.getHours() + Number(this.slotForm.durataOre));
    return this.formatLocalDateTime(inizio);
  }

  get riepilogoFascia(): string {
    return `${this.slotForm.data} · ${this.slotForm.oraInizio} - ${this.orarioFineSelezionato.slice(11, 16)}`;
  }

  get dataMinima(): string {
    return this.formatDateOnly(new Date());
  }

  get profiloFormValido(): boolean {
    return (
      /^[A-Z0-9]{16}$/i.test(this.profiloForm.codiceFiscale.trim()) &&
      !!this.profiloForm.nome.trim() &&
      !!this.profiloForm.cognome.trim()
    );
  }

  get veicoloFormValido(): boolean {
    return (
      /^[A-Z]{2}[0-9]{3}[A-Z]{2}$/i.test(this.veicoloForm.targa.trim()) &&
      !!this.veicoloForm.marca.trim() &&
      !!this.veicoloForm.modello.trim()
    );
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

  cambiaSezione(sezione: SezioneDashboard): void {
    this.sezioneAttiva = sezione;
    this.errore = "";
    this.messaggio = "";
  }

  impostaVistaZona(vista: VistaZona): void {
    this.vistaZona = vista;
  }

  conteggioLiberi(zona: ZonaParcheggio): number {
    return this.getPostiByZona(zona).filter(
      (p) => p.prenotabile && p.disponibile,
    ).length;
  }

  conteggioTotaliPrenotabili(zona: ZonaParcheggio): number {
    return this.getPostiByZona(zona).filter((p) => p.prenotabile).length;
  }

  caricaDatiPrivati(): void {
    this.api.getProfilo().subscribe({
      next: (profilo) => {
        this.profilo = profilo;
        this.profiloForm = {
          codiceFiscale: profilo.codiceFiscale,
          nome: profilo.nome,
          cognome: profilo.cognome,
        };
        this.caricaVeicoli();
        this.caricaPrenotazioni();
      },
      error: () => {
        this.sezioneAttiva = "profilo";
        this.messaggio = "Completa il tuo profilo per iniziare a prenotare";
      },
    });
  }

  salvaProfilo(): void {
    this.api
      .salvaProfilo({
        codiceFiscale: this.profiloForm.codiceFiscale.toUpperCase().trim(),
        nome: this.profiloForm.nome.trim(),
        cognome: this.profiloForm.cognome.trim(),
      })
      .subscribe({
        next: (profilo) => {
          this.profilo = profilo;
          this.messaggio = "Profilo salvato.";
          this.errore = "";
          this.caricaVeicoli();
          this.caricaPrenotazioni();
        },
        error: (err) => this.mostraErrore(err),
      });
  }

  caricaVeicoli(): void {
    this.api.getVeicoli().subscribe({
      next: (data) => {
        this.veicoli = data;
        if (data.length && !this.prenotazioneForm.veicoloId) {
          this.prenotazioneForm.veicoloId = data[0].id;
        }
      },
      error: (err) => this.mostraErrore(err),
    });
  }

  creaVeicolo(): void {
    this.api
      .creaVeicolo({
        targa: this.veicoloForm.targa.toUpperCase().trim(),
        marca: this.veicoloForm.marca.trim(),
        modello: this.veicoloForm.modello.trim(),
      })
      .subscribe({
        next: () => {
          this.messaggio = "Veicolo inserito.";
          this.errore = "";
          this.veicoloForm = { targa: "", marca: "", modello: "" };
          this.caricaVeicoli();
        },
        error: (err) => this.mostraErrore(err),
      });
  }

  cancellaVeicolo(id: number): void {
    this.api.cancellaVeicolo(id).subscribe({
      next: () => {
        this.messaggio = "Veicolo eliminato.";
        this.errore = "";
        this.caricaVeicoli();
      },
      error: (err) => this.mostraErrore(err),
    });
  }

  caricaPrenotazioni(): void {
    this.api.getPrenotazioni().subscribe({
      next: (data) => (this.prenotazioni = data),
      error: (err) => this.mostraErrore(err),
    });
  }

  mostraDisponibilita(): void {
    if (!this.slotForm.data || !this.slotForm.oraInizio) {
      this.errore = "Seleziona una data e un orario validi.";
      return;
    }

    if (this.parseLocalDateTime(this.orarioInizioSelezionato) < new Date()) {
      this.errore =
        "Non puoi cercare disponibilità per una fascia già trascorsa.";
      return;
    }

    this.caricamentoDisponibilita = true;
    this.api
      .getDisponibilitaParcheggi(
        this.orarioInizioSelezionato,
        this.orarioFineSelezionato,
      )
      .subscribe({
        next: (data) => {
          this.parcheggi = data;
          this.fasciaCaricata = true;
          this.caricamentoDisponibilita = false;
          this.messaggio = "";
          this.errore = "";
        },
        error: (err) => {
          this.caricamentoDisponibilita = false;
          this.fasciaCaricata = false;
          this.mostraErrore(err);
        },
      });
  }

  prenota(parcheggio: PostoMappa): void {
    if (!this.profilo) {
      this.sezioneAttiva = "profilo";
      this.errore = "Completa prima il profilo.";
      return;
    }

    if (!this.veicoli.length || !this.prenotazioneForm.veicoloId) {
      this.sezioneAttiva = "profilo";
      this.errore = "Registra prima almeno un veicolo.";
      return;
    }

    if (!this.fasciaCaricata) {
      this.errore = "Seleziona prima la fascia oraria.";
      return;
    }

    if (!parcheggio.prenotabile) {
      this.errore =
        "Questa area è riservata allo scarico merci e non è prenotabile.";
      return;
    }

    if (!parcheggio.disponibile) {
      this.errore = "Questo posto non è disponibile nella fascia selezionata.";
      return;
    }

    this.api
      .creaPrenotazione({
        veicoloId: this.prenotazioneForm.veicoloId,
        parcheggioId: parcheggio.id,
        orarioInizio: this.orarioInizioSelezionato,
        orarioFine: this.orarioFineSelezionato,
      })
      .subscribe({
        next: () => {
          this.messaggio = `Posto ${parcheggio.numero} prenotato.`;
          this.errore = "";
          this.mostraDisponibilita();
          this.caricaPrenotazioni();
        },
        error: (err) => this.mostraErrore(err),
      });
  }

  cancellaPrenotazione(id: number): void {
    this.api.cancellaPrenotazione(id).subscribe({
      next: () => {
        this.messaggio = "Prenotazione cancellata.";
        this.caricaPrenotazioni();
        if (this.fasciaCaricata) {
          this.mostraDisponibilita();
        }
      },
      error: (err) => this.mostraErrore(err),
    });
  }

  aggiornaFascia(): void {
    this.fasciaCaricata = false;
    this.parcheggi = [];
  }

  trackByNumero(_: number, posto: PostoMappa): number {
    return posto.numero;
  }

  spotPlanimetriaStyle(posto: PostoPlanimetria): Record<string, string> {
    return {
      left: `${posto.left}%`,
      top: `${posto.top}%`,
      width: `${posto.width}%`,
      height: `${posto.height}%`,
      transform: `rotate(${posto.rotation || 0}deg)`,
    };
  }

  private postoVisibile(posto: PostoMappa): boolean {
    return this.vistaZona === "tutte" || posto.zona === this.vistaZona;
  }

  private getPosizionePlanimetria(numero: number): PosizionePlanimetria {
    const posizioni: Record<number, PosizionePlanimetria> = {};
    const add = (
      n: number,
      left: number,
      top: number,
      width: number,
      height: number,
      rotation = 0,
    ) => {
      posizioni[n] = { left, top, width, height, rotation };
    };
    const addSeq = (
      start: number,
      count: number,
      left: number,
      top: number,
      dx: number,
      dy: number,
      width: number,
      height: number,
      rotation = 0,
    ) => {
      for (let i = 0; i < count; i++) {
        add(start + i, left + dx * i, top + dy * i, width, height, rotation);
      }
    };

    //Zona A e Zona B ho cercato di farle simmetriche rispetto alla strada

    // Zona A: fascia superiore con scarico merci e posti disabili.
    add(1, 10.1, 25.8, 7.0, 6.5);
    addSeq(2, 4, 17.35, 25.8, 4.45, 0, 4.25, 6.5);

    // Zona A: perimetro e file interne.
    addSeq(6, 10, 3.85, 34.6, 0, 4.3, 4.55, 3.65);
    addSeq(16, 12, 6.7, 78.8, 2.72, 0, 2.48, 6.0);
    addSeq(28, 9, 40.05, 42.1, 0, 4.0, 3.2, 3.35);
    addSeq(37, 4, 18.55, 41.9, 0, 5.45, 3.45, 3.55);
    addSeq(41, 4, 26.55, 41.9, 0, 5.45, 3.45, 3.55);

    // Zona B: stessa conformazione della Zona A, centrata e speculare rispetto alla strada centrale.
    add(45, 65.05, 25.8, 7.0, 6.5);
    addSeq(46, 4, 72.3, 25.8, 4.45, 0, 4.25, 6.5);

    addSeq(50, 10, 91.6, 34.6, 0, 4.3, 4.55, 3.65);
    addSeq(60, 11, 62.26, 78.8, 2.72, 0, 2.48, 6.0);
    addSeq(71, 9, 56.75, 42.1, 0, 4.0, 3.2, 3.35);
    addSeq(80, 4, 70.0, 41.9, 0, 5.45, 3.45, 3.55);
    addSeq(84, 4, 78.0, 41.9, 0, 5.45, 3.45, 3.55);

    return posizioni[numero] || { left: 48, top: 50, width: 3, height: 3 };
  }

  private getPostiByZona(zona: ZonaParcheggio): PostoMappa[] {
    return zona === "A" ? this.postiZonaA : this.postiZonaB;
  }

  private toPostoMappa(parcheggio: Parcheggio): PostoMappa {
    const tipo = this.getTipoPosto(parcheggio.numero);

    return {
      ...parcheggio,
      zona: parcheggio.numero <= 44 ? "A" : "B",
      tipo,
      prenotabile: tipo !== "scarico merci",
      titolo:
        tipo === "disabili"
          ? "Parcheggio disabili"
          : tipo === "scarico merci"
            ? "Scarico merci"
            : "Posto auto",
      icona:
        tipo === "disabili"
          ? "♿"
          : tipo === "scarico merci"
            ? "↔"
            : parcheggio.disponibile
              ? "P"
              : "🚘",
    };
  }

  private getTipoPosto(numero: number): TipoPosto {
    if (this.postiDisabili.has(numero)) {
      return "disabili";
    }
    if (this.postiScaricoMerci.has(numero)) {
      return "scarico merci";
    }
    return "standard";
  }

  private mostraErrore(err: unknown): void {
    const erroreHttp = err as {
      error?: { message?: string };
      message?: string;
    };
    this.errore =
      erroreHttp.error?.message || erroreHttp.message || "Errore imprevisto";
    this.messaggio = "";
  }

  private defaultSlot(): {
    data: string;
    oraInizio: string;
    durataOre: number;
  } {
    const next = new Date();
    next.setHours(next.getHours() + 1, 0, 0, 0);

    if (next.getHours() < 6) {
      next.setHours(6, 0, 0, 0);
    }

    if (next.getHours() > 22) {
      next.setDate(next.getDate() + 1);
      next.setHours(6, 0, 0, 0);
    }

    return {
      data: this.formatDateOnly(next),
      oraInizio: `${String(next.getHours()).padStart(2, "0")}:00`,
      durataOre: 1,
    };
  }

  private parseLocalDateTime(value: string): Date {
    const [datePart, timePart] = value.split("T");
    const [year, month, day] = datePart.split("-").map(Number);
    const [hour, minute, second = 0] = timePart.split(":").map(Number);
    return new Date(year, month - 1, day, hour, minute, second, 0);
  }

  private formatLocalDateTime(date: Date): string {
    return `${this.formatDateOnly(date)}T${String(date.getHours()).padStart(2, "0")}:00:00`;
  }

  private formatDateOnly(date: Date): string {
    return [
      date.getFullYear(),
      String(date.getMonth() + 1).padStart(2, "0"),
      String(date.getDate()).padStart(2, "0"),
    ].join("-");
  }
}
