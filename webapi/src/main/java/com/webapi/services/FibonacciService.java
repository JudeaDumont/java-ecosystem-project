package com.webapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FibonacciService {

    @Autowired
    public FibonacciService() {
    }

    public List<Long> get(Long num1, Long num2, Long iterations) {
        return fibonacci(num1, num2, iterations, new ArrayList<>());
    }

    private static List<Long> fibonacci(Long num1, Long num2, Long iterations, List<Long> list) {
        list.add(num1 + num2);
        iterations -= 1;
        if (iterations > 0) {
            return fibonacci(num2, num1 + num2, iterations, list);
        } else {
            return list;
        }
    }
}
