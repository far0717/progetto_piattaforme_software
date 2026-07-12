import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { from, switchMap } from 'rxjs';
import { KeycloakService } from './keycloak.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloak = inject(KeycloakService);

  //la from converte la Promise<string | undefined> che ottengo dal keycloak.getToken(),
    // in un Observable<string | undefined>
  return from(keycloak.getToken()).pipe(
    switchMap(token => {
      if (!token) {
        return next(req);
      }
      return next(req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      }));
    })
  );
};
