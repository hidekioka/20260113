package com.hidekioka.code_assessment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ExchangeRateResponseDTO implements Serializable {

    List<ExchangeRateDTO> data;

    public List<ExchangeRateDTO> getData() {
        return data;
    }

    public void setData(List<ExchangeRateDTO> data) {
        this.data = data;
    }
}

