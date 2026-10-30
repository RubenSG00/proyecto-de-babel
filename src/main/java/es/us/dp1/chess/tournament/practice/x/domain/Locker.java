package es.us.dp1.chess.tournament.practice.x.domain;

import java.util.Objects;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// Lombok's @EqualsAndHashCode(of = {"id"}) ignored the id, because it is inherited from
// BaseEntity: equals and hashCode are written by hand, based only on the id (T4 p.47)
@Entity
@Getter
@Setter
public class Locker extends BaseEntity {

    @NotBlank
    String code;

    String building;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Locker other = (Locker) obj;
        return Objects.equals(getId(), other.getId());
    }

    // Consistent with equals: equal lockers (same id) have the same hash code
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
