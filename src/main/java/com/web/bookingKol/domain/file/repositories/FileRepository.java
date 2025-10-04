package com.web.bookingKol.domain.file.repositories;

import com.web.bookingKol.domain.file.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {

    @Query("""
            SELECT f FROM File f
            LEFT JOIN FETCH f.fileUsages fu
            WHERE f.fileType IN ('IMAGE', 'VIDEO') AND f.status = 'ACTIVE'
            AND fu.targetId = :kolId AND fu.isActive = true ORDER BY f.createdAt DESC
            """)
    List<File> findAllImageAndVideoActiveByKolId(@Param("kolId") UUID kolId);
}
