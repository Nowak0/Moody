package app.moody.service;

import app.moody.dto.MoodReadDTO;
import app.moody.dto.MoodWriteDTO;
import app.moody.dto.StatisticDTO;
import app.moody.entity.Mood;
import app.moody.exception.ResourceNotFoundException;
import app.moody.mapper.MoodMapper;
import app.moody.repository.MoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MoodService {
    private final MoodRepository moodRepository;
    private final MoodMapper moodMapper;
    private final StatisticService statisticService;
    private final AIAdviceService aiAdviceService;

    public MoodReadDTO getMood(UUID id) {
        Mood mood = moodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mood of id " + id + " was not found"));
        return moodMapper.toReadDTO(mood);
    }

    public List<MoodReadDTO> getMoods() {
        return moodRepository.findAll()
                .stream()
                .map(moodMapper::toReadDTO)
                .collect(Collectors.toList());
    }

    public MoodReadDTO saveMood(MoodWriteDTO writeDTO) {
        if(writeDTO.getDate() == null) {
            writeDTO.setDate(LocalDateTime.now());
        }

        Mood mood = moodMapper.toEntity(writeDTO);
        Optional<String> aiAdvice = aiAdviceService.createAdvice(mood, statisticService.getStats());
        if(aiAdvice.isPresent()) {
            mood.setAiAdvice(aiAdvice.get());
        }

        mood = moodRepository.save(mood);
        return moodMapper.toReadDTO(mood);
    }

    public void deleteMood(UUID id) {
        if(!moodRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mood of id " + id + " was not found");
        }

        moodRepository.deleteById(id);
    }
}
