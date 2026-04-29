import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { App } from './app';
import { MoodService } from './mood.service';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        {
          provide: MoodService,
          useValue: {
            getMoods: () => of([]),
            getStats: () => of({ avgMood: 0, streak: 0, trend: 'CONST' }),
            createMood: () =>
              of({
                id: '1',
                value: 5,
                note: '',
                date: '2024-01-01T00:00:00'
              })
          }
        }
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Moody');
  });
});
