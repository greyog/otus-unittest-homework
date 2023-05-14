package otus.study.cashmachine.bank.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    }

    @Test
    void getSum() {
    }

    @Test
    void getAccount() {
    }

    @Test
    void checkBalance() {
    }
}
