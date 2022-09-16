package com.webapi.unittests;

import com.webapi.api.controllers.CandidateController;
import com.webapi.databasedrivers.DuplicatePrimaryKeyException;
import com.webapi.databasedrivers.postgres.services.PostgreSqlCandidateDaoService;
import com.webapi.model.candidate.Candidate;
import com.webapi.services.CandidateService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebApiPostgresqlControllerTest {

    static private final CandidateController candidateController =
            new CandidateController(
                    new CandidateService(
                            new PostgreSqlCandidateDaoService(
                                    new JdbcTemplate(
                                            testutil.PostgresDataSource.getDataSource()))));

    @Test
    @Order(1)
    void test_Save_GetByName_Delete() throws DuplicatePrimaryKeyException {
        String uuid = UUID.randomUUID().toString();

        int rowsInserted = candidateController.save(new Candidate(uuid));
        assert (rowsInserted == 1);

        List<Candidate> candidatesMatchingName = candidateController.getByName(uuid);
        assert (candidatesMatchingName.size() == 1);
        assert (Objects.equals(candidatesMatchingName.get(0).getName(), uuid));

        assert (candidateController.delete(candidatesMatchingName.get(0).getId()) == 1);
    }

    @Test
    @Order(2)
    void test_Save_Save_GetAll_GetByName_GetByName_GetAll_Delete_Delete_GetAll() throws DuplicatePrimaryKeyException {
        String uuid1 = UUID.randomUUID().toString();
        String uuid2 = UUID.randomUUID().toString();

        Collection<Candidate> candidates = candidateController.getAll();
        int candidatesSize = candidates.size();

        int rowsInserted = candidateController.save(new Candidate(uuid1));
        assert (rowsInserted == 1);
        int rowsInserted2 = candidateController.save(new Candidate(uuid2));
        assert (rowsInserted2 == 1);

        Collection<Candidate> candidatesAfterInsert = candidateController.getAll();
        assert (candidatesAfterInsert.size() == candidatesSize + 2);

        List<Candidate> candidatesMatchingName = candidateController.getByName(uuid1);
        assert (candidatesMatchingName.size() == 1);
        assert (Objects.equals(candidatesMatchingName.get(0).getName(), uuid1));

        List<Candidate> candidatesMatchingName2 = candidateController.getByName(uuid2);
        assert (candidatesMatchingName2.size() == 1);
        assert (Objects.equals(candidatesMatchingName2.get(0).getName(), uuid2));

        assert (candidateController.delete(candidatesMatchingName.get(0).getId()) == 1);
        assert (candidateController.delete(candidatesMatchingName2.get(0).getId()) == 1);

        Collection<Candidate> candidatesAfterDelete = candidateController.getAll();
        assert (candidatesAfterDelete.size() == candidatesSize);
    }

    @Test
    @Order(3)
    void test_SaveID_Get_Del() throws DuplicatePrimaryKeyException {
        String uuid1 = UUID.randomUUID().toString();

        Long id = candidateController.saveReturnID(new Candidate(uuid1));
        assert (id != null);
        assert (id != 0);

        Candidate candidate = candidateController.get(id);
        assert (candidate != null);

        assert (candidateController.delete(candidate.getId()) == 1);
    }

    @Test
    @Order(4)
    void test_SaveID_Get_Update_GetByName_GetByName_Delete() throws DuplicatePrimaryKeyException {
        String uuid1 = UUID.randomUUID().toString();
        String changeUuid1 = UUID.randomUUID().toString();

        Long id = candidateController.saveReturnID(new Candidate(uuid1));
        assert (id != null);
        assert (id != 0);

        Candidate candidate = candidateController.get(id);
        assert (candidate != null);

        candidate.setName(changeUuid1);

        int update = candidateController.put(candidate);

        List<Candidate> candidatesMatchingName = candidateController.getByName(uuid1);
        assert (candidatesMatchingName.size() == 0);

        List<Candidate> candidatesMatchingChangeName = candidateController.getByName(changeUuid1);
        assert (Objects.equals(candidatesMatchingChangeName.get(0).getName(), changeUuid1));

        assert (update == 1);
        assert (candidateController.delete(candidate.getId()) == 1);
    }

    @Test
    @Order(5)
    void test_SaveID_Get_Del_Del() throws DuplicatePrimaryKeyException {
        String uuid1 = UUID.randomUUID().toString();

        Long id = candidateController.saveReturnID(new Candidate(uuid1));
        assert (id != null && id != 0);

        Candidate candidate = candidateController.get(id);
        assert (candidate != null);

        assert (candidateController.delete(candidate.getId()) == 1);
        assert (candidateController.delete(candidate.getId()) == 0);
    }

    @Test
    @Order(6)
    void test_SaveReturnID2() throws DuplicatePrimaryKeyException {
        Candidate kraken = new Candidate("kraken");
        Long id = candidateController.saveReturnID(kraken);
        kraken.setId(id); //postgresql uses raw sql, it isn't going to manipulate the java object for you.
        assert (id != null && id != 0);
        assert (candidateController.delete(kraken.getId()) == 1);
    }

    @Test
    @Order(7)
    void test_BadUpdate() {
        String uuid1 = UUID.randomUUID().toString();
        assert (candidateController.put(new Candidate(0L, uuid1)) == 0);
    }
}
