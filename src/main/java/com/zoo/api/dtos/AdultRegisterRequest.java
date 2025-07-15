package com.zoo.api.dtos;

import com.zoo.api.enums.AdultType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdultRegisterRequest {

    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^0[1-9](\\d{2}){4}$", message = "Numéro de téléphone invalide (format FR)")
    private String phone;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 14, message = "Le mot de passe doit contenir au moins 14 caractères")
    private String password;
    
    @NotNull(message = "Le type doit être précisé (PARENT, PROFESSEUR, AUXILIAIRE)")
    private AdultType type;
}
