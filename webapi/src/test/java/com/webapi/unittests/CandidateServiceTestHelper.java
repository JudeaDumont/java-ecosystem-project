package com.webapi.unittests;

import com.webapi.databasedrivers.DuplicatePrimaryKeyException;
import com.webapi.model.candidate.Candidate;
import com.webapi.services.CandidateService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CandidateServiceTestHelper {
    CandidateService candidateService;

    CandidateServiceTestHelper(CandidateService candidateService){
        this.candidateService = candidateService;
    }

    public Candidate createCandidateWithUUIDName() {
        String uuid = UUID.randomUUID().toString();
        return new Candidate(uuid);
    }

    public Candidate createCandidateWithNameFromArg(String name) {
        return new Candidate(name);
    }

    public Candidate saveCandidateWithName(Candidate candidateWithUUIDName) {
        int rowsInserted = candidateService.save(candidateWithUUIDName);
        assert (rowsInserted == 1);
        return candidateWithUUIDName;
    }

    public Candidate createCandidateWithNameSaveCandidateReturnCandidate(String name) {
        Candidate candidateWithUUIDName = createCandidateWithNameFromArg(name);
        return saveCandidateWithName(candidateWithUUIDName);
    }

    public Candidate createCandidateSaveCandidateReturnCandidate() {
        Candidate candidateWithUUIDName = createCandidateWithUUIDName();
        return saveCandidateWithName(candidateWithUUIDName);
    }

    public List<Candidate> getCandidatesByName(String candidateName) {
        return getCandidatesByName(candidateName, 1);
    }

    public List<Candidate> getCandidatesByName(String candidateName, int numOfExpectedCandidates) {
        List<Candidate> candidatesMatchingName = candidateService.getByName(candidateName);
        assert (candidatesMatchingName.size() == numOfExpectedCandidates);
        assert (Objects.equals(candidatesMatchingName.get(0).getName(), candidateName));
        return candidatesMatchingName;
    }

    public Long createCandidateSaveCandidateReturnID() {
        Long id = candidateService.saveReturnID(new Candidate(UUID.randomUUID().toString()));
        assert (id != null);
        assert (id != 0);
        return id;
    }

    public void deleteCandidates(List<Candidate> candidatesByName) {
        for (Candidate candidate: candidatesByName) {
            assert (candidateService.delete(candidate) == 1);
        }
    }

    public Candidate createCandidateSaveCandidateGetCandidateByIDReturnCandidate() throws DuplicatePrimaryKeyException {
        Long id = createCandidateSaveCandidateReturnID();
        Candidate candidate = candidateService.get(id);
        assert (candidate != null);
        return candidate;
    }

    public String updateCandidate(Candidate candidate) {
        String changeUuid1 = UUID.randomUUID().toString();
        candidate.setName(changeUuid1);
        int update = candidateService.update(candidate);
        assert (update == 1);
        return changeUuid1;
    }
}
