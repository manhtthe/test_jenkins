package com.web.bookingKol.domain.admin;

import com.web.bookingKol.domain.kol.dtos.NewKolDTO;
import com.web.bookingKol.domain.kol.dtos.UpdateKolDTO;
import com.web.bookingKol.domain.kol.services.KolProfileService;
import com.web.bookingKol.domain.user.models.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("admin/kol")
public class AdminKolRestController {
    @Autowired
    private KolProfileService kolProfileService;

    @PostMapping("/create-new-kol")
    public ResponseEntity<?> createNewKolAccount(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @RequestPart(value = "fileAvatar", required = false) MultipartFile fileAvatar,
                                                 @RequestPart @Valid NewKolDTO newKolDTO) {
        UUID AdminId = userDetails.getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(kolProfileService.createNewKolAccount(AdminId, newKolDTO, fileAvatar));
    }

    @PutMapping("/update/{kolId}")
    public ResponseEntity<?> updateKolProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable UUID kolId,
                                              @RequestPart(value = "fileAvatar", required = false) MultipartFile fileAvatar,
                                              @RequestPart(value = "updateKolDTO", required = false) UpdateKolDTO updateKolDTO) {
        UUID AdminId = userDetails.getId();
        return ResponseEntity.status(HttpStatus.OK)
                .body(kolProfileService.updateKolProfile(AdminId, kolId, updateKolDTO, fileAvatar));
    }

    @PostMapping("/medias/upload/{kolId}")
    public ResponseEntity<?> uploadKolImagePortfolio(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable UUID kolId,
                                                     @RequestParam("files") List<MultipartFile> files) {
        UUID AdminId = userDetails.getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(kolProfileService.uploadKolImagePortfolio(AdminId, kolId, files));
    }

    @GetMapping("/medias/all/{kolId}")
    public ResponseEntity<?> getAllKol(@PathVariable UUID kolId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(kolProfileService.getAllKolMediaFiles(kolId));
    }

    @PutMapping("/medias/activate/{kolId}")
    public ResponseEntity<?> activateKolMediaFile(@PathVariable UUID kolId,
                                                  @RequestParam List<UUID> fileUsageIds) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(kolProfileService.activateOrDeactivateKolMediaFile(kolId, fileUsageIds, true));
    }

    @PutMapping("/medias/deactivate/{kolId}")
    public ResponseEntity<?> deactivateKolMediaFile(@PathVariable UUID kolId,
                                                    @RequestParam List<UUID> fileUsageIds) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(kolProfileService.activateOrDeactivateKolMediaFile(kolId, fileUsageIds, false));
    }
}
