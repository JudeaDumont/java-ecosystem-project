package persistentdatatests;

import com.webapi.webapi.databasedrivers.Dao;
import com.webapi.webapi.databasedrivers.postgres.services.PostgreSqlCandidateDaoService;
import com.webapi.webapi.model.candidate.Candidate;
import com.webapi.webapi.model.candidate.NonExistentCandidateException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Objects;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestPostgreSqlCandidateDao {

    public static Long candidateId = null;

    private static final Dao<Candidate, Long, NonExistentCandidateException> candidateDAO =
            new PostgreSqlCandidateDaoService(
                    new JdbcTemplate(
                            testutil.PostgresDataSource.getDataSource()));

    @Test
    @Order(1)
    public void testSave() {
        Candidate c = new Candidate(
                1L,
                "chef");
        assert (candidateDAO.save(c) != 0);
    }

    @Test
    @Order(2)
    public void testPrintCandidates() {
        candidateId = candidateDAO.getAll().stream().findFirst().get().getId();
    }

    //todo: the idea that tests have to run in order for later tests to work is a bad idea
    @Test
    @Order(3)
    public void testGet() throws NonExistentCandidateException {
        assert (Objects.nonNull(candidateDAO.get(candidateId)));
    }

    @Test
    @Order(4)
    public void testUpdate() throws NonExistentCandidateException {

        Candidate firstCandidate = candidateDAO.get(candidateId);
        firstCandidate.setName("Franklin");

        assert (candidateDAO.update(firstCandidate) == 1);
    }

    @Test
    @Order(5)
    public void testDelete() throws NonExistentCandidateException {

        assert (candidateDAO.delete(candidateDAO.get(candidateId)) == 1);
    }
}
