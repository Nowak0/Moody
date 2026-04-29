import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface MoodRead {
  id: string;
  value: number;
  note: string;
  date: string;
  aiAdvice?: string;
}

export interface MoodWrite {
  value: number;
  note?: string;
  date: string;
}

export interface Statistic {
  avgMood: number;
  streak: number;
  trend: string;
}

@Injectable({
  providedIn: 'root'
})
export class MoodService {
  private readonly baseUrl = '/api/moods';

  constructor(private readonly http: HttpClient) {}

  getMoods(): Observable<MoodRead[]> {
    return this.http.get<MoodRead[]>(this.baseUrl);
  }

  getStats(): Observable<Statistic> {
    return this.http.get<Statistic>(`${this.baseUrl}/statistics`);
  }

  createMood(payload: MoodWrite): Observable<MoodRead> {
    return this.http.post<MoodRead>(this.baseUrl, payload);
  }

  deleteMood(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
