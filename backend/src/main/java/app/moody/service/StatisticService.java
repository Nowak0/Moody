package app.moody.service;

import app.moody.dto.StatisticDTO;
import app.moody.entity.Mood;
import app.moody.entity.Trend;
import app.moody.repository.MoodRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class StatisticService {
    private final int DAYS_MEASUREMENT = 7;
    private final MoodRepository moodRepository;

    public StatisticService(MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
    }

    public StatisticDTO getStats() {
        float avgMood = getAverageMood();
        int streak = calculateStreak();
        Trend trend = calculateTrend();

        return StatisticDTO.builder()
                .avgMood(avgMood)
                .streak(streak)
                .trend(trend)
                .build();
    }

    private float getAverageMood() {
        LocalDateTime since = LocalDateTime.now().minusDays(DAYS_MEASUREMENT);

        Float result = moodRepository.getAverageMood(since);
        if(result == null) {
            return 0;
        } else {
            return result;
        }
    }

    private int calculateStreak() {
        int streak = 0;
        LocalDateTime now = LocalDateTime.now();
        List<Mood> moods = moodRepository.findAllByOrderByDateDesc();

        for(Mood mood : moods) {
            LocalDateTime date2 = mood.getDate();
            boolean withinOneDay = now.toLocalDate().isEqual(date2.toLocalDate())
                    || ChronoUnit.DAYS.between(now.toLocalDate(), date2.toLocalDate()) <= 1;

            if(!withinOneDay) {
                break;
            } else {
                streak++;
                now = date2;
            }
        }

        return streak;
    }

    private Trend calculateTrend() {
        List<Mood> moods = moodRepository.findTop2ByOrderByDateDesc();
        if (moods.size() <= 1) {
            return Trend.CONST;
        } else if (moods.get(0).getValue() > moods.get(1).getValue()) {
            return Trend.UP;
        } else if (moods.get(0).getValue() < moods.get(1).getValue()) {
            return Trend.DOWN;
        } else {
            return Trend.CONST;
        }
    }
}
