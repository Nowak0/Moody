package app.moody.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Mood {
    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false, name="mood_value")
    private int value;

    @Column(name="mood_note")
    private String note;

    @Column(nullable = false, name="mood_date")
    private LocalDateTime date;

    @Column
    private String aiAdvice;
}
