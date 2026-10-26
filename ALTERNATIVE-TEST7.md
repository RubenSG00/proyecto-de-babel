# Alternative exercise 7 — layered architecture enforced by tests

Teacher-only file. **Delete before publishing to students** (same as `EXAM-STRUCTURE.md`).

This is a drop-in alternative to exercise 7. The functionality is the same (the `GET {id}/ranking`
endpoint), so Tests 6, 9 and 10 stay consistent, but the grading also checks that the solution follows
the project's layered architecture. A solution that works end to end but injects
`FriendlyChallengeRepository` into the controller, runs the access check in the controller, reads
users through `UserRepository`, or puts `@Transactional` on the controller loses points.

## How to switch to this variant

1. Delete `src/test/java/es/us/dp1/chess/tournament/Test7.java`.
2. Rename `Test7Alt.java` to `Test7.java` (class name too). You can also rename the `test7Alt*`
   methods to `test7*` if you like.
3. Replace the Test 7 section in `README.md` and `README.en.md` with the statements below.
4. If you *keep* the original Test 7, delete `Test7Alt.java` instead. Only one of them should reach
   students, or exercise 7 is graded twice.

## What `Test7Alt` checks (10 tests, all failing on the unsolved scaffold)

| Test | Kind | What it checks |
|---|---|---|
| `test7AltCanGetRankingAsParticipant` | functional | Full stack (repository mocked): 200 + ranking body in order |
| `test7AltCannotGetRankingOfNonExistentChallengeOrWithoutFinishedMatches` | functional | 404 for both cases |
| `test7AltCannotGetRankingIfNotParticipant` | functional | 403 |
| `test7AltControllerDoesNotDependOnRepositories` | structural (reflection) | The handler for `GET /api/v1/challenges/{id}/ranking` lives in `FriendlyChallengeController`. The controller has no field, constructor parameter or method parameter that is a Spring Data `Repository`, a `@Repository`, an `EntityManager`/`EntityManagerFactory`, a `JdbcTemplate` or a `DataSource`. It also checks the values Spring actually injected, so a field typed `Object` holding a repository is still caught. The controller must have a `FriendlyChallengeService` injected. |
| `test7AltControllerDelegatesOnTheServiceWithoutAccessingData` | behavioural (mocks) | `FriendlyChallengeService` is a `@MockitoSpyBean` stubbed to return a ranking that can't be derived from the repository data. The endpoint must return exactly that, call `getRanking(1)`, and have **no interactions** with `FriendlyChallengeRepository` (`@MockitoBean`) or `UserService` (`@MockitoSpyBean`). |
| `test7AltControllerLetsServiceExceptionsPropagate` | behavioural (mocks) | The stubbed service throws `ResourceNotFoundException` / `AccessDeniedException`, and the controller must turn them into 404 / 403 without touching the repository or `UserService`. Catches controllers that swallow exceptions or re-check things themselves. |
| `test7AltTransactionsAreDemarcatedInTheServiceLayer` | structural | `getRanking` in the service is `@Transactional`. Neither the controller class nor any of its methods is `@Transactional` (Spring or Jakarta). |
| `test7AltServiceDependsOnlyOnRepositoriesAndServices` | structural + behavioural | The service has no web-layer dependencies (`ResponseEntity`, servlet request/response/session, controllers) in fields, parameters or return types. Its only repository is `FriendlyChallengeRepository`, with no `UserRepository`, `EntityManager` or `JdbcTemplate`. With plain Mockito mocks, `getRanking` must call `findParticipantPoints` and `UserService.findCurrentUser()`. |
| `test7AltServiceComputesRankingUsingItsRepository` | unit (Mockito) | Maps `Object[]` rows to `RankingEntry` in the right order, obtaining data via `repo.findById` + `repo.findParticipantPoints` |
| `test7AltServiceEnforcesTheBusinessRules` | unit (Mockito) | The service itself (not the controller) throws `ResourceNotFoundException` (missing challenge / no finished matches) and `AccessDeniedException` (non-participant) |

Validated against a correct reference solution (10/10), the unsolved scaffold (0/10) and these
wrong solutions:

| Wrong solution | Result |
|---|---|
| Repository injected into the controller, logic in the controller, service left as `null` | 4/10 (only the 3 functional tests and the transaction test pass) |
| Correct solution plus an unused repository field in the controller | 9/10 |
| Controller runs the access check with `UserService` | 8/10 |
| Service reads the user through `UserRepository` + `SecurityContextHolder` | 7/10 |
| `@Transactional` on the controller method | 9/10 |
| Controller catches exceptions and returns 200 | 7/10 |

---

## Statement (ES) — replaces "Test 7" in README.md

### Test 7 – Creación del controlador para devolver la clasificación de un desafío respetando la arquitectura en capas

Crear un método en el controlador `FriendlyChallengeController` (alojado en el paquete `es.us.dp1.chess.tournament.challenge`) que permita devolver la clasificación de un desafío. El método debe responder a peticiones tipo GET en la URL:

```
http://localhost:8080/api/v1/challenges/X/ranking
```

El método debe devolver la clasificación del desafío ordenada de mayor a menor puntuación, donde X es la Id del desafío. Recuerde que la clase `UserService` tiene un método `findCurrentUser()` que permite obtener el usuario actualmente logado, debe usar dicho método para comprobar el acceso.

Se debe devolver el código de estado **404 (NOT_FOUND)** en los siguientes casos:

- En caso de que no exista un desafío con el id especificado en el sistema.
- En caso de que aún no se haya finalizado ninguna partida del desafío.

En caso de que el usuario logado no sea uno de los participantes de dicho desafío debe devolverse un código de estado **403 (FORBIDDEN)**. Nótese que se proporcionan excepciones ya creadas para representar estos escenarios, como la clase `ResourceNotFoundException` o la clase `AccessDeniedException`.

Para implementar este ejercicio debe modificar tanto la clase `FriendlyChallengeService` (método `getRanking`) como la clase `FriendlyChallengeController`. El servicio `FriendlyChallengeService` y el repositorio `FriendlyChallengeRepository` ya se proporcionan, y **su solución debe respetar la arquitectura en capas de la aplicación**. Las pruebas de este ejercicio comprueban automáticamente (usando mocks e inspección de las clases) que se cumplen las siguientes reglas, y fallarán si no se respetan aunque el endpoint devuelva el resultado correcto:

1. **El controlador sólo depende de servicios.** `FriendlyChallengeController` no puede inyectar ni usar repositorios (`FriendlyChallengeRepository` ni ningún otro), ni `EntityManager` ni ningún otro mecanismo de acceso a datos. Debe obtener la clasificación invocando al método `getRanking` de `FriendlyChallengeService`.
2. **La lógica de negocio está en el servicio.** Comprobar si el desafío existe, si el usuario logado es participante o si hay partidas finalizadas, así como construir la clasificación, es responsabilidad de `FriendlyChallengeService`. El controlador no debe usar `UserService` ni hacer esas comprobaciones: sólo delega en el servicio y construye la respuesta HTTP.
3. **El controlador no captura las excepciones del servicio.** Las excepciones `ResourceNotFoundException` y `AccessDeniedException` lanzadas por el servicio deben propagarse, para que el manejador de excepciones de la aplicación las traduzca a los códigos 404 y 403.
4. **El servicio accede a sus datos a través de su repositorio y a los de otros módulos a través de sus servicios.** `FriendlyChallengeService` debe obtener el desafío y los puntos mediante `FriendlyChallengeRepository`, y el usuario logado mediante `UserService` (no mediante `UserRepository`, `SecurityContextHolder` ni `EntityManager`).
5. **El servicio no depende de la capa web.** `FriendlyChallengeService` no puede usar `ResponseEntity`, objetos de petición/respuesta HTTP ni controladores.
6. **Las transacciones se definen en el servicio.** El método `getRanking` del servicio debe ser transaccional y el controlador (ni la clase ni sus métodos) no debe anotarse con `@Transactional`.

Este ejercicio es independiente del 6 y puede resolverse aún cuando el ejercicio 6 no esté resuelto, sencillamente invocando al repositorio desde el código del servicio (puesto que el repositorio en los tests está mockeado).

---

## Statement (EN) — replaces "Test 7" in README.en.md

### Test 7 – Creating the controller to return the ranking of a challenge while following the layered architecture

Create a method in the `FriendlyChallengeController` controller (located in the `es.us.dp1.chess.tournament.challenge` package) that returns the ranking of a challenge. The method must respond to GET requests at the URL:

```
http://localhost:8080/api/v1/challenges/X/ranking
```

The method must return the challenge's ranking sorted from highest to lowest score, where X is the challenge's Id. Remember that the `UserService` class has a `findCurrentUser()` method that lets you obtain the currently logged-in user; you must use this method to check access.

Status code **404 (NOT_FOUND)** must be returned in the following cases:

- If there is no challenge with the specified id in the system.
- If no match of the challenge has been finished yet.

If the logged-in user is not one of the participants of that challenge, a status code of **403 (FORBIDDEN)** must be returned. Note that exceptions are already provided to represent these scenarios, such as the `ResourceNotFoundException` class or the `AccessDeniedException` class.

To implement this exercise you must modify both the `FriendlyChallengeService` class (`getRanking` method) and the `FriendlyChallengeController` class. The `FriendlyChallengeService` service and the `FriendlyChallengeRepository` repository are already provided, and **your solution must follow the application's layered architecture**. The tests of this exercise automatically check (using mocks and class inspection) that the following rules are met. They will fail if a rule is broken, even if the endpoint returns the right result:

1. **The controller depends only on services.** `FriendlyChallengeController` must not inject or use repositories (`FriendlyChallengeRepository` or any other), nor `EntityManager` or any other data-access mechanism. It must obtain the ranking by calling the `getRanking` method of `FriendlyChallengeService`.
2. **Business logic lives in the service.** Checking whether the challenge exists, whether the logged-in user is a participant, whether there are finished matches, and building the ranking are all responsibilities of `FriendlyChallengeService`. The controller must not use `UserService` or perform those checks. It only delegates to the service and builds the HTTP response.
3. **The controller does not catch the service's exceptions.** The `ResourceNotFoundException` and `AccessDeniedException` thrown by the service must propagate, so that the application's exception handler translates them into 404 and 403 status codes.
4. **The service accesses its own data through its repository, and data from other modules through their services.** `FriendlyChallengeService` must obtain the challenge and the points through `FriendlyChallengeRepository`, and the logged-in user through `UserService` (not through `UserRepository`, `SecurityContextHolder` or `EntityManager`).
5. **The service does not depend on the web layer.** `FriendlyChallengeService` must not use `ResponseEntity`, HTTP request/response objects or controllers.
6. **Transactions are defined in the service.** The service's `getRanking` method must be transactional, and the controller (neither the class nor its methods) must not be annotated with `@Transactional`.

This exercise is independent of Exercise 6 and can be solved even if Exercise 6 has not been completed, simply by invoking the repository from the service code, since the repository is mocked in the tests.
