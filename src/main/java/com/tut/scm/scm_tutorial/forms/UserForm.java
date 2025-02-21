package com.tut.scm.scm_tutorial.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserForm {

    @NotBlank(message = "UserName is Required")
    @Size(min=3,message="minimum 3 character is required")
    private String name;
    @Email(message="Invalid Email Address")
    @NotBlank(message="Email is required")
    private String email;
    @Size(min=4,message="minimum 4 character required")
    private String password;
    @Size(min=10,message="phoneNumber must be of size 10")
    private String phoneNumber;
    @NotBlank(message = "Male of Female")
    private String gender;
    private String about;
}
