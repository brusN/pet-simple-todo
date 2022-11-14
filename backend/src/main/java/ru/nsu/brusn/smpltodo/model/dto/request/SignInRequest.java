package ru.nsu.brusn.smpltodo.model.dto.request;

import lombok.Getter;

@Getter
public class SignInRequest {
    private String username;
    private String password;
}
