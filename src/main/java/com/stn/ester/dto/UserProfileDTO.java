package com.stn.ester.dto;

import com.stn.ester.entities.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
public class UserProfileDTO {

    AssetFile profilePicture;
    Biodata biodata;
    String username;
    String email;
    Collection<String> roleStrings;
    Collection<Role> roles;
    Long id;

    public UserProfileDTO(User user) {
        profilePicture = user.getProfilePicture();
        biodata = user.getBiodata();
        username = user.getUsername();
        email = user.getEmail();
        roleStrings = new ArrayList<>();
        roles = new ArrayList<>();
        for (RoleGroup roleGroup : user.getRoleGroups()) {
            roleStrings.add(roleGroup.getRole().getName());
            roles.add(roleGroup.getRole());
        }
        id = user.getId();
    }
}
