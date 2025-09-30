package com.web.bookingKol.domain.file.repositories;

import com.web.bookingKol.domain.file.models.FileUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileUsageRepository extends JpaRepository<FileUsage, UUID> {
}
