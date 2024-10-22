package fr.nelson.you_are_the_hero.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class ApiKeyService {
    @Value("${admin.secret.key}")
    private String adminSecretKey;

    public void setAdminSecretKey(String adminSecretKey) {
        this.adminSecretKey = adminSecretKey;
    }

    public boolean isValidApiKey(String key){
        return adminSecretKey.equals(key);
    }

    public boolean isComformApiKey() {
        if (adminSecretKey == null || adminSecretKey.isEmpty()) {
            return false;
        }

        try {
            byte[] decodedKey = Base64.getDecoder().decode(adminSecretKey);
            return decodedKey.length == 32;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
