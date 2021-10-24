package wordsmith.reverser.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReverserTest {
    private final Reverser reverser = new Reverser();

    @ParameterizedTest
    @CsvSource(value = {
            "Hello | olleH",
            "Hello hi bye | olleH ih eyb",
            "Britt-Marie | ttirB-eiraM",
            "Britt-Marie-Test | ttirB-eiraM-tseT",
            "Hej. Two sentences. | jeH. owT secnetnes.",
            "The red fox crosses the ice, intent on none of my business. | ehT der xof sessorc eht eci, tnetni no enon fo ym ssenisub.",
            "My favorite song is played-a-live | yM etirovaf gnos si deyalp-a-evil"
            }, delimiter = '|')
    public void reverser(String original, String reversed) {
        assertEquals(reversed, reverser.reverseSentence(original));
    }
}
