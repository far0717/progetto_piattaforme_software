import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { KeycloakService } from './keycloak.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  constructor(public readonly keycloak: KeycloakService) {}

  login(): void { this.keycloak.login(); }
  registrati(): void { this.keycloak.register(); }
  logout(): void { this.keycloak.logout(); }
}
