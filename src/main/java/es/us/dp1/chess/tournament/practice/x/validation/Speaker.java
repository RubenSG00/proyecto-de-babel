package es.us.dp1.chess.tournament.practice.x.validation;

import java.math.BigDecimal;
import java.time.LocalDate;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Speaker extends BaseEntity {

    String fullName;

    String email;

    String phone;

    Integer age;

    BigDecimal rating;

    LocalDate birthDate;

    String bio;

}
