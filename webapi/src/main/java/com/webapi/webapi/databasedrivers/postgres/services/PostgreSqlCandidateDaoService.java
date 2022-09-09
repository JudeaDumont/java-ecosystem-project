package com.webapi.webapi.databasedrivers.postgres.services;

import com.webapi.webapi.databasedrivers.Dao;
import com.webapi.webapi.model.candidate.Candidate;
import com.webapi.webapi.model.candidate.NonExistentCandidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Collection;

@Repository("postgresCandidate")
public class PostgreSqlCandidateDaoService implements
        Dao<Candidate, Long, NonExistentCandidateException> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostgreSqlCandidateDaoService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Candidate get(Long id) {
        String sql = "SELECT * FROM Candidate WHERE id = ?";
        return jdbcTemplate.queryForObject(sql,
                new Object[]{id},
                new int[]{Types.BIGINT},
                (resultSet, i) -> {
                    return new Candidate(resultSet.getLong("id"), resultSet.getString("name"));
                });
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
    public int save(Candidate e) {
        String query = "insert into Candidate values(DEFAULT,'" + e.getName() + "')";
        return jdbcTemplate.update(query);
    }

    @Override
    public int update(Candidate e) {
        String query = "update Candidate set name='" + e.getName() + "' where id='" + e.getId() + "' ";
        return jdbcTemplate.update(query);
    }

    @Override
    public int delete(Candidate e) {
        String query = "delete from Candidate where id='" + e.getId() + "' ";
        return jdbcTemplate.update(query);
    }
}
