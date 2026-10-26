# Exam structure map

Maintained across convocatorias — read first when migrating, update in parallel with README.md.
**Delete before publishing to students** (together with `ALTERNATIVE-TEST7.md`).

## Scaffold (pure technical infra, no domain content, kept as-is across every convocatoria)
- `ReflexiveTest` — generic reflection-based assertion helper (`checkThatFieldIsAnnotatedWith`, `checkThatFieldsAreMandatory`, `checkThatValuesAreNotValid`, `checkThatEntityIsValid`, `checkLinkedById`, `checkContainsById`, `checkTransactional`, `checkTransactionalRollback`, `isMethodAnnotatedWithTest/BeforeEach/AfterEach`, `setValue`/`getFieldValueReflexively`). Domain-agnostic, never rewritten.
- `auth/`, `configuration/`, `developers/`, `util/` — login, security, JWT, generic converters, aspect logging. Untouched by any exercise.
- `model/BaseEntity.java`, `NamedEntity.java`, `Person.java` — base JPA classes.
- `user/User.java`, `Authorities*.java`, `UserRepository`, `UserService`, `UserRestController` — generic user management. `User.rating` (Integer) was added this convocatoria as a plain field (no exercise attached to it) — a small domain-adjacent tweak, but the class's role (account/auth) is still scaffold-level.
- `exceptions/{ResourceNotFoundException,AccessDeniedException,ResourceNotOwnedException,TokenRefreshException,ExceptionHandlerController,ErrorMessage}` — generic exception infra.

## Given — long-lived chess domain (persists across convocatorias, expect minor per-sitting tweaks)
Not pure scaffold (it's domain content, not generic infra) and not invented for this sitting
either — this is the chess-tournament substrate the exam franchise has always been built on. It
survives redesigns largely intact, but each convocatoria tends to lightly reshape it (this sitting
renamed `creator/opponent`→`white/black` and added `winner`); don't assume it's frozen the way the
Scaffold section above is.
- `match/ChessMatch.java` / `match/ChessBoard.java` — chess match entity+board (`white`, `black`, `winner`, board link). Given root that Test 5's service wraps.
- `match/ChessMatchRepository.java` — plain `CrudRepository<ChessMatch,Integer>`, no exercise.
- `match/Piece.java`, `PieceColor.java`, `PieceType.java`, `Position.java`, `ChessMatchType.java` — chess-board value model backing `ChessBoard`.

## Given — this convocatoria's root (invented fresh for this sitting; already fully working; not an exercise)
Just as new as the To-build classes below in the sense that neither existed before this
convocatoria's redesign — the difference is purely that these were deliberately built already-complete,
so the To-build exercises (and Tests 5-10 generally) have a stable foundation to point at from
minute one (the structural independence technique). A future redesign is free to replace this
entire cluster with a different given root, unlike the chess domain above.
- `challenge/FriendlyChallenge.java` — `@ManyToMany` to `ChessMatch` (`matches`) and `User` (`participants`), fully mapped, seeded (3 rows).
- `challenge/FriendlyChallengeRepository.java` — given `CrudRepository`, **except** `findParticipantPoints(Integer)` which is the Test 6 target (see below).
- `challenge/FriendlyChallengeService.java` — given CRUD (`findAll`/`findById`/`save`), **except** `getRanking(Integer)` which is the Test 7 target.
- `challenge/FriendlyChallengeController.java` — given `GET` (list) and `GET {id}`, **missing** the `GET {id}/ranking` endpoint (Test 7 target).
- `challenge/RankingEntry.java` — given DTO `{User user; Long points;}` used to serialize the ranking JSON.
- `matchmaking/` (except for `matchmaking/ClosestRatingPairFinderTest.java`) - provides an interface + 4 implementations that the student needs to test in exercise 8.

## To-build (invented for this convocatoria; Tests 1-4 and 8 construct/exercise these)
- `challenge/ChallengeAnnouncement.java` — entity to map (Test1/Test2); relations point outward to `FriendlyChallenge`/`User` (given), never the reverse.
- `challenge/ChallengeAnnouncementRepository.java` — plain interface to turn into a working `CrudRepository` (Test1).
- `challenge/AnnouncementResponse.java` — entity to map (Test1/Test2); relations point outward to `ChallengeAnnouncement`/`User`.
- `challenge/AnnouncementResponseRepository.java` — plain interface to turn into a working `CrudRepository` (Test1).
- `matchmaking/ClosestRatingPairFinderTest.java` — the only to-build file for exercise 8; the rest of `matchmaking/` (the interface + 4 implementations) is given, since the exercise grades the student's *own test class*, not an implementation.
- Note: `AnnouncementStatus`/`ResponseStatus` (the two enums) are already complete and not themselves an exercise, but they only make sense alongside the to-build entities above, so they're not worth a fourth category of their own.

## Backend exercises

| # | Category | Classes/files involved | Given/to-build | Test file | Mechanism notes |
|---|---|---|---|---|---|
| 1 | Entity + repository creation | `ChallengeAnnouncement`, `AnnouncementResponse`, their repositories, `AnnouncementStatus`/`ResponseStatus` enums | to-build | `Test1.java` | `@DataJpaTest` + `ReflexiveTest`. Repos autowired `required=false`, plain interfaces (no `CrudRepository` yet) — must gain a `findById` at minimum. Constraints: `message` size (10-60 on announcement, 5-200 on response), `status` mandatory + `@Enumerated(STRING)`, `respondedAt` mandatory on response. Static factories `Test1.createValidAnnouncement(em)`/`createValidResponse(em)`/`createUser(name)` reused by Test2/Test5. |
| 2 | Relations between entities | `ChallengeAnnouncement.challenge/recipients`, `AnnouncementResponse.announcement/recipient` | to-build | `Test2.java` | Same base class. `challenge`/`announcement`/`recipient` → `@ManyToOne`+`@NotNull`; `recipients` → `@ManyToMany`, min-size 2 checked via **direct setter call + `em.flush()` inside `assertThrows`** (the generic `checkThatValuesAreNotValid` helper can't resolve a `List`-typed Lombok setter reflectively from an `ArrayList` runtime value — see `references/design-notes.md` in the skill). Relations point to-build → given (`challenge` → `FriendlyChallenge` id 1/2) and to-build → given (`User`), never the reverse. |
| 3 | Seed-data rows | 3 `ChallengeAnnouncement` rows (ids 1-3), 3 `AnnouncementResponse` rows (ids 1-3) | to-build (student writes the `INSERT`s in `data.sql`) | `Test3.java` | `em.find` + exact literal field assertions (`message`, `status`, `sentAt`/`respondedAt`) + `checkLinkedById` for `challenge`/`announcement`/`recipient`. Announcement 1→challenge 1, 2→challenge 1, 3→challenge 2. Response 1→announcement 1/recipient 4, response 2→announcement 3/recipient 5, response 3→announcement 3/recipient 6. **No seed rows for these two entities exist in the committed `data.sql`** — that absence is intentional, it's this exercise. |
| 4 | Join-table / collection seed-data | `ChallengeAnnouncement.recipients` join rows | to-build (student writes the join-table `INSERT`s) | `Test4.java` | One `checkContainsById(ChallengeAnnouncement.class, id, "getRecipients", userId, em)` per expected member. Announcement 1 → users 4,5. Announcement 2 → users 4,5. Announcement 3 → users 5,6,7. |
| 5 | Service business rule on a GIVEN entity, validated via a checked exception | `match/ChessMatchService.java` (`findAll`/`findById`/`save`), `exceptions/InvalidWinnerException.java` | to-build (service body only — `ChessMatch`/`ChessMatchRepository` are given) | `Test5.java` | `@ExtendWith(MockitoExtension.class)`, `@Mock ChessMatchRepository`. `InvalidWinnerException extends Exception` (checked) — `save` must declare `throws` and `@Transactional(rollbackFor = InvalidWinnerException.class)`, asserted explicitly via `checkTransactionalRollback` as its own test, separate from the plain `checkTransactional` calls for all 3 methods. Business rule: `save` must accept `winner == null` or `winner ∈ {white, black}`, else throw without calling `repo.save`. |
| 6 | Custom aggregate query on a GIVEN repository | `FriendlyChallengeRepository.findParticipantPoints(Integer)` | to-build (query body; `FriendlyChallenge`/`ChessMatch` given, fully seeded) | `Test6.java` | Plain `@DataJpaTest`, no mocking, real seeded data. JPQL `SUM(CASE WHEN winner=... THEN 2 WHEN winner IS NULL AND finish IS NOT NULL THEN 1 ELSE 0 END)` idiom + `ORDER BY` descending (a missing `ORDER BY` was a real bug caught here in a previous pass). 5 test methods across 3 fixtures (challenge 1: win+draw; challenge 2: no finished matches → empty; challenge 3, dedicated fixture with 3 distinct totals and no ties: win/draw/loss values, unfinished-match exclusion, and descending order — added specifically so ties can't pass the order assertion by accident). |
| 7 | REST controller wrapping the query/service | `FriendlyChallengeService.getRanking`, `FriendlyChallengeController` (missing `GET {id}/ranking`) | to-build | `Test7.java` | `@SpringBootTest(RANDOM_PORT)` + MockMvc + `@WithMockUser`. **Uses the mock-based independence technique**: `@MockitoBean FriendlyChallengeRepository` — stubs `findParticipantPoints`/`findById` directly, so this exercise's grade never depends on whether Test 6's query is actually correct. Asserts status **and** `jsonPath` body content/order (`$[0].user.id`, `$[0].points`, ...), not just size — this caught a real missing-sort bug in `getRanking` in a previous pass. Cases: participant → 200 + ordered ranking; non-existent challenge → 404; challenge with no finished matches → 404; authenticated but non-participant → 403. |
| 8 | Algorithm interface, student writes the test class | `challenge/matchmaking/ClosestRatingPairFinder` + 4 implementations (`ValidClosestRatingPairFinder` correct; `NaiveClosestRatingPairFinder` wrong tie-break; `CarelessClosestRatingPairFinder` skips negative-rating validation; `RecklessClosestRatingPairFinder` skips minimum-size validation), `InsufficientPlayersException`/`InvalidRatingException` | to-build: only `matchmaking/ClosestRatingPairFinderTest.java` (student's test class, starts as a bare `fail()`) | `Test8.java` (dispatcher, given) | `Test8` wraps each implementation in a call-counting `WrapperAlgorithm`, reflection-runs every `@Test` method the student wrote in `ClosestRatingPairFinderTest` against all 4 implementations, and checks pass/fail matches the expected outcome per implementation. Each buggy impl is only catchable by a specific non-trivial case (a genuine tie, a negative rating, a 0/1-length array). |

**Alternative 7 (`Test7Alt.java`, statement in `ALTERNATIVE-TEST7.md`)** — same endpoint, contract and fixtures as Test 7, but it also enforces the layered architecture. It combines reflection (the controller has no repository or persistence dependency; the service has no web-layer dependency and uses `UserService` rather than `UserRepository`; there is no `@Transactional` on the controller) with mocks. `@MockitoSpyBean FriendlyChallengeService` is stubbed with a ranking that can't be derived from the repository data, `@MockitoBean FriendlyChallengeRepository` and `@MockitoSpyBean UserService` must see zero interactions from the controller, and the service is unit-tested with plain Mockito mocks. The handler is located through `RequestMappingHandlerMapping`, so every test (the structural ones included) fails until the endpoint exists. Keep exactly one of `Test7`/`Test7Alt` in the published repo.

## Frontend exercises

| # | Category | Component | Test file | Notes |
|---|---|---|---|---|
| 9 | Fetch + render | `challenges/challenge.js` (`Challenge`, empty stub) | `Challenge.test.js` | Fetches `/api/v1/challenges/1/ranking` (MSW fixture in `frontend/src/mocks/handlers.js`), renders "Challenge info"/"Ranking" headings synchronously and "Leader: john_doe (6 points)" after `waitFor`. |
| 10 | Render-from-props | `challenges/challengeDetail.js` (`ChallengeDetail`, empty stub) | `ChallengeDetail.test.js` | Pure render of a `ranking` prop (no fetch): a `<ul>` with one `<li>` per entry, each with a unique `id` attribute (derived from `user.id`, not array index) and text `username - points`. |

## Independence notes
- Structural (given/to-build direction): `ChallengeAnnouncement`/`AnnouncementResponse` (to-build) point at `FriendlyChallenge`/`User` (given) via `@ManyToOne`, never the reverse — `FriendlyChallenge` has no knowledge of announcements, so Tests 5-10 never depend on Tests 1-4 mapping successfully for the app context to boot.
- Mock-based: Test 7's controller test mocks `FriendlyChallengeRepository` (`@MockitoBean`) instead of relying on real seeded data through Test 6's query, so a wrong `findParticipantPoints` implementation can't drag down Test 7's grade.
- Test 8 is structured as "given dispatcher + to-build test class" rather than given/to-build entities — the independence here is that the 4 algorithm implementations are all given (correct+buggy), only the student's own test file is graded, so no entity-mapping dependency applies to this exercise at all.

## Domain notes for next migration
- The chess-match/board/piece value model (`match/` package minus `ChessMatchService`) and the whole `user`/`auth` stack are likely to survive future redesigns unless the new statement drops chess entirely — but "survive" means "reshape slightly," not "untouched," per the long-lived-domain note above.
- `FriendlyChallenge` is this convocatoria's given root specifically because it was deliberately built already-complete before the to-build domain was designed. It was invented from screatch for this convocatoria.
- The two new to-build entities (`ChallengeAnnouncement`, `AnnouncementResponse`) were invented from scratch for this convocatoria specifically to give Tests 1-4 something to build without disturbing `FriendlyChallenge` — a future redesign can swap these for different to-build nouns as long as the same one-way relation direction is preserved.
