package com.black.garlic.common.service;

import com.black.garlic.common.constant.Constant;
import com.black.garlic.common.entity.Profile;
import com.black.garlic.common.repository.ProfileRepository;
import com.black.garlic.common.dto.ProfileDTO;
import com.black.garlic.common.service.mapper.ProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Transactional
    public ProfileDTO.UserInfoWithToken userLogin(ProfileDTO.UserInfo userInfo, Constant.LoginType loginType, String accessToken, String refreshToken) {
        Optional<Profile> profileOptional = checkProfileExist(userInfo.getProfileId());

        if (profileOptional.isPresent()) {
            return this.updateProfile(profileOptional.get(), userInfo, accessToken, refreshToken);
        } else {
            return this.createProfile(userInfo, loginType, accessToken, refreshToken);
        }
    }

    public ProfileDTO.UserInfo getProfile(String profileId) {
        Optional<Profile> profileOptional = this.checkProfileExist(profileId);

        if (profileOptional.isPresent()) {
            return profileMapper.profileToProfileDTO(profileOptional.get());
        } else {
            return new ProfileDTO.UserInfo();
        }
    }

    public Optional<Profile> checkProfileExist(String profileId) {
        return profileRepository.findByProfileId(profileId);
    }

    @Transactional
    public ProfileDTO.UserInfoWithToken updateProfile(Profile profile, ProfileDTO.UserInfo userInfo, String accessToken, String refreshToken) {
        boolean changed = false;

        if (!userInfo.getNickname().equals(profile.getNickname())){
            changed = true;
            profile.setNickname(userInfo.getNickname());
        }

        if (userInfo.getEmail() != null && !userInfo.getEmail().equals(profile.getEmail())) {
            changed = true;
            profile.setEmail(userInfo.getEmail());
        }

        if (changed) {
            profileRepository.save(profile);
        }

        return new ProfileDTO.UserInfoWithToken(profile, accessToken, refreshToken);
    }

    @Transactional
    public ProfileDTO.UserInfoWithToken createProfile(ProfileDTO.UserInfo userInfo, Constant.LoginType loginType, String accessToken, String refreshToken) {
        Profile profile = new Profile();

        profile.setProfileId(userInfo.getProfileId());
        profile.setNickname(userInfo.getNickname());
        profile.setEmail(userInfo.getEmail());
        profile.setLoginType(loginType);
        profile.setCreatedBy(userInfo.getProfileId());
        profile.setModifiedBy(userInfo.getProfileId());

        profileRepository.save(profile);

        return new ProfileDTO.UserInfoWithToken(profile, accessToken, refreshToken);
    }

    @Transactional
    public void deleteProfile(String profileId) {
        profileRepository.deleteById(profileId);
    }
}
