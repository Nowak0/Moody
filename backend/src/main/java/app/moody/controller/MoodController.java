package app.moody.controller;

import app.moody.dto.MoodReadDTO;
import app.moody.dto.MoodWriteDTO;
import app.moody.dto.StatisticDTO;
import app.moody.service.MoodService;
import app.moody.service.StatisticService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/moods")
@AllArgsConstructor
public class MoodController {
    private final MoodService moodService;
    private final StatisticService statisticService;

    @GetMapping("/{id}")
    public ResponseEntity<MoodReadDTO> getMood(@PathVariable UUID id) {
        return ResponseEntity.ok(moodService.getMood(id));
    }

    @GetMapping
    public ResponseEntity<List<MoodReadDTO>> getMoods() {
        return ResponseEntity.ok(moodService.getMoods());
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticDTO> getStatistics() {
        return ResponseEntity.ok(statisticService.getStats());
    }

    @PostMapping
    public ResponseEntity<MoodReadDTO> saveMood(@RequestBody @Valid MoodWriteDTO writeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(moodService.saveMood(writeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMood(@PathVariable UUID id) {
        moodService.deleteMood(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
