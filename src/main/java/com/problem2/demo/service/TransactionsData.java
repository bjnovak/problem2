package com.problem2.demo.service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.problem2.demo.model.Transaction;

public class TransactionsData {

    private static int getPoints(int amount) {
        if (amount <= 50) return 0;
        else if (amount > 50 && amount <= 100) return amount - 50;
        else return ((amount - 100) * 2) + 50;
    }

    public static ArrayList<Transaction> transactions() {
        try {
            Resource resource = new ClassPathResource("data.json");

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(resource.getInputStream());

            JsonNode dataArray = jsonNode.path("transactions");
            ArrayList<Transaction> tx = new ArrayList<>();

            for (JsonNode data : dataArray) {
                tx.add(new Transaction(
                    data.path("id").asText(),
                    data.path("customerId").asInt(),
                    data.path("amount").asInt(),
                    data.path("points").asInt(),
                    data.path("date").asText()
                ));
            }

            return tx;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Map<Integer, Integer> totals() {
        ArrayList<Transaction> transactions = TransactionsData.transactions();

        Map<Integer, Integer> customerTotals = new HashMap<>();

        for (Transaction tx : Objects.requireNonNull(transactions)) {
            final int customerId = tx.customerId();
            if (customerTotals.containsKey(customerId)) {
                customerTotals.put(customerId, customerTotals.get(customerId) + getPoints(tx.amount()));
            } else {
                customerTotals.put(customerId, getPoints(tx.amount()));
            }
        }

        return customerTotals;
    }

    public static Map<Integer, Map<String, Integer>> totalsByMonth() {
        ArrayList<Transaction> transactions = TransactionsData.transactions();

        Map<Integer, Map<String, Integer>> totalsByMonth = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");

        for (Transaction tx : Objects.requireNonNull(transactions)) {
            Instant instant = Instant.parse(tx.date());
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
            String monthId = dateTime.format(formatter);

            final int customerId = tx.customerId();

            if (totalsByMonth.containsKey(customerId) && totalsByMonth.get(customerId).containsKey(monthId)) {
                totalsByMonth.get(customerId).put(monthId,
                        totalsByMonth.get(customerId).get(monthId) + getPoints(tx.amount())
                );
            } else {
                if (!totalsByMonth.containsKey(customerId)) {
                    Map<String, Integer> newMonth = new HashMap<>();
                    newMonth.put(monthId, getPoints(tx.amount()));
                    totalsByMonth.put(customerId, newMonth);
                } else {
                    totalsByMonth.get(customerId).put(monthId, getPoints(tx.amount()));
                }
            }
        }

        return totalsByMonth;
    }

}
