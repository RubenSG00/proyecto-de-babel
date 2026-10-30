package es.us.dp1.chess.tournament.practice.x.mapping;

import java.time.LocalDateTime;
import java.util.Date;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

// The names of an existing database; JPQL still uses the entity name (ClinicVisit)
@Entity
@Table(name = "clinic_visits")
@Getter
@Setter
public class ClinicVisit extends BaseEntity {

    @Column(name = "visit_date")
    LocalDateTime date;

    @Column(name = "notes")
    String description;

    // @Temporal is only for java.util.Date/Calendar (java.time types are mapped without it)
    @Temporal(TemporalType.DATE)
    Date petBirthDate;

}
