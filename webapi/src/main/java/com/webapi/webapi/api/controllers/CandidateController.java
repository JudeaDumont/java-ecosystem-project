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

    //todo: non of these mappings are going to work besides save, they need to be annotated such that they can accept
    // json as arguments
    @GetMapping
    public Candidate get(Long id) throws NonExistentCandidateException {
        return candidateService.get(id);
    }

    @GetMapping
    @RequestMapping("api/v1/candidate/getAll")
    public Collection<Candidate> getAll() {
        return candidateService.getAll();
    }
}
