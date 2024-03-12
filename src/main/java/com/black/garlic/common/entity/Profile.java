package com.black.garlic.common.entity;

import com.black.garlic.common.constant.Constant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class Profile extends AbstractAuditingEntity {
    @Id
    @Column(name = "profile_id", nullable = false, length = 50)
    private String profileId;

    @Column(name = "nickname", length = 100)
    private String nickname;

    @Column(name = "email", length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", length = 100)
    private Constant.LoginType loginType;
}
