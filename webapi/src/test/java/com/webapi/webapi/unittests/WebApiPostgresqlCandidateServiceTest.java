package com.webapi.webapi.unittests;

import com.webapi.webapi.databasedrivers.DuplicatePrimaryKeyException;
import com.webapi.webapi.databasedrivers.postgres.services.PostgreSqlCandidateDaoService;
import com.webapi.webapi.model.candidate.Candidate;
import com.webapi.webapi.model.candidate.NonExistentCandidateException;
import com.webapi.webapi.services.CandidateService;
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
class WebApiPostgresqlCandidateServiceTest {

    static private final CandidateService candidateService =
            new CandidateService(
                    new PostgreSqlCandidateDaoService(
                            new JdbcTemplate(
                                    testutil.PostgresDataSource.getDataSource())));

    @Test
    @Order(1)
    void test_Save_GetByName_Delete() throws NonExistentCandidateException {
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
    void test_Save_Save_GetAll_GetByName_GetByName_GetAll_Delete_Delete_GetAll() throws NonExistentCandidateException {
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
    void test_SaveID_Get_Del() throws NonExistentCandidateException, DuplicatePrimaryKeyException {
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
    void test_SaveID_Get_Update_GetByName_GetByName_Delete() throws NonExistentCandidateException, DuplicatePrimaryKeyException {
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
    void test_SaveID_Get_Del_Del() throws NonExistentCandidateException, DuplicatePrimaryKeyException {
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
    void test_SaveReturnID2() throws NonExistentCandidateException {
        Candidate kraken = new Candidate("kraken");
        Long id = candidateService.saveReturnID(kraken);
        kraken.setId(id); //postgresql uses raw sql, it isn't going to manipulate the java object for you.
        assert (id != null && id != 0);
        assert (candidateService.delete(kraken) == 1);
    }

    @Test
    @Order(7)
    void test_BadUpdate() throws NonExistentCandidateException {
        String uuid1 = UUID.randomUUID().toString();
        assert (candidateService.update(new Candidate(0L, uuid1)) == 0);
    }
}
