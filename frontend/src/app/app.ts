import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize, forkJoin } from 'rxjs';
import { MoodRead, MoodService, Statistic } from './mood.service';

@Component({
  selector: 'app-root',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  private readonly moodService = inject(MoodService);

  protected readonly view = signal<'log' | 'stats'>('log');
  protected readonly moods = signal<MoodRead[]>([]);
  protected readonly stats = signal<Statistic | null>(null);
  protected readonly isSubmitting = signal(false);
  protected readonly isLoading = signal(false);
  protected readonly deletingIds = signal<Set<string>>(new Set());
  protected readonly formError = signal<string | null>(null);
  protected readonly loadError = signal<string | null>(null);
  protected readonly deleteError = signal<string | null>(null);
  protected readonly successMessage = signal<string | null>(null);

  protected readonly moodForm = new FormGroup({
    value: new FormControl(5, {
      nonNullable: true,
      validators: [Validators.min(1), Validators.max(10)]
    }),
    note: new FormControl('', { nonNullable: false }),
    date: new FormControl(this.formatDateInput(new Date()), { nonNullable: true }),
    time: new FormControl(this.formatTimeInput(new Date()), { nonNullable: true })
  });

  constructor() {
    this.refreshDashboard();
  }

  protected selectView(view: 'log' | 'stats'): void {
    this.view.set(view);
    if (view === 'stats') {
      this.refreshDashboard();
    }
  }

  protected submitMood(): void {
    this.formError.set(null);
    this.successMessage.set(null);

    if (this.moodForm.invalid) {
      this.formError.set('Mood value must be between 1 and 10.');
      this.moodForm.markAllAsTouched();
      return;
    }

    const { value, note, date, time } = this.moodForm.getRawValue();
    const payload = {
      value,
      note: note?.trim(),
      date: `${date}T${time}:00`
    };

    this.isSubmitting.set(true);

    this.moodService
      .createMood(payload)
      .pipe(finalize(() => this.isSubmitting.set(false)))
      .subscribe({
        next: () => {
          const now = new Date();
          this.moodForm.patchValue({
            note: '',
            date: this.formatDateInput(now),
            time: this.formatTimeInput(now)
          });
          this.successMessage.set('Mood saved.');
          this.refreshDashboard();
        },
        error: () => {
          this.formError.set('Unable to save mood right now.');
        }
      });
  }

  protected refreshDashboard(): void {
    this.isLoading.set(true);
    this.loadError.set(null);
    this.deleteError.set(null);

    forkJoin({
      moods: this.moodService.getMoods(),
      stats: this.moodService.getStats()
    })
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: ({ moods, stats }) => {
          const sortedMoods = [...moods].sort(
            (first, second) => new Date(second.date).getTime() - new Date(first.date).getTime()
          );
          this.moods.set(sortedMoods);
          this.stats.set(stats);
        },
        error: () => {
          this.loadError.set('Unable to load stats or history');
        }
      });
  }

  protected deleteMood(id: string): void {
    this.deleteError.set(null);
    const updatedIds = new Set(this.deletingIds());
    updatedIds.add(id);
    this.deletingIds.set(updatedIds);

    this.moodService
        .deleteMood(id)
        .pipe(
            finalize(() => {
                const remaining = new Set(this.deletingIds());
                remaining.delete(id);
                this.deletingIds.set(remaining);
            })
        )
        .subscribe({
            next: () => {
                this.moods.set(this.moods().filter((mood) => mood.id !== id));
                this.refreshStats();
            },
            error: () => {
                this.deleteError.set('Unable to delete mood');
            }
        })
  }

  protected formatAvg(avg?: number): string {
    if (avg === undefined || avg === null || Number.isNaN(avg)) {
      return '—';
    }
    return avg.toFixed(1);
  }

  protected formatDateTime(value: string): string {
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) {
      return value;
    }
    return new Intl.DateTimeFormat('en', {
      dateStyle: 'medium',
      timeStyle: 'short'
    }).format(date);
  }

  protected trackById(_: number, mood: MoodRead): string {
    return mood.id;
  }

  protected isDeleting(id: string): boolean {
    return this.deletingIds().has(id);
  }

  private formatDateInput(date: Date): string {
    return date.toISOString().split('T')[0];
  }

  private formatTimeInput(date: Date): string {
    return date.toTimeString().slice(0, 5);
  }

  private refreshStats(): void {
    this.moodService.getStats().subscribe({
        next: (stats) => this.stats.set(stats),
        error: () => {
            this.stats.set(null);
            this.loadError.set('Unable to refresh stats or history')
        }
    });
  }
}
