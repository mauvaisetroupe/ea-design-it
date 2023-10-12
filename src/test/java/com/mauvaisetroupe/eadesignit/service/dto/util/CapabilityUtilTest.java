package com.mauvaisetroupe.eadesignit.service.dto.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;


import com.mauvaisetroupe.eadesignit.domain.Capability;

public class CapabilityUtilTest {

    Map<String,Capability> capabilitiesMap = new HashMap<>();
    long index = 0;

    @Test
    public void testBuildCapabilityTree() {

        Capability root = createChild(null, "ROOT", -2);
        Capability domain = createChild(root, "DOMAIN", -1);   
        

        for (int i = 0; i <= 3; i++) {
            Capability l0 = createChild(domain, "L0." + i , 0);
            for (int j = 0; j <= 3; j++) {       
                Capability l1 = createChild(l0, "L1." + i + "." + j , 1);    
                for (int k = 0; k <= 3; k++) {       
                    Capability l2 = createChild(l1, "L2." + i + "." + j + "." + k , 2);    
                }
            }
        }

        CapabilityUtil capabilityUtil = new CapabilityUtil();
        
        Capability test1 = capabilityUtil.buildCapabilityTree(Arrays.asList(capabilitiesMap.get("L2.1.1.1")));
        assertEquals("ROOT",test1.getName());
        checkSize(test1,new int[] {1,1,1,1,0});        


        // 2 capabilities in 2 differents lines
        Capability test2 = capabilityUtil.buildCapabilityTree(Arrays.asList(capabilitiesMap.get("L2.1.1.1"), capabilitiesMap.get("L2.1.1.2")));
        assertEquals("ROOT",test1.getName());
        checkSize(test2,new int[] {1,1,1,2,0});        

        // 2 capabilities in same line
        Capability test3 = capabilityUtil.buildCapabilityTree(Arrays.asList(capabilitiesMap.get("L2.1.1.1"), capabilitiesMap.get("L1.1.1")));
        assertEquals("ROOT",test1.getName());
        checkSize(test3,new int[] {1,1,1,1,0});       
        
        // 2 capabilities in 2 differents lines
        Capability test4 = capabilityUtil.buildCapabilityTree(Arrays.asList(capabilitiesMap.get("L2.1.1.1"), capabilitiesMap.get("L1.1.2")));
        assertEquals("ROOT",test4.getName());
        Capability domain4 = test4.getSubCapabilities().iterator().next();  
        Capability l0_4 = domain4.getSubCapabilities().iterator().next();
        Assert.assertEquals(2,l0_4.getSubCapabilities().size());
        Capability sub = getCapaByMName(l0_4.getSubCapabilities(),"L1.1.1");
        Assert.assertEquals(1, sub.getSubCapabilities().size());
        sub = getCapaByMName(l0_4.getSubCapabilities(),"L1.1.2");
        Assert.assertEquals(0, sub.getSubCapabilities().size());
    }

    private void checkSize(Capability test1, int[] sizes) {
        for (int i = 0; i < sizes.length; i++) {
            System.out.println(sizes[i] + " : " + test1.getName() + " " + test1.getSubCapabilities());
            Assert.assertEquals(sizes[i], test1.getSubCapabilities().size());
            if (test1.getSubCapabilities().iterator().hasNext()) {
                test1 = test1.getSubCapabilities().iterator().next();
            }
            else {
                test1 = null;
            }
        }
    }

    private Capability createChild(Capability parent, String name, int level) {
        Capability capability = new Capability(name,level,null);
        capability.setId(index++);
        if (parent!=null) {
            parent.addSubCapabilities(capability);
        }
        capabilitiesMap.put(name, capability);
        return capability;
    }

    private Capability getCapaByMName(Collection<Capability> capabilities, String name) {
        Capability result = null;
        for (Capability capability : capabilities) {
            if (name.equals(capability.getName())) return capability;
        }
        return result;
    }
}
