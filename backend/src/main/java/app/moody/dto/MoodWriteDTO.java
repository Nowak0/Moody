package app.moody.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MoodWriteDTO {
    @Min(1)
    @Max(10)
    @NotNull
    private int value;

    private String note;

    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @PastOrPresent
    private LocalDateTime date;
}
