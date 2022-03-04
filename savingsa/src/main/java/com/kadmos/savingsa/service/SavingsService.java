package com.kadmos.savingsa.service;

import com.kadmos.savingsa.domain.History;
import com.kadmos.savingsa.domain.Savings;
import com.kadmos.savingsa.dto.BalanceDto;
import com.kadmos.savingsa.dto.DepositDto;
import com.kadmos.savingsa.dto.HistoryDto;
import com.kadmos.savingsa.dto.WithdrawDto;
import com.kadmos.savingsa.exception.SavingsException;
import com.kadmos.savingsa.repo.HistoryRepository;
import com.kadmos.savingsa.repo.SavingsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class SavingsService {

  private static final long DEFAULT_ID = 1L;
  private final SavingsRepository savingsRepository;
  private final HistoryRepository historyRepository;
  private final ModelMapper modelMapper;

  public SavingsService(
      SavingsRepository savingsRepository,
      HistoryRepository historyRepository,
      ModelMapper modelMapper) {
    this.savingsRepository = savingsRepository;
    this.historyRepository = historyRepository;
    this.modelMapper = modelMapper;
  }

  public BalanceDto getBalance() {
    checkEmptySavingsEntity();

    return new BalanceDto(savingsRepository.getById(DEFAULT_ID).getAmount());
  }

  public BalanceDto getBalance(Savings savings) {
    checkEmptySavingsEntity();

    return new BalanceDto(savings.getAmount());
  }

  public BalanceDto increaseBalance(DepositDto depositDto) {
    checkEmptySavingsEntity();

    Savings savings = savingsRepository.getById(DEFAULT_ID);
    savings.setAmount(savings.getAmount() + depositDto.getDeposit());
    savingsRepository.save(savings);

    saveHistory(savings, depositDto.getDeposit(), 0.0f);

    return getBalance(savings);
  }

  public BalanceDto decreaseBalance(WithdrawDto withdrawDto) {
    checkEmptySavingsEntity();

    Savings savings = savingsRepository.getById(DEFAULT_ID);

    if (withdrawDto.getWithdraw() > savings.getAmount())
      throw new SavingsException("Your savings are not enough");

    savings.setAmount(savings.getAmount() - withdrawDto.getWithdraw());
    savingsRepository.save(savings);

    saveHistory(savings, 0.0f, withdrawDto.getWithdraw());

    return getBalance(savings);
  }

  public List<HistoryDto> getHistory() {
    List<HistoryDto> response = new ArrayList<>();
    historyRepository
        .findAllByOrderByIdDesc()
        .forEach(history -> response.add(modelMapper.map(history, HistoryDto.class)));

    return response;
  }

  private void checkEmptySavingsEntity() {
    if (!savingsRepository.existsById(DEFAULT_ID)) {
      Savings savings = new Savings();
      savings.setId(DEFAULT_ID);
      savings.setAmount(0.0f);
      savingsRepository.save(savings);
    }
  }

  private void saveHistory(Savings savings, float deposit, float withdraw) {
    History history = new History();
    history.setBalance(savings.getAmount());
    history.setDeposit(deposit);
    history.setWithdraw(withdraw);
    history.setTransactionDate(LocalDateTime.now());
    history.setSavings(savings);

    historyRepository.save(history);
  }
}
