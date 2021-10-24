package wordsmith.reverser.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import wordsmith.reverser.model.Reversal;
import wordsmith.reverser.repository.ReversalRepository;

import java.util.List;
import java.util.Map;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReverserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReversalRepository reversalRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void SetUp() {
        String sql = "truncate reversal";
        jdbcTemplate.update(sql, Map.of());
        JavaTimeModule module = new JavaTimeModule();
        objectMapper.registerModule(module);
    }

    @Test
    void persistsAndReturnsAReversal() throws Exception {
        MvcResult response = mockMvc.perform(post("/v1/reverse")
                        .contentType("application/json")
                        .content("hej")
                )
                .andExpect(status().isOk())
                .andReturn();

        String json = response.getResponse().getContentAsString();
        Reversal responseReversal = objectMapper.readValue(json, Reversal.class);
        Reversal storedReversal = reversalRepository.getReversal(responseReversal.getId());

        assertThat(storedReversal, pojo(Reversal.class)
                .where(Reversal::getOriginal, is(responseReversal.getOriginal()))
                .where(Reversal::getReversed, is(responseReversal.getReversed()))
                .where(Reversal::getCreated, is(responseReversal.getCreated()))
                .where(Reversal::getId, is(responseReversal.getId()))
        );
    }

    @Test
    void returnsTheLatestFiveReversals() throws Exception {
        reversalRepository.storeReversal("hej1", "1jeh");
        reversalRepository.storeReversal("hej2", "2jeh");
        reversalRepository.storeReversal("hej3", "3jeh");
        reversalRepository.storeReversal("hej4", "4jeh");
        reversalRepository.storeReversal("hej5", "5jeh");
        reversalRepository.storeReversal("hej6", "6jeh");

        var response = mockMvc.perform(get("/v1/reverse"))
                .andExpect(status().isOk())
                .andReturn();

        String json = response.getResponse().getContentAsString();
        List<Reversal> reversals = objectMapper.readValue(json, new TypeReference<>() {
        });

        assertThat(reversals, contains(
                pojo(Reversal.class)
                        .where(Reversal::getOriginal, is("hej6"))
                        .where(Reversal::getReversed, is("6jeh")),
                pojo(Reversal.class)
                        .where(Reversal::getOriginal, is("hej5"))
                        .where(Reversal::getReversed, is("5jeh")),
                pojo(Reversal.class)
                        .where(Reversal::getOriginal, is("hej4"))
                        .where(Reversal::getReversed, is("4jeh")),
                pojo(Reversal.class)
                        .where(Reversal::getOriginal, is("hej3"))
                        .where(Reversal::getReversed, is("3jeh")),
                pojo(Reversal.class)
                        .where(Reversal::getOriginal, is("hej2"))
                        .where(Reversal::getReversed, is("2jeh"))));
    }
}
