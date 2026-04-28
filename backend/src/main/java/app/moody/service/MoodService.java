package app.moody.service;

import app.moody.dto.MoodReadDTO;
import app.moody.dto.MoodWriteDTO;
import app.moody.entity.Mood;
import app.moody.exception.ResourceNotFoundException;
import app.moody.mapper.MoodMapper;
import app.moody.repository.MoodRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MoodService {
    private final MoodRepository moodRepository;
    private final MoodMapper moodMapper;

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
        Mood mood = moodRepository.save(moodMapper.toEntity(writeDTO));
        return moodMapper.toReadDTO(mood);
    }

    public void deleteMood(UUID id) {
        if(!moodRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mood of id " + id + " was not found");
        }

        moodRepository.deleteById(id);
    }
}
