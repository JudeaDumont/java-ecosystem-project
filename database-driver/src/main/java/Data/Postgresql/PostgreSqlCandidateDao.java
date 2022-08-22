package Data.Postgresql;

import Data.Candidate.Candidate;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSqlCandidateDao implements Dao<Candidate, Long> {

    private static final Logger LOGGER =
            Logger.getLogger(PostgreSqlCandidateDao.class.getName());
    private final Connection connection;

    public PostgreSqlCandidateDao() {
        this.connection = JdbcConnection.getConnection();
    }

    @Override
    public Candidate get(Long id) {
        Candidate Candidate = null;
        String sql = "SELECT * FROM Candidate WHERE id = " + id;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                // todo: the reason why you can't generify this is that
                // todo: there is nothing mapping results from queries to their
                // todo: object type in java land (like hibernate), so you have to manually do it like this
                // todo: add another package for postgresqlHibernate
                Candidate = new Candidate(
                        id,
                        resultSet.getString("name")
                );
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return Candidate;
    }

    @Override
    public Collection<Candidate> getAll() {
        Collection<Candidate> Candidates = new ArrayList<>();
        String sql = "SELECT * FROM Candidate";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Candidate Candidate = new Candidate(
                        resultSet.getLong("id"),
                        resultSet.getString("name"));

                Candidates.add(Candidate);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return Candidates;
    }


    @Override
    public Long save(Candidate candidate) {
        // I like this pattern for checking parameters
        String message = "The Candidate to be added should not be null";
        Objects.requireNonNull(candidate, message);

        String sql = "INSERT INTO "
                + "Candidate(name) "
                + "VALUES(?)";

        Long generatedId = null;

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, candidate.getName());

            int numberOfInsertedRows = statement.executeUpdate();

            // Retrieve the auto-generated id
            if (numberOfInsertedRows > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedId = resultSet.getLong(1);
                        candidate.setId(generatedId); //doesn't do it on its own
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return generatedId;
    }

    @Override
    public boolean delete(Candidate candidate) {
        String message = "The Candidate to be deleted should not be null";
        Objects.requireNonNull(candidate, message);

        String sql = "DELETE FROM Candidate WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, candidate.getId());

            return statement.executeUpdate() == 1;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }


    @Override
    public boolean update(Candidate candidate) {
        String message = "The Candidate to be updated should not be null";
        Objects.requireNonNull(candidate, message);
        String sql = "UPDATE Candidate "
                + "SET "
                + "name = ? "
                + "WHERE "
                + "id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, candidate.getName());
            statement.setLong(2, candidate.getId());

            return statement.executeUpdate() == 1;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
