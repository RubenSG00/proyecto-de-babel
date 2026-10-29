package es.us.dp1.chess.tournament.practice.x.mapping;

import java.math.BigDecimal;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TaxInvoice extends BaseEntity {

    @NotNull
    Integer fiscalYear;

    @NotNull
    Integer serialNumber;

    @NotNull
    BigDecimal amount;

}
