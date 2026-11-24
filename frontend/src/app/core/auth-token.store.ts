import { Injectable, signal } from '@angular/core';

const tokenKey = 'nexo.accessToken';

@Injectable({ providedIn: 'root' })
export class AuthTokenStore {
  readonly token = signal<string | null>(this.readToken());

  set(token: string): void {
    this.token.set(token);
    if (typeof localStorage !== 'undefined') localStorage.setItem(tokenKey, token);
  }

  clear(): void {
    this.token.set(null);
    if (typeof localStorage !== 'undefined') localStorage.removeItem(tokenKey);
  }

  private readToken(): string | null {
    return typeof localStorage === 'undefined' ? null : localStorage.getItem(tokenKey);
  }
}
