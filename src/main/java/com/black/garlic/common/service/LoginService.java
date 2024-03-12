package com.black.garlic.common.service;

import com.black.garlic.common.constant.Constant;
import com.black.garlic.common.controller.vm.LoginVM;
import com.black.garlic.common.dto.ProfileDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.LinkedHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {

    @Value("${kakao.token.endpoint}")
    private String KAKAO_TOKEN_ENDPOINT;
    @Value("${kakao.user-info.endpoint}")
    private String KAKAO_USER_INFO_ENDPOINT;
    @Value("${kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${naver.token.endpoint}")
    private String NAVER_TOKEN_ENDPOINT;
    @Value("${naver.user-info.endpoint}")
    private String NAVER_USER_INFO_ENDPOINT;
    @Value("${naver.client-id}")
    private String NAVER_CLIENT_ID;
    @Value("${naver.client-secret-id}")
    private String NAVER_CLIENT_SECRET_ID;
    @Value("${login-redirect.endpoint}")
    private String REDIRECT_URI;

    private final ProfileService profileService;
    private final RequestService requestService;

    @Transactional
    public ProfileDTO.UserInfoWithToken login(LoginVM.LoginParam param) {
        if (param.getLoginType().toUpperCase().equals(Constant.LoginType.KAKAO.toString())) {
            return this.fetchKakaoToken(param);
        } else if (param.getLoginType().toUpperCase().equals(Constant.LoginType.NAVER.toString())) {
            return this.fetchNaverToken(param);
        } else {
            return new ProfileDTO.UserInfoWithToken();
        }
    }

    @Transactional
    public ProfileDTO.UserInfoWithToken fetchKakaoToken(LoginVM.LoginParam param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("grant_type=authorization_code");
        stringBuilder.append("&client_id=" + KAKAO_CLIENT_ID);
        stringBuilder.append("&redirect_uri=" + REDIRECT_URI + "/kakao");
        stringBuilder.append("&code=").append(param.getCode());

        try {
            Object response = requestService.fetchRequestByString(headers, stringBuilder.toString(), KAKAO_TOKEN_ENDPOINT, HttpMethod.POST);

            ProfileDTO.KakaoLoginToken kakaoLoginTokenDTO = new ProfileDTO.KakaoLoginToken((LinkedHashMap<String, Object>) response);

            return this.fetchKakaoUserInfo(kakaoLoginTokenDTO.getAccessToken(), kakaoLoginTokenDTO.getRefreshToken());
        } catch (Exception e) {
            return new ProfileDTO.UserInfoWithToken();
        }
    }

    @Transactional
    public ProfileDTO.UserInfoWithToken fetchKakaoUserInfo(String accessToken, String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("property_keys", "[\"kakao_account.profile\",\"kakao_account.email\"]");

        try {
            Object response = requestService.fetchRequest(headers, params, KAKAO_USER_INFO_ENDPOINT, HttpMethod.POST);

            ProfileDTO.KakaoUserInfo kakaoUserInfoDTO = new ProfileDTO.KakaoUserInfo((LinkedHashMap<String, Object>) response);

            return profileService.userLogin(kakaoUserInfoDTO, Constant.LoginType.KAKAO, accessToken, refreshToken);
        } catch (Exception e) {
            return new ProfileDTO.UserInfoWithToken();
        }
    }

    @Transactional
    public ProfileDTO.UserInfoWithToken fetchNaverToken(LoginVM.LoginParam param) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("grant_type=authorization_code");
        stringBuilder.append("&client_id=" + NAVER_CLIENT_ID);
        stringBuilder.append("&client_secret=" + NAVER_CLIENT_SECRET_ID);
        stringBuilder.append("&code=").append(param.getCode());

        try {
            Object response = requestService.fetchRequest(null, null, NAVER_TOKEN_ENDPOINT + "?" + stringBuilder, HttpMethod.GET);

            ProfileDTO.NaverLoginToken naverLoginTokenDTO = new ProfileDTO.NaverLoginToken((LinkedHashMap<String, Object>) response);

            return this.fetchNaverUserInfo(naverLoginTokenDTO.getAccessToken(), naverLoginTokenDTO.getRefreshToken());
        } catch (Exception e) {
            return new ProfileDTO.UserInfoWithToken();
        }
    }

    @Transactional
    public ProfileDTO.UserInfoWithToken fetchNaverUserInfo(String accessToken, String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        try {
            Object response = requestService.fetchRequest(headers, null, NAVER_USER_INFO_ENDPOINT, HttpMethod.GET);

            ProfileDTO.NaverUserInfo naverUserInfoDTO = new ProfileDTO.NaverUserInfo((LinkedHashMap<String, Object>) response);

            return profileService.userLogin(naverUserInfoDTO, Constant.LoginType.NAVER, accessToken, refreshToken);
        } catch (Exception e) {
            return new ProfileDTO.UserInfoWithToken();
        }
    }
}
