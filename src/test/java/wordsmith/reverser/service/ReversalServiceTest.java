package wordsmith.reverser.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wordsmith.reverser.repository.ReversalRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ReversalServiceTest {
    private final ReversalRepository reversalRepository = mock(ReversalRepository.class);
    private final Reverser reverser = mock(Reverser.class);
    private final ReversalService reversalService = new ReversalService(reversalRepository, reverser);

    @Test
    void throwsExceptionWhenSentenceIsTooLong() {
        String input = "d".repeat(501);
        assertThrows(IllegalArgumentException.class,
                () -> reversalService.reverse(input));
        verifyNoInteractions(reverser);
        verifyNoInteractions(reversalRepository);
    }

    @ParameterizedTest
    @ValueSource(ints = {500, 499})
    void reversesAStringShorterThan500Characters(int stringLength) {
        String input = "a".repeat(stringLength);
        String reversed = "a mocked string";
        when(reverser.reverseSentence(input)).thenReturn(reversed);

        reversalService.reverse(input);

        verify(reverser).reverseSentence(input);
        verify(reversalRepository).storeReversal(input, reversed);
    }
}
