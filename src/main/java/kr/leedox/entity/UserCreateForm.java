package kr.leedox.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateForm {
    @NotEmpty(message = "Not Empty")
    private String email;

    @Size(min = 2, max = 25, message = "Min 2")
    @NotEmpty(message = "Not Empty")
    private String username;

    @Size(min = 8, max = 25, message = "Min 8")
    @NotEmpty(message = "Not Empty")
    private String password1;

    @Size(min = 8, max = 25, message = "Min 8")
    @NotEmpty(message = "Not Empty")
    private String password2;
}
