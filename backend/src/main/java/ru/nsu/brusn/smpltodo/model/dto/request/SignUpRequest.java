package ru.nsu.brusn.smpltodo.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String username;
    private String password;
}
