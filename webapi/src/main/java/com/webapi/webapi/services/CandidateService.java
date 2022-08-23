package com.webapi.webapi.services;

import data.candidate.Candidate;
import data.postgresql.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class CandidateService {

    private final Dao<Candidate, Long> candidateDao;

    @Autowired
    public CandidateService(@Qualifier("postgres") Dao<Candidate, Long> candidateDao) {
        this.candidateDao = candidateDao;
    }

    @PostMapping
    public Long save(Candidate candidate){
        return candidateDao.save(candidate);
    }

    @GetMapping
    public Candidate get(Long id){
        return candidateDao.get(id);
    }
}
