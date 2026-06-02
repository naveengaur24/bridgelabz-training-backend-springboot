package com.fundoonotes.fundoo_notes.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "Name cannot be empty!")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty!")
    @Size(min = 6, message = "Password must be at least 6 characters!")
    private String password;
}


//DTO ka kaam hai — client se aane wale data ko Java Object mein convert karna
//taaki server us data ke saath kaam kar sake.


//Register ke time server ko chahiye:
//	  name + email + password   → 3 cheezein
//
//	Login ke time server ko chahiye:
//	  email + password          → sirf 2 cheezein
//

