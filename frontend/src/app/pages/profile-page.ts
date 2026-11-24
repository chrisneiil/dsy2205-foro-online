import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthStore } from '../services/auth-store';

@Component({
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    @if (auth.currentUser(); as user) {
      <section class="profile-page">
        <a class="back-link" routerLink="/">← Volver al foro</a>
        <div class="profile-header">
          <span class="avatar avatar-large">{{ initials(user.displayName) }}</span>
          <div>
            <span class="eyebrow">TU ESPACIO EN NEXO</span>
            <h1>Mi perfil</h1>
            <p class="muted">Administra tu información y preferencias.</p>
          </div>
        </div>
        <div class="profile-grid">
          <aside class="profile-menu card">
            <span class="eyebrow">CUENTA</span
            ><a class="profile-menu-active" href="#informacion"
              >Información personal <span>›</span></a
            ><a href="#seguridad">Seguridad <span>›</span></a
            ><a href="#actividad">Mi actividad <span>›</span></a>
            <div class="profile-role">
              <span>Rol actual</span
              ><strong>{{ user.role === 'ADMIN' ? 'Administrador' : 'Miembro' }}</strong>
            </div>
          </aside>
          <form
            class="profile-form card"
            #form="ngForm"
            (ngSubmit)="save()"
            novalidate
            id="informacion"
          >
            <div>
              <span class="eyebrow">INFORMACIÓN PERSONAL</span>
              <h2>Datos de tu cuenta</h2>
              <p class="muted">Esta información forma parte de tu perfil de comunidad.</p>
            </div>
            <label for="profile-name">Nombre para mostrar</label
            ><input
              id="profile-name"
              name="displayName"
              [(ngModel)]="displayName"
              required
              minlength="2"
              maxlength="50"
              autocomplete="name"
              #nameField="ngModel"
            />
            @if (nameField.invalid && nameField.touched) {
              <small class="field-error">Escribe un nombre de entre 2 y 50 caracteres.</small>
            }
            <label for="profile-email">Correo electrónico</label
            ><input
              id="profile-email"
              name="email"
              type="email"
              [(ngModel)]="email"
              required
              email
              maxlength="120"
              autocomplete="email"
              #emailField="ngModel"
            />
            @if (emailField.invalid && emailField.touched) {
              <small class="field-error">Ingresa un correo válido.</small>
            }
            <div class="profile-divider" id="seguridad"></div>
            <div>
              <span class="eyebrow">SEGURIDAD</span>
              <h3>Cambiar contraseña</h3>
              <p class="muted">Déjalo en blanco para mantener tu contraseña actual.</p>
            </div>
            <label for="profile-password">Nueva contraseña</label
            ><input
              id="profile-password"
              name="password"
              type="password"
              [(ngModel)]="password"
              minlength="10"
              maxlength="64"
              pattern="(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{10,64}"
              autocomplete="new-password"
              placeholder="Al menos 10 caracteres"
              #passwordField="ngModel"
            />
            @if (password && passwordField.invalid) {
              <small class="field-error"
                >Debe incluir mayúsculas, minúsculas, número y símbolo; entre 10 y 64
                caracteres.</small
              >
            }
            @if (saved) {
              <p class="success-alert" role="status">Tus cambios se guardaron correctamente.</p>
            }
            @if (error) {
              <p class="form-alert" role="alert">{{ error }}</p>
            }
            <div class="form-actions">
              <button
                class="button button-primary"
                type="submit"
                [disabled]="form.invalid || saving"
              >
                {{ saving ? 'Guardando…' : 'Guardar cambios' }}</button><button
                class="button button-quiet"
                type="button"
                (click)="reset(user.displayName, user.email)"
              >
                Cancelar
              </button>
            </div>
            <div class="profile-activity" id="actividad">
              <span class="activity-icon">✳</span>
              <div>
                <strong>Tu comunidad, a tu ritmo</strong>
                <p>Las conversaciones donde participas aparecerán en tu perfil.</p>
              </div>
            </div>
          </form>
        </div>
      </section>
    } @else {
      <div class="empty-state">
        <h2>Inicia sesión para ver tu perfil</h2>
        <a class="button button-primary" routerLink="/login">Iniciar sesión</a>
      </div>
    }
  `,
})
export class ProfilePage {
  readonly auth = inject(AuthStore);
  private readonly router = inject(Router);
  displayName = this.auth.currentUser()?.displayName ?? '';
  email = this.auth.currentUser()?.email ?? '';
  password = '';
  saved = false;
  error = '';
  saving = false;

  constructor() {
    if (!this.auth.currentUser()) void this.router.navigate(['/login']);
  }
  initials(name: string): string {
    return name
      .split(' ')
      .slice(0, 2)
      .map((part) => part.charAt(0))
      .join('')
      .toUpperCase();
  }
  reset(name: string, email: string): void {
    this.displayName = name;
    this.email = email;
    this.password = '';
    this.saved = false;
  }
  async save(): Promise<void> {
    this.saved = false;
    this.error = '';
    this.saving = true;
    try {
      await this.auth.updateProfile(this.displayName, this.email, this.password);
      this.password = '';
      this.saved = true;
    } catch (error) {
      this.error = this.auth.errorMessage(error);
    } finally {
      this.saving = false;
    }
  }
}
