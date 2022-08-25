package com.webapi.webapi.services;

import com.webapi.webapi.databasedrivers.Dao;
import com.webapi.webapi.model.candidate.Candidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CandidateService {

    private final Dao<Candidate, Long> candidateDao;

    @Autowired
    public CandidateService(@Qualifier("postgres") Dao<Candidate, Long> candidateDao) {
        this.candidateDao = candidateDao;
    }

    public Long save(Candidate candidate) {
        return candidateDao.save(candidate);
    }

    public Candidate get(Long id) {
        return candidateDao.get(id);
    }

    public Collection<Candidate> getAll() {
        return candidateDao.getAll();
    }
}
