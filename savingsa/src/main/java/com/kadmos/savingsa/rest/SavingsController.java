package com.kadmos.savingsa.rest;

import com.kadmos.savingsa.dto.BalanceDto;
import com.kadmos.savingsa.dto.DepositDto;
import com.kadmos.savingsa.dto.HistoryDto;
import com.kadmos.savingsa.dto.WithdrawDto;
import com.kadmos.savingsa.service.SavingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("savings/a")
public class SavingsController {

  private final SavingsService savingsService;

  public SavingsController(SavingsService savingsService) {
    this.savingsService = savingsService;
  }

  @GetMapping("balance")
  public ResponseEntity<BalanceDto> getBalance() {
    return ResponseEntity.ok().body(savingsService.getBalance());
  }

  @GetMapping("history")
  public ResponseEntity<List<HistoryDto>> getHistory() {
    return ResponseEntity.ok().body(savingsService.getHistory());
  }

  @PostMapping("increase")
  public ResponseEntity<BalanceDto> increaseBalance(@RequestBody DepositDto request) {
    return ResponseEntity.ok().body(savingsService.increaseBalance(request));
  }

  @PostMapping("decrease")
  public ResponseEntity<BalanceDto> decreaseBalance(@RequestBody WithdrawDto request) {
    return ResponseEntity.ok().body(savingsService.decreaseBalance(request));
  }
}
