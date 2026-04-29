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
import java.util.stream.Collectors;

@Service
public class StatisticService {
    private static final int DAYS_MEASUREMENT = 7;
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
        List<LocalDate> moodDates = moodRepository.findAllByOrderByDateDesc()
                .stream()
                .map(mood -> mood.getDate().toLocalDate())
                .distinct()
                .collect(Collectors.toList());

        if(moodDates.isEmpty()) {
            return 0;
        }

        LocalDate today = LocalDate.now();
        if(!moodDates.get(0).isEqual(today)) {
            return 0;
        }

        int streak = 1;
        for(int i = 1; i<moodDates.size(); i++) {
            long daysBetween = ChronoUnit.DAYS.between(moodDates.get(i), moodDates.get(i-1));
            if(daysBetween != 1) {
                break;
            } else {
                streak++;
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
