package com.hidekioka.code_assessment.service;

import com.hidekioka.code_assessment.dto.TransactionDTO;
import com.hidekioka.code_assessment.entity.TransactionEntity;
import com.hidekioka.code_assessment.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@Transactional
public class TransactionService {
    public static final ModelMapper MODEL_MAPPER = new ModelMapper();
    public final Logger LOGGER = Logger.getLogger(getClass().getName());

    @Autowired
    TransactionRepository deviceRepository;

    public TransactionDTO create(TransactionDTO transactionDTO) {
        BigDecimal roundedValue = transactionDTO.getPurchaseAmount().setScale(2, RoundingMode.HALF_UP);
        transactionDTO.setPurchaseAmount(roundedValue);
        TransactionEntity entity = MODEL_MAPPER.map(transactionDTO, TransactionEntity.class);
        deviceRepository.save(entity);
        return MODEL_MAPPER.map(entity, TransactionDTO.class);
    }

    public TransactionDTO find(Integer id, Optional<String> currency) {
        Optional<TransactionEntity> entity = deviceRepository.findById(id);
        return MODEL_MAPPER.map(entity, TransactionDTO.class);
    }

    public List<TransactionDTO> findAll(Optional<String> currency) {
        List<TransactionEntity> entities = deviceRepository.findAll();
        return entities.stream()
                .map(entity -> MODEL_MAPPER.map(entity, TransactionDTO.class))
                .toList();
    }
}
