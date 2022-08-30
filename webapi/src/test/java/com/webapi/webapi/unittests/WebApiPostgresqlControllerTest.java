package com.webapi.webapi.unittests;

import com.webapi.webapi.api.controllers.CandidateController;
import com.webapi.webapi.databasedrivers.postgres.services.PostgreSqlCandidateDaoService;
import com.webapi.webapi.model.candidate.Candidate;
import com.webapi.webapi.model.candidate.NonExistentCandidateException;
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
class WebApiPostgresqlControllerTest {

    static private Long candidateId = null;

    static private final CandidateController candidateController =
            new CandidateController(
                    new CandidateService(new PostgreSqlCandidateDaoService()));

    @Test
    @Order(1)
    void testSave() {
        candidateId = candidateController.save(new Candidate("muselk"));
        assert (candidateId != 0);
    }

    @Test
    @Order(2)
    void testGet() throws NonExistentCandidateException {
        Candidate candidate = candidateController.get(candidateId);
        assert (Objects.equals(candidate.getName(), "muselk"));
    }

    @Test
    @Order(3)
    void testUpdate() throws NonExistentCandidateException {
        Candidate candidate = candidateController.get(candidateId);
        candidate.setName("shrek");
        boolean updated = candidateController.put(candidate);
        assert (updated);
    }

    @Test
    @Order(4)
    void testDelete() throws NonExistentCandidateException {
        Candidate candidate = candidateController.get(candidateId);
        boolean deleted = candidateController.delete(candidate.getId());
        assert (deleted);
    }

    @Test
    @Order(5)
    void testGetAll() {
        Collection<Candidate> candidates = candidateController.getAll();
        assert (candidates.size() != 0);
    }
}
