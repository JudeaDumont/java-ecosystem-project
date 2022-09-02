package com.webapi.webapi.unittests;

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
import java.util.Objects;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebApiPostgresqlCandidateServiceTest {

    static private Long candidateId = null;

    static private final CandidateService candidateService =
            new CandidateService(
                    new PostgreSqlCandidateDaoService(
                            new JdbcTemplate(
                                    testutil.PostgresDataSource.getDataSource())));

    @Test
    @Order(1)
    void testSave() {
        int rowsInserted = candidateService.save(new Candidate("muselk"));
        assert (rowsInserted != 0);
    }


    @Test
    @Order(2)
    void testGetAll() {
        Collection<Candidate> candidates = candidateService.getAll();
        candidateId = candidates.stream().findFirst().get().getId();
        assert (candidates.size() != 0);
    }

    @Test
    @Order(3)
    void testGet() throws NonExistentCandidateException {
        Candidate candidate = candidateService.get(candidateId);
        assert (Objects.nonNull(candidate.getName()));
        assert (candidate.getId() != null);
        assert (candidate.getId() != 0);
    }

    @Test
    @Order(4)
    void testUpdate() throws NonExistentCandidateException {
        Candidate candidate = candidateService.get(candidateId);
        candidate.setName("shrek");
        assert (candidateService.update(candidate) == 1);
    }

    @Test
    @Order(5)
    void testDelete() throws NonExistentCandidateException {
        Candidate candidate = candidateService.get(candidateId);
        assert (candidateService.delete(candidate) == 1);
    }
}
