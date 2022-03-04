package com.kadmos.savingsa.rest;

import com.kadmos.savingsa.dto.BalanceDto;
import com.kadmos.savingsa.dto.DepositDto;
import com.kadmos.savingsa.dto.HistoryDto;
import com.kadmos.savingsa.dto.WithdrawDto;
import com.kadmos.savingsa.exception.Error;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SavingsControllerTest {

  @LocalServerPort private int port;
  private final String requestMapping = "/savings/a";

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void get_balance() throws MalformedURLException {
    ResponseEntity<BalanceDto> response =
        restTemplate.getForEntity(
            new URL("http://localhost:" + port + requestMapping + "/balance").toString(),
            BalanceDto.class);
    assertTrue(Objects.requireNonNull(response.getBody()).getAmount() >= 0.0f);
  }

  @Test
  void get_history() throws MalformedURLException {
    ResponseEntity<HistoryDto[]> response =
        restTemplate.getForEntity(
            new URL("http://localhost:" + port + requestMapping + "/history").toString(),
            HistoryDto[].class);
    assertNotNull(response.getBody());
  }

  @Test
  void increase_balance() throws MalformedURLException {
    ResponseEntity<BalanceDto> balanceDto =
        restTemplate.getForEntity(
            new URL("http://localhost:" + port + requestMapping + "/balance").toString(),
            BalanceDto.class);

    ResponseEntity<BalanceDto> response =
        restTemplate.postForEntity(
            new URL("http://localhost:" + port + requestMapping + "/increase").toString(),
            new DepositDto(5),
            BalanceDto.class);
    assertEquals(
        Objects.requireNonNull(response.getBody()).getAmount(),
        Objects.requireNonNull(balanceDto.getBody()).getAmount() + 5);
  }

  @Test
  void decrease_balance() throws MalformedURLException {
    ResponseEntity<BalanceDto> balanceDto =
        restTemplate.getForEntity(
            new URL("http://localhost:" + port + requestMapping + "/balance").toString(),
            BalanceDto.class);

    float amount = Objects.requireNonNull(balanceDto.getBody()).getAmount();
    if (amount == 0.0f) amount = amount + 10;

    ResponseEntity<BalanceDto> response =
        restTemplate.postForEntity(
            new URL("http://localhost:" + port + requestMapping + "/decrease").toString(),
            new WithdrawDto(0.5f),
            BalanceDto.class);
    assertEquals(Objects.requireNonNull(response.getBody()).getAmount(), amount - 0.5f);
  }

  @Test
  void decrease_balance_with_exception() throws MalformedURLException {
    ResponseEntity<BalanceDto> balanceDto =
        restTemplate.getForEntity(
            new URL("http://localhost:" + port + requestMapping + "/balance").toString(),
            BalanceDto.class);

    float amount = Objects.requireNonNull(balanceDto.getBody()).getAmount();

    ResponseEntity<Error> response =
        restTemplate.postForEntity(
            new URL("http://localhost:" + port + requestMapping + "/decrease").toString(),
            new WithdrawDto(amount + 5),
            Error.class);
    assertEquals(
        "Your savings are not enough", Objects.requireNonNull(response.getBody()).getDescription());
  }
}
