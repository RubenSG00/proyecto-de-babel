package es.us.dp1.chess.tournament.practice.x.mapping;

import java.math.BigDecimal;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// The pair is unique, not each attribute: the column names are the database (snake case) names
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "fiscal_year", "serial_number" }))
@Getter
@Setter
public class TaxInvoice extends BaseEntity {

    @NotNull
    @Min(2000)
    @Max(3000)
    Integer fiscalYear;

    @NotNull
    @Min(0)
    Integer serialNumber;

    @NotNull
    BigDecimal amount;

}
