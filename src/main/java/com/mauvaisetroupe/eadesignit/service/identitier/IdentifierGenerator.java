package com.mauvaisetroupe.eadesignit.service.identitier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdentifierGenerator {

    public Set<String> generatePotentielIdentifiers(Set<String> exisitingIdentifiers) {
        Set<String> potentialIdentifiers = new HashSet<>();
        Map<String, Integer> numberMap = new HashMap<>();
        Map<String, String> numberAsStringMap = new HashMap<>();

        for (String identifier : exisitingIdentifiers) {
            Pattern p = Pattern.compile("(.*?)(\\d+)");
            Matcher m = p.matcher(identifier);
            boolean b = m.matches();
            if (b) {
                String prefix = m.group(1);
                String numberAsString = m.group(2);
                int number = Integer.parseInt(numberAsString);
                if (numberMap.get(prefix) == null) {
                    numberMap.put(prefix, number);
                    numberAsStringMap.put(prefix, numberAsString);
                } else {
                    if (number > numberMap.get(prefix)) {
                        numberMap.put(prefix, number);
                        numberAsStringMap.put(prefix, numberAsString);
                    }
                }
            }
        }
        numberMap.forEach((k, v) -> {
            int newNumber = v + 1;
            String formatted = String.format("%0" + numberAsStringMap.get(k).length() + "d", newNumber);
            potentialIdentifiers.add("" + k + formatted);
        });
        return potentialIdentifiers;
    }
}
