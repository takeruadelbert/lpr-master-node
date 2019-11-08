package com.stn.ester.dto.entity;

import com.stn.ester.dto.base.EntityDTO;
import com.stn.ester.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSimpleDTO extends EntityDTO<User> {

    public Long id;
    public String username;
    public String email;
    public String fullName;
    public String userGroup;

    public UserSimpleDTO(User user) {
        super(user);
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullName = user.getBiodata().getFullname();
    }
}
