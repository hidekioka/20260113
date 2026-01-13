package com.hidekioka.code_assessment.service;

import com.hidekioka.code_assessment.dto.ExchangeRateDTO;
import com.hidekioka.code_assessment.dto.ExchangeRateResponseDTO;
import com.hidekioka.code_assessment.dto.TransactionDTO;
import com.hidekioka.code_assessment.entity.TransactionEntity;
import com.hidekioka.code_assessment.exception.BusinessRuleException;
import com.hidekioka.code_assessment.repository.TransactionRepository;
import com.hidekioka.code_assessment.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@Transactional
public class TransactionService {
    public static final ModelMapper MODEL_MAPPER = new ModelMapper();
    public static final String EXCHANGE_RATE_NOT_FOUND = "Exchange rate not found.";
    public final Logger LOGGER = Logger.getLogger(getClass().getName());

    @Autowired
    TransactionRepository deviceRepository;

    @Autowired
    RestClient externalCurrencyExchangeAPI;

    public TransactionDTO create(TransactionDTO transactionDTO) {
        transactionDTO.setPurchaseAmount(Utils.currencyRound(transactionDTO.getPurchaseAmount()));
        TransactionEntity entity = MODEL_MAPPER.map(transactionDTO, TransactionEntity.class);
        deviceRepository.save(entity);
        return MODEL_MAPPER.map(entity, TransactionDTO.class);
    }

    public TransactionDTO find(Integer id, Optional<String> currency) throws BusinessRuleException {
        Optional<TransactionEntity> entity = deviceRepository.findById(id);
        TransactionDTO result = MODEL_MAPPER.map(entity, TransactionDTO.class);
        if (currency.isPresent() && entity.isPresent()) {
            BigDecimal exchangeRate = calculateCurrencyExchange(currency.get(), entity.get().getTransactionDate());
            if (exchangeRate == null) {
                throw new BusinessRuleException(EXCHANGE_RATE_NOT_FOUND);
            }
            result.setExchangeRate(exchangeRate);
            result.setConvertedPurchaseAmount(Utils.currencyRound(entity.get().getPurchaseAmount().multiply(exchangeRate)));
        }
        return result;
    }

    public List<TransactionDTO> findAll(Optional<String> currency) throws BusinessRuleException {
        List<TransactionEntity> entities = deviceRepository.findAll();
        List<TransactionDTO> result = entities.stream()
                .map(entity -> MODEL_MAPPER.map(entity, TransactionDTO.class))
                .toList();
        for (TransactionDTO transactionDTO : result) {
            if (currency.isPresent()) {
                BigDecimal exchangeRate = calculateCurrencyExchange(currency.get(), transactionDTO.getTransactionDate());
                if (exchangeRate == null) {
                    throw new BusinessRuleException(EXCHANGE_RATE_NOT_FOUND);
                }
                transactionDTO.setExchangeRate(exchangeRate);
                transactionDTO.setConvertedPurchaseAmount(Utils.currencyRound(transactionDTO.getPurchaseAmount().multiply(exchangeRate)));
            }
        }
        return result;
    }

    /**
     * Calls external API to get the exchange rate of a selected currency
     * Note: This is always called when executing find or findAll methods for simplicity.
     * Another solution could be loading all the values in memory or in a database once when the application starts or whenever a call is made,
     * this would avoid external calls in each find.
     *
     * @return Value of the exchange rate or null if none was found
     */
    private BigDecimal calculateCurrencyExchange(String currency, LocalDate transactionDate) {
        ExchangeRateResponseDTO exchangeRateResponseDTO = getExchangeRate(currency, transactionDate);
        // selectedRate is the rate that is closest to the transactionDate
        ExchangeRateDTO selectedRate = exchangeRateResponseDTO.getData().stream().max(Comparator.comparing(ExchangeRateDTO::getEffectiveDate)).orElse(null);
        return selectedRate != null ? selectedRate.getExchangeRate() : null;
    }

    /**
     * Get exchange rates from the external API that matches the 6 previous months from the transaction date
     */
    private ExchangeRateResponseDTO getExchangeRate(String currency, LocalDate transactionDate) {
        LocalDate sixMonthsBefore = transactionDate.minusMonths(6);
        String searchString = "country_currency_desc:in:(" + currency + ")" +
                ",record_date:gte:" + sixMonthsBefore +
                ",record_date:lte:" + transactionDate;
        return externalCurrencyExchangeAPI
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("accounting/od/rates_of_exchange")
                        .queryParam("filter", searchString)
                        .build())
                .retrieve()
                .body(ExchangeRateResponseDTO.class);
    }
}
