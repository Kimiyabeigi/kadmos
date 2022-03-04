package com.kadmos.savingsb.repo;

import com.kadmos.savingsb.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
  List<History> findAllByOrderByIdDesc();
}
