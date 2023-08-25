package com.webapi.api.controllers;

import com.webapi.databasedrivers.DuplicatePrimaryKeyException;
import com.webapi.services.FibonacciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api/v1/fibonacci")
@RestController
public class FibonacciController {
    private final FibonacciService fibonacciService;

    @Autowired
    public FibonacciController(FibonacciService fibonacciService) {
        this.fibonacciService = fibonacciService;
    }

    @GetMapping("/{num1}/{num2}/{iterations}")
    public List<Long> get(@PathVariable Long num1, @PathVariable Long num2, @PathVariable Long iterations)
            throws DuplicatePrimaryKeyException {
        return fibonacciService.get(num1, num2, iterations);
    }

}
