package es.us.dp1.chess.tournament.practice.x.relationships;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PurchaseInvoice extends BaseEntity {

    @NotBlank
    String customer;

    @Transient
    List<InvoiceLine> lines = new ArrayList<>();

    public BigDecimal getTotal() {
        // TODO: Implement to solve Test X19
        return null;
    }

}
