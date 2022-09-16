package com.webapi.webapi.api.controllers;

import com.webapi.webapi.databasedrivers.DuplicatePrimaryKeyException;
import com.webapi.webapi.model.candidate.Candidate;
import com.webapi.webapi.model.candidate.NonExistentCandidateException;
import com.webapi.webapi.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RequestMapping("api/v1/candidate")
@RestController
public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public int save(@RequestBody Candidate candidate) {
        return candidateService.save(candidate);
    }

    @GetMapping("/{id}")
    public Candidate get(@PathVariable Long id) throws NonExistentCandidateException, DuplicatePrimaryKeyException {
        return candidateService.get(id);
    }

    @GetMapping
    public Collection<Candidate> getAll() {
        return candidateService.getAll();
    }

    @GetMapping("/getByName/{name}")
    public List<Candidate> getByName(@PathVariable String name) throws NonExistentCandidateException {
        return candidateService.getByName(name);
    }

    @PutMapping
    public int put(@RequestBody Candidate candidate) throws NonExistentCandidateException {
        return candidateService.update(candidate);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) throws NonExistentCandidateException, DuplicatePrimaryKeyException {
        Candidate candidateToDelete = candidateService.get(id);
        if (candidateToDelete == null) {
            return 0;
        }
        return candidateService.delete(candidateToDelete);
    }

    @PostMapping("/saveReturnID")
    public Long saveReturnID(@RequestBody Candidate candidate) {
        return candidateService.saveReturnID(candidate);
    }
}
