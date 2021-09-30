package com.es.phoneshop.service.security;

public interface DosProtectionService {
    boolean isAllowed(String ip);
    void shutdown();
}
