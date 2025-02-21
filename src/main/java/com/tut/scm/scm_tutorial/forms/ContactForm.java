package com.tut.scm.scm_tutorial.forms;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class ContactForm {


    @NotBlank(message="Name Should not be blank")
    private String contactName;

    @NotBlank(message="Email Should not be blank")
    @Email(message="Invalid Email Address [example@gmail.com]")
    private String contactEmail;

    @NotBlank(message="Phone Number is required")
    @Size(min=10,max = 10,message = "Number Should be 10 digits")
    private String phoneNumber;

    @NotBlank(message="Enter valid address")
    private String address;

    private String description;

    private boolean favourite;

    private String websiteLink;

    private String linkedInLink;

    
    private MultipartFile contactImage; 

    private String picture;
}
