package com.example.MCM.domain.cash.repository;

import com.example.MCM.domain.cash.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashRepository extends JpaRepository<CashLog, Long> {
}
