package otus.study.cashmachine.bank.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import otus.study.cashmachine.bank.dao.AccountDao;
import otus.study.cashmachine.bank.data.Account;
import otus.study.cashmachine.bank.service.impl.AccountServiceImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AccountServiceTest {

    AccountDao accountDao;

    AccountServiceImpl accountServiceImpl;

    @BeforeEach
    void setup() {
        accountDao = mock(AccountDao.class);
        accountServiceImpl = new AccountServiceImpl(accountDao);
    }

    @Test
    void testCreateAccountWithMatcher() {
        BigDecimal expectedAmount = BigDecimal.ONE;

        ArgumentMatcher<Account> matcher = new ArgumentMatcher<Account>() {
            @Override
            public boolean matches(Account argument) {
                return argument.getAmount().equals(expectedAmount);
            }
        };

        when(accountDao.saveAccount(argThat(matcher))).thenReturn(new Account(0, expectedAmount));
        Account actual = accountServiceImpl.createAccount(expectedAmount);

        assertEquals(expectedAmount, actual.getAmount());
    }

    @Test
    void testCreateAccountWithCaptor() {
        Account expected = new Account(0, BigDecimal.ONE);
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);

        when(accountDao.saveAccount(any())).thenReturn(expected);

        Account actual = accountServiceImpl.createAccount(expected.getAmount());
        verify(accountDao).saveAccount(accountCaptor.capture());

        assertEquals(expected.getAmount(), accountCaptor.getValue().getAmount());
        assertEquals(expected.getAmount(), actual.getAmount());
    }

    @Test
    void addSum() {
        long accId = 123L;
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal toAdd = BigDecimal.ONE;
        Account account = new Account(accId, amount);

        when(accountDao.getAccount(anyLong())).thenReturn(account);
        BigDecimal actualAmount = accountServiceImpl.putMoney(accId, toAdd);

        verify(accountDao).getAccount(anyLong());

        BigDecimal expectedAmount = amount.add(toAdd);
        assertEquals(expectedAmount, actualAmount);
    }

    @Test
    void getSumEnoughMoney() {
        long accId = 123L;
        BigDecimal amount = BigDecimal.TEN;
        BigDecimal amountToGet = BigDecimal.ONE;
        BigDecimal expectedAmount = amount.subtract(amountToGet);
        Account account = new Account(accId, amount);

        when(accountDao.getAccount(anyLong())).thenReturn(account);
        BigDecimal actual = accountServiceImpl.getMoney(accId, amountToGet);

        verify(accountDao).getAccount(anyLong());

        assertEquals(expectedAmount, actual);
    }

    @Test
    void getSumNotEnoughMoney() {
        long accId = 123L;
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal amountToGet = BigDecimal.ONE;
        BigDecimal expectedAmount = amount.subtract(amountToGet);
        Account account = new Account(accId, amount);

        when(accountDao.getAccount(anyLong())).thenReturn(account);

        Exception e = assertThrows(IllegalArgumentException.class,
                () -> accountServiceImpl.getMoney(accId, amountToGet));

        verify(accountDao).getAccount(anyLong());

        assertEquals("Not enough money", e.getMessage());
    }

    @Test
    void getAccount() {
        long accId = 123L;
        BigDecimal amount = BigDecimal.ONE;
        Account account = new Account(accId, amount);

        when(accountDao.getAccount(anyLong())).thenReturn(account);
        Account actual = accountServiceImpl.getAccount(accId);

        verify(accountDao).getAccount(anyLong());

        assertEquals(accId, actual.getId());
        assertEquals(amount, actual.getAmount());
    }

    @Test
    void checkBalance() {
        long accId = 123L;
        BigDecimal amount = BigDecimal.ONE;
        Account account = new Account(accId, amount);

        when(accountDao.getAccount(anyLong())).thenReturn(account);
        BigDecimal actualAmount = accountServiceImpl.checkBalance(accId);

        verify(accountDao).getAccount(anyLong());

        assertEquals(amount, actualAmount);
    }
}
