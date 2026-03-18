package kg.nurtelecom.o_subscriber_service.repository;

import kg.nurtelecom.o_subscriber_service.dto.SubscriberSummaryResponse;
import kg.nurtelecom.o_subscriber_service.exception.ResourceNotFoundException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

@Repository
public class SubscriberJdbcDao {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final JdbcOperations jdbcOperations;
    private final JdbcClient jdbcClient;

    public SubscriberJdbcDao(DataSource dataSource,
                             JdbcTemplate jdbcTemplate,
                             JdbcOperations jdbcOperations,
                             JdbcClient jdbcClient) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcOperations = jdbcOperations;
        this.jdbcClient = jdbcClient;
    }

    // Native JDBC
    public int countSubscribersNativeJdbc() {
        String sql = "select count(*) from subscribers";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error counting subscribers with Native JDBC", e);
        }
    }

    // JdbcTemplate
    public List<SubscriberSummaryResponse> findAllSummariesJdbcTemplate() {
        String sql = """
                select id, full_name, phone_number, balance, tariff_plan
                from subscribers
                order by id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapSummary(rs));
    }

    // JdbcOperations
    public SubscriberSummaryResponse findSummaryByIdJdbcOperations(Long id) {
        String sql = """
                select id, full_name, phone_number, balance, tariff_plan
                from subscribers
                where id = ?
                """;

        List<SubscriberSummaryResponse> results =
                jdbcOperations.query(sql, (rs, rowNum) -> mapSummary(rs), id);

        if (results.isEmpty()) {
            throw new ResourceNotFoundException("Subscriber not found with id: " + id);
        }

        return results.get(0);
    }

    // JdbcClient
    public List<SubscriberSummaryResponse> findSubscribersWithBalanceGreaterThanJdbcClient(BigDecimal amount) {
        String sql = """
                select id, full_name, phone_number, balance, tariff_plan
                from subscribers
                where balance > :amount
                order by balance desc
                """;

        return jdbcClient.sql(sql)
                .param("amount", amount)
                .query((rs, rowNum) -> mapSummary(rs))
                .list();
    }

    // JdbcTemplate update
    public int deactivateSubscriberJdbcTemplate(Long id) {
        String sql = "update subscribers set active = false where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    // JdbcTemplate update email
    public int updateEmailJdbcTemplate(Long id, String email) {
        String sql = "update subscribers set email = ? where id = ?";
        return jdbcTemplate.update(sql, email, id);
    }

    // JdbcOperations read email
    public String findEmailByIdJdbcOperations(Long id) {
        String sql = "select email from subscribers where id = ?";

        List<String> results = jdbcOperations.query(
                sql,
                (rs, rowNum) -> rs.getString("email"),
                id
        );

        if (results.isEmpty()) {
            throw new ResourceNotFoundException("Subscriber not found with id: " + id);
        }

        return results.get(0);
    }

    // JdbcClient list without email
    public List<SubscriberSummaryResponse> findSubscribersWithoutEmailJdbcClient() {
        String sql = """
                select id, full_name, phone_number, balance, tariff_plan
                from subscribers
                where email is null or trim(email) = ''
                order by id
                """;

        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> mapSummary(rs))
                .list();
    }

    private SubscriberSummaryResponse mapSummary(ResultSet rs) throws SQLException {
        return new SubscriberSummaryResponse(
                rs.getLong("id"),
                rs.getString("full_name"),
                rs.getString("phone_number"),
                rs.getBigDecimal("balance"),
                rs.getString("tariff_plan")
        );
    }
}