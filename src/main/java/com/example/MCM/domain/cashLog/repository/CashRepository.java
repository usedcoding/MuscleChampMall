package com.example.MCM.domain.cashLog.repository;

import com.example.MCM.domain.cashLog.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashRepository extends JpaRepository<CashLog, Long> {
}
