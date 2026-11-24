import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import {
  CategoryResponse,
  CommentResponse,
  LoginResponse,
  TopicResponse,
  UserResponse,
} from '../models/forum.models';

const usersApi = '/users/api';
const forumApi = '/forum/api';
const moderationApi = '/admin/api';

@Injectable({ providedIn: 'root' })
export class UsersApi {
  private readonly http = inject(HttpClient);

  register(request: {
    username: string;
    email: string;
    displayName: string;
    password: string;
  }): Promise<UserResponse> {
    return firstValueFrom(this.http.post<UserResponse>(`${usersApi}/auth/register`, request));
  }

  login(username: string, password: string): Promise<LoginResponse> {
    return firstValueFrom(
      this.http.post<LoginResponse>(`${usersApi}/auth/login`, { username, password }),
    );
  }

  currentUser(): Promise<UserResponse> {
    return firstValueFrom(this.http.get<UserResponse>(`${usersApi}/auth/me`));
  }

  updateUser(
    id: number,
    request: { email: string; displayName: string; password?: string },
  ): Promise<UserResponse> {
    return firstValueFrom(this.http.put<UserResponse>(`${usersApi}/users/${id}`, request));
  }

  listUsers(): Promise<UserResponse[]> {
    return firstValueFrom(this.http.get<UserResponse[]>(`${usersApi}/users`));
  }
}

@Injectable({ providedIn: 'root' })
export class ForumApi {
  private readonly http = inject(HttpClient);

  categories(): Promise<CategoryResponse[]> {
    return firstValueFrom(this.http.get<CategoryResponse[]>(`${forumApi}/categories`));
  }

  topics(): Promise<TopicResponse[]> {
    return firstValueFrom(this.http.get<TopicResponse[]>(`${forumApi}/topics`));
  }

  topic(id: number): Promise<TopicResponse> {
    return firstValueFrom(this.http.get<TopicResponse>(`${forumApi}/topics/${id}`));
  }

  comments(topicId: number): Promise<CommentResponse[]> {
    return firstValueFrom(
      this.http.get<CommentResponse[]>(`${forumApi}/topics/${topicId}/comments`),
    );
  }

  createTopic(request: {
    title: string;
    body: string;
    categoryId: number;
  }): Promise<TopicResponse> {
    return firstValueFrom(this.http.post<TopicResponse>(`${forumApi}/topics`, request));
  }

  createComment(topicId: number, body: string): Promise<CommentResponse> {
    return firstValueFrom(
      this.http.post<CommentResponse>(`${forumApi}/topics/${topicId}/comments`, { body }),
    );
  }

  adminTopics(): Promise<TopicResponse[]> {
    return firstValueFrom(this.http.get<TopicResponse[]>(`${moderationApi}/admin/topics`));
  }

  adminComments(): Promise<CommentResponse[]> {
    return firstValueFrom(this.http.get<CommentResponse[]>(`${moderationApi}/admin/comments`));
  }

  hideTopic(id: number): Promise<TopicResponse> {
    return firstValueFrom(this.http.put<TopicResponse>(`${moderationApi}/admin/topics/${id}/hide`, {}));
  }

  hideComment(id: number): Promise<void> {
    return firstValueFrom(this.http.put<void>(`${moderationApi}/admin/comments/${id}/hide`, {}));
  }
}
