package app.moody.dto;

import app.moody.entity.Trend;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class StatisticDTO {
    private float avgMood;
    private int streak;
    private Trend trend = Trend.CONST;
}
