import {
  ApplicationConfig,
  inject,
  provideAppInitializer,
  provideBrowserGlobalErrorListeners,
} from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';
import { authInterceptor } from './core/auth.interceptor';
import { AuthStore } from './services/auth-store';
import { ForumStore } from './services/forum-store';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    provideAppInitializer(async () => {
      const auth = inject(AuthStore);
      const forum = inject(ForumStore);
      await auth.restore();
      await forum.load();
      if (auth.isAdmin()) await Promise.allSettled([forum.loadAdmin(), auth.loadUsers()]);
    }),
  ],
};
