package com.kadmos.savingsa.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_history_a")
@Data
public class History {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private float deposit;

  private float withdraw;

  private float balance;

  private LocalDateTime transactionDate;

  @ManyToOne
  @JoinColumn(name = "savings_a_id", nullable = false)
  private Savings savings;
}
