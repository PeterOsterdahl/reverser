package wordsmith.reverser.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wordsmith.reverser.model.Reversal;
import wordsmith.reverser.service.ReversalService;

import java.util.List;


@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
public class ReverserController {

    private final ReversalService reversalService;

    @PostMapping(value = "/v1/reverse")
    public Reversal reverse(@RequestBody String sentence) {
        return reversalService.reverse(sentence);
    }

    @GetMapping(value = "/v1/reverse")
    public List<Reversal> getLatestReversals() {
        return reversalService.getLatestReversals();
    }
}


