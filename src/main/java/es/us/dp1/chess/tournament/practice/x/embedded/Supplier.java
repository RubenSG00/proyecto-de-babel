package es.us.dp1.chess.tournament.practice.x.embedded;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Supplier extends BaseEntity {

    @NotBlank
    String name;

    // The same embeddable type twice would map the columns name and phone twice:
    // each embedded attribute renames its columns
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "sales_name")),
        @AttributeOverride(name = "phone", column = @Column(name = "sales_phone"))
    })
    Contact salesContact;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "support_name")),
        @AttributeOverride(name = "phone", column = @Column(name = "support_phone"))
    })
    Contact supportContact;

}
