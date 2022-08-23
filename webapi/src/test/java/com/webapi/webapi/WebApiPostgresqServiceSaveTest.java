package com.webapi.webapi;

import com.webapi.webapi.services.CandidateService;
import data.candidate.Candidate;
import data.postgresql.PostgreSqlCandidateDaoService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebApiPostgresqServiceSaveTest {

    static private Long candidateId = null;

    @Test
    @Order(1)
    void testSave() {
        CandidateService candidateService =
                new CandidateService(
                        new PostgreSqlCandidateDaoService());
        candidateId = candidateService.save(new Candidate("muselk"));
        assert (candidateId != 0);
    }

    @Test
    @Order(2)
    void testGet() {
        CandidateService candidateService =
                new CandidateService(
                        new PostgreSqlCandidateDaoService());
        Candidate candidate = candidateService.get(candidateId);
        assert (Objects.equals(candidate.getName(), "muselk"));
    }
}
