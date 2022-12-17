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

    CandidateServiceTestHelper helper = new CandidateServiceTestHelper(candidateService);

    @Test
    @Order(1)
    void test_Save_GetByName_Delete() {
        Candidate candidateWithUUIDName = helper.createCandidateSaveCandidateReturnCandidate();
        List<Candidate> candidatesMatchingName = helper.getCandidatesByName(candidateWithUUIDName.getName());
        assert (candidateService.delete(candidatesMatchingName.get(0)) == 1);
    }

    @Test
    @Order(2)
    void test_CandidateWithSameName() {
        int candidatesSize = candidateService.getAll().size();

        String candidateName = helper.createCandidateSaveCandidateReturnCandidate().getName();
        helper.createCandidateWithNameSaveCandidateReturnCandidate(candidateName);

        List<Candidate> candidatesByName = helper.getCandidatesByName(candidateName, 2);
        if(!(candidatesByName.size() == candidatesSize + 2)){
            throw new RuntimeException(
                    "candidatesByName.size() == candidatesSize + 2\n" +
                    candidatesByName.size() + "==" + candidatesSize + 2 + "\n" +
                            candidateService.getAll().toString()
            );
        }

        helper.deleteCandidates(candidatesByName);

        Collection<Candidate> candidatesAfterDelete = candidateService.getAll();
        assert (candidatesAfterDelete.size() == candidatesSize);
    }

    @Test
    @Order(3)
    void test_SaveID_Get_Del() throws DuplicatePrimaryKeyException {
        Candidate candidate = helper.createCandidateSaveCandidateGetCandidateByIDReturnCandidate();
        assert (candidateService.delete(candidate) == 1);
    }

    @Test
    @Order(4)
    void test_SaveID_Get_Update_GetByName_GetByName_Delete() throws DuplicatePrimaryKeyException {
        Candidate candidate = helper.createCandidateSaveCandidateGetCandidateByIDReturnCandidate();
        String nameBeforeChange = candidate.getName();

        String changeUuid1 = helper.updateCandidate(candidate);

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
        assert (candidateService.delete(helper.createCandidateSaveCandidateGetCandidateByIDReturnCandidate()) == 1);
    }

    @Test
    @Order(7)
    void test_BadUpdate() {
        assert (candidateService.update(new Candidate(0L, UUID.randomUUID().toString())) == 0);
    }
}
