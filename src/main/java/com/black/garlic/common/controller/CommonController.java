package com.black.garlic.common.controller;

import com.black.garlic.common.constant.Path;
import com.black.garlic.common.controller.vm.LoginVM;
import com.black.garlic.common.service.LoginService;
import com.black.garlic.common.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommonController {

    private final LoginService loginService;
    private final ProfileService profileService;

    @GetMapping(value = Path.LOGIN, produces = "application/json")
    public Object login(LoginVM.LoginParam param) throws Exception {
        return loginService.login(param);
    }

    @GetMapping(value = Path.PROFILE, produces = "application/json")
    public Object getProfile(LoginVM.ProfileParam param) throws Exception {
        return profileService.getProfile(param.getProfileId());
    }
}
