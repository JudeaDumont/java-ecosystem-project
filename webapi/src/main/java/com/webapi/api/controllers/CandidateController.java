package com.webapi.api.controllers;

import com.webapi.databasedrivers.DuplicatePrimaryKeyException;
import com.webapi.model.Response;
import com.webapi.model.candidate.Candidate;
import com.webapi.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("api/v1/candidate")
@RestController
public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    //todo: make another post mapping that can take multiple candidates
    //todo: make a delete mapping that takes multiple candidates
    @PostMapping
    public int save(@RequestBody Candidate candidate) {
        return candidateService.save(candidate);
    }

    @GetMapping("/{candidateID}")
    public Candidate get(@PathVariable Long candidateID) throws DuplicatePrimaryKeyException {
        return candidateService.get(candidateID);
    }
    @GetMapping
    public ResponseEntity<Response>
    getAll() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("candidates", candidateService.getAll()))
                        .message("Candidates retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/getByName/{candidateName}")
    public List<Candidate> getByName(@PathVariable String candidateName) {
        return candidateService.getByName(candidateName);
    }

    @PutMapping
    public int put(@RequestBody Candidate candidate) {
        return candidateService.update(candidate);
    }

    @DeleteMapping("/{candidateID}")
    public int delete(@PathVariable Long candidateID) throws DuplicatePrimaryKeyException {
        Candidate candidateToDelete = candidateService.get(candidateID);
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
