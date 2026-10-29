package es.us.dp1.chess.tournament.practice.b;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ReadingClubRepository extends CrudRepository<ReadingClub, Integer> {

    // "l.borrower = u" keeps only the club's loans borrowed by a member (rows without a loan of
    // that member disappear); whether each loan is overdue is decided in the CASE, so members
    // whose loans are all on time still appear with 0. Ties are broken by username.
    @Query("SELECT u, SUM(CASE WHEN l.returnDate IS NOT NULL AND l.returnDate > l.dueDate THEN 1 "
            + "WHEN l.returnDate IS NULL AND l.dueDate < :referenceDate THEN 1 ELSE 0 END) "
            + "FROM ReadingClub c JOIN c.members u JOIN c.loans l "
            + "WHERE c.id = :clubId AND l.borrower = u "
            + "GROUP BY u ORDER BY 2 DESC, u.username ASC")
    List<Object[]> findOverdueCounts(@Param("clubId") Integer clubId, @Param("referenceDate") LocalDate referenceDate);

}
