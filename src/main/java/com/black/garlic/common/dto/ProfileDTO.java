package com.black.garlic.common.dto;

import com.black.garlic.common.entity.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

import java.util.LinkedHashMap;
import java.util.Objects;

public class ProfileDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class KakaoLoginToken {
        private String accessToken;
        private String tokenType;
        private String refreshToken;
        private String idToken;
        private Integer expiresIn;
        private String scope;
        private Integer refreshTokenExpiresIn;

        public KakaoLoginToken(LinkedHashMap<String, Object> response) {
            this.accessToken = (String) response.get("access_token");
            this.tokenType = (String) response.get("token_type");
            this.refreshToken = (String) response.get("refresh_token");
            this.idToken = (String) response.get("id_token");
            this.expiresIn = (Integer) response.get("expires_in");
            this.scope = (String) response.get("scope");
            this.refreshTokenExpiresIn = (Integer) response.get("refresh_token_expires_in");
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserInfo {
        protected String profileId;
        protected String nickname;
        protected String email;

        public UserInfo(String profileId, String nickname, String email) {
            this.profileId = profileId;
            this.nickname = nickname;
            this.email = email;
        }

        public UserInfo(Profile profile) {
            this.profileId = profile.getProfileId();
            this.nickname = profile.getNickname();
            this.email = profile.getEmail();
        }
    }

    @Getter
    @Setter
    public static class KakaoUserInfo extends UserInfo {
        public KakaoUserInfo(LinkedHashMap<String, Object> response) {
            super.profileId = String.valueOf(response.get("id"));

            LinkedHashMap<String, Object> kakaoAccount = (LinkedHashMap<String, Object>) response.get("kakao_account");

            if (kakaoAccount.get("email") != null) {
                super.email = (String) kakaoAccount.get("email");
            } else {
                super.email = "";
            }

            LinkedHashMap<String, Object> profile = (LinkedHashMap<String, Object>) kakaoAccount.get("profile");

            super.nickname = (String) profile.get("nickname");
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class NaverLoginToken {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private String expiresIn;

        public NaverLoginToken(LinkedHashMap<String, Object> response) {
            this.accessToken = (String) response.get("access_token");
            this.refreshToken = (String) response.get("refresh_token");
            this.tokenType = (String) response.get("token_type");
            this.expiresIn = (String) response.get("expires_in");
        }
    }

    @Getter
    @Setter
    public static class NaverUserInfo extends UserInfo {
        public NaverUserInfo (LinkedHashMap<String, Object> response) {
            LinkedHashMap<String, Object> naverAccount = (LinkedHashMap<String, Object>) response.get("response");

            super.profileId = (String) naverAccount.get("id");
            super.nickname = (String) naverAccount.get("nickname");
            super.email = (String) naverAccount.get("email");
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserInfoWithToken extends UserInfo {
        private String accessToken;
        private String refreshToken;

        public UserInfoWithToken(Profile profile, String accessToken, String refreshToken) {
            super.profileId = profile.getProfileId();
            super.nickname = profile.getNickname();
            super.email = profile.getEmail();
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}
