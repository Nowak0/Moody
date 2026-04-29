package app.moody.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MoodReadDTO {
    private UUID id;
    private int value;
    private String note;
    private LocalDateTime date;
    private String aiAdvice;
}
