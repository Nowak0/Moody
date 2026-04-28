package app.moody.dto;

import app.moody.entity.Trend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class StatisticDTO {
    private float avgMood;
    private int streak;
    private Trend trend = Trend.CONST;
}
