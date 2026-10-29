package es.us.dp1.chess.tournament.practice.x.relationships;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PurchaseInvoice extends BaseEntity {

    @NotBlank
    String customer;

    // Composition: the lines live and die with their invoice (cascade ALL), and a line removed
    // from the collection is deleted (orphanRemoval). 1..n: @NotEmpty (not null, at least one).
    @NotEmpty
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<InvoiceLine> lines = new ArrayList<>();

    // Derived data: computed, not stored
    public BigDecimal getTotal() {
        return lines.stream()
            .map(l -> l.getUnitPrice().multiply(BigDecimal.valueOf(l.getUnits())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
