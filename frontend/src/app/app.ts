import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthStore } from './services/auth-store';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  readonly auth = inject(AuthStore);
  private readonly router = inject(Router);

  initials(name: string): string {
    return name
      .split(' ')
      .slice(0, 2)
      .map((part) => part.charAt(0))
      .join('')
      .toUpperCase();
  }

  signOut(): void {
    this.auth.signOut();
    void this.router.navigate(['/']);
  }

  goCategories(event: Event): void {
    event.preventDefault();
    if (this.router.url !== '/') void this.router.navigate(['/'], { fragment: 'categorias' });
    else document.getElementById('categorias')?.scrollIntoView({ behavior: 'smooth' });
  }
}
