package wordsmith.reverser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wordsmith.reverser.model.Reversal;
import wordsmith.reverser.repository.ReversalRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReversalService {
    private final ReversalRepository reversalRepository;
    private final Reverser reverser;

    public Reversal reverse(String original) {
        validateInput(original);
        String reversed = reverser.reverseSentence(original);
        return reversalRepository.storeReversal(original, reversed);
    }

    private void validateInput(String original) {
        if (original.length() > 500) {
            throw new IllegalArgumentException();
        }
    }

    public List<Reversal> getLatestReversals() {
        return reversalRepository.getLatestReversals();
    }
}
