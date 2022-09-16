package com.webapi.webapi.databasedrivers.postgres.services;

import com.webapi.webapi.databasedrivers.CandidateDao;
import com.webapi.webapi.databasedrivers.DuplicatePrimaryKeyException;
import com.webapi.webapi.model.candidate.Candidate;
import com.webapi.webapi.model.candidate.NonExistentCandidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//todo: create a hierarchy of dao interfaces, DAO -> CandidateDAO, etc.
// Then, the question becomes: what common methods operate on our data?
// The commonalities can be exploited such that the DAOs are named for the methods that operate on data, not the objects.
// Then, you use templates to communicate with the data source, and the object mapper is generated meta models

//todo: replace methods with usage of jdbc.core.simple implementations
@Repository("postgresCandidate")
public class PostgreSqlCandidateDaoService implements
        CandidateDao<Candidate, Long, NonExistentCandidateException> {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertIntoCandidate;

    @Autowired
    public PostgreSqlCandidateDaoService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertIntoCandidate = new SimpleJdbcInsert(jdbcTemplate).withTableName("Candidate").usingGeneratedKeyColumns("id");
    }

    @Override
    public Candidate get(Long id) throws DuplicatePrimaryKeyException {
        String sql = "SELECT * FROM Candidate WHERE id = " + id.toString();

        List<Candidate> candidatesMatchingID = jdbcTemplate.query(sql, (resultSet, i) -> {
//            if (!resultSet.isBeforeFirst()) {
            return new Candidate(resultSet.getLong("id"), resultSet.getString("name"));
//            }
//            return null;
        });
        if (candidatesMatchingID.size() == 1) {
            return candidatesMatchingID.get(0);
        } else if (candidatesMatchingID.size() == 0) {
            return null;
        }
        throw new DuplicatePrimaryKeyException();
    }

    @Override
    public Collection<Candidate> getAll() {
        String sql = "SELECT * FROM Candidate";
        return jdbcTemplate.query(sql, (resultSet, i) ->
                new Candidate(
                        resultSet.getLong("id"),
                        resultSet.getString("name")));
    }

    @Override
    public int save(Candidate candidate) {
        String query = "insert into Candidate values(DEFAULT,'" + candidate.getName() + "')";
        return jdbcTemplate.update(query);
    }

    public Long saveReturnID(Candidate candidate) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", candidate.getName());

        return (Long) insertIntoCandidate.executeAndReturnKey(parameters);
    }

    @Override
    public List<Candidate> getByName(String name) {
        String sql = "SELECT * FROM Candidate WHERE name='" + name + "' ";
        return jdbcTemplate.query(sql, (resultSet, i) ->
                new Candidate(
                        resultSet.getLong("id"),
                        resultSet.getString("name")));
    }

    @Override
    public int update(Candidate candidate) {
        String query = "update Candidate set name='" + candidate.getName() + "' where id='" + candidate.getId() + "' ";
        return jdbcTemplate.update(query);
    }

    @Override
    public int delete(Candidate candidate) {
        String query = "delete from Candidate where id='" + candidate.getId() + "' ";
        return jdbcTemplate.update(query);
    }
}
