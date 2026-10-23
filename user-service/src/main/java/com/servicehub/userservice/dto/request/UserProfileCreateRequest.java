package com.servicehub.userservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileCreateRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String authUserId; // Gateway/Auth isko Long bhejta hai, aap isko apne Entity ke hisab se String ya Long rakh sakte hain
}