package com.es.phoneshop.service.impl.secure;

import com.es.phoneshop.service.security.DosProtectionService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long RESET_TIME = 60;
    private static final long THRESHOLD = 30;

    private final Timer timer;

    private Map<String, Long> countMap = new ConcurrentHashMap<>();

    private DefaultDosProtectionService() {
        timer = new Timer();
        timer.scheduleAtFixedRate(getTimerTask(), 1000, RESET_TIME * 1000);
    }

    public static DosProtectionService getInstance() {
        return Holder.dosService;
    }

    private static class Holder {
        private static final DosProtectionService dosService = new DefaultDosProtectionService();
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.getOrDefault(ip, 0L);
        countMap.put(ip, ++count);
        return count <= THRESHOLD;
    }

    @Override
    public void shutdown() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                countMap.clear();
                System.out.printf("[DOSFilter][%s] - map cleared%n", LocalTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_TIME));
            }
        };
    }

}
