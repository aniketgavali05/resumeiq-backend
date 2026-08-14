
package com.resumeiq.backend.mapper;

import org.springframework.stereotype.Component;

import com.resumeiq.backend.entity.User;
import com.resumeiq.backend.response.UserResponse;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {

        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());

        if (user.getRole() != null) {
            response.setRole(
                    user.getRole()
                            .getName()
                            .name()
            );
        }

        response.setProfileImage(
                user.getProfileImage()
        );

        response.setEnabled(
                user.getEnabled()
        );

        response.setEmailVerified(
                user.getEmailVerified()
        );

        return response;
    }
}

