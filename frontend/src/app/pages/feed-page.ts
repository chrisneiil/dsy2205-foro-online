import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthStore } from '../services/auth-store';
import { ForumStore } from '../services/forum-store';

@Component({
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    @if (forum.error()) {
      <p class="form-alert" role="alert">{{ forum.error() }}</p>
    }
    <section class="hero">
      <div class="hero-copy">
        <span class="eyebrow"><span class="live-dot"></span> UN ESPACIO PARA TODAS LAS IDEAS</span>
        <h1>Conversemos sobre<br /><em>lo que nos mueve.</em></h1>
        <p>Una comunidad abierta para preguntar, aprender y compartir lo que te importa.</p>
        <div class="hero-actions">
          <button class="button button-primary" type="button" (click)="openComposer()">
            ＋ Iniciar conversación</button
          ><a class="text-link" href="#categorias">Explorar temas <span>↓</span></a>
        </div>
        <div class="community-proof">
          <div class="avatar-stack"><span>✳</span><span>+</span></div>
          <span
            ><strong>{{ forum.categories().length }}</strong> espacios para conversar</span
          >
        </div>
      </div>
      <div class="hero-card">
        <div class="hero-card-top">
          <span class="eyebrow">PRINCIPIO DE NEXO</span><span class="spark">✳</span>
        </div>
        <div class="quote-mark">“</div>
        <h2>Las preguntas que compartimos nos acercan a respuestas que solos no encontraríamos.</h2>
        <div class="hero-card-author">
          <span class="avatar avatar-sand">N</span
          ><span><strong>Comunidad Nexo</strong><small>Aprendemos en conjunto</small></span>
        </div>
        <div class="hero-card-decoration"></div>
      </div>
    </section>

    <section class="category-section" id="categorias">
      <div class="section-heading">
        <div>
          <span class="eyebrow">ENCUENTRA TU ESPACIO</span>
          <h2>
            Categorías <span class="heading-count">{{ forum.categories().length }}</span>
          </h2>
        </div>
        <a class="text-link" href="#conversaciones">Ver todas <span>→</span></a>
      </div>
      <div class="category-grid row g-2">
        @for (category of forum.categories(); track category.id) {
          <div class="col-6 col-lg-4">
            <button
              class="category-card"
              [class.category-selected]="activeCategory === category.id"
              type="button"
              (click)="toggleCategory(category.id)"
            >
              <span class="category-icon" [class]="'category-icon ' + category.color">{{
                category.icon
              }}</span><span class="category-content"
                ><strong>{{ category.name }}</strong
                ><small>{{ category.description }}</small
                ><em>{{ category.topics }} conversaciones</em></span
              ><span class="card-arrow">↗</span>
            </button>
          </div>
        }
      </div>
    </section>

    <section class="discussion-layout" id="conversaciones">
      <div class="discussion-main">
        <div class="section-heading discussion-heading">
          <div>
            <span class="eyebrow">LA CONVERSACIÓN SIGUE</span>
            <h2>Lo más reciente</h2>
          </div>
          <div class="sort-tabs">
            <button [class.selected]="sortMode === 'recent'" (click)="sortMode = 'recent'">
              Recientes</button
            ><button [class.selected]="sortMode === 'popular'" (click)="sortMode = 'popular'">
              Populares
            </button>
          </div>
        </div>
        <div class="search-row">
          <label class="search-box"
            ><span>⌕</span
            ><input
              [(ngModel)]="query"
              name="query"
              placeholder="Buscar conversaciones..."
              aria-label="Buscar conversaciones" /></label
          ><button class="filter-button" type="button" (click)="activeCategory = null">
            {{
              activeCategory ? forum.categoryName(activeCategory) + ' ×' : 'Todas las categorías⌄'
            }}
          </button>
        </div>
        @if (composerOpen) {
          <form class="composer card" #topicForm="ngForm" (ngSubmit)="publish()" novalidate>
            <div class="composer-heading">
              <div>
                <span class="eyebrow">NUEVA CONVERSACIÓN</span>
                <h3>¿Qué te gustaría compartir?</h3>
              </div>
              <button
                class="icon-button"
                type="button"
                aria-label="Cerrar"
                (click)="composerOpen = false"
              >
                ×
              </button>
            </div>
            @if (!auth.currentUser()) {
              <p class="form-note">
                Inicia sesión para publicar. <a routerLink="/login">Entrar a Nexo</a>
              </p>
            }
            @if (publishError) {
              <p class="form-alert" role="alert">{{ publishError }}</p>
            }
            <label for="topic-title">Título</label
            ><input
              id="topic-title"
              name="title"
              [(ngModel)]="draftTitle"
              required
              minlength="8"
              maxlength="120"
              placeholder="Resume tu idea en una frase"
              #titleField="ngModel"
            />
            @if (titleField.invalid && titleField.touched) {
              <small class="field-error">El título debe tener entre 8 y 120 caracteres.</small>
            }
            <label for="topic-category">Categoría</label
            ><select id="topic-category" name="category" [(ngModel)]="draftCategory" required>
              <option [ngValue]="null" disabled>Elige una categoría</option>
              @for (category of forum.categories(); track category.id) {
                <option [ngValue]="category.id">{{ category.name }}</option>
              }
            </select>
            <label for="topic-body">Tu mensaje</label
            ><textarea
              id="topic-body"
              name="body"
              [(ngModel)]="draftBody"
              required
              minlength="20"
              maxlength="2000"
              rows="4"
              placeholder="Cuéntanos un poco más..."
              #bodyField="ngModel"
            ></textarea>
            @if (bodyField.invalid && bodyField.touched) {
              <small class="field-error">Escribe entre 20 y 2000 caracteres.</small>
            }
            <div class="composer-footer">
              <small>Al publicar, aceptas las normas de convivencia de Nexo.</small
              ><button
                class="button button-primary"
                type="submit"
                [disabled]="topicForm.invalid || !auth.currentUser()"
              >
                Publicar conversación
              </button>
            </div>
          </form>
        }
        <div class="topic-list">
          @for (topic of filteredTopics; track topic.id) {
            <article class="topic-card" [class.topic-pinned]="topic.pinned">
              <span class="topic-avatar" [class]="'topic-avatar avatar-tone-' + (topic.id % 5)">{{
                initials(topic.author)
              }}</span>
              <div class="topic-content">
                <div class="topic-meta">
                  <span class="topic-category">{{ forum.categoryName(topic.categoryId) }}</span>
                  @if (topic.pinned) {
                    <span class="pinned-label">✳ Destacado</span>
                  }
                  <span class="topic-date">{{ topic.createdAt }}</span>
                </div>
                <a class="topic-title" [routerLink]="['/topic', topic.id]">{{ topic.title }}</a>
                <p>{{ topic.excerpt }}</p>
                <div class="topic-footer">
                  <span
                    >por <strong>{{ topic.author }}</strong></span
                  ><span class="topic-stats"
                    ><span>◌ {{ topic.replies }} respuestas</span></span
                  >
                </div>
              </div>
              <a
                class="topic-open"
                [routerLink]="['/topic', topic.id]"
                aria-label="Abrir conversación"
                >↗</a
              >
            </article>
          } @empty {
            <div class="empty-state">
              <span>⌕</span>
              <h3>No encontramos conversaciones</h3>
              <p>Prueba otro término o categoría.</p>
              <button class="button button-quiet" (click)="clearFilters()">Limpiar filtros</button>
            </div>
          }
        </div>
        <button class="load-more" type="button" (click)="showAll = !showAll">
          {{ showAll ? 'Mostrar menos' : 'Cargar más conversaciones' }} <span>↓</span>
        </button>
      </div>
      <aside class="sidebar">
        <div class="welcome-card">
          <span class="eyebrow">BIENVENIDA, BIENVENIDO</span>
          <h3>Tu próxima conversación empieza aquí.</h3>
          <p>Cuéntale a la comunidad qué tienes en mente.</p>
          <button class="button button-dark button-wide" type="button" (click)="openComposer()">
            Escribir una publicación <span>→</span>
          </button>
          <div class="welcome-symbol">n</div>
        </div>
        <div class="side-panel">
          <div class="side-panel-heading">
            <h3>En comunidad</h3>
            <span class="online-indicator">● Actualizado</span>
          </div>
          <div class="stat-row">
            <span>Conversaciones</span><strong>{{ forum.visibleTopics().length }}</strong>
          </div>
          <div class="stat-row">
            <span>Respuestas</span><strong>{{ forum.totalReplies() }}</strong>
          </div>
          <div class="stat-row">
            <span>Categorías</span><strong>{{ forum.categories().length }}</strong>
          </div>
        </div>
        <div class="community-note">
          <span>✳</span>
          <p>
            <strong>Hagamos de Nexo un buen lugar.</strong> Escucha, respeta y aporta a la
            conversación.
          </p>
          <a href="#normas">Lee nuestras normas →</a>
        </div>
      </aside>
    </section>
    <section class="community-rules" id="normas">
      <span>✳</span>
      <div>
        <strong>Una comunidad que escucha crece mejor.</strong>
        <p>Sé amable, cuida los datos personales y comparte desde el respeto.</p>
      </div>
      <a class="text-link" href="#top">Conocer las normas <span>→</span></a>
    </section>
  `,
})
export class FeedPage {
  readonly forum = inject(ForumStore);
  readonly auth = inject(AuthStore);
  private readonly router = inject(Router);
  activeCategory: number | null = null;
  query = '';
  sortMode: 'recent' | 'popular' = 'recent';
  composerOpen = false;
  showAll = false;
  draftTitle = '';
  draftBody = '';
  draftCategory: number | null = null;
  publishError = '';
  publishing = false;

  get filteredTopics() {
    const query = this.query.trim().toLowerCase();
    const topics = this.forum
      .visibleTopics()
      .filter(
        (topic) =>
          (!this.activeCategory || topic.categoryId === this.activeCategory) &&
          (!query ||
            `${topic.title} ${topic.excerpt} ${topic.author}`.toLowerCase().includes(query)),
      );
    if (this.sortMode === 'popular') topics.sort((a, b) => b.replies - a.replies);
    return this.showAll ? topics : topics.slice(0, 5);
  }

  toggleCategory(categoryId: number): void {
    this.activeCategory = this.activeCategory === categoryId ? null : categoryId;
  }
  clearFilters(): void {
    this.query = '';
    this.activeCategory = null;
  }
  initials(name: string): string {
    return name
      .split(' ')
      .slice(0, 2)
      .map((part) => part.charAt(0))
      .join('')
      .toUpperCase();
  }

  openComposer(): void {
    if (!this.auth.currentUser()) {
      void this.router.navigate(['/login']);
      return;
    }
    this.composerOpen = !this.composerOpen;
    document.getElementById('conversaciones')?.scrollIntoView({ behavior: 'smooth' });
  }

  async publish(): Promise<void> {
    const user = this.auth.currentUser();
    if (!user || !this.draftCategory) return;
    this.publishError = '';
    this.publishing = true;
    try {
      await this.forum.createTopic(this.draftTitle, this.draftCategory, this.draftBody);
      this.draftTitle = '';
      this.draftBody = '';
      this.draftCategory = null;
      this.composerOpen = false;
    } catch (error) {
      this.publishError = this.auth.errorMessage(error);
    } finally {
      this.publishing = false;
    }
  }
}
