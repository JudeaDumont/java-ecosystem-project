package com.webapi.webapi.services;

import com.webapi.webapi.databasedrivers.CandidateDao;
import com.webapi.webapi.model.candidate.Candidate;
import com.webapi.webapi.model.candidate.NonExistentCandidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CandidateService {

    private final CandidateDao<Candidate, Long, NonExistentCandidateException> candidateDao;

    @Autowired
    public CandidateService(@Qualifier("postgresCandidate") CandidateDao<Candidate, Long, NonExistentCandidateException> candidateDao) {
        this.candidateDao = candidateDao;
    }

    public int save(Candidate candidate) {
        return candidateDao.save(candidate);
    }

    public Long saveReturnID(Candidate candidate) {
        return candidateDao.saveReturnID(candidate);
    }

    public Candidate get(Long id) throws NonExistentCandidateException {
        return candidateDao.get(id);
    }

    public List<Candidate> getByName(String name) throws NonExistentCandidateException {
        return candidateDao.getByName(name);
    }

    public int update(Candidate candidate) throws NonExistentCandidateException {
        return candidateDao.update(candidate);
    }

    public int delete(Candidate candidate) throws NonExistentCandidateException {
        return candidateDao.delete(candidate);
    }

    public Collection<Candidate> getAll() {
        return candidateDao.getAll();
    }
}
