import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthStore } from '../services/auth-store';
import { ForumStore } from '../services/forum-store';

@Component({
  standalone: true,
  imports: [RouterLink],
  template: `
    <section class="admin-page">
      <a class="back-link" routerLink="/">← Volver al foro</a>
      <div class="admin-heading">
        <div>
          <span class="eyebrow">ESPACIO DE ADMINISTRACIÓN</span>
          <h1>Panel de comunidad</h1>
          <p class="muted">Cuida la conversación y mantén Nexo como un buen lugar para todos.</p>
        </div>
        <span class="admin-badge">✳ ADMINISTRADOR</span>
      </div>
      @if (forum.error()) {
        <p class="form-alert" role="alert">{{ forum.error() }}</p>
      }
      @if (error) {
        <p class="form-alert" role="alert">{{ error }}</p>
      }
      <div class="admin-stats">
        <article>
          <span>Miembros</span><strong>{{ auth.users().length }}</strong
          ><small>cuentas registradas</small>
        </article>
        <article>
          <span>Conversaciones visibles</span><strong>{{ activeTopicCount }}</strong
          ><small>temas publicados</small>
        </article>
        <article>
          <span>Respuestas visibles</span><strong>{{ visibleComments.length }}</strong
          ><small>en la comunidad</small>
        </article>
      </div>
      <div class="admin-columns">
        <section class="admin-panel card">
          <div class="admin-panel-heading">
            <div>
              <span class="eyebrow">REVISIÓN DE CONTENIDO</span>
              <h2>Conversaciones</h2>
            </div>
            <span class="heading-count">{{ forum.adminTopics().length }}</span>
          </div>
          @for (topic of forum.adminTopics(); track topic.id) {
            <article class="moderation-row">
              <div>
                <span class="topic-category">{{ forum.categoryName(topic.categoryId) }}</span>
                <h3>{{ topic.title }}</h3>
                <p>Por {{ topic.author }} · {{ topic.replies }} respuestas</p>
              </div>
              <button
                class="button button-danger"
                type="button"
                [disabled]="topic.hidden || busy"
                (click)="hideTopic(topic.id)"
              >
                {{ topic.hidden ? 'Oculto' : 'Ocultar' }}
              </button>
            </article>
          }
        </section>
        <section class="admin-panel card">
          <div class="admin-panel-heading">
            <div>
              <span class="eyebrow">CONVIVENCIA</span>
              <h2>Respuestas</h2>
            </div>
            <span class="heading-count">{{ visibleComments.length }}</span>
          </div>
          @for (comment of forum.adminComments(); track comment.id) {
            <article class="moderation-row moderation-comment">
              <div>
                <p>“{{ comment.text }}”</p>
                <small>{{ comment.author }} · Tema #{{ comment.topicId }}</small>
              </div>
              <button
                class="button button-danger"
                type="button"
                [disabled]="comment.hidden || busy"
                (click)="hideComment(comment.id)"
              >
                {{ comment.hidden ? 'Oculto' : 'Ocultar' }}
              </button>
            </article>
          }
        </section>
      </div>
      <section class="admin-note">
        <span>✓</span>
        <p>
          <strong>Las acciones de moderación son inmediatas.</strong> El contenido se oculta para la
          comunidad y permanece disponible en este panel.
        </p>
      </section>
    </section>
  `,
})
export class AdminPage {
  readonly forum = inject(ForumStore);
  readonly auth = inject(AuthStore);
  error = '';
  busy = false;
  get visibleComments() {
    return this.forum.adminComments().filter((comment) => !comment.hidden);
  }
  get activeTopicCount() {
    return this.forum.adminTopics().filter((topic) => !topic.hidden).length;
  }

  async hideTopic(id: number): Promise<void> {
    await this.moderate(() => this.forum.hideTopic(id));
  }

  async hideComment(id: number): Promise<void> {
    await this.moderate(() => this.forum.hideComment(id));
  }

  private async moderate(action: () => Promise<void>): Promise<void> {
    this.error = '';
    this.busy = true;
    try {
      await action();
    } catch (error) {
      this.error = this.auth.errorMessage(error);
    } finally {
      this.busy = false;
    }
  }
}
