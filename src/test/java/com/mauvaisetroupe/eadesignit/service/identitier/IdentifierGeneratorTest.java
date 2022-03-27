package com.mauvaisetroupe.eadesignit.service.identitier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

public class IdentifierGeneratorTest {

    @Test
    void testGeneratePotentielIdentifiers() {
        Set<String> exisitingIdentifiers = new TreeSet<>();
        exisitingIdentifiers.add("CUST.001");
        exisitingIdentifiers.add("CUST.002");
        exisitingIdentifiers.add("CUST.003");
        exisitingIdentifiers.add("INV.001");
        exisitingIdentifiers.add("INV.002");
        exisitingIdentifiers.add("INV.010");
        exisitingIdentifiers.add("TRO,001");
        exisitingIdentifiers.add("TRO,002");

        IdentifierGenerator generator = new IdentifierGenerator(exisitingIdentifiers);

        Set<String> potentialIdentifiers = generator.getPotentialIdentifiers();

        System.out.println(potentialIdentifiers);
        assertEquals(3, potentialIdentifiers.size());
        assertTrue(potentialIdentifiers.contains("CUST.004"));
        assertTrue(potentialIdentifiers.contains("INV.011"));
        assertTrue(potentialIdentifiers.contains("TRO,003"));
    }
}
