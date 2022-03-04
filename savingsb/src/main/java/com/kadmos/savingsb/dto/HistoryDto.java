package com.kadmos.savingsb.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@JsonPropertyOrder({"transactionDate", "deposit", "withdraw", "balance"})
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDto {
  private float deposit;

  private float withdraw;

  private float balance;

  private LocalDateTime transactionDate;
}
