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

    //todo: the integration tests will need to recreate this flow to satisfy "tests can be run individually" and "passing tests don't effect data"
    //todo: hibernateControllerTest should exist for thoroughness.
    @Test
    @Order(1)
    void test_Save_GetByName_Delete() {
        String uuid = UUID.randomUUID().toString();

        int rowsInserted = candidateService.save(new Candidate(uuid));
        assert (rowsInserted == 1);

        List<Candidate> candidatesMatchingName = candidateService.getByName(uuid);
        assert (candidatesMatchingName.size() == 1);
        assert (Objects.equals(candidatesMatchingName.get(0).getName(), uuid));

        assert (candidateService.delete(candidatesMatchingName.get(0)) == 1);
    }

    @Test
    @Order(2)
    void test_Save_Save_GetAll_GetByName_GetByName_GetAll_Delete_Delete_GetAll() {
        String uuid1 = UUID.randomUUID().toString();
        String uuid2 = UUID.randomUUID().toString();

        Collection<Candidate> candidates = candidateService.getAll();
        int candidatesSize = candidates.size();

        int rowsInserted = candidateService.save(new Candidate(uuid1));
        assert (rowsInserted == 1);
        int rowsInserted2 = candidateService.save(new Candidate(uuid2));
        assert (rowsInserted2 == 1);

        Collection<Candidate> candidatesAfterInsert = candidateService.getAll();
        assert (candidatesAfterInsert.size() == candidatesSize + 2);

        List<Candidate> candidatesMatchingName = candidateService.getByName(uuid1);
        assert (candidatesMatchingName.size() == 1);
        assert (Objects.equals(candidatesMatchingName.get(0).getName(), uuid1));

        List<Candidate> candidatesMatchingName2 = candidateService.getByName(uuid2);
        assert (candidatesMatchingName2.size() == 1);
        assert (Objects.equals(candidatesMatchingName2.get(0).getName(), uuid2));

        assert (candidateService.delete(candidatesMatchingName.get(0)) == 1);
        assert (candidateService.delete(candidatesMatchingName2.get(0)) == 1);

        Collection<Candidate> candidatesAfterDelete = candidateService.getAll();
        assert (candidatesAfterDelete.size() == candidatesSize);
    }

    @Test
    @Order(3)
    void test_SaveID_Get_Del() throws DuplicatePrimaryKeyException {
        String uuid1 = UUID.randomUUID().toString();

        Long id = candidateService.saveReturnID(new Candidate(uuid1));
        assert (id != null);
        assert (id != 0);

        Candidate candidate = candidateService.get(id);
        assert (candidate != null);

        assert (candidateService.delete(candidate) == 1);
    }

    @Test
    @Order(4)
    void test_SaveID_Get_Update_GetByName_GetByName_Delete() throws DuplicatePrimaryKeyException {
        String uuid1 = UUID.randomUUID().toString();
        String changeUuid1 = UUID.randomUUID().toString();

        Long id = candidateService.saveReturnID(new Candidate(uuid1));
        assert (id != null);
        assert (id != 0);

        Candidate candidate = candidateService.get(id);
        assert (candidate != null);

        candidate.setName(changeUuid1);

        int update = candidateService.update(candidate);

        List<Candidate> candidatesMatchingName = candidateService.getByName(uuid1);
        assert (candidatesMatchingName.size() == 0);

        List<Candidate> candidatesMatchingChangeName = candidateService.getByName(changeUuid1);
        assert (Objects.equals(candidatesMatchingChangeName.get(0).getName(), changeUuid1));

        assert (update == 1);
        assert (candidateService.delete(candidate) == 1);
    }

    @Test
    @Order(5)
    void test_SaveID_Get_Del_Del() throws DuplicatePrimaryKeyException {
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
    void test_SaveReturnID() {
        Candidate kraken = new Candidate("kraken");
        Long id = candidateService.saveReturnID(kraken);
        assert (id != null && id != 0);
        assert (candidateService.delete(kraken) == 1);
    }

    @Test
    @Order(7)
    void test_BadUpdate() {
        String uuid1 = UUID.randomUUID().toString();
        assert (candidateService.update(new Candidate(0L, uuid1)) == 0);
    }
}
