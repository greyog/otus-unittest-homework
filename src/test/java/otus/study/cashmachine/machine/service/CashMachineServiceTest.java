package otus.study.cashmachine.machine.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import otus.study.cashmachine.bank.dao.CardsDao;
import otus.study.cashmachine.bank.data.Card;
import otus.study.cashmachine.bank.service.AccountService;
import otus.study.cashmachine.bank.service.impl.CardServiceImpl;
import otus.study.cashmachine.machine.data.CashMachine;
import otus.study.cashmachine.machine.data.MoneyBox;
import otus.study.cashmachine.machine.service.impl.CashMachineServiceImpl;
import otus.study.cashmachine.machine.service.util.CardServiceUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CashMachineServiceTest {

    @Spy
    @InjectMocks
    private CardServiceImpl cardService;

    @Mock
    private CardsDao cardsDao;

    @Mock
    private AccountService accountService;

    @Mock
    private MoneyBoxService moneyBoxService;

    private CashMachineServiceImpl cashMachineService;

    private CashMachine cashMachine = new CashMachine(new MoneyBox());

    @BeforeEach
    void init() {
        cashMachineService = new CashMachineServiceImpl(cardService, accountService, moneyBoxService);
    }


    @Test
    void testGetMoneyWithSpy() {
        BigDecimal amount = BigDecimal.valueOf(100);
        String cardNum = "1234";
        String pin = "0000";

        doReturn(amount).when(cardService).getMoney(cardNum, pin, amount);
        when(moneyBoxService.getMoney(any(), anyInt())).thenReturn(List.of(1, 0 , 0, 0));

        List<Integer> actual = cashMachineService.getMoney(cashMachine, "1234", "0000", amount);

        verify(cardService).getMoney(eq(cardNum), eq(pin), eq(amount));

        List<Integer> expected = List.of(1, 0, 0, 0);
        assertEquals(expected, actual);
    }

    @Test
    void putMoney() {
    }

    @Test
    void checkBalance() {

    }

    @Test
    void changePin() {
// @TODO create change pin test using spy as implementation and ArgumentCaptor and thenReturn
        String cardNum = "1234";
        String pin = "0000";
        String newPin = "0001";
        String oldPinHash = CardServiceUtil.getHash(pin);

        ArgumentCaptor<Card> cardCaptor= ArgumentCaptor.forClass(Card.class);
        when(cardsDao.getCardByNumber(cardNum))
                .thenReturn(new Card(0L, cardNum, 0L, oldPinHash));

        cashMachineService.changePin(cardNum, pin , newPin);

        verify(cardService).cnangePin(anyString(), anyString(), anyString());
        verify(cardsDao).saveCard(cardCaptor.capture());

        assertEquals(cardNum, cardCaptor.getValue().getNumber());
        assertEquals(CardServiceUtil.getHash(newPin), cardCaptor.getValue().getPinCode());

    }

    @Test
    void changePinWithAnswer() {
// @TODO create change pin test using spy as implementation and mock an thenAnswer

    }
}