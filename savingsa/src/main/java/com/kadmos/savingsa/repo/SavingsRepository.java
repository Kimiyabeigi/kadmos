package com.kadmos.savingsa.repo;

import com.kadmos.savingsa.domain.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, Long> {}
