package com.nfinity.example179.application.Authorization.Permission.Dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class CreatePermissionInput {

	@NotNull(message = "Name Should not be null")
    private String name;
	
	@NotNull(message = "Display Name Should not be null")
    @Length(max = 128, message = "Display Name must be less than 128 characters")
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

