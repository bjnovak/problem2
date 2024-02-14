package com.problem2.demo.service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.problem2.demo.model.Transaction;

public class TransactionsData {

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
                    Date.from(LocalDateTime.ofInstant(
                        Instant.parse(data.path("date").asText()), ZoneId.of("UTC"))
                        .atZone(ZoneId.systemDefault()).toInstant()
                    )
                ));
            }

            return tx;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Map<String, Integer> totals() {
        try {
            Resource resource = new ClassPathResource("data.json");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(resource.getInputStream());
            Map<String, Integer> total = new HashMap<>();
            JsonNode totalsByMonth = jsonNode.path("totals");

            for (Iterator<String> it = totalsByMonth.fieldNames(); it.hasNext(); ) {
                String customerId = it.next();
                JsonNode monthData = totalsByMonth.path(customerId);
                total.put(customerId, Integer.parseInt(monthData.toString()));
            }

            return total;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Map<String, Map<String, Integer>> totalsByMonth() {
        try {
            Resource resource = new ClassPathResource("data.json");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(resource.getInputStream());
            Map<String, Map<String, Integer>> totals = new HashMap<>();
            JsonNode totalsByMonth = jsonNode.path("totalsByMonth");

            for (Iterator<String> it = totalsByMonth.fieldNames(); it.hasNext(); ) {
                String customerId = it.next();
                Map<String, Integer> monthTotals = new HashMap<>();
                JsonNode monthData = totalsByMonth.path(customerId);

                for (Iterator<String> monthIt = monthData.fieldNames(); monthIt.hasNext(); ) {
                    String month = monthIt.next();
                    monthTotals.put(month, Integer.parseInt(monthData.path(month).toString()));
                }

                totals.put(customerId, monthTotals);
            }

            return totals;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}