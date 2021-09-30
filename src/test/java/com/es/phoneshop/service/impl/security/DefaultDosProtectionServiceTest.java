package com.es.phoneshop.service.impl.security;

import com.es.phoneshop.service.impl.secure.DefaultDosProtectionService;
import com.es.phoneshop.service.security.DosProtectionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDosProtectionServiceTest {

    @Spy
    private ConcurrentHashMap<String, Long> countMap;

    private static final String TEST_IP1 = "127.0.0.1";
    private static final String TEST_IP2 = "127.0.0.2";

    @InjectMocks
    private final DosProtectionService dosProtectionService = DefaultDosProtectionService.getInstance();

    @Test
    public void testAllowedIp() {
        countMap.put(TEST_IP1, 29L);
        assertTrue(dosProtectionService.isAllowed(TEST_IP1));
    }

    @Test
    public void testBlockedIp() {
        countMap.put(TEST_IP1, 31L);
        assertFalse(dosProtectionService.isAllowed(TEST_IP1));
    }

    @Test
    public void testMapClearingByTimer() throws InterruptedException {
        countMap.put(TEST_IP1, 31L);
        Thread.sleep(1000);
        assertTrue(dosProtectionService.isAllowed(TEST_IP1));
    }

    @Test
    public void testAllowedIpWhenSomeoneIsBlocked() {
        countMap.put(TEST_IP1, 31L);
        assertTrue(dosProtectionService.isAllowed(TEST_IP2));
    }

}
