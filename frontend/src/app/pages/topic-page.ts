import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ForumTopic } from '../models/forum.models';
import { AuthStore } from '../services/auth-store';
import { ForumStore } from '../services/forum-store';

@Component({
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <a class="back-link page-back" routerLink="/">← Volver a conversaciones</a>
    @if (topic(); as item) {
      <section class="topic-detail-layout">
        <article class="topic-detail card">
          <div class="topic-detail-meta">
            <span class="topic-category">{{ forum.categoryName(item.categoryId) }}</span><span>{{
              item.createdAt
            }}</span>
            @if (item.pinned) {
              <span class="pinned-label">✳ Destacado</span>
            }
          </div>
          <h1>{{ item.title }}</h1>
          <p class="detail-byline">
            Publicado por <strong>{{ item.author }}</strong> · {{ item.createdAt }}
          </p>
          <div class="detail-divider"></div>
          <p class="topic-body">{{ item.body }}</p>
          <div class="detail-actions">
            <span>◌ {{ item.replies }} respuestas</span
            ><button class="button button-quiet" type="button" (click)="share()">
              ↗ Compartir
            </button>
          </div>
          <section class="comments-section">
            <div class="section-heading">
              <div>
                <span class="eyebrow">LA CONVERSACIÓN CONTINÚA</span>
                <h2>
                  Respuestas <span class="heading-count">{{ visibleComments.length }}</span>
                </h2>
              </div>
            </div>
            @for (comment of visibleComments; track comment.id) {
              <article class="comment-card">
                <span class="topic-avatar avatar-tone-{{ comment.id % 5 }}">{{
                  initials(comment.author)
                }}</span>
                <div>
                  <div class="comment-meta">
                    <strong>{{ comment.author }}</strong><span>{{ comment.createdAt }}</span>
                  </div>
                  <p>{{ comment.text }}</p>
                </div>
              </article>
            }
            @if (!visibleComments.length) {
              <p class="muted">Aún no hay respuestas. Inicia la conversación.</p>
            }
            @if (auth.currentUser(); as user) {
              <form class="reply-form" #replyForm="ngForm" (ngSubmit)="reply()" novalidate>
                <label for="reply-text">Escribe tu respuesta, {{ user.displayName }}</label
                ><textarea
                  id="reply-text"
                  name="reply"
                  [(ngModel)]="replyText"
                  required
                  minlength="3"
                  maxlength="1000"
                  rows="4"
                  placeholder="Aporta a la conversación..."
                  #replyField="ngModel"
                ></textarea>
                @if (replyField.invalid && replyField.touched) {
                  <small class="field-error"
                    >La respuesta debe tener entre 3 y 1000 caracteres.</small
                  >
                }
                @if (replyError) {
                  <p class="form-alert" role="alert">{{ replyError }}</p>
                }
                <div class="reply-actions">
                  <span class="muted">Mantengamos un tono respetuoso.</span
                  ><button
                    class="button button-primary"
                    type="submit"
                    [disabled]="replyForm.invalid || submitting"
                  >
                    {{ submitting ? 'Publicando…' : 'Publicar respuesta' }}
                  </button>
                </div>
              </form>
            } @else {
              <div class="signin-prompt">
                <span>✳</span>
                <div>
                  <strong>¿Tienes algo que aportar?</strong>
                  <p>Inicia sesión para responder a esta conversación.</p>
                </div>
                <a class="button button-primary" routerLink="/login">Iniciar sesión</a>
              </div>
            }
          </section>
        </article>
        <aside class="sidebar topic-sidebar">
          <div class="side-panel">
            <span class="eyebrow">SOBRE ESTA CONVERSACIÓN</span>
            <h3>{{ forum.categoryName(item.categoryId) }}</h3>
            <p>Un espacio para compartir ideas, aprender y ayudarnos entre todos.</p>
            <div class="stat-row">
              <span>Respuestas</span><strong>{{ item.replies }}</strong>
            </div>
            <div class="stat-row">
              <span>Estado</span><strong>{{ item.hidden ? 'Oculto' : 'Activo' }}</strong>
            </div>
          </div>
          <a class="button button-primary button-wide" routerLink="/">Explorar más temas →</a>
        </aside>
      </section>
    } @else {
      <div class="empty-state">
        <h2>Esta conversación no está disponible</h2>
        <a class="button button-primary" routerLink="/">Volver al foro</a>
      </div>
    }
  `,
})
export class TopicPage {
  readonly forum = inject(ForumStore);
  readonly auth = inject(AuthStore);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  readonly topic = signal<ForumTopic | undefined>(undefined);
  replyText = '';
  replyError = '';
  submitting = false;

  constructor() {
    this.route.paramMap.subscribe((params) => {
      const item = this.forum
        .topics()
        .find((topic) => topic.id === Number(params.get('id')) && !topic.hidden);
      this.topic.set(item);
      if (item) void this.loadComments(item.id);
    });
  }

  get visibleComments() {
    const item = this.topic();
    return item
      ? this.forum.comments().filter((comment) => comment.topicId === item.id && !comment.hidden)
      : [];
  }

  initials(name: string): string {
    return name
      .split(' ')
      .slice(0, 2)
      .map((part) => part.charAt(0))
      .join('')
      .toUpperCase();
  }

  private async loadComments(topicId: number): Promise<void> {
    try {
      await this.forum.loadComments(topicId);
    } catch {
      this.replyError = 'No se pudieron cargar las respuestas de esta conversación.';
    }
  }

  async reply(): Promise<void> {
    const item = this.topic();
    const user = this.auth.currentUser();
    if (!item || !user) return;
    this.replyError = '';
    this.submitting = true;
    try {
      await this.forum.addComment(item.id, this.replyText);
      this.replyText = '';
    } catch (error) {
      this.replyError = this.auth.errorMessage(error);
    } finally {
      this.submitting = false;
    }
  }
  share(): void {
    void navigator.clipboard?.writeText(window.location.href);
  }
}
