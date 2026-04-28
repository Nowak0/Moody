package app.moody.mapper;

import app.moody.dto.MoodReadDTO;
import app.moody.dto.MoodWriteDTO;
import app.moody.entity.Mood;
import org.springframework.stereotype.Component;

@Component
public class MoodMapper {
    public Mood toEntity(MoodWriteDTO dto) {
        return Mood.builder()
                .value(dto.getValue())
                .note(dto.getNote())
//                .date(dto.getDate())
                .build();
    }

    public MoodReadDTO toReadDTO(Mood mood) {
        return MoodReadDTO.builder()
                .id(mood.getId())
                .value(mood.getValue())
                .note(mood.getNote())
                .date(mood.getDate())
                .build();
    }
}
