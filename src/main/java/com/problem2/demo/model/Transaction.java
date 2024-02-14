package com.problem2.demo.model;

import java.util.Date;

public record Transaction(String id, int customerId, int amount, int points, Date date) { }