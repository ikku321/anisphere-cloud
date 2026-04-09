// UserDTOO.java

package com.iikun.anicomment.entity.DTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDTO {
    private String userId;
    private String username;
    private String nickname;
    private String avatarUrl;
    private String phone;
    private String email;
    private Integer exp;
    private Integer level;
    private Integer status;
    private String role;
    private Integer followersCount;
    private Integer followingCount;
    private LocalDate birthday;
    private String bio;
    private String gender;
}
