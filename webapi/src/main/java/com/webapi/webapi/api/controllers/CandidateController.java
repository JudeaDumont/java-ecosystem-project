package com.webapi.webapi.api.controllers;

import com.webapi.webapi.model.candidate.Candidate;
import com.webapi.webapi.model.candidate.NonExistentCandidateException;
import com.webapi.webapi.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

//todo: test the controller via integration tests and a "TestHttpClient"
@RequestMapping("api/v1/candidate")
@RestController
public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public Long save(@RequestBody Candidate candidate) {
        return candidateService.save(candidate);
    }

    @GetMapping("/{id}")
    public Candidate get(@PathVariable Long id) throws NonExistentCandidateException {
        return candidateService.get(id);
    }

    @GetMapping
    public Collection<Candidate> getAll() {
        return candidateService.getAll();
    }

    @PutMapping
    public boolean put(@RequestBody Candidate candidate) throws NonExistentCandidateException {
        return candidateService.update(candidate);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) throws NonExistentCandidateException {
        Candidate candidateToDelete = candidateService.get(id);
        return candidateService.delete(candidateToDelete);
    }
}
