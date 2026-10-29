package es.us.dp1.chess.tournament.practice.x.relationships;

import java.math.BigDecimal;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InvoiceLine extends BaseEntity {

    @NotBlank
    String product;

    @NotNull
    @Min(1)
    Integer units;

    @NotNull
    @DecimalMin("0.0")
    BigDecimal unitPrice;

}
