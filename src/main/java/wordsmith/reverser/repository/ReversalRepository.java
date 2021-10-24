package wordsmith.reverser.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import wordsmith.reverser.model.Reversal;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class ReversalRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ReversalRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Reversal storeReversal(String original, String reversed) {
        Map<String, Object> params = Map.of(
                "id", UUID.randomUUID(),
                "original", original,
                "reversed", reversed
        );

        String sql = "insert into reversal(id, original, reversed) " +
                "values(:id, :original, :reversed) returning *";
        return jdbcTemplate.queryForObject(sql, params, toReversal());
    }

    public Reversal getReversal(UUID id) {
        Map<String, Object> params = Map.of("id", id);

        String sql = "select * from reversal where id = :id";

        return jdbcTemplate.queryForObject(sql, params, toReversal());
    }

    public List<Reversal> getLatestReversals() {
        String sql = "select * from reversal order by created desc limit 5";

        return jdbcTemplate.query(sql, toReversal());
    }

    private RowMapper<Reversal> toReversal() {
        return (rs, rowNum) -> Reversal.builder()
                .id(UUID.fromString(rs.getString("id")))
                .original(rs.getString("original"))
                .reversed(rs.getString("reversed"))
                .created(rs.getTimestamp("created").toInstant())
                .build();
    }
}
