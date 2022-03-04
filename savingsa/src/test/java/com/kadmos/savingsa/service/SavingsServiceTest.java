package com.kadmos.savingsa.service;

import com.kadmos.savingsa.dto.BalanceDto;
import com.kadmos.savingsa.dto.DepositDto;
import com.kadmos.savingsa.dto.WithdrawDto;
import com.kadmos.savingsa.exception.SavingsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
class SavingsServiceTest {

  @Autowired private SavingsService savingsService;

  @Test
  void get_balance() {
    BalanceDto balance = savingsService.getBalance();
    Assertions.assertTrue(balance.getAmount() >= 0.0f);
  }

  @Test
  void increase_balance() {
    BalanceDto balance = savingsService.getBalance();
    savingsService.increaseBalance(new DepositDto(5));

    BalanceDto newBalance = savingsService.getBalance();
    Assertions.assertAll(
        () -> assertEquals(balance.getAmount() + 5, newBalance.getAmount()),
        () -> assertTrue(balance.getAmount() < newBalance.getAmount()));
  }

  @Test
  void decrease_balance_with_exception() {
    BalanceDto balance = savingsService.getBalance();

    Assertions.assertThrowsExactly(
        SavingsException.class,
        () -> savingsService.decreaseBalance(new WithdrawDto(balance.getAmount() + 10)));
  }

  @Test
  void decrease_balance() {
    BalanceDto balance = savingsService.getBalance();
    if (balance.getAmount() == 0.0f) {
      savingsService.increaseBalance(new DepositDto(1));
      balance = savingsService.getBalance();
    }

    savingsService.decreaseBalance(new WithdrawDto(0.5f));
    BalanceDto newBalance = savingsService.getBalance();

    BalanceDto finalBalance = balance;
    Assertions.assertAll(
        () -> assertEquals(finalBalance.getAmount() - 0.5f, newBalance.getAmount()),
        () -> assertTrue(finalBalance.getAmount() > newBalance.getAmount()));
  }
}
