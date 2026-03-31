// UserDTOO.java

package com.iikun.anicomment.entity.DTO;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String role;
    private String gender;
    private long level;
    private String userId;
    private long followingCount;
    private String phone;
    private String nickname;
    private long followersCount;
    private long exp;
    private String email;
    private String username;
    private long status;

    public String getRole() { return role; }
    public void setRole(String value) { this.role = value; }

    public String getGender() { return gender; }
    public void setGender(String value) { this.gender = value; }

    public long getLevel() { return level; }
    public void setLevel(long value) { this.level = value; }

    public String getUserId() { return userId; }
    public void setUserId(String value) { this.userId = value; }

    public long getFollowingCount() { return followingCount; }
    public void setFollowingCount(long value) { this.followingCount = value; }

    public String getPhone() { return phone; }
    public void setPhone(String value) { this.phone = value; }

    public String getNickname() { return nickname; }
    public void setNickname(String value) { this.nickname = value; }

    public long getFollowersCount() { return followersCount; }
    public void setFollowersCount(long value) { this.followersCount = value; }

    public long getExp() { return exp; }
    public void setExp(long value) { this.exp = value; }

    public String getEmail() { return email; }
    public void setEmail(String value) { this.email = value; }

    public String getUsername() { return username; }
    public void setUsername(String value) { this.username = value; }

    public long getStatus() { return status; }
    public void setStatus(long value) { this.status = value; }
}
