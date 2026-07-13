import {
  ApplicationConfig,
  provideAppInitializer,
  inject,
} from "@angular/core";
import { provideHttpClient, withInterceptors } from "@angular/common/http";
import { KeycloakService } from "./keycloak.service";
import { authInterceptor } from "./auth.interceptor";

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withInterceptors([authInterceptor])),
    //da authInterceptor passano le richieste HTTP del client, esso cerca di
    //aggiungere il JWT
    provideAppInitializer(() => inject(KeycloakService).init()),
    //provideAppInitializer permette di eseguire qualcosa prima che Angular completi l’avvio
  ],
};
