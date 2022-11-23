package com.webapi.api.controllers;

import com.webapi.databasedrivers.DuplicatePrimaryKeyException;
import com.webapi.model.candidate.Candidate;
import com.webapi.services.CandidateService;
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

    @GetMapping("/{candidateID}")
    public Candidate get(@PathVariable Long candidateID) throws DuplicatePrimaryKeyException {
        return candidateService.get(candidateID);
    }
    //todo: Wrap the results in an object named for response i.e. "CandidateResponse" that has time stamp information etc.
    //  export interface CandidateResponse {
    //    timeStamp: Date;
    //    statusCode: number;
    //    status: string;
    //    reason: string;
    //    message: string;
    //    developerMessage: string;
    //    data: {
    //        candidates?: Candidate[],
    //        candidate?: Candidate,
    //    }
    //}
    @GetMapping
    public Collection<Candidate> getAll() {
        return candidateService.getAll();
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
