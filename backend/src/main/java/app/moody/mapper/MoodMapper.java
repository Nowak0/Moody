package app.moody.mapper;

import app.moody.dto.MoodReadDTO;
import app.moody.dto.MoodWriteDTO;
import app.moody.entity.Mood;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MoodMapper {
    Mood toEntity(MoodWriteDTO writeDTO);
    MoodReadDTO toReadDTO(Mood mood);
}
