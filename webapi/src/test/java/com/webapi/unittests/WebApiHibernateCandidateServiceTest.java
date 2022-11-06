package com.webapi.unittests;

import com.webapi.databasedrivers.DuplicatePrimaryKeyException;
import com.webapi.databasedrivers.hibernateinmemory.services.HibernateCandidateDaoService;
import com.webapi.model.candidate.Candidate;
import com.webapi.services.CandidateService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebApiHibernateCandidateServiceTest {

    static private final CandidateService candidateService =
            new CandidateService(
                    new HibernateCandidateDaoService());

    //todo: hibernateControllerTest should exist for thoroughness.

    private Candidate createCandidateWithUUIDName() {
        String uuid = UUID.randomUUID().toString();
        return new Candidate(uuid);
    }

    private Candidate createCandidateWithNameFromArg(String name) {
        return new Candidate(name);
    }

    private Candidate saveCandidateWithName(Candidate candidateWithUUIDName) {
        int rowsInserted = candidateService.save(candidateWithUUIDName);
        assert (rowsInserted == 1);
        return candidateWithUUIDName;
    }

    private Candidate createCandidateWithNameSaveCandidateReturnCandidate(String name) {
        Candidate candidateWithUUIDName = createCandidateWithNameFromArg(name);
        return saveCandidateWithName(candidateWithUUIDName);
    }

    private Candidate createCandidateSaveCandidateReturnCandidate() {
        Candidate candidateWithUUIDName = createCandidateWithUUIDName();
        return saveCandidateWithName(candidateWithUUIDName);
    }

    private List<Candidate> getCandidatesByName(String candidateName) {
        return getCandidatesByName(candidateName, 1);
    }

    private List<Candidate> getCandidatesByName(String candidateName, int numOfExpectedCandidates) {
        List<Candidate> candidatesMatchingName = candidateService.getByName(candidateName);
        assert (candidatesMatchingName.size() == numOfExpectedCandidates);
        assert (Objects.equals(candidatesMatchingName.get(0).getName(), candidateName));
        return candidatesMatchingName;
    }

    private Long createCandidateSaveCandidateReturnID() {
        Long id = candidateService.saveReturnID(new Candidate(UUID.randomUUID().toString()));
        assert (id != null);
        assert (id != 0);
        return id;
    }

    private void deleteCandidates(List<Candidate> candidatesByName) {
        for (Candidate candidate: candidatesByName) {
            assert (candidateService.delete(candidate) == 1);
        }
    }

    private Candidate createCandidateSaveCandidateGetCandidateByIDReturnCandidate() throws DuplicatePrimaryKeyException {
        Long id = createCandidateSaveCandidateReturnID();
        Candidate candidate = candidateService.get(id);
        assert (candidate != null);
        return candidate;
    }

    private String updateCandidate(Candidate candidate) {
        String changeUuid1 = UUID.randomUUID().toString();
        candidate.setName(changeUuid1);
        int update = candidateService.update(candidate);
        assert (update == 1);
        return changeUuid1;
    }

    @Test
    @Order(1)
    void test_Save_GetByName_Delete() {
        Candidate candidateWithUUIDName = createCandidateSaveCandidateReturnCandidate();
        List<Candidate> candidatesMatchingName = getCandidatesByName(candidateWithUUIDName.getName());
        assert (candidateService.delete(candidatesMatchingName.get(0)) == 1);
    }

    @Test
    @Order(2)
    void test_Save_Save_GetAll_GetByName_GetByName_GetAll_Delete_Delete_GetAll() {
        int candidatesSize = candidateService.getAll().size();

        String candidateName = createCandidateSaveCandidateReturnCandidate().getName();

        createCandidateWithNameSaveCandidateReturnCandidate(candidateName);

        List<Candidate> candidatesByName = getCandidatesByName(candidateName, 2);
        assert (candidatesByName.size() == candidatesSize + 2);

        deleteCandidates(candidatesByName);

        Collection<Candidate> candidatesAfterDelete = candidateService.getAll();
        assert (candidatesAfterDelete.size() == candidatesSize);
    }

    @Test
    @Order(3)
    void test_SaveID_Get_Del() throws DuplicatePrimaryKeyException {
        Candidate candidate = createCandidateSaveCandidateGetCandidateByIDReturnCandidate();
        assert (candidateService.delete(candidate) == 1);
    }

    @Test
    @Order(4)
    void test_SaveID_Get_Update_GetByName_GetByName_Delete() throws DuplicatePrimaryKeyException {
        Candidate candidate = createCandidateSaveCandidateGetCandidateByIDReturnCandidate();
        String nameBeforeChange = candidate.getName();

        String changeUuid1 = updateCandidate(candidate);

        List<Candidate> candidatesMatchingName = candidateService.getByName(nameBeforeChange);
        assert (candidatesMatchingName.size() == 0);

        List<Candidate> candidatesMatchingChangeName = candidateService.getByName(changeUuid1);
        assert (Objects.equals(candidatesMatchingChangeName.get(0).getName(), changeUuid1));
        assert (candidateService.delete(candidate) == 1);
    }

    @Test
    @Order(5)
    void test_BadDelete() throws DuplicatePrimaryKeyException {
        String uuid1 = UUID.randomUUID().toString();

        Long id = candidateService.saveReturnID(new Candidate(uuid1));
        assert (id != null && id != 0);

        Candidate candidate = candidateService.get(id);
        assert (candidate != null);

        assert (candidateService.delete(candidate) == 1);
        assert (candidateService.delete(candidate) == 0);
    }

    @Test
    @Order(6)
    void test_SaveReturnID() throws DuplicatePrimaryKeyException {
        assert (candidateService.delete(createCandidateSaveCandidateGetCandidateByIDReturnCandidate()) == 1);
    }

    @Test
    @Order(7)
    void test_BadUpdate() {
        assert (candidateService.update(new Candidate(0L, UUID.randomUUID().toString())) == 0);
    }
}
