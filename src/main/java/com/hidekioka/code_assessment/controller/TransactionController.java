package com.hidekioka.code_assessment.controller;

import com.hidekioka.code_assessment.dto.TransactionDTO;
import com.hidekioka.code_assessment.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<TransactionDTO> find(@PathVariable Integer id, @RequestParam("currency") Optional<String> currency) {
        TransactionDTO dto = transactionService.find(id, currency);
        if (dto == null) {
            return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // Retrieve all Purchase Transactions in a Specified Country’s Currency
    @GetMapping("")
    public ResponseEntity<List<TransactionDTO>> findAll(@RequestParam("currency") Optional<String> currency) {
        List<TransactionDTO> dtoList = transactionService.findAll(currency);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}
