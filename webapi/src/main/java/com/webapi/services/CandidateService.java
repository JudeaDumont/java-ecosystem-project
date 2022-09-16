package com.webapi.services;

import com.webapi.databasedrivers.CandidateDao;
import com.webapi.databasedrivers.DuplicatePrimaryKeyException;
import com.webapi.model.candidate.Candidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CandidateService {

    private final CandidateDao<Candidate, Long> candidateDao;

    @Autowired
    public CandidateService(@Qualifier("postgresCandidate") CandidateDao<Candidate, Long> candidateDao) {
        this.candidateDao = candidateDao;
    }

    public int save(Candidate candidate) {
        return candidateDao.save(candidate);
    }

    public Long saveReturnID(Candidate candidate) {
        return candidateDao.saveReturnID(candidate);
    }

    public Candidate get(Long id) throws DuplicatePrimaryKeyException {
        return candidateDao.get(id);
    }

    public List<Candidate> getByName(String name) {
        return candidateDao.getByName(name);
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
