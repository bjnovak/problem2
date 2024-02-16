package com.problem2.demo.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.problem2.demo.model.Transaction;
import com.problem2.demo.service.TransactionsData;

@RestController
public class TransactionsController {

    @GetMapping("/transactions")
    public ArrayList<Transaction> transactions() {
        return TransactionsData.transactions();
    }

    @GetMapping("/totals")
    public Map<Integer, Integer> totals() {
        return TransactionsData.totals();
    }

    @GetMapping("/totals-by-month")
    public Map<Integer, Map<String, Integer>> totalsByMonth() {
        return TransactionsData.totalsByMonth();
    }

}
