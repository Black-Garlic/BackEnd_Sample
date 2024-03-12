package com.black.garlic.common.service.mapper;

import com.black.garlic.common.entity.Profile;
import com.black.garlic.common.dto.ProfileDTO;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class ProfileMapper {

    public ProfileDTO.UserInfo profileToProfileDTO(Profile profile) {
        return new ProfileDTO.UserInfo(profile);
    }
}
