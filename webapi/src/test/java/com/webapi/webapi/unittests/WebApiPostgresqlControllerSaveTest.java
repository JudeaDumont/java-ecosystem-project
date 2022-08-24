package com.webapi.webapi.unittests;

import com.webapi.webapi.api.CandidateController;
import com.webapi.webapi.model.Candidate;
import com.webapi.webapi.model.PostgreSqlCandidateDaoService;
import com.webapi.webapi.services.CandidateService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebApiPostgresqlControllerSaveTest {

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

//    @Test
//    @Order(2)
//    void testGet() {
//        Candidate candidate = candidateController.get(candidateId);
//        assert (Objects.equals(candidate.getName(), "muselk"));
//    }

//    @Test
//    @Order(3)
//    void testGetAll() {
//        Collection<Candidate> candidates = candidateController.getAll();
//        assert (candidates.size() != 0);
//    }
}
