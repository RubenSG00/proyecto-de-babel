package es.us.dp1.chess.tournament;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import es.us.dp1.chess.tournament.challenge.FriendlyChallenge;
import es.us.dp1.chess.tournament.challenge.FriendlyChallengeController;
import es.us.dp1.chess.tournament.challenge.FriendlyChallengeRepository;
import es.us.dp1.chess.tournament.challenge.FriendlyChallengeService;
import es.us.dp1.chess.tournament.challenge.RankingEntry;
import es.us.dp1.chess.tournament.exceptions.AccessDeniedException;
import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
import es.us.dp1.chess.tournament.user.User;
import es.us.dp1.chess.tournament.user.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Alternative version of Test 7 that, besides the functional behaviour of the
 * ranking endpoint, validates that the solution follows the layered architecture
 * of the project:
 * <ul>
 * <li>Controllers depend on services, never on repositories (or any other
 * persistence API).</li>
 * <li>Controllers delegate all the business logic (access control, validations,
 * computations) to the services and let exceptions propagate.</li>
 * <li>Services access their data through their own repository, and data of other
 * modules (users) through the corresponding service.</li>
 * <li>Services do not depend on the web layer, and transactions are demarcated in
 * the service layer, not in the controllers.</li>
 * </ul>
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Test7Alt extends ReflexiveTest {

	private static final String URL = "/api/v1/challenges";
	private static final String RANKING_PATTERN = "/api/v1/challenges/{id}/ranking";

	@Autowired
	private WebApplicationContext context;

	@Autowired
	@Qualifier("requestMappingHandlerMapping")
	private RequestMappingHandlerMapping handlerMapping;

	@MockitoSpyBean
	private UserService userService;

	@MockitoSpyBean
	private FriendlyChallengeService friendlyChallengeService;

	@MockitoBean
	private FriendlyChallengeRepository friendlyChallengeRepository;

	private MockMvc mockMvc;

	private User player1;
	private User player2;
	private User player3;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();

		player1 = userService.findUser("player1");
		player2 = userService.findUser("player2");
		player3 = userService.findUser("player3");

		FriendlyChallenge challenge1 = createChallenge(1, 2, player1, player2);
		FriendlyChallenge challenge2 = createChallenge(2, 0, player1, player2);

		when(friendlyChallengeRepository.findById(1)).thenReturn(Optional.of(challenge1));
		when(friendlyChallengeRepository.findById(2)).thenReturn(Optional.of(challenge2));
		when(friendlyChallengeRepository.findById(474629)).thenReturn(Optional.empty());
		when(friendlyChallengeRepository.existsById(1)).thenReturn(true);
		when(friendlyChallengeRepository.existsById(2)).thenReturn(true);
		when(friendlyChallengeRepository.existsById(474629)).thenReturn(false);
		when(friendlyChallengeRepository.findParticipantPoints(1)).thenReturn(List.of(
				new Object[] { player1, 3L },
				new Object[] { player2, 1L }));
		when(friendlyChallengeRepository.findParticipantPoints(2)).thenReturn(List.of());

		clearInvocations(userService, friendlyChallengeService, friendlyChallengeRepository);
	}

	// ------------------------------------------------------------------------
	// Functional behaviour (whole stack, repository mocked)
	// ------------------------------------------------------------------------

	@Test
	@WithMockUser(username = "player1", authorities = { "PLAYER" })
	public void test7AltCanGetRankingAsParticipant() throws Exception {
		mockMvc.perform(get(URL + "/1/ranking"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].user.id").value(4))
				.andExpect(jsonPath("$[0].points").value(3))
				.andExpect(jsonPath("$[1].user.id").value(5))
				.andExpect(jsonPath("$[1].points").value(1));
	}

	@Test
	@WithMockUser(username = "player1", authorities = { "PLAYER" })
	public void test7AltCannotGetRankingOfNonExistentChallengeOrWithoutFinishedMatches() throws Exception {
		mockMvc.perform(get(URL + "/474629/ranking"))
				.andExpect(status().isNotFound());
		mockMvc.perform(get(URL + "/2/ranking"))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "player3", authorities = { "PLAYER" })
	public void test7AltCannotGetRankingIfNotParticipant() throws Exception {
		mockMvc.perform(get(URL + "/1/ranking"))
				.andExpect(status().isForbidden());
	}

	// ------------------------------------------------------------------------
	// Controller layer: depends only on services and delegates on them
	// ------------------------------------------------------------------------

	@Test
	public void test7AltControllerDoesNotDependOnRepositories() {
		HandlerMethod handler = findRankingHandler();
		Class<?> controllerClass = AopUtils.getTargetClass(handler.getBean() instanceof String name
				? context.getBean(name)
				: handler.getBean());

		assertEquals(FriendlyChallengeController.class, controllerClass,
				"The ranking endpoint must be implemented in FriendlyChallengeController");

		List<String> violations = new ArrayList<>();
		collectPersistenceDependencies(controllerClass, violations);
		assertNoViolations("FriendlyChallengeController must not depend on repositories or on any "
				+ "persistence API; it must use FriendlyChallengeService instead", violations);

		Object controller = context.getBean(FriendlyChallengeController.class);
		Object injectedService = findInjectedDependency(AopUtils.getTargetClass(controller),
				unwrap(controller), FriendlyChallengeService.class);
		assertNotNull(injectedService,
				"FriendlyChallengeController must use FriendlyChallengeService (injected by Spring)");
	}

	@Test
	@WithMockUser(username = "player1", authorities = { "PLAYER" })
	public void test7AltControllerDelegatesOnTheServiceWithoutAccessingData() throws Exception {
		findRankingHandler();
		// The service returns a ranking that cannot be derived from the repository data,
		// so the controller must return exactly what the service provides:
		List<RankingEntry> ranking = List.of(
				new RankingEntry(player3, 7L),
				new RankingEntry(player2, 5L),
				new RankingEntry(player1, 2L));
		doReturn(ranking).when(friendlyChallengeService).getRanking(1);
		clearInvocations(friendlyChallengeService);

		mockMvc.perform(get(URL + "/1/ranking"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].user.id").value(player3.getId()))
				.andExpect(jsonPath("$[0].points").value(7))
				.andExpect(jsonPath("$[1].user.id").value(player2.getId()))
				.andExpect(jsonPath("$[1].points").value(5))
				.andExpect(jsonPath("$[2].user.id").value(player1.getId()))
				.andExpect(jsonPath("$[2].points").value(2));

		verify(friendlyChallengeService, description(
				"The ranking endpoint must obtain the ranking by invoking FriendlyChallengeService.getRanking"))
				.getRanking(1);
		expectNoInteractions(friendlyChallengeRepository,
				"The controller must not access FriendlyChallengeRepository, only FriendlyChallengeService");
		// Access control is business logic, and it belongs to the service:
		expectNoInteractions(userService,
				"The controller must not use UserService: checking the access is business logic of the service");
	}

	@Test
	@WithMockUser(username = "player1", authorities = { "PLAYER" })
	public void test7AltControllerLetsServiceExceptionsPropagate() throws Exception {
		findRankingHandler();
		doThrow(new ResourceNotFoundException("FriendlyChallenge", "id", 1))
				.when(friendlyChallengeService).getRanking(1);
		doThrow(new AccessDeniedException("Not a participant"))
				.when(friendlyChallengeService).getRanking(2);

		mockMvc.perform(get(URL + "/1/ranking"))
				.andExpect(status().isNotFound());
		mockMvc.perform(get(URL + "/2/ranking"))
				.andExpect(status().isForbidden());

		verify(friendlyChallengeService, times(2).description(
				"The ranking endpoint must invoke FriendlyChallengeService.getRanking once per request"))
				.getRanking(anyInt());
		expectNoInteractions(friendlyChallengeRepository,
				"The controller must not access FriendlyChallengeRepository, only FriendlyChallengeService");
		expectNoInteractions(userService,
				"The controller must not use UserService: checking the access is business logic of the service");
	}

	@Test
	public void test7AltTransactionsAreDemarcatedInTheServiceLayer() {
		HandlerMethod handler = findRankingHandler();
		checkTransactional(FriendlyChallengeService.class, "getRanking", Integer.class);

		assertFalse(isTransactional(FriendlyChallengeController.class),
				"FriendlyChallengeController must not be transactional: transactions belong to the service layer");
		assertFalse(isTransactional(handler.getMethod()),
				"The ranking endpoint must not be transactional: transactions belong to the service layer");
		for (Method m : FriendlyChallengeController.class.getDeclaredMethods())
			assertFalse(isTransactional(m), "The controller method " + m.getName()
					+ " must not be transactional: transactions belong to the service layer");
	}

	// ------------------------------------------------------------------------
	// Service layer: uses its repository and other services, not the web layer
	// ------------------------------------------------------------------------

	@Test
	public void test7AltServiceDependsOnlyOnRepositoriesAndServices() {
		List<String> violations = new ArrayList<>();
		collectWebDependencies(FriendlyChallengeService.class, violations);
		for (Field f : allFields(FriendlyChallengeService.class)) {
			if (isRepositoryType(f.getType()) && !FriendlyChallengeRepository.class.equals(f.getType()))
				violations.add("field '" + f.getName() + "' of type " + f.getType().getSimpleName()
						+ " (use the service of that module instead of its repository)");
			if (EntityManager.class.isAssignableFrom(f.getType())
					|| EntityManagerFactory.class.isAssignableFrom(f.getType())
					|| JdbcTemplate.class.isAssignableFrom(f.getType()))
				violations.add("field '" + f.getName() + "' of type " + f.getType().getSimpleName()
						+ " (access the data through FriendlyChallengeRepository)");
		}
		assertNoViolations("FriendlyChallengeService must only depend on its repository and on other services",
				violations);

		FriendlyChallengeRepository repo = mock(FriendlyChallengeRepository.class);
		UserService users = mock(UserService.class);
		mockChallengeRepository(repo);
		when(users.findCurrentUser()).thenReturn(player1);

		new FriendlyChallengeService(repo, users).getRanking(1);

		verify(repo, atLeastOnce().description(
				"FriendlyChallengeService must obtain the points through FriendlyChallengeRepository.findParticipantPoints"))
				.findParticipantPoints(1);
		verify(users, atLeastOnce().description(
				"FriendlyChallengeService must obtain the current user through UserService.findCurrentUser"))
				.findCurrentUser();
	}

	@Test
	public void test7AltServiceComputesRankingUsingItsRepository() {
		FriendlyChallengeRepository repo = mock(FriendlyChallengeRepository.class);
		UserService users = mock(UserService.class);
		mockChallengeRepository(repo);
		when(users.findCurrentUser()).thenReturn(player2);

		List<RankingEntry> ranking = new FriendlyChallengeService(repo, users).getRanking(1);

		assertNotNull(ranking, "getRanking must not return null");
		assertEquals(3, ranking.size(), "The ranking must contain one entry per row returned by the repository");
		assertEquals(player1, ranking.get(0).getUser());
		assertEquals(5L, ranking.get(0).getPoints().longValue());
		assertEquals(player3, ranking.get(1).getUser());
		assertEquals(3L, ranking.get(1).getPoints().longValue());
		assertEquals(player2, ranking.get(2).getUser());
		assertEquals(1L, ranking.get(2).getPoints().longValue());
		verify(repo, atLeastOnce().description(
				"FriendlyChallengeService must obtain the challenge through FriendlyChallengeRepository.findById"))
				.findById(1);
		verify(repo, atLeastOnce().description(
				"FriendlyChallengeService must obtain the points through FriendlyChallengeRepository.findParticipantPoints"))
				.findParticipantPoints(1);
	}

	@Test
	public void test7AltServiceEnforcesTheBusinessRules() {
		FriendlyChallengeRepository repo = mock(FriendlyChallengeRepository.class);
		UserService users = mock(UserService.class);
		mockChallengeRepository(repo);
		FriendlyChallengeService service = new FriendlyChallengeService(repo, users);

		when(users.findCurrentUser()).thenReturn(player1);
		assertThrows(ResourceNotFoundException.class, () -> service.getRanking(474629),
				"getRanking must throw ResourceNotFoundException if the challenge does not exist");
		assertThrows(ResourceNotFoundException.class, () -> service.getRanking(2),
				"getRanking must throw ResourceNotFoundException if no match of the challenge is finished");

		User outsider = userService.findUser("player4");
		when(users.findCurrentUser()).thenReturn(outsider);
		assertThrows(AccessDeniedException.class, () -> service.getRanking(1),
				"getRanking must throw AccessDeniedException if the current user is not a participant");
		verify(users, atLeastOnce().description(
				"FriendlyChallengeService must obtain the current user through UserService.findCurrentUser"))
				.findCurrentUser();
	}

	// ------------------------------------------------------------------------
	// Helpers
	// ------------------------------------------------------------------------

	private FriendlyChallenge createChallenge(Integer id, Integer completedMatches, User... participants) {
		FriendlyChallenge challenge = new FriendlyChallenge();
		challenge.setId(id);
		challenge.setStartDate("2026-07-01");
		challenge.setEndDate("2026-07-02");
		challenge.setTotalMatches(2);
		challenge.setCompletedMatches(completedMatches);
		challenge.setParticipants(List.of(participants));
		return challenge;
	}

	private void mockChallengeRepository(FriendlyChallengeRepository repo) {
		FriendlyChallenge challenge1 = createChallenge(1, 3, player1, player2, player3);
		FriendlyChallenge challenge2 = createChallenge(2, 0, player1, player2);
		when(repo.findById(anyInt())).thenReturn(Optional.empty());
		when(repo.findById(1)).thenReturn(Optional.of(challenge1));
		when(repo.findById(2)).thenReturn(Optional.of(challenge2));
		when(repo.existsById(1)).thenReturn(true);
		when(repo.existsById(2)).thenReturn(true);
		when(repo.findParticipantPoints(anyInt())).thenReturn(List.of());
		when(repo.findParticipantPoints(1)).thenReturn(List.of(
				new Object[] { player1, 5L },
				new Object[] { player3, 3L },
				new Object[] { player2, 1L }));
	}

	private HandlerMethod findRankingHandler() {
		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMapping.getHandlerMethods().entrySet()) {
			RequestMappingInfo info = entry.getKey();
			boolean isGet = info.getMethodsCondition().getMethods().isEmpty()
					|| info.getMethodsCondition().getMethods().stream().anyMatch(m -> m.name().equals("GET"));
			boolean matchesPath = info.getPatternValues().stream()
					.map(p -> p.replaceAll("\\{[^}]*\\}", "{id}"))
					.anyMatch(RANKING_PATTERN::equals);
			if (isGet && matchesPath)
				return entry.getValue();
		}
		fail("There is no controller method responding to GET " + RANKING_PATTERN);
		return null;
	}

	private static Object unwrap(Object bean) {
		try {
			if (AopUtils.isAopProxy(bean) && bean instanceof org.springframework.aop.framework.Advised advised)
				return advised.getTargetSource().getTarget();
		} catch (Exception e) {
			fail("Unable to inspect the controller bean: " + e.getMessage());
		}
		return bean;
	}

	private static Object findInjectedDependency(Class<?> type, Object instance, Class<?> dependencyType) {
		for (Field f : allFields(type)) {
			if (!dependencyType.isAssignableFrom(f.getType()))
				continue;
			try {
				f.setAccessible(true);
				Object value = f.get(instance);
				if (value != null)
					return value;
			} catch (ReflectiveOperationException | RuntimeException e) {
				// Keep looking in the remaining fields
			}
		}
		return null;
	}

	private void collectPersistenceDependencies(Class<?> type, List<String> violations) {
		for (Field f : allFields(type))
			if (isPersistenceType(f.getType()))
				violations.add("field '" + f.getName() + "' of type " + f.getType().getSimpleName());
		for (Constructor<?> c : type.getDeclaredConstructors())
			for (Parameter p : c.getParameters())
				if (isPersistenceType(p.getType()))
					violations.add("constructor parameter of type " + p.getType().getSimpleName());
		for (Method m : type.getDeclaredMethods())
			for (Parameter p : m.getParameters())
				if (isPersistenceType(p.getType()))
					violations.add("parameter of type " + p.getType().getSimpleName() + " in method " + m.getName());

		// Also inspect the values actually injected by Spring (e.g. fields declared as Object)
		Object controller = unwrap(context.getBean(type));
		for (Field f : allFields(type)) {
			try {
				f.setAccessible(true);
				Object value = f.get(controller);
				if (value != null && !isPersistenceType(f.getType())
						&& (value instanceof org.springframework.data.repository.Repository<?, ?>
								|| value instanceof EntityManager || value instanceof JdbcTemplate))
					violations.add("field '" + f.getName() + "' holding a " + value.getClass().getSimpleName());
			} catch (ReflectiveOperationException | RuntimeException e) {
				// Static or inaccessible fields are not relevant for this check
			}
		}
	}

	private static void collectWebDependencies(Class<?> type, List<String> violations) {
		for (Field f : allFields(type))
			if (isWebType(f.getType()))
				violations.add("field '" + f.getName() + "' of type " + f.getType().getSimpleName()
						+ " (services must not depend on the web layer)");
		for (Method m : type.getDeclaredMethods()) {
			if (isWebType(m.getReturnType()))
				violations.add("method " + m.getName() + " returns " + m.getReturnType().getSimpleName()
						+ " (services must not depend on the web layer)");
			for (Parameter p : m.getParameters())
				if (isWebType(p.getType()))
					violations.add("method " + m.getName() + " receives a " + p.getType().getSimpleName()
							+ " (services must not depend on the web layer)");
		}
	}

	private static boolean isRepositoryType(Class<?> type) {
		return org.springframework.data.repository.Repository.class.isAssignableFrom(type)
				|| type.isAnnotationPresent(org.springframework.stereotype.Repository.class);
	}

	private static boolean isPersistenceType(Class<?> type) {
		return isRepositoryType(type)
				|| EntityManager.class.isAssignableFrom(type)
				|| EntityManagerFactory.class.isAssignableFrom(type)
				|| JdbcTemplate.class.isAssignableFrom(type)
				|| javax.sql.DataSource.class.isAssignableFrom(type);
	}

	private static boolean isWebType(Class<?> type) {
		return ResponseEntity.class.isAssignableFrom(type)
				|| ServletRequest.class.isAssignableFrom(type)
				|| ServletResponse.class.isAssignableFrom(type)
				|| HttpSession.class.isAssignableFrom(type)
				|| type.isAnnotationPresent(RestController.class)
				|| type.isAnnotationPresent(Controller.class);
	}

	private static boolean isTransactional(AnnotatedElement element) {
		return element.isAnnotationPresent(Transactional.class)
				|| element.isAnnotationPresent(jakarta.transaction.Transactional.class);
	}

	private static List<Field> allFields(Class<?> type) {
		List<Field> fields = new ArrayList<>();
		for (Class<?> c = type; c != null && c != Object.class; c = c.getSuperclass())
			for (Field f : c.getDeclaredFields())
				if (!f.isSynthetic())
					fields.add(f);
		return fields;
	}

	private static void expectNoInteractions(Object mock, String message) {
		try {
			verifyNoInteractions(mock);
		} catch (MockitoAssertionError e) {
			fail(message);
		}
	}

	private static void assertNoViolations(String rule, List<String> violations) {
		if (!violations.isEmpty())
			fail(rule + ". Violations found: " + String.join("; ", violations));
	}
}
