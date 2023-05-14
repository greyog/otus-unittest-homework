package otus.study.cashmachine.bank.service.impl;

import otus.study.cashmachine.bank.dao.CardsDao;
import otus.study.cashmachine.bank.data.Card;
import otus.study.cashmachine.bank.service.AccountService;
import otus.study.cashmachine.bank.service.CardService;

import java.math.BigDecimal;

import static otus.study.cashmachine.machine.service.util.CardServiceUtil.checkPin;
import static otus.study.cashmachine.machine.service.util.CardServiceUtil.getHash;


public class CardServiceImpl implements CardService {
    AccountService accountService;

    CardsDao cardsDao;

    public CardServiceImpl(final AccountService accountService, final CardsDao cardsDao) {
        this.accountService = accountService;
        this.cardsDao = cardsDao;
    }

    @Override
    public Card createCard(String number, Long accountId, String pinCode) {
        return cardsDao.createCard(number, accountId, pinCode);
    }

    @Override
    public boolean cnangePin(String number, String oldPin, String newPin) {
        Card card = cardsDao.getCardByNumber(number);

        if (card == null) {
            throw new IllegalArgumentException("No card found");
        }

        try {
            checkPin(card, oldPin);
            card.setPinHash(getHash(newPin));
            cardsDao.saveCard(card);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public BigDecimal getMoney(String number, String pin, BigDecimal sum) {
        Card card = cardsDao.getCardByNumber(number);

        if (card == null) {
            throw new IllegalArgumentException("No card found");
        }

        checkPin(card, pin);
        return accountService.getMoney(card.getAccountId(), sum);
    }

    @Override
    public BigDecimal putMoney(String number, String pin, BigDecimal sum) {
        Card card = cardsDao.getCardByNumber(number);

        if (card == null) {
            throw new IllegalArgumentException("No card found");
        }
        checkPin(card, pin);
        return accountService.putMoney(card.getAccountId(), sum);
    }

    @Override
    public BigDecimal getBalance(String number, String pin) {
        Card card = cardsDao.getCardByNumber(number);

        if (card == null) {
            throw new IllegalArgumentException("No card found");
        }
        checkPin(card, pin);
        return accountService.checkBalance(card.getId());
    }
}