package com.web.bookingKol.domain.file.mappers;

import com.web.bookingKol.domain.file.dtos.FileUsageDTO;
import com.web.bookingKol.domain.file.models.FileUsage;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface FileUsageMapper {
    FileUsageDTO toDto(FileUsage fileUsage);

    List<FileUsageDTO> toDtoList(List<FileUsage> fileUsages);

    Set<FileUsageDTO> toDtoSet(Set<FileUsage> fileUsages);
}
