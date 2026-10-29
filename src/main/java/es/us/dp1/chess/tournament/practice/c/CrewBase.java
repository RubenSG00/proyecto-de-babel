package es.us.dp1.chess.tournament.practice.c;

import java.util.List;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CrewBase extends BaseEntity {

    @NotBlank
    String name;

    @NotBlank
    String city;

    @NotNull
    @Size(min = 2)
    @ManyToMany
    @JoinTable(name = "crew_base_pilots",
        joinColumns = @JoinColumn(name = "crew_base_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> pilots;

    @ManyToMany
    @JoinTable(name = "crew_base_flights",
        joinColumns = @JoinColumn(name = "crew_base_id"),
        inverseJoinColumns = @JoinColumn(name = "flight_id"))
    List<Flight> flights;

}
