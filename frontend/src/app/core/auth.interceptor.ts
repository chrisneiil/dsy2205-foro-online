import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthTokenStore } from './auth-token.store';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const token = inject(AuthTokenStore).token();
  if (!token || (!request.url.startsWith('/users/') && !request.url.startsWith('/forum/'))) {
    return next(request);
  }

  return next(request.clone({ setHeaders: { Authorization: `Bearer ${token}` } }));
};
