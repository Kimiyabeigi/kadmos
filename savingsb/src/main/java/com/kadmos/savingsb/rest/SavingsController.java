package com.kadmos.savingsb.rest;

import com.kadmos.savingsb.dto.BalanceDto;
import com.kadmos.savingsb.dto.DepositDto;
import com.kadmos.savingsb.dto.HistoryDto;
import com.kadmos.savingsb.dto.WithdrawDto;
import com.kadmos.savingsb.service.SavingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("savings/b")
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
