package com.hidekioka.code_assessment.controller;

import com.hidekioka.code_assessment.dto.TransactionDTO;
import com.hidekioka.code_assessment.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    // Requirement #1: Store a Purchase Transaction
    @PostMapping("")
    public ResponseEntity<TransactionDTO> create(@Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdDTO = transactionService.create(transactionDTO);
        return new ResponseEntity<>(createdDTO, HttpStatus.CREATED);
    }

    // Retrieve a Purchase Transaction in a Specified Country’s Currency
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> find(@PathVariable Integer id) {
        // TODO: implement
        return null;
    }
}
