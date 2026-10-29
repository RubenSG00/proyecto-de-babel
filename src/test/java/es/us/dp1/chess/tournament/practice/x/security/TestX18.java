package es.us.dp1.chess.tournament.practice.x.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@Transactional
public class TestX18 {

    @Autowired
    WebApplicationContext context;

    @Autowired
    ExpenseRepository repo;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "admin1", authorities = { "ADMIN" })
    public void testX18AnAdminApprovesTheExpenseOfSomebodyElse() throws Exception {
        mockMvc.perform(post("/api/v1/expenses/3/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.approvedBy.username").value("admin1"));
        Expense e = repo.findById(3).get();
        assertEquals(ExpenseStatus.APPROVED, e.getStatus());
        assertEquals(1, e.getApprovedBy().getId());
    }

    @Test
    @WithMockUser(username = "admin1", authorities = { "ADMIN" })
    public void testX18NobodyApprovesTheirOwnExpenses() throws Exception {
        mockMvc.perform(post("/api/v1/expenses/5/approve")).andExpect(status().isForbidden());
        assertEquals(ExpenseStatus.PENDING, repo.findById(5).get().getStatus());
    }

    @Test
    @WithMockUser(username = "player2", authorities = { "PLAYER" })
    public void testX18OnlyAdminsApprove() throws Exception {
        mockMvc.perform(post("/api/v1/expenses/1/approve")).andExpect(status().isForbidden());
        assertEquals(ExpenseStatus.PENDING, repo.findById(1).get().getStatus());
    }

    @Test
    @WithMockUser(username = "admin1", authorities = { "ADMIN" })
    public void testX18UnknownExpenseIs404() throws Exception {
        mockMvc.perform(post("/api/v1/expenses/474629/approve")).andExpect(status().isNotFound());
    }

    @Test
    public void testX18ApproveIsTransactional() throws NoSuchMethodException {
        assertNotNull(ExpenseService.class.getDeclaredMethod("approve", Integer.class).getAnnotation(Transactional.class),
                "approve must be @Transactional");
    }

}
