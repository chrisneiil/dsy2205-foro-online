import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthStore } from '../services/auth-store';

@Component({
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: ` <section class="auth-layout">
    <div class="auth-art">
      <span class="eyebrow">UN LUGAR PARA CONVERSAR</span>
      <h1>Las buenas ideas<br />crecen en comunidad.</h1>
      <p>Encuentra personas, comparte lo que sabes y abre conversaciones que valen la pena.</p>
      <div class="auth-art-orbit"><span>n</span><i>●</i><b>✳</b><em>◌</em></div>
    </div>
    <div class="auth-card">
      <a class="back-link" routerLink="/">← Volver al foro</a
      ><span class="eyebrow">QUÉ BUENO VERTE</span>
      <h2>Inicia sesión</h2>
      <p class="muted">Continúa donde quedó tu última conversación.</p>
      <form #form="ngForm" (ngSubmit)="submit()" novalidate>
        <label for="login-email">Correo electrónico</label
        ><input
          id="login-email"
          name="email"
          type="email"
          [(ngModel)]="email"
          required
          email
          autocomplete="email"
          placeholder="tu@correo.cl"
          #emailField="ngModel"
        />
        @if (emailField.invalid && emailField.touched) {
          <small class="field-error">Ingresa un correo válido.</small>
        }
        <label for="login-password">Contraseña</label
        ><input
          id="login-password"
          name="password"
          type="password"
          [(ngModel)]="password"
          required
          minlength="8"
          autocomplete="current-password"
          placeholder="Tu contraseña"
          #passwordField="ngModel"
        />
        @if (passwordField.invalid && passwordField.touched) {
          <small class="field-error">La contraseña debe tener al menos 8 caracteres.</small>
        }
        @if (error) {
          <p class="form-alert" role="alert">{{ error }}</p>
        }
        <button
          class="button button-primary button-wide"
          type="submit"
          [disabled]="form.invalid || submitting"
        >
          {{ submitting ? 'Conectando…' : 'Iniciar sesión' }} <span>→</span>
        </button>
      </form>
      <a class="forgot-link" routerLink="/recover">¿Olvidaste tu contraseña?</a>
      <p class="auth-switch">¿Aún no tienes cuenta? <a routerLink="/register">Regístrate</a></p>
    </div>
  </section>`,
})
export class LoginPage {
  private readonly auth = inject(AuthStore);
  private readonly router = inject(Router);
  email = '';
  password = '';
  error = '';
  submitting = false;

  async submit(): Promise<void> {
    this.error = '';
    this.submitting = true;
    try {
      await this.auth.login(this.email, this.password);
      await this.router.navigate([this.auth.isAdmin() ? '/admin' : '/']);
    } catch (error) {
      this.error = this.auth.errorMessage(error);
    } finally {
      this.submitting = false;
    }
  }
}

@Component({
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: ` <section class="auth-layout">
    <div class="auth-art">
      <span class="eyebrow">TU COMUNIDAD TE ESPERA</span>
      <h1>Una conversación<br />puede abrir caminos.</h1>
      <p>Crea tu espacio en Nexo y empieza a compartir con personas que tienen algo en común.</p>
      <div class="auth-art-orbit"><span>n</span><i>✦</i><b>●</b><em>◌</em></div>
    </div>
    <div class="auth-card auth-card-tall">
      <a class="back-link" routerLink="/">← Volver al foro</a
      ><span class="eyebrow">EMPIEZA POR AQUÍ</span>
      <h2>Crear una cuenta</h2>
      <p class="muted">Te tomará menos de un minuto.</p>
      <form #form="ngForm" (ngSubmit)="submit()" novalidate>
        <label for="register-username">Nombre de usuario</label
        ><input
          id="register-username"
          name="username"
          [(ngModel)]="username"
          required
          minlength="3"
          maxlength="40"
          pattern="[A-Za-z0-9._-]+"
          autocomplete="username"
          placeholder="Ej. alex.gonzalez"
          #usernameField="ngModel"
        />
        @if (usernameField.invalid && usernameField.touched) {
          <small class="field-error"
            >Usa entre 3 y 40 letras, números, puntos, guiones o guiones bajos.</small
          >
        }
        <label for="register-name">Nombre para mostrar</label
        ><input
          id="register-name"
          name="name"
          [(ngModel)]="name"
          required
          minlength="2"
          maxlength="80"
          autocomplete="name"
          placeholder="Ej. Alex González"
          #nameField="ngModel"
        />
        @if (nameField.invalid && nameField.touched) {
          <small class="field-error">Escribe un nombre de entre 2 y 50 caracteres.</small>
        }
        <label for="register-email">Correo electrónico</label
        ><input
          id="register-email"
          name="email"
          type="email"
          [(ngModel)]="email"
          required
          email
          maxlength="120"
          autocomplete="email"
          placeholder="tu@correo.cl"
          #emailField="ngModel"
        />
        @if (emailField.invalid && emailField.touched) {
          <small class="field-error">Ingresa un correo válido.</small>
        }
        <label for="register-password">Contraseña</label
        ><input
          id="register-password"
          name="password"
          type="password"
          [(ngModel)]="password"
          required
          minlength="10"
          maxlength="64"
          pattern="(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{10,64}"
          autocomplete="new-password"
          placeholder="Crea una contraseña segura"
          #passwordField="ngModel"
        />
        <div class="password-rules">
          <span [class.rule-ok]="password.length >= 10">✓ 10 a 64 caracteres</span
          ><span [class.rule-ok]="hasLower() && hasUpper()">✓ Combina mayúsculas y minúsculas</span
          ><span [class.rule-ok]="hasNumber()">✓ Incluye un número</span
          ><span [class.rule-ok]="hasSymbol()">✓ Incluye un carácter especial</span>
        </div>
        @if (passwordField.invalid && passwordField.touched) {
          <small class="field-error">La contraseña aún no cumple todas las condiciones.</small>
        }
        <label for="register-confirm">Repite la contraseña</label
        ><input
          id="register-confirm"
          name="confirm"
          type="password"
          [(ngModel)]="confirm"
          required
          autocomplete="new-password"
          placeholder="Escríbela nuevamente"
          #confirmField="ngModel"
        />
        @if (confirmField.touched && confirm !== password) {
          <small class="field-error">Las contraseñas no coinciden.</small>
        }
        @if (error) {
          <p class="form-alert" role="alert">{{ error }}</p>
        }
        <button
          class="button button-primary button-wide"
          type="submit"
          [disabled]="form.invalid || confirm !== password || submitting"
        >
          {{ submitting ? 'Creando cuenta…' : 'Crear mi cuenta' }} <span>→</span>
        </button>
      </form>
      <p class="auth-switch">¿Ya tienes cuenta? <a routerLink="/login">Inicia sesión</a></p>
    </div>
  </section>`,
})
export class RegisterPage {
  private readonly auth = inject(AuthStore);
  private readonly router = inject(Router);
  name = '';
  username = '';
  email = '';
  password = '';
  confirm = '';
  error = '';
  submitting = false;
  hasLower = (): boolean => /[a-z]/.test(this.password);
  hasUpper = (): boolean => /[A-Z]/.test(this.password);
  hasNumber = (): boolean => /[0-9]/.test(this.password);
  hasSymbol = (): boolean => /[^A-Za-z0-9]/.test(this.password);

  async submit(): Promise<void> {
    this.error = '';
    this.submitting = true;
    try {
      await this.auth.register(this.username, this.name, this.email, this.password);
      await this.router.navigate(['/profile']);
    } catch (error) {
      this.error = this.auth.errorMessage(error);
    } finally {
      this.submitting = false;
    }
  }
}

@Component({
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: ` <section class="auth-layout">
    <div class="auth-art">
      <span class="eyebrow">ACCESO A NEXO</span>
      <h1>Volver a conversar<br />está a un paso.</h1>
      <p>Te ayudaremos a recuperar el acceso a tu cuenta.</p>
      <div class="auth-art-orbit"><span>n</span><i>↗</i><b>●</b><em>◌</em></div>
    </div>
    <div class="auth-card">
      <a class="back-link" routerLink="/login">← Volver a iniciar sesión</a
      ><span class="eyebrow">RECUPERAR ACCESO</span>
      <h2>¿Olvidaste tu contraseña?</h2>
      <p class="muted">
        Ingresa el correo asociado a tu cuenta para solicitar ayuda al administrador.
      </p>
      <form #form="ngForm" (ngSubmit)="submit()" novalidate>
        <label for="recover-email">Correo electrónico</label
        ><input
          id="recover-email"
          name="email"
          type="email"
          [(ngModel)]="email"
          required
          email
          autocomplete="email"
          placeholder="tu@correo.cl"
          #emailField="ngModel"
        />
        @if (emailField.invalid && emailField.touched) {
          <small class="field-error">Ingresa un correo válido.</small>
        }
        @if (sent) {
          <p class="success-alert" role="status">
            La recuperación automática no está configurada. Solicita ayuda al administrador del
            foro.
          </p>
        }
        <button class="button button-primary button-wide" type="submit" [disabled]="form.invalid">
          Solicitar ayuda <span>→</span>
        </button>
      </form>
    </div>
  </section>`,
})
export class RecoverPage {
  email = '';
  sent = false;
  submit(): void {
    this.sent = true;
  }
}
