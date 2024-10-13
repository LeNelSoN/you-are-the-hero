package fr.nelson.you_are_the_hero.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FailedAttemptService {

    private final Map<String, Integer> attemptsCache = new HashMap<>();
    private final Map<String, Long> blockedUsers = new HashMap<>();
    @Value("${admin.try.attempts}")
    private int maxAttempts;
    @Value("${block.duration}")
    private long blockTime;

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public void setBlockTime(long blockTime) {
        this.blockTime = blockTime;
    }

    public void registerFailedAttempt(String userIp) {
        int attempts = attemptsCache.getOrDefault(userIp, 0) + 1;
        attemptsCache.put(userIp, attempts);

        if (attempts >= maxAttempts) {
            blockedUsers.put(userIp, System.currentTimeMillis() + blockTime);
            attemptsCache.remove(userIp);
        }
    }

    public boolean isBlocked(String userIp) {
        Long blockedUntil = blockedUsers.get(userIp);
        if (blockedUntil == null) {
            return false;
        }

        if (System.currentTimeMillis() > blockedUntil) {
            blockedUsers.remove(userIp);
            return false;
        }
        return true;
    }

    public void resetAttempts(String userIp) {
        attemptsCache.remove(userIp);
    }
}

