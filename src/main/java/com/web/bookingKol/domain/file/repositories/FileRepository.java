package com.web.bookingKol.domain.file.repositories;

import com.web.bookingKol.domain.file.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {
}
