package es.us.dp1.chess.tournament.practice.x.validation;

import java.math.BigDecimal;
import java.time.LocalDate;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Speaker extends BaseEntity {

    @NotBlank
    @Size(min = 3, max = 40)
    String fullName;

    // @Email (like every constraint except the @Not... ones) accepts null
    @NotNull
    @Email
    String email;

    // "Exactly 9 digits": @Digits only limits the maximum number of digits, so a regex is needed
    @NotNull
    @Pattern(regexp = "[0-9]{9}")
    String phone;

    // @Min/@Max for integer types
    @NotNull
    @Min(18)
    @Max(99)
    Integer age;

    // @DecimalMin/@DecimalMax for BigDecimal; @Digits limits the number of decimals
    @DecimalMin("0.0")
    @DecimalMax("5.0")
    @Digits(integer = 1, fraction = 1)
    BigDecimal rating;

    @Past
    LocalDate birthDate;

    @Size(max = 300)
    String bio;

}
