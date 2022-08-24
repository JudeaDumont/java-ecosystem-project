package com.webapi.webapi.api;

import com.webapi.webapi.model.Candidate;
import com.webapi.webapi.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @GetMapping
//    public Candidate get(Long id){
//        return candidateService.get(id);
//    }
//
//    @GetMapping
//    public Collection<Candidate> getAll(){
//        return candidateService.getAll();
//    }
}
