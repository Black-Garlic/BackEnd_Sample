package com.black.garlic.common.controller.vm;

import lombok.Getter;
import lombok.Setter;

public class LoginVM {
    @Getter
    @Setter
    public static class LoginParam {
        private String loginType;
        private String code;
    }

    @Getter
    @Setter
    public static class ProfileParam {
        private String profileId;
    }
}
