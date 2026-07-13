import { Injectable } from "@angular/core";
import Keycloak from "keycloak-js";

//dichiaro la classe come servizio Angular, e con providedIn:'root' indico
//che esiste una sola istanza del servizio, pattern singleton
@Injectable({ providedIn: "root" })
export class KeycloakService {
  private readonly keycloak = new Keycloak({
    url: "http://localhost:8080",
    realm: "parking",
    clientId: "parking-frontend",
  });

  async init(): Promise<void> {
    await this.keycloak.init({
      onLoad: "check-sso",
      pkceMethod: "S256",
      checkLoginIframe: false,
    });
  } //una promessa non produce un valore concreto, ma segnala quando l’operazione è terminata
  //onLoad: 'check-sso' indica che Keycloak controlla se l’utente ha già una sessione attiva,
  // ma non lo obbliga a effettuare subito il login.
  //Se l’utente non è autenticato,l’applicazione mostra la pagina iniziale di login/registrazione
  isLoggedIn(): boolean {
    return !!this.keycloak.authenticated;
  } //il doppio punto esclamatico converte il valore undefined in false, possiamo avere,true,false,undefined
  /*
    !!true è true
    !!false è false
    !!undefined è false
   */

  login(): Promise<void> {
    return this.keycloak.login();
  }

  register(): Promise<void> {
    return this.keycloak.register();
  }

  logout(): Promise<void> {
    return this.keycloak.logout({ redirectUri: window.location.origin });
  }

  async getToken(): Promise<string | undefined> {
    if (!this.isLoggedIn()) {
      return undefined;
    }
    await this.keycloak.updateToken(30); //30 sono secondi
    return this.keycloak.token;
  } // getToken() è usata dall'interceptor

  username(): string {
    return (this.keycloak.tokenParsed?.["preferred_username"] as string) || "";
  }

  email(): string {
    return (this.keycloak.tokenParsed?.["email"] as string) || "";
  }

  //tokenParsed contiene il JWT già decodificato
  //preferred_username contiene lo username di keycloak che l'utente ha impostato quando ha
  // fatto la registrazione al sito, e l'operatore ?. è chiamato
  //optional chaining,se tokenParsed non esiste viene data un stringa vuota grazie a || ''
}
