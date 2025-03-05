package com.gurakbu.delivery.domain.user.dto.request;

import com.gurakbu.delivery.domain.user.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {

    @NotBlank
    @Email(message = "올바른 이메일 형식을 입력해주세요")
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,16}$",
            message = "비밀번호는 대소문자/숫자/특수문자를 포함하여 8자 이상,16자 이하이어야 합니다."
    )
    private String password;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String name;

    private UserRole userRole;
}
