export type UserRole = 'USER' | 'ADMIN';

export interface ForumUser {
  id: number;
  username: string;
  displayName: string;
  email: string;
  role: UserRole;
  active: boolean;
}

export interface Category {
  id: number;
  name: string;
  description: string;
  color: string;
  icon: string;
  topics: number;
}

export interface ForumComment {
  id: number;
  topicId: number;
  author: string;
  text: string;
  createdAt: string;
  hidden: boolean;
}

export interface ForumTopic {
  id: number;
  categoryId: number;
  title: string;
  excerpt: string;
  body: string;
  author: string;
  createdAt: string;
  replies: number;
  pinned: boolean;
  hidden: boolean;
}

export interface UserResponse extends ForumUser {
  createdAt: string;
}

export interface LoginResponse {
  token: string;
  tokenType: string;
  expiresInSeconds: number;
  user: UserResponse;
}

export interface TopicResponse {
  id: number;
  title: string;
  body: string;
  status: 'ACTIVE' | 'HIDDEN';
  categoryId: number;
  category: string;
  authorId: number;
  author: string;
  createdAt: string;
  updatedAt: string;
  replies: number;
}

export interface CategoryResponse {
  id: number;
  name: string;
  description: string;
}

export interface CommentResponse {
  id: number;
  body: string;
  status: 'ACTIVE' | 'HIDDEN';
  author: string;
  topicId: number;
  createdAt: string;
}
