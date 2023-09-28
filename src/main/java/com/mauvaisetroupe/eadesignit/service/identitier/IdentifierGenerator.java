package com.mauvaisetroupe.eadesignit.service.identitier;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdentifierGenerator {

    private Set<String> potentialIdentifiers = new HashSet<>();

    public Set<String> getPotentialIdentifiers() {
        return potentialIdentifiers;
    }

    private Map<String, Long> numberMap = new HashMap<>();
    private Map<String, String> numberAsStringMap = new HashMap<>();
    private Collection<String> exisitingIdentifiers;

    public IdentifierGenerator(Collection<String> exisitingIdentifiers) {
        this.exisitingIdentifiers = exisitingIdentifiers;
        init();
    }

    private void init() {
        for (String identifier : exisitingIdentifiers) {
            if (identifier != null) {
                Pattern p = Pattern.compile("(.*?)(\\d+)");
                Matcher m = p.matcher(identifier);
                boolean b = m.matches();
                try {
                    if (b) {
                        String prefix = m.group(1);
                        String numberAsString = m.group(2);
                        long number = Long.parseLong(numberAsString);
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
                } catch (NumberFormatException e) {}
            }
        }
        potentialIdentifiers = new HashSet<>();
        numberMap.forEach((k, v) -> {
            long newNumber = v + 1;
            String formatted = String.format("%0" + numberAsStringMap.get(k).length() + "d", newNumber);
            potentialIdentifiers.add("" + k + formatted);
        });
    }

    public String getNext(String prefix) {
        String result = null;
        for (String string : potentialIdentifiers) {
            if (string.startsWith(prefix)) {
                result = string;
            }
        }
        if (result == null) result = prefix + "000";
        this.exisitingIdentifiers.add(result);
        init();
        return result;
    }
}
