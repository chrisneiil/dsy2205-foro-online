import { Routes } from '@angular/router';
import { FeedPage } from './pages/feed-page';
import { AdminPage } from './pages/admin-page';
import { LoginPage, RecoverPage, RegisterPage } from './pages/auth-pages';
import { ProfilePage } from './pages/profile-page';
import { TopicPage } from './pages/topic-page';
import { adminGuard } from './services/guards';

export const routes: Routes = [
  { path: '', component: FeedPage, title: 'Explorar · Nexo' },
  { path: 'topic/:id', component: TopicPage, title: 'Conversación · Nexo' },
  { path: 'login', component: LoginPage, title: 'Iniciar sesión · Nexo' },
  { path: 'register', component: RegisterPage, title: 'Crear cuenta · Nexo' },
  { path: 'recover', component: RecoverPage, title: 'Recuperar acceso · Nexo' },
  { path: 'profile', component: ProfilePage, title: 'Mi perfil · Nexo' },
  {
    path: 'admin',
    component: AdminPage,
    canActivate: [adminGuard],
    title: 'Administración · Nexo',
  },
  { path: '**', redirectTo: '' },
];
