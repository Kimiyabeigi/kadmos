package com.kadmos.savingsb.repo;

import com.kadmos.savingsb.domain.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, Long> {}
