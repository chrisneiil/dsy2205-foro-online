import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthTokenStore } from '../core/auth-token.store';
import { UsersApi } from '../core/api-clients';
import { ForumUser, UserResponse } from '../models/forum.models';

const userKey = 'nexo.currentUser';

@Injectable({ providedIn: 'root' })
export class AuthStore {
  private readonly api = inject(UsersApi);
  private readonly tokenStore = inject(AuthTokenStore);
  readonly users = signal<UserResponse[]>([]);
  readonly currentUser = signal<ForumUser | null>(this.readUser());
  readonly isAdmin = computed(() => this.currentUser()?.role === 'ADMIN');
  readonly errorMessage = (error: unknown): string => {
    if (error instanceof HttpErrorResponse) {
      const message = error.error?.detail ?? error.error?.message;
      if (message) return message;
      if (error.status === 0)
        return 'No se pudo conectar con los servicios. Revisa que el backend y Oracle estén disponibles.';
      if (error.status === 401) return 'Correo/usuario o contraseña incorrectos.';
      if (error.status === 409) return 'Ese nombre de usuario o correo ya está registrado.';
    }
    return 'No se pudo completar la operación. Inténtalo nuevamente.';
  };

  async restore(): Promise<void> {
    if (!this.tokenStore.token()) {
      this.signOut();
      return;
    }
    try {
      this.setCurrentUser(await this.api.currentUser());
    } catch {
      this.signOut();
    }
  }

  async login(identifier: string, password: string): Promise<void> {
    const response = await this.api.login(identifier.trim(), password);
    this.tokenStore.set(response.token);
    this.setCurrentUser(response.user);
  }

  async register(
    username: string,
    displayName: string,
    email: string,
    password: string,
  ): Promise<void> {
    await this.api.register({
      username: username.trim(),
      displayName: displayName.trim(),
      email: email.trim(),
      password,
    });
    await this.login(email, password);
  }

  async updateProfile(displayName: string, email: string, password?: string): Promise<void> {
    const current = this.currentUser();
    if (!current) return;
    const updated = await this.api.updateUser(current.id, {
      displayName: displayName.trim(),
      email: email.trim(),
      password: password || undefined,
    });
    this.setCurrentUser(updated);
  }

  async loadUsers(): Promise<void> {
    this.users.set(await this.api.listUsers());
  }

  signOut(): void {
    this.tokenStore.clear();
    this.currentUser.set(null);
    if (typeof localStorage !== 'undefined') localStorage.removeItem(userKey);
  }

  private setCurrentUser(user: UserResponse): void {
    this.currentUser.set(user);
    if (typeof localStorage !== 'undefined') localStorage.setItem(userKey, JSON.stringify(user));
  }

  private readUser(): ForumUser | null {
    if (typeof localStorage === 'undefined') return null;
    try {
      const value = localStorage.getItem(userKey);
      return value ? (JSON.parse(value) as ForumUser) : null;
    } catch {
      localStorage.removeItem(userKey);
      return null;
    }
  }
}
