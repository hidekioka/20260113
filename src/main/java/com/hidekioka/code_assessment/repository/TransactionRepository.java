package com.hidekioka.code_assessment.repository;

import com.hidekioka.code_assessment.entity.TransactionEntity;
import com.hidekioka.code_assessment.utils.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
}
