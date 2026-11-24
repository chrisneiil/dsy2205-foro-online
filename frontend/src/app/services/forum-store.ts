import { Injectable, computed, inject, signal } from '@angular/core';
import { ForumApi } from '../core/api-clients';
import {
  Category,
  CommentResponse,
  ForumComment,
  ForumTopic,
  TopicResponse,
} from '../models/forum.models';

const categoryStyle: Record<string, { color: string; icon: string }> = {
  'Java y Spring': { color: 'coral', icon: '⌘' },
  'Bases de datos': { color: 'blue', icon: '◉' },
  Frontend: { color: 'violet', icon: '▤' },
  'DevOps y Git': { color: 'gold', icon: '☼' },
  'Ayuda y consultas': { color: 'green', icon: '?' },
};

@Injectable({ providedIn: 'root' })
export class ForumStore {
  private readonly api = inject(ForumApi);
  readonly categories = signal<Category[]>([]);
  readonly topics = signal<ForumTopic[]>([]);
  readonly comments = signal<ForumComment[]>([]);
  readonly visibleComments = computed(() => this.comments().filter((comment) => !comment.hidden));
  readonly adminTopics = signal<ForumTopic[]>([]);
  readonly adminComments = signal<ForumComment[]>([]);
  readonly visibleTopics = computed(() => this.topics().filter((topic) => !topic.hidden));
  readonly totalReplies = computed(() =>
    this.visibleTopics().reduce((total, topic) => total + topic.replies, 0),
  );
  readonly error = signal('');

  async load(): Promise<void> {
    try {
      const [categories, topics] = await Promise.all([this.api.categories(), this.api.topics()]);
      this.categories.set(
        categories.map((category) => ({
          ...category,
          ...(categoryStyle[category.name] ?? { color: 'pink', icon: '✳' }),
          topics: topics.filter((topic) => topic.categoryId === category.id).length,
        })),
      );
      this.topics.set(topics.map((topic) => this.toTopic(topic)));
      this.comments.set([]);
      this.error.set('');
    } catch {
      this.error.set('No se pudo cargar el foro. Comprueba la conexión con el backend y Oracle.');
    }
  }

  async loadAdmin(): Promise<void> {
    try {
      const [topics, comments] = await Promise.all([
        this.api.adminTopics(),
        this.api.adminComments(),
      ]);
      this.adminTopics.set(topics.map((topic) => this.toTopic(topic)));
      this.adminComments.set(comments.map((comment) => this.toComment(comment)));
    } catch {
      this.error.set('No se pudo cargar la información de moderación.');
    }
  }

  async loadComments(topicId: number): Promise<void> {
    const comments = await this.api.comments(topicId);
    this.comments.update((items) => [
      ...items.filter((comment) => comment.topicId !== topicId),
      ...comments.map((comment) => this.toComment(comment)),
    ]);
  }

  categoryName(categoryId: number): string {
    return this.categories().find((category) => category.id === categoryId)?.name ?? 'Categoría';
  }

  async createTopic(title: string, categoryId: number, body: string): Promise<void> {
    const topic = await this.api.createTopic({
      title: title.trim(),
      categoryId,
      body: body.trim(),
    });
    this.topics.update((items) => [this.toTopic(topic), ...items]);
    this.categories.update((items) =>
      items.map((category) =>
        category.id === categoryId ? { ...category, topics: category.topics + 1 } : category,
      ),
    );
  }

  async addComment(topicId: number, text: string): Promise<void> {
    const comment = await this.api.createComment(topicId, text.trim());
    this.comments.update((items) => [...items, this.toComment(comment)]);
    this.topics.update((items) =>
      items.map((topic) =>
        topic.id === topicId ? { ...topic, replies: topic.replies + 1 } : topic,
      ),
    );
  }

  async hideTopic(topicId: number): Promise<void> {
    await this.api.hideTopic(topicId);
    this.adminTopics.update((items) =>
      items.map((topic) => (topic.id === topicId ? { ...topic, hidden: true } : topic)),
    );
    this.topics.update((items) =>
      items.map((topic) => (topic.id === topicId ? { ...topic, hidden: true } : topic)),
    );
  }

  async hideComment(commentId: number): Promise<void> {
    await this.api.hideComment(commentId);
    this.adminComments.update((items) =>
      items.map((comment) => (comment.id === commentId ? { ...comment, hidden: true } : comment)),
    );
    this.comments.update((items) =>
      items.map((comment) => (comment.id === commentId ? { ...comment, hidden: true } : comment)),
    );
  }

  async restoreTopic(topicId: number): Promise<void> {
    const topic = await this.api.restoreTopic(topicId);
    const restored = this.toTopic(topic);
    this.adminTopics.update((items) => items.map((item) => (item.id === topicId ? restored : item)));
    this.topics.update((items) => items.map((item) => (item.id === topicId ? restored : item)));
  }

  async restoreComment(commentId: number): Promise<void> {
    await this.api.restoreComment(commentId);
    this.adminComments.update((items) =>
      items.map((comment) => (comment.id === commentId ? { ...comment, hidden: false } : comment)),
    );
    this.comments.update((items) =>
      items.map((comment) => (comment.id === commentId ? { ...comment, hidden: false } : comment)),
    );
  }

  private toTopic(topic: TopicResponse): ForumTopic {
    return {
      id: topic.id,
      categoryId: topic.categoryId,
      title: topic.title,
      excerpt: topic.body.length > 180 ? `${topic.body.slice(0, 177)}…` : topic.body,
      body: topic.body,
      author: topic.author,
      createdAt: this.formatDate(topic.createdAt),
      replies: topic.replies,
      pinned: false,
      hidden: topic.status === 'HIDDEN',
    };
  }

  private toComment(comment: CommentResponse): ForumComment {
    return {
      id: comment.id,
      topicId: comment.topicId,
      author: comment.author,
      text: comment.body,
      createdAt: this.formatDate(comment.createdAt),
      hidden: comment.status === 'HIDDEN',
    };
  }

  private formatDate(value: string): string {
    return new Intl.DateTimeFormat('es-CL', { dateStyle: 'short', timeStyle: 'short' }).format(
      new Date(value),
    );
  }
}
