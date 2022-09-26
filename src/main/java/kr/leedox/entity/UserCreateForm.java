package kr.leedox.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserCreateForm {
    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;

    @Size(min = 2, max = 25, message = "이름은 2자 이상입니다.")
    @NotEmpty(message = "이름은 필수항목입니다.")
    private String username;

    @Size(min = 8, max = 25, message = "비밀번호는 8자 이상입니다.")
    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @Size(min = 8, max = 25, message = "비밀번호 확인은 8자 이상입니다.")
    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

}
