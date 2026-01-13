package com.hidekioka.code_assessment;

import com.hidekioka.code_assessment.dto.TransactionDTO;
import com.hidekioka.code_assessment.exception.BusinessRuleException;
import com.hidekioka.code_assessment.service.TransactionService;
import com.hidekioka.code_assessment.utils.DeviceState;
import com.hidekioka.code_assessment.utils.Utils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Due to the limited scope of the application, I choose to create integrated tests by using a h2 in memory database for
 * the tests instead of a more unit testing approach that needs mocking of the db calls.
 * The service layer that contains the business rules of the application will be entirely covered by these tests
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CodeAssessmentApplicationTests {

    @Autowired
    private TransactionService transactionService;

    @Test
    @Order(10)
    void create() {
        // Creates many dtos that will be used later for other tests, validates only the first creation
        TransactionDTO dto1 = sampleDTO1();
        TransactionDTO createdDTO = transactionService.create(dto1);
        transactionService.create(sampleDTO2());
        transactionService.create(sampleDTO3());
        transactionService.create(sampleDTO4());
        Assertions.assertEquals(1, createdDTO.getId());
        Assertions.assertEquals(dto1.getTransactionDate(), createdDTO.getTransactionDate());
        Assertions.assertEquals(dto1.getDescription(), createdDTO.getDescription());
        Assertions.assertEquals(Utils.currencyRound(dto1.getPurchaseAmount()), createdDTO.getPurchaseAmount());
        Assertions.assertNull(createdDTO.getExchangeRate());
        Assertions.assertNull(createdDTO.getConvertedPurchaseAmount());
    }

    @Test
    @Order(20)
    void find() throws BusinessRuleException {
        TransactionDTO dto1 = sampleDTO1();
        TransactionDTO searchedDTO = transactionService.find(1, Optional.empty());
        Assertions.assertEquals(dto1.getTransactionDate(), searchedDTO.getTransactionDate());
        Assertions.assertEquals(dto1.getDescription(), searchedDTO.getDescription());
        Assertions.assertEquals(Utils.currencyRound(dto1.getPurchaseAmount()), searchedDTO.getPurchaseAmount());
        Assertions.assertNull(searchedDTO.getExchangeRate());
        Assertions.assertNull(searchedDTO.getConvertedPurchaseAmount());
    }

    @Test
    @Order(21)
    void findWithCurrency() throws BusinessRuleException {
        TransactionDTO dto1 = sampleDTO1();
        TransactionDTO searchedDTO = transactionService.find(1, Optional.of("Brazil-Real"));
        Assertions.assertEquals(dto1.getTransactionDate(), searchedDTO.getTransactionDate());
        Assertions.assertEquals(dto1.getDescription(), searchedDTO.getDescription());
        Assertions.assertEquals(Utils.currencyRound(dto1.getPurchaseAmount()), searchedDTO.getPurchaseAmount());
        BigDecimal exchangeRate = new BigDecimal("5.477");
        Assertions.assertEquals(exchangeRate, searchedDTO.getExchangeRate()); // Value comes from the external api
        Assertions.assertEquals(Utils.currencyRound(searchedDTO.getPurchaseAmount().multiply(exchangeRate)), searchedDTO.getConvertedPurchaseAmount());
    }

    @Test
    @Order(30)
    void findAll() throws BusinessRuleException {
        TransactionDTO dto1 = sampleDTO1();
        List<TransactionDTO> searchedDTOList = transactionService.findAll(Optional.empty());
        Assertions.assertEquals(4, searchedDTOList.size());
        searchedDTOList.forEach(dto -> {
                    Assertions.assertNull(dto.getConvertedPurchaseAmount());
                    Assertions.assertNull(dto.getExchangeRate());
                }
        );
    }

    @Test
    @Order(31)
    void findAllWithCurrency() throws BusinessRuleException {
        TransactionDTO dto1 = sampleDTO1();
        List<TransactionDTO> searchedDTOList = transactionService.findAll(Optional.of("Brazil-Real"));
        Assertions.assertEquals(4, searchedDTOList.size());
        searchedDTOList.forEach(dto -> {
                    Assertions.assertNotNull(dto.getConvertedPurchaseAmount());
                    Assertions.assertNotNull(dto.getExchangeRate());
                }
        );
    }

    @Test
    @Order(40)
    void findInvalidMissingId() throws BusinessRuleException {
        TransactionDTO dto1 = sampleDTO1();
        TransactionDTO searchedDTO = transactionService.find(100, Optional.empty());
        Assertions.assertNull(searchedDTO);
    }

    @Test
    @Order(41)
    void findInvalidNoExchange() throws BusinessRuleException {
        TransactionDTO dto = sampleDTODateWithoutExchange();
        TransactionDTO createdDTO = transactionService.create(dto);
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            transactionService.find(5, Optional.of("Brazil-Real"));
        });
        Assertions.assertEquals(TransactionService.EXCHANGE_RATE_NOT_FOUND, exception.getMessage());
    }

    @Test
    @Order(42)
    void findAllInvalidNoExchange() throws BusinessRuleException {
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            transactionService.findAll(Optional.of("Brazil-Real"));
        });
        Assertions.assertEquals(TransactionService.EXCHANGE_RATE_NOT_FOUND, exception.getMessage());
    }

    TransactionDTO sampleDTO1() {
        TransactionDTO dto = new TransactionDTO();
        dto.setDescription("Description 01");
        dto.setTransactionDate(LocalDate.of(2026, 1, 1));
        dto.setPurchaseAmount(new BigDecimal("10.00"));
        return dto;
    }

    TransactionDTO sampleDTO2() {
        TransactionDTO dto = new TransactionDTO();
        dto.setDescription("Description 02");
        dto.setTransactionDate(LocalDate.of(2026, 1, 1));
        dto.setPurchaseAmount(new BigDecimal("20.00"));
        return dto;
    }

    TransactionDTO sampleDTO3() {
        TransactionDTO dto = new TransactionDTO();
        dto.setDescription("Description 03");
        dto.setTransactionDate(LocalDate.of(2026, 1, 1));
        dto.setPurchaseAmount(new BigDecimal("1.9999999999"));
        return dto;
    }

    TransactionDTO sampleDTO4() {
        TransactionDTO dto = new TransactionDTO();
        dto.setDescription("Description 04");
        dto.setTransactionDate(LocalDate.of(2026, 1, 1));
        dto.setPurchaseAmount(new BigDecimal("1.1111111111"));
        return dto;
    }

    TransactionDTO sampleDTODateWithoutExchange() {
        TransactionDTO dto = new TransactionDTO();
        dto.setDescription("Description 05");
        dto.setTransactionDate(LocalDate.of(1970, 1, 1));
        dto.setPurchaseAmount(new BigDecimal("10.00"));
        return dto;
    }

}
