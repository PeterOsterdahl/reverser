package wordsmith.reverser.service;

import org.springframework.stereotype.Component;

@Component
public class Reverser {

    public String reverseSentence(String sentence) {
        var words = sentence.split(" ");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length - 1; i++) {
            result.append(reverse(words[i]));
            result.append(" ");
        }
        result.append(reverse(words[words.length-1]));
        return result.toString();
    }

    private String reverse(String s) {
        StringBuilder result = new StringBuilder();
        if (s.contains("-")) {
            return reverse(firstPart(s))
                    + "-"
                    + reverse(secondPart(s));
        }

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.' || s.charAt(i) == ',') {
                result.insert(i, s.charAt(i));
                continue;
            }
            result.insert(0, s.charAt(i));
        }
        return result.toString();
    }

    private String secondPart(String s) {
        return s.substring(s.indexOf("-") + 1);
    }

    private String firstPart(String s) {
        return s.substring(0, s.indexOf("-"));
    }
}
