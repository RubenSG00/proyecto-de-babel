package es.us.dp1.chess.tournament.practice.x.transactions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class TestX07 {

    @Mock
    WalletRepository repo;

    WalletService service;

    Wallet alice, bob;

    @BeforeEach
    public void setup() {
        service = new WalletService(repo);
        alice = wallet(1, "alice", 100);
        bob = wallet(2, "bob", 20);
        lenient().when(repo.findById(1)).thenReturn(Optional.of(alice));
        lenient().when(repo.findById(2)).thenReturn(Optional.of(bob));
        lenient().when(repo.findById(99)).thenReturn(Optional.empty());
        lenient().when(repo.save(any(Wallet.class))).thenAnswer(i -> i.getArgument(0));
    }

    private static Wallet wallet(Integer id, String owner, int balance) {
        Wallet w = new Wallet();
        w.setId(id);
        w.setOwner(owner);
        w.setBalance(balance);
        return w;
    }

    private static Transactional transactional(String name, Class<?>... types) throws NoSuchMethodException {
        return WalletService.class.getDeclaredMethod(name, types).getAnnotation(Transactional.class);
    }

    @Test
    public void testX07TransactionsAreDeclaredAtClassLevel() throws NoSuchMethodException {
        assertNotNull(WalletService.class.getAnnotation(Service.class), "WalletService must be a Spring service");
        assertNotNull(WalletService.class.getAnnotation(Transactional.class),
                "WalletService must be annotated with Spring's @Transactional at class level");
        assertNull(transactional("deposit", Integer.class, int.class),
                "deposit must not have a method-level @Transactional: the class-level one is enough");
    }

    @Test
    public void testX07ReadsAreReadOnly() throws NoSuchMethodException {
        Transactional findAll = transactional("findAll");
        Transactional findById = transactional("findById", Integer.class);
        assertTrue(findAll != null && findAll.readOnly(), "findAll must be a read-only transaction");
        assertTrue(findById != null && findById.readOnly(), "findById must be a read-only transaction");
    }

    @Test
    public void testX07CheckedExceptionRollsBack() throws NoSuchMethodException {
        for (Method m : new Method[] {
                WalletService.class.getDeclaredMethod("withdraw", Integer.class, int.class),
                WalletService.class.getDeclaredMethod("transfer", Integer.class, Integer.class, int.class) }) {
            Transactional t = m.getAnnotation(Transactional.class);
            assertNotNull(t, m.getName() + " needs its own @Transactional to declare the rollback");
            assertTrue(Arrays.asList(t.rollbackFor()).contains(InsufficientFundsException.class),
                    m.getName() + " must roll back when InsufficientFundsException is thrown");
            assertFalse(t.readOnly(), m.getName() + " writes, so it cannot be read-only");
        }
    }

    @Test
    public void testX07FindAllAndFindById() {
        when(repo.findAll()).thenReturn(List.of(alice, bob));
        assertEquals(List.of(alice, bob), service.findAll());
        assertSame(alice, service.findById(1));
        assertThrows(ResourceNotFoundException.class, () -> service.findById(99));
    }

    @Test
    public void testX07Deposit() {
        Wallet result = service.deposit(1, 50);
        assertEquals(150, alice.getBalance());
        assertSame(alice, result);
        verify(repo).save(alice);
        assertThrows(IllegalArgumentException.class, () -> service.deposit(2, 0));
        assertThrows(IllegalArgumentException.class, () -> service.deposit(2, -5));
        assertEquals(20, bob.getBalance());
        verify(repo, never()).save(bob);
    }

    @Test
    public void testX07Withdraw() throws InsufficientFundsException {
        service.withdraw(1, 100);
        assertEquals(0, alice.getBalance());
        verify(repo).save(alice);
        assertThrows(InsufficientFundsException.class, () -> service.withdraw(2, 21));
        assertThrows(IllegalArgumentException.class, () -> service.withdraw(2, 0));
        assertEquals(20, bob.getBalance());
        verify(repo, never()).save(bob);
    }

    @Test
    public void testX07Transfer() throws InsufficientFundsException {
        service.transfer(1, 2, 30);
        assertEquals(70, alice.getBalance());
        assertEquals(50, bob.getBalance());
        verify(repo).save(alice);
        verify(repo).save(bob);
    }

    @Test
    public void testX07TransferWithoutFundsSavesNothing() {
        assertThrows(InsufficientFundsException.class, () -> service.transfer(2, 1, 21));
        verify(repo, never()).save(any(Wallet.class));
        assertEquals(20, bob.getBalance());
        assertEquals(100, alice.getBalance());
    }

}
