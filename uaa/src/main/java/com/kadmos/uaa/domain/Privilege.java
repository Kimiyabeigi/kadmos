package com.kadmos.uaa.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "t_privilege")
@Data
public class Privilege {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;
}
