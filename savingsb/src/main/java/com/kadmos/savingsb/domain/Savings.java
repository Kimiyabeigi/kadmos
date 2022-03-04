package com.kadmos.savingsb.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "t_savings_b")
@Data
public class Savings {
  @Id private Long id;

  private float amount;

  @Version private long version;
}
