# DP1 practical exam – Cheat sheet

Every concept used to solve the exercises of [EXERCISES.md](EXERCISES.md) (Blocks 0, A, B, C and X), with the exact syntax of this project (Spring Boot 3.5, Java 21, `jakarta.*`, Hibernate 6, H2, React 18). The exercise ids in brackets point to a worked example. The appendix lists JPQL and SQL queries from easiest to hardest.

---

## 0. Exam survival rules

- **A project that does not compile or does not start Spring gets 0.** A broken mapping (missing `@Id`, a typo in `mappedBy`, a repeated column) or a broken `INSERT` in `data.sql` makes *every* Spring test fail. Run the whole suite from time to time, not only the test you are working on.
- Never change class names, method names, return types or parameters: the tests depend on them.
- Exercises 3–4 depend on 1–2; the rest are independent. If you are stuck, move on.
- Run one test: `./mvnw test -Dtest=Test5` (PowerShell: `.\mvnw.cmd test "-Dtest=Test5"`). React: `cd frontend` then `npx react-scripts test --watchAll=false src/challenges`.
- Imports are `jakarta.persistence.*` and `jakarta.validation.*`, never `javax.*`. `@Transactional` is `org.springframework.transaction.annotation.Transactional`.

---

## 1. Entities and attributes [0.1, A1, B1, C1]

```java
@Entity
@Getter @Setter                       // Lombok
public class ChallengeAnnouncement extends BaseEntity {   // BaseEntity gives the Integer id (@Id + sequence)

    @NotNull @Size(min = 10, max = 60)
    String message;

    @NotNull @Enumerated(EnumType.STRING)  // stored as "DRAFT"/"SENT"; the default ORDINAL stores 0/1/2
    AnnouncementStatus status;

    LocalDateTime sentAt;                  // optional: no constraint
}
```

- Repository: `public interface XRepository extends CrudRepository<X, Integer> { }` — `findById` returns `Optional<X>`, `findAll` returns `Iterable<X>` (redeclare `List<X> findAll();` to get a list).
- Generated ids start at **100** (`initialValue = 100`): use ids below 100 in `data.sql`.
- Mapped superclasses of the project: `BaseEntity` (id), `NamedEntity` (`name` with `@Size(min = 3, max = 50)`), `Person` (`firstName`, `lastName` with `@NotEmpty`). They have no table [X38].

---

## 2. Bean Validation [X01, X02]

| Annotation | Applies to | Notes |
|---|---|---|
| `@NotNull` | anything | `""` and `[]` are valid |
| `@NotEmpty` | String, collection | not null and length/size > 0; `"   "` is valid |
| `@NotBlank` | String | not null and at least one non-whitespace character |
| `@Size(min, max)` | String, collection | use it instead of Hibernate's `@Length`; on an `Integer` it throws `UnexpectedTypeException` |
| `@Min` / `@Max` | integer types | `@Min(0) Integer readCount` |
| `@DecimalMin("0.0")` / `@DecimalMax("5.0")` | `BigDecimal`, `Double` | the value is a **String**; the course says not to use them on `double`/`float` |
| `@Digits(integer = 1, fraction = 1)` | numbers | maximum number of digits (not "exactly") |
| `@Pattern(regexp = "SR-\\d{4}")` | String | must match the **whole** string; double the backslash in Java |
| `@Email` | String | |
| `@Past` / `@Future` / `@PastOrPresent` | dates | |
| `@Valid` | nested object | cascades the validation (embedded objects, request bodies) |

- **Every constraint except the `@Not…` ones accepts `null`.** A mandatory attribute always needs `@NotNull`/`@NotBlank` *and* its other constraints (`@NotNull @Min(0)`, `@NotNull @Email`).
- "Exactly 9 digits": `@Pattern(regexp = "[0-9]{9}")`, not `@Digits(integer = 9, fraction = 0)` (that accepts 8).
- Use `Double`/`Integer`, not `double`/`int`, when the value may be null.
- Validation runs when JPA persists/flushes (and when a controller receives a `@Valid` body), **not** on `INSERT`s of `data.sql`. Collection constraints (`@Size(min = 2)` on a list) are checked on flush.

---

## 3. Relationships [0.2, A2, B2, C2, X19–X21]

### 3.1 Cardinality of the navigable end → annotations

| UML end | Annotations |
|---|---|
| `1` | `@NotNull @ManyToOne(optional = false)` |
| `0..1` | `@ManyToOne` (no `@NotNull`, no `optional = false`) |
| `0..n` | `@ManyToMany List<X>` |
| `1..n` | `@NotNull @NotEmpty @ManyToMany` (or `@Size(min = 1)`) |
| `2..n` | `@NotNull @Size(min = 2) @ManyToMany` |
| `0..3` | `@Size(max = 3) @ManyToMany` (no `@NotNull`) |

- Unidirectional: nothing is added to the other class. Many-to-one from the many side → FK column `<attribute>_id`.
- "A user receives many announcements and an announcement has many users" → `@ManyToMany`, not `@OneToMany`.
- Remove **all** the `@Transient` annotations when the statement says so.

### 3.2 Bidirectional with `mappedBy` [C2, X20]

```java
// Owning side (holds the FK report_id)
@NotNull @ManyToOne(optional = false)
SafetyReport report;

// Inverse side: mappedBy = name of the attribute in the OTHER class
@OneToMany(mappedBy = "report")
List<ReportAction> actions;
```

- Without `mappedBy` JPA creates two unidirectional associations and an extra join table.
- Only the owning side is stored: keep both sides coherent with a helper (`course.addLesson(l)` sets `lessons.add(l)` **and** `l.setCourse(this)`).
- Returned by a controller, both sides loop forever in JSON → `@JsonIgnore` on the collection side [X24].

### 3.3 Cascades

| Relation | Mapping |
|---|---|
| Composition (parts die with the whole) | `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)` |
| Aggregation (parts survive) | `cascade = { PERSIST, MERGE, DETACH, REFRESH }` (everything but `REMOVE`) |
| Mandatory one-to-one that lives with its owner | `@NotNull @OneToOne(optional = false, cascade = CascadeType.ALL)` |

- `orphanRemoval = true`: a part removed from the collection is deleted. Without it only the join row disappears.

### 3.4 Value objects and generalizations [X36–X39]

```java
@Embeddable @Getter @Setter @NoArgsConstructor
public class PostalAddress { @NotBlank String street; @NotNull @Pattern(regexp = "[0-9]{5}") String zipCode; }

@NotNull @Valid @Embedded        // columns street, zip_code in the owner's table; @Valid to validate inside
PostalAddress address;

@Embedded                        // the same embeddable twice: rename its columns
@AttributeOverrides({
    @AttributeOverride(name = "name",  column = @Column(name = "sales_name")),
    @AttributeOverride(name = "phone", column = @Column(name = "sales_phone")) })
Contact salesContact;

@MappedSuperclass                // no table: its attributes go to each subclass table (@Entity would create one)
public abstract class Vehicle extends BaseEntity { @NotBlank String brand; }
```

---

## 4. Other mappings [X22, X23, C1]

```java
@Entity
@Table(name = "clinic_visits",
       uniqueConstraints = @UniqueConstraint(columnNames = { "fiscal_year", "serial_number" }))  // DB names
public class ClinicVisit extends BaseEntity {
    @Column(name = "visit_date") LocalDateTime date;
    @Column(unique = true) String code;          // a DB constraint: fails on flush, not on validation
    @Temporal(TemporalType.DATE) Date petBirthDate;  // only for java.util.Date/Calendar
}
```

- JPQL keeps using the **entity** name (`ClinicVisit`), SQL uses the table name.
- `@Column(nullable = false)` is only a schema constraint; the graders look for `@NotNull`.

---

## 5. `data.sql` [0.3, 0.4, A3, A4…]

- Naming strategy: `CamelCase` → `snake_case`. Table `ChallengeAnnouncement` → `challenge_announcement`; attribute `sentAt` → `sent_at`; many-to-one `challenge` → `challenge_id`.
- Default join table of a unidirectional `@ManyToMany`: `<owner_table>_<attribute>(<owner_table>_id, <attribute>_id)`, e.g. `challenge_announcement_recipients(challenge_announcement_id, recipients_id)`; `mentionedMembers` → `book_review_mentioned_members(book_review_id, mentioned_members_id)`.
- Order: referenced rows first (users, challenges) → the entity → its join table.
- Enums as text (`'SENT'`), dates `'2026-09-10'`, instants `'2026-07-01 10:00:00'`, `NULL` without quotes, booleans `true/false`, a quote inside a string is doubled: `'I can''t make it'`.
- "Mentions nobody" = no rows, not a row with `NULL`.
- Avoid H2 reserved words as names (`VALUE`, `YEAR`, `END`, `USER`…).

See Appendix B for complete examples.

---

## 6. Repositories and derived queries [X03, X04]

Spring Data builds the query from the method name (no `@Query`):

| Keyword | Example |
|---|---|
| (equals) / `And` / `Or` | `findByCity(String c)`, `findByGenreOrCity(MusicGenre g, String c)` |
| `LessThan` / `LessThanEqual` / `GreaterThan` | `findByPriceLessThanEqualOrderByPriceAsc(Integer p)` |
| `Before` / `After` (strict) / `Between` (both ends included) | `findByCityAndDateAfterOrderByDateAsc(String c, LocalDate d)` |
| `True` / `False` / `IsNull` / `IsNotNull` | `findBySoldOutFalse()` |
| `Containing` / `StartingWith` / `EndingWith` / `IgnoreCase` | `findByTitleContainingIgnoreCase(String t)` |
| `In` | `findByGenreIn(Collection<MusicGenre> g)` |
| `OrderBy…Asc/Desc` | `findByTitleStartingWithOrderByTitleDesc(String p)` |
| `Optional<X>` return | zero or one result (several rows throw) |

- Property names must match attribute names exactly (`findBySoldoutFalse` fails at startup).
- A service uses its own repository and **the services** of other modules (`UserService`, not `UserRepository`).

---

## 7. JPQL with `@Query` [0.6, A6, B6, C6, X05, X06]

```java
@Query("SELECT m FROM Musician m WHERE m.salary BETWEEN :low AND :high ORDER BY m.salary DESC")
List<Musician> musiciansEarningBetween(@Param("low") Integer low, @Param("high") Integer high);
```

- Entity and attribute names, not tables/columns. Positional `?1` or named `:name` + `@Param("name")`.
- `= NULL` is never true: `IS NULL`. Collections: `IS EMPTY`, `IS NOT EMPTY`, `MEMBER OF`.
- `SUM`/`COUNT` return `Long`: read results with `((Number) row[1]).longValue()`.
- `ORDER BY 2` sorts by the second element of the `SELECT`.
- `WHERE` filters rows **before** grouping; `HAVING` filters groups **after** (conditions on a total go in `HAVING`).
- The pattern of exercise 6: join the root with its participants **and** its matches, keep the rows where the participant played (`WHERE`), decide the points in a `CASE` inside `SUM`, `GROUP BY` the participant.
- Hibernate 6 removes duplicate **entities** from join results by itself, but not duplicate **scalar** values: write `DISTINCT` anyway.
- Usernames are strings: `"player10" < "player2"`. A tie-break on `username` differs from one on the id.

All the queries, from easy to hard, are in Appendix A.

---

## 8. Services and transactions [0.5, A5, B5, C5, X07, X08]

```java
@Service
public class ChessMatchService {
    ChessMatchRepository repo;

    @Autowired
    public ChessMatchService(ChessMatchRepository repo) { this.repo = repo; }

    @Transactional(readOnly = true)
    public List<ChessMatch> findAll() { return repo.findAll(); }

    @Transactional(rollbackFor = InvalidWinnerException.class)   // checked exception!
    public ChessMatch save(ChessMatch m) throws InvalidWinnerException {
        if (m.getWinner() != null && !m.getWinner().equals(m.getWhite()) && !m.getWinner().equals(m.getBlack()))
            throw new InvalidWinnerException();      // check BEFORE saving
        return repo.save(m);
    }
}
```

- Spring rolls back **unchecked** exceptions (`RuntimeException`) by default; a **checked** exception (`extends Exception`) **commits** unless it is listed in `rollbackFor`. Several: `rollbackFor = { A.class, B.class }`.
- `readOnly = true` on methods that only read; never on a method that writes.
- Method level vs class level: `@Transactional` on the class makes every public method transactional; a method-level annotation overrides it for that method [B5, X07].
- Self-invocation: `this.enroll(...)` does not go through the Spring proxy, so the annotation of `enroll` is ignored; the annotation of the caller is what counts [X08].
- `ResourceNotFoundException` (→ 404) and `AccessDeniedException` (→ 403) of the `exceptions` package are unchecked.
- Dates: `finish.isAfter(start)` is strict; `ChronoUnit.DAYS.between(a, b)`, `ChronoUnit.MONTHS.between(a, b)` counts **full** months; `date.plusMonths(n)`.
- `BigDecimal`: `a.multiply(b)`, `BigDecimal.valueOf(n)`, compare with `compareTo` (`2.50` is not `equals` to `2.5`).

---

## 9. REST controllers [0.7, A7, B7, C7, X11–X14]

```java
@RestController
@RequestMapping("/api/v1/challenges")
public class FriendlyChallengeController {
    private final FriendlyChallengeService service;
    @Autowired public FriendlyChallengeController(FriendlyChallengeService s) { this.service = s; }

    @GetMapping("{id}/ranking")
    public ResponseEntity<List<RankingEntry>> getRanking(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.getRanking(id), HttpStatus.OK);
    }

    @GetMapping("{id}/pilot-ranking")       // ?minScore=N optional
    public ResponseEntity<List<PilotScore>> ranking(@PathVariable("id") Integer id,
            @RequestParam(required = false, defaultValue = "0") Integer minScore) { … }

    @PostMapping
    public ResponseEntity<Suggestion> create(@RequestBody @Valid Suggestion s) {   // invalid body → 400
        Suggestion n = new Suggestion();
        BeanUtils.copyProperties(s, n, "id", "author", "createdAt");   // mass assignment protection
        n.setAuthor(userService.findCurrentUser());
        return new ResponseEntity<>(service.save(n), HttpStatus.CREATED);   // 201
    }

    @PutMapping("{id}")
    public ResponseEntity<Tournament> update(@PathVariable("id") Integer id, @RequestBody @Valid Tournament t) {
        return new ResponseEntity<>(service.update(id, t), HttpStatus.OK);   // service: findById → copyProperties(t, stored, "id") → save
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        service.delete(id);                                   // findById first: deleteById of a missing id does nothing
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);   // 204, no body
    }
}
```

| Code | When |
|---|---|
| 200 OK | GET, PUT |
| 201 CREATED | POST that creates |
| 204 NO_CONTENT | DELETE without body |
| 400 BAD_REQUEST | invalid `@Valid` body, business rule broken |
| 401 UNAUTHORIZED | no token (the scaffold does it) |
| 403 FORBIDDEN | authenticated but not allowed (`AccessDeniedException`) |
| 404 NOT_FOUND | does not exist (`ResourceNotFoundException`) |

**Layered architecture** (mock `Test7Alt`, C7): the controller only depends on its service — no repositories, no `EntityManager`, no `UserService`, no `try/catch`, no `@Transactional`. All the rules (existence, access, business checks) live in the transactional service method, which uses its repository and other services. The service never returns `ResponseEntity`.

**Mass assignment** [X12, X13]: never save the object received from the client. Create a new one (or load the stored one) and copy only the allowed properties; the server sets the owner, flags and dates. The id of the URL always wins over the id of the body.

---

## 10. Access checks in the service [0.7, B7, X17, X18]

```java
FriendlyChallenge c = findById(id);                          // 404 first
User current = userService.findCurrentUser();
boolean participant = c.getParticipants().stream()
    .anyMatch(p -> p.getId().equals(current.getId()));       // compare ids: User has no equals
if (!participant && !current.hasAuthority("ADMIN"))
    throw new AccessDeniedException("...");                  // 403
List<Object[]> rows = repo.findParticipantPoints(id);
if (rows.isEmpty()) throw new ResourceNotFoundException("...");   // 404
return rows.stream().map(r -> new RankingEntry((User) r[0], ((Number) r[1]).longValue())).toList();
```

- Follow the order of the statement: it decides which code wins when several rules fail.
- Use the project's `exceptions.AccessDeniedException`, not Spring Security's class with the same name.

**Securing API paths** [X42, X43] in `SecurityConfiguration`, before `anyRequest()` (the first matching rule wins):

```java
.requestMatchers("/api/v1/tournaments/**").hasAuthority(PLAYER)
.requestMatchers("/api/v1/developers").permitAll()     // public
.anyRequest().authenticated()
```

---

## 11. Error handling [C7, X15, X16]

```java
// In ExceptionHandlerController (@RestControllerAdvice)
@ExceptionHandler(value = VenueCapacityExceededException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public ResponseEntity<ErrorMessage> venueCapacityExceeded(VenueCapacityExceededException ex, WebRequest request) {
    ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), ex.getMessage(),
            request.getDescription(false));          // "uri=/api/v1/venues/1/availability"
    return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
}
```

- In this project `@ResponseStatus` on the exception class is **not enough**: the `@ExceptionHandler(Exception.class)` already there catches it first → 500. Add a handler method.
- A checked business exception travels through the controller with `throws`, not `try/catch`.

---

## 12. JSON and DTOs [X24, X25]

- Bidirectional relation in a response → infinite JSON. `@JsonIgnore` (Jackson) on the **collection** side; offer the collection through its own endpoint.
- A DTO hides data (the password hash of `User`) and decouples the API from the model: a class with only the public fields, a constructor and getters.

---

## 13. Custom validation [X40, X41]

```java
@Constraint(validatedBy = MultipleOfValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)          // without it the annotation is silently ignored
public @interface MultipleOf {
    int value();
    String message() default "must be a multiple of {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class MultipleOfValidator implements ConstraintValidator<MultipleOf, Integer> {
    private int divisor;
    @Override public void initialize(MultipleOf a) { divisor = a.value(); }
    @Override public boolean isValid(Integer v, ConstraintValidatorContext c) { return v == null || v % divisor == 0; }
}

// Spring Validator: rules over several attributes
public class RoomBookingValidator implements org.springframework.validation.Validator {
    public boolean supports(Class<?> clazz) { return RoomBooking.class.isAssignableFrom(clazz); }
    public void validate(Object target, Errors errors) {
        RoomBooking b = (RoomBooking) target;
        if (b.getCheckIn() == null) errors.rejectValue("checkIn", "booking.checkIn.required");
    }
}
```

---

## 14. Logging [X48, X49]

```java
@Slf4j                                   // Lombok: private static final Logger log
@Service
public class RegistrationAuditService {
    void register(String u) {
        log.warn("Rejected registration: invalid username '{}'", u);
        log.error("Registration failed for {}", u, e);   // exception LAST → stack trace
        log.info("User {} registered", u);
    }
}
```

- Levels: TRACE < DEBUG < INFO < WARN < ERROR. A level shows itself and the more severe ones.
- `application.properties`: `logging.level.es.us.dp1.chess.tournament.practice.x.logging=DEBUG` (only that package).
- Never log passwords or tokens.

---

## 15. EntityManager and the persistence context [X50, X51]

| Operation | Transition | Notes |
|---|---|---|
| `em.persist(e)` | new → managed | the id is assigned now, the `INSERT` waits for the flush |
| `em.find(X.class, id)` | → managed | `null` if it does not exist |
| setter on a managed entity | — | written at the flush (dirty checking): no `save`/`merge` needed |
| `em.remove(e)` | managed → removed | undone with `em.persist(e)` |
| `em.detach(e)` | managed → detached | later changes are lost |
| `em.merge(d)` | detached → managed | returns a **different**, managed object |

---

## 16. Domain logic and identity [X52, X53]

- **Domain logic** (rules about the entity's own data: `renew`, `calculateEarlyTerminationFee`) in the entity; **application logic** (transactions, find, save, coordinate) in the service.
- Lombok's `@EqualsAndHashCode(of = {"id"})` **ignores an inherited id** (two `ChessMatch` with different ids are equal). Write it by hand:

```java
@Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return Objects.equals(getId(), ((Locker) o).getId());
}
@Override public int hashCode() { return Objects.hashCode(getId()); }
```

- `User` has no `equals`: compare users by id (`a.getId().equals(b.getId())`).

---

## 17. Writing the tests of exercise 8 [0.8, A8, B8, C8]

```java
public class ClosestRatingPairFinderTest {
    ClosestRatingPairFinder algorithm = new ValidClosestRatingPairFinder();   // always use THIS attribute

    @Test
    public void onATieReturnsSmallestSumOfIndices() throws Exception {
        assertArrayEquals(new int[] { 1, 2 }, algorithm.findClosestPair(new int[] { 0, 10, 14, 100, 200, 4 }));
    }

    @Test
    public void emptyArrayThrows() {
        assertThrows(InsufficientPlayersException.class, () -> algorithm.findClosestPair(new int[] {}));
    }

    public void setAlgorithm(ClosestRatingPairFinder a) { this.algorithm = a; }
}
```

- One test per rule: each example of the statement, every boundary (just below / on / just above), the tie-break, the empty result, every exception.
- Methods `public void`, no parameters, `throws Exception` for checked exceptions. No mocks, no Spring annotations, no `@ParameterizedTest`. Remove the `fail()` placeholder.
- The grader only counts an `AssertionFailedError` as a detected bug: use `assertThrows`/`assertEquals`/`assertArrayEquals`. A `try { …; fail(); } catch` lets a faulty implementation's `ArrayIndexOutOfBoundsException` escape undetected; `assertAll` throws a different error type.

---

## 18. React (mock style: `fetch`) [0.9, 0.10, A9–C10, X26–X29, X44–X47]

```jsx
export default function Challenge({ challengeId }) {
    const [ranking, setRanking] = useState([]);          // null if "loading" must differ from "empty" [C9]
    const jwt = tokenService.getLocalAccessToken();
    useEffect(() => {
        fetch(`/api/v1/challenges/${challengeId}/ranking`, { headers: { Authorization: `Bearer ${jwt}` } })
            .then((response) => response.json())
            .then((json) => setRanking(json));
    }, [challengeId, jwt]);                              // dependency array: not on every render

    return (
        <div>
            <h1>Challenge info</h1>
            <h3>Ranking</h3>
            {ranking.length > 0 && <div>Leader: {ranking[0].user.username} ({ranking[0].points} points)</div>}
        </div>
    );
}

// List from props: key for React (not in the DOM) + a real id attribute
<ul>{ranking.map((e, i) => <li key={e.user.id} id={`ranking-${e.user.id}`}>#{i + 1} {e.user.username} - {e.points}</li>)}</ul>
```

- `getByText` matches the text of one element: put the whole sentence in a single element.
- Conditional rendering: `if (list.length === 0) return <p>No overdue loans</p>;` or `cond ? <A/> : <B/>`.
- Filter without mutating props: `ranking.filter((e) => minScore === undefined || e.score >= minScore)`.
- State: replace, never mutate — `setVotes({ ...votes, yoga: votes.yoga + 1 })`, `[...tasks, t]`, `tasks.filter(...)`.
- Lifting state up: state in the closest common parent; data down through props, events up through callbacks (`onVote`).
- Custom hook (`useMembers`) = state + effect + fetch, returns `{ members }`; presentation component = props only; page = composition. Network client = the only place that calls `fetch`.
- Router (v6): `<Routes><Route path="/destinations/:id" element={<D/>} /><Route path="*" element={<NotFound/>} /></Routes>`, `<Link to="...">`, `const { id } = useParams();`, `const navigate = useNavigate();`. Never put a `BrowserRouter` inside another router.
- Context: `const Ctx = createContext({ username: "guest" }); export default Ctx;` → `<Ctx.Provider value={{ username }}>` → `const { username } = useContext(Ctx);`.

### 18.1 The 2026-27 template (Lab 2) [X09, X10, X30–X35]

```jsx
// services/partners.js – one function per API operation (api.js adds /api/v1 and the JWT)
export const getAllPartners = async () => (await api.get("/partners")).data;
export const getPartnerById = async (id) => (await api.get(`/partners/${id}`)).data;

// container: the function itself, without parentheses
const [partners, setPartners] = useFetchState([], partnersApi.getAllPartners);

// edit/create container
const { id } = useParams();
const { errorModal, showError } = useErrorModal();
const [referee, setReferee] = useFetchState(empty, () => refereesApi.getRefereeById(id), [id],
    { skip: id === "new", onError: showError });
function handleChange(e) { const { name, value } = e.target; setReferee({ ...referee, [name]: value }); }
async function handleSubmit(e) {
    e.preventDefault();
    try {
        if (referee.id) await refereesApi.updateReferee(referee.id, referee);   // PUT
        else await refereesApi.createReferee(referee);                          // POST
        navigate("/referees");
    } catch (err) { showError(err.response?.data?.message ?? "Error saving referee"); }
}

// list container with deletion
function handleDelete(id) {
    deleteFromList(clubsApi.deleteClub, id, [clubs, setClubs], [alerts, setAlerts], { onError: showError });
}
// JSX: {alerts.map((a) => a.alert)} {errorModal} <ChessClubTable clubs={clubs} onDelete={handleDelete} />

// row: report the id, delay the call with an arrow function
<Button outline color="danger" onClick={() => onDelete(t.id)}>Delete</Button>
```

- Container/table/row: the container decides *which* data, table and row decide *how* (`sponsor.properties?.logoUrl ? … : imgNotFound`).
- Reusable fields: `<Label for={name}>` + `<Input id={name} name={name} value={value ?? ""} …/>`; options constant declared outside the component.
- Roles: `jwt_decode(tokenService.getLocalAccessToken()).authorities`; register role routes only for that role, wrapped in `<PrivateRoute>`; the menu only shows links. The real protection is the backend.

### 18.2 Writing React tests (RTL) [X46, X47]

- `render(<C />)`, then query by role first: `screen.getByRole("heading", { level: 2, name: "Top players" })`, `getAllByRole("listitem")`.
- Absence: `expect(screen.queryByRole("list")).not.toBeInTheDocument()`.
- Interaction: `const user = userEvent.setup(); await user.click(screen.getByRole("button", { name: /like/i }));`.
- Async data: register the fake API with `server.use(rest.get("*/api/v1/quotes/today", (req, res, ctx) => res(ctx.json({...}))))` and wait with `await screen.findByText(...)` or `waitFor`.

---

## 19. Verified traps of this project

1. A custom exception with `@ResponseStatus` still gives **500**: add an `@ExceptionHandler` method.
2. Lombok `@EqualsAndHashCode(of = {"id"})` ignores the inherited id.
3. `User` has no `equals`: compare ids (in `Test7Alt` the current user is another instance).
4. Hibernate 6 de-duplicates entity rows of a join without `DISTINCT`, but not scalar rows.
5. `deleteById` of a missing id does nothing (no exception): look the entity up first for a 404.
6. Generated ids start at 100; `data.sql` is not validated.
7. `ReflexiveTest.isFieldAnnotatedWith` returns the **opposite** (true when absent), and `invokeMethodReflexively` only calls methods without parameters.

---

## Appendix A – JPQL queries, from easy to hard

Entities of the examples: `Musician(name, instrument, salary, agent, bands)`, `Band(name, city)`, `FriendlyChallenge(participants, matches)`, `ChessMatch(white, black, winner, finish)`.

### Level 1 – filters and sorting

```sql
-- 1. All, sorted
SELECT m FROM Musician m ORDER BY m.name

-- 2. Equality with a positional parameter
SELECT m FROM Musician m WHERE m.instrument = ?1 ORDER BY m.name

-- 3. Named parameter (method parameter: @Param("city") String city)
SELECT b FROM Band b WHERE b.city = :city

-- 4. Null values (never "= NULL")
SELECT m FROM Musician m WHERE m.agent IS NULL ORDER BY m.name

-- 5. Range, both ends included, descending order
SELECT m FROM Musician m WHERE m.salary BETWEEN :low AND :high ORDER BY m.salary DESC

-- 6. Text patterns (case-sensitive)
SELECT m FROM Musician m WHERE m.name LIKE CONCAT(:prefix, '%') ORDER BY m.name

-- 7. Case-insensitive search
SELECT c FROM Concert c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :text, '%'))

-- 8. Several conditions and a list of values
SELECT c FROM Concert c WHERE c.soldOut = false AND c.genre IN :genres ORDER BY c.date ASC, c.price ASC
```

### Level 2 – navigating relationships

```sql
-- 9. Through a many-to-one (implicit join with the dot)
SELECT r FROM BookReview r WHERE r.club.id = :clubId

-- 10. Collections empty / not empty
SELECT m FROM Musician m WHERE m.bands IS EMPTY
SELECT m FROM Musician m WHERE m.bands IS NOT EMPTY

-- 11. Explicit join over a collection, each musician once
SELECT DISTINCT m FROM Musician m JOIN m.bands b WHERE b.city = :city
-- course syntax, equivalent:
SELECT DISTINCT m FROM Musician m, IN (m.bands) b WHERE b.city = :city

-- 12. MEMBER OF
SELECT fc FROM FriendlyChallenge fc WHERE :user MEMBER OF fc.participants

-- 13. Distinct scalar values (here DISTINCT really matters)
SELECT DISTINCT m.instrument FROM Musician m JOIN m.bands b WHERE b.city = :city ORDER BY m.instrument

-- 14. Self-join: compare with another row of the same entity
SELECT DISTINCT m1 FROM Musician m1, Musician m2 WHERE m1.salary > m2.salary AND m2.name = :name
```

### Level 3 – aggregates

```sql
-- 15. Count (returns Long)
SELECT COUNT(m) FROM Musician m JOIN m.bands b WHERE b.id = :bandId

-- 16. Group and count, sorted by the count
SELECT b.city, COUNT(b) FROM Band b GROUP BY b.city ORDER BY 2 DESC

-- 17. Other aggregates
SELECT MAX(m.salary), MIN(m.salary), AVG(m.salary), SUM(m.salary) FROM Musician m

-- 18. Entity + aggregate: rows are Object[] { Musician, Long }
SELECT m, COUNT(b) FROM Musician m JOIN m.bands b GROUP BY m ORDER BY 2 DESC

-- 19. HAVING: filter the groups by their total
SELECT b, COUNT(m) FROM Musician m JOIN m.bands b GROUP BY b HAVING COUNT(m) >= 2
```

### Level 4 – conditional aggregates (exercise 6 of every block)

```sql
-- 20. Mock Test 6: 2 points the winner, 1 each in a draw, 0 the loser; only finished matches
SELECT u, SUM(CASE WHEN m.winner = u THEN 2 WHEN m.winner IS NULL THEN 1 ELSE 0 END)
FROM FriendlyChallenge fc JOIN fc.participants u JOIN fc.matches m
WHERE fc.id = :challengeId AND m.finish IS NOT NULL AND (m.white = u OR m.black = u)
GROUP BY u ORDER BY 2 DESC

-- 21. A6: different points for trainee and coach, depending on a Boolean
SELECT u, SUM(CASE WHEN s.trainee = u AND s.goalAchieved = true THEN 3
                   WHEN s.coach = u AND s.goalAchieved = true THEN 1 ELSE 0 END)
FROM TrainingProgram p JOIN p.members u JOIN p.sessions s
WHERE p.id = :programId AND s.finish IS NOT NULL AND (s.trainee = u OR s.coach = u)
GROUP BY u ORDER BY 2 DESC

-- 22. B6: two parameters, dates compared directly, tie-break by username
SELECT u, SUM(CASE WHEN l.returnDate IS NOT NULL AND l.returnDate > l.dueDate THEN 1
                   WHEN l.returnDate IS NULL AND l.dueDate < :referenceDate THEN 1 ELSE 0 END)
FROM ReadingClub c JOIN c.members u JOIN c.loans l
WHERE c.id = :clubId AND l.borrower = u
GROUP BY u ORDER BY 2 DESC, u.username ASC

-- 23. C6: the sum of two CASEs, HAVING on the total, tie-break by username
SELECT p, SUM((CASE WHEN f.captain = p THEN 3 ELSE 1 END)
            + (CASE WHEN f.distanceKm >= 3000 THEN 1 ELSE 0 END))
FROM CrewBase b JOIN b.pilots p JOIN b.flights f
WHERE b.id = :baseId AND f.arrival IS NOT NULL AND f.diverted = false
  AND (f.captain = p OR f.firstOfficer = p)
GROUP BY p
HAVING SUM((CASE WHEN f.captain = p THEN 3 ELSE 1 END)
         + (CASE WHEN f.distanceKm >= 3000 THEN 1 ELSE 0 END)) >= :minScore
ORDER BY 2 DESC, p.username ASC
```

How to read a level-4 query:
1. `FROM … JOIN … JOIN …` builds one row per (participant, match) pair of the root.
2. `WHERE` keeps only the rows that count: finished, and **played by that participant**. A row removed here does not even make the participant appear.
3. `CASE` decides the points of each remaining row; a loss is `ELSE 0`, so losers still appear with 0.
4. `GROUP BY` the participant adds them up; `HAVING` filters on the total; `ORDER BY 2 DESC` sorts by the total, then the tie-break.

Typical mistakes: the "finished" filter inside the `CASE` instead of the `WHERE` (people who only played unfinished matches appear with 0); forgetting "played the match" (a draw gives points to every participant); sorting by id instead of username.

---

## Appendix B – SQL for `data.sql`, from easy to hard

```sql
-- 1. Simple row: an enum as text, NULL without quotes, a timestamp
INSERT INTO challenge_announcement (id, message, status, sent_at, challenge_id) VALUES
    (2, 'New format proposal for next season', 'DRAFT', NULL, 1);

-- 2. Several rows in one INSERT; a quote doubled inside the text; date and FK columns
INSERT INTO announcement_response (id, message, status, responded_at, announcement_id, recipient_id) VALUES
    (1, 'Got it, will submit by Friday', 'ACCEPTED', '2026-07-01 11:00:00', 1, 4),
    (3, 'I can''t make it in August', 'DECLINED', '2026-08-02 12:00:00', 3, 6);

-- 3. An optional many-to-one (0..1) left empty
INSERT INTO notice_acknowledgement (id, comment, status, acknowledged_at, notice_id, member_id, delegate_id) VALUES
    (1, 'Thanks, noted', 'CONFIRMED', '2026-09-01 09:15:00', 1, 4, NULL);

-- 4. Default join table of a unidirectional @ManyToMany (after both referenced tables)
INSERT INTO challenge_announcement_recipients (challenge_announcement_id, recipients_id) VALUES
    (1, 4), (1, 5),
    (3, 5), (3, 6), (3, 7);

-- 5. Join table of an attribute with two words: mentionedMembers → mentioned_members_id
INSERT INTO book_review_mentioned_members (book_review_id, mentioned_members_id) VALUES
    (1, 5), (1, 6), (2, 4);

-- 6. Bidirectional @OneToMany(mappedBy): only the FK of the owning side, no join table
INSERT INTO report_action (id, action_taken, status, deadline, closed_at, priority, report_id, assignee_id) VALUES
    (1, 'Full inspection of the left wing', 'CLOSED', '2026-07-10', '2026-07-09 12:00:00', 1, 1, 10);

-- 7. Explicit @JoinTable: read the names in the annotation
--    @JoinTable(name = "friendly_challenge_participants",
--               joinColumns = @JoinColumn(name = "friendly_challenge_id"),
--               inverseJoinColumns = @JoinColumn(name = "user_id"))
INSERT INTO friendly_challenge_participants (friendly_challenge_id, user_id) VALUES (1, 4), (1, 5);

-- 8. A complete, correctly ordered section: parent rows → entity rows → join rows
INSERT INTO band (id, name, city, founded_year) VALUES (1, 'The Riffs', 'Seville', 1998);
INSERT INTO musician (id, name, instrument, salary, agent) VALUES (1, 'Ana Torres', 'guitar', 2000, 'Agency One');
INSERT INTO musician_bands (musician_id, bands_id) VALUES (1, 1);
```

Plain-SQL queries to check the data in the H2 console (`/h2-console`, JDBC URL `jdbc:h2:mem:testdb`), from easy to hard:

```sql
SELECT * FROM challenge_announcement WHERE status = 'SENT';
SELECT a.id, u.username FROM challenge_announcement_recipients r
    JOIN appusers u ON u.id = r.recipients_id JOIN challenge_announcement a ON a.id = r.challenge_announcement_id;
SELECT status, COUNT(*) FROM announcement_response GROUP BY status HAVING COUNT(*) > 1;
SELECT u.username,
       SUM(CASE WHEN m.winner_id = u.id THEN 2 WHEN m.winner_id IS NULL THEN 1 ELSE 0 END) AS points
FROM friendly_challenge_participants p
     JOIN appusers u ON u.id = p.user_id
     JOIN friendly_challenge_matches fm ON fm.friendly_challenge_id = p.friendly_challenge_id
     JOIN chess_match m ON m.id = fm.chess_match_id
WHERE p.friendly_challenge_id = 1 AND m.finish IS NOT NULL AND (m.white_id = u.id OR m.black_id = u.id)
GROUP BY u.username ORDER BY points DESC;
```
