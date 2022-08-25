package com.webapi.webapi.unittests;

import com.webapi.webapi.databasedrivers.postgres.PostgreSqlCandidateDaoService;
import com.webapi.webapi.model.candidate.Candidate;
import com.webapi.webapi.services.CandidateService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Objects;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebApiPostgresqlServiceSaveTest {

    static private Long candidateId = null;

    static private final CandidateService candidateService =
            new CandidateService(
                    new PostgreSqlCandidateDaoService());

    @Test
    @Order(1)
    void testSave() {
        candidateId = candidateService.save(new Candidate("muselk"));
        assert (candidateId != 0);
    }

    @Test
    @Order(2)
    void testGet() {
        Candidate candidate = candidateService.get(candidateId);
        assert (Objects.equals(candidate.getName(), "muselk"));
    }

    @Test
    @Order(3)
    void testGetAll() {
        Collection<Candidate> candidates = candidateService.getAll();
        assert (candidates.size() != 0);
    }
}
