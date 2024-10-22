package fr.nelson.you_are_the_hero.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class ApiKeyServiceTest {
    @InjectMocks
    ApiKeyService apiKeyService;

    @Test
    public void testIsValidApiKey_ValidKey() {
        String validKey = "this_is_a_256_bit_key_12345678";
        apiKeyService.setAdminSecretKey(validKey);
        assertTrue(apiKeyService.isValidApiKey(validKey));
    }

    @Test
    public void testIsValidApiKey_InvalidKey() {
        String validKey = "this_is_a_256_bit_key_12345678";
        apiKeyService.setAdminSecretKey(validKey);
        assertFalse(apiKeyService.isValidApiKey("invalid_key"));
    }

    @Test
    public void testIsComformApiKey_ValidKey() {
        String validKey = "xG1N1N2KXOT2UeF1Y9XkZGRjMT1gE2LR2I8Y6H3y5qA=";
        apiKeyService.setAdminSecretKey(validKey);
        assertTrue(apiKeyService.isComformApiKey());
    }

    @Test
    public void testIsComformApiKey_NullKey() {
        apiKeyService.setAdminSecretKey(null);
        assertFalse(apiKeyService.isComformApiKey());
    }

    @Test
    public void testIsComformApiKey_EmptyKey() {
        apiKeyService.setAdminSecretKey("");
        assertFalse(apiKeyService.isComformApiKey());
    }

    @Test
    public void testIsComformApiKey_InvalidBase64() {
        apiKeyService.setAdminSecretKey("invalid_base64_key");
        assertFalse(apiKeyService.isComformApiKey());
    }
}
