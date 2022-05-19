package com.bezkoder.spring.data.mongodb.authservice.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSummary {

    private String id;
    private String username;
    private String name;
    private String profilePicture;
    private String email;
    private String type;
}
