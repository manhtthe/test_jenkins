package com.web.bookingKol.domain.kol.mappers;

import com.web.bookingKol.domain.kol.dtos.KolCreatedDTO;
import com.web.bookingKol.domain.kol.models.KolProfile;
import com.web.bookingKol.domain.user.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KolCreatedMapper {
    @Autowired
    private CategoryMapper categoryMapper;

    public KolCreatedDTO toDto(KolProfile kol, User user) {
        if (kol == null) return null;
        KolCreatedDTO dto = new KolCreatedDTO();
        dto.setId(kol.getId());
        dto.setUserId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setDisplayName(kol.getDisplayName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setGender(user.getGender());
        dto.setStatus(user.getStatus());
        dto.setDob(kol.getDob());
        dto.setBio(kol.getBio());
        dto.setExperience(kol.getExperience());
        dto.setCountry(kol.getCountry());
        dto.setCity(kol.getCity());
        dto.setLanguages(kol.getLanguages());
        dto.setRateCardNote(kol.getRateCardNote());
        dto.setMinBookingPrice(kol.getMinBookingPrice());
        dto.setIsAvailable(kol.getIsAvailable());
        dto.setCreatedAt(kol.getCreatedAt());

        dto.setCategories(categoryMapper.toDtoSet(kol.getCategories()));
        return dto;
    }
}
