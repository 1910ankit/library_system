package com.ankit.librarySystem.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterBorrowerDTO {

	private String name;
	
	@Email
    @Schema(description = "Email address", example = "202kumarankit@gmail.com", requiredMode = RequiredMode.REQUIRED)
    private String email;
}
