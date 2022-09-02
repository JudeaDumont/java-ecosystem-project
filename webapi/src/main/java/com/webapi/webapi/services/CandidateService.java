package com.webapi.webapi.services;

import com.webapi.webapi.databasedrivers.Dao;
import com.webapi.webapi.model.candidate.Candidate;
import com.webapi.webapi.model.candidate.NonExistentCandidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CandidateService {

    private final Dao<Candidate, Long, NonExistentCandidateException> candidateDao;

    @Autowired
    public CandidateService(@Qualifier("postgresCandidate") Dao<Candidate, Long, NonExistentCandidateException> candidateDao) {
        this.candidateDao = candidateDao;
    }

    public int save(Candidate candidate) {
        return candidateDao.save(candidate);
    }

    public Candidate get(Long id) throws NonExistentCandidateException {
        return candidateDao.get(id);
    }

    public int update(Candidate candidate) {
        return candidateDao.update(candidate);
    }

    public int delete(Candidate candidate) {
        return candidateDao.delete(candidate);
    }

    public Collection<Candidate> getAll() {
        return candidateDao.getAll();
    }
}
