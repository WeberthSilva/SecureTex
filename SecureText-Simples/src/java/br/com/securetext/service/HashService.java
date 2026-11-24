package br.com.securetext.service;

import br.com.securetext.util.ConversorUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Gera e compara hashes SHA-256.
 */
public class HashService {

    private static final String ALGORITMO = "SHA-256";

    /**
     * Gera o hash SHA-256 de uma mensagem e retorna 64 caracteres hexadecimais.
     */
    public String gerarHash(String mensagem) {
        if (mensagem == null) {
            throw new IllegalArgumentException("A mensagem não pode ser nula.");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITMO);
            byte[] hash = digest.digest(mensagem.getBytes(StandardCharsets.UTF_8));
            return ConversorUtil.bytesParaHexadecimal(hash);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 faz parte das implementações padrão do Java.
            throw new IllegalStateException("SHA-256 não está disponível nesta JVM.", e);
        }
    }

    /**
     * Compara dois códigos hash.
     */
    public boolean compararHashes(String hash1, String hash2) {
        if (hash1 == null || hash2 == null) {
            return false;
        }
        return MessageDigest.isEqual(
                hash1.getBytes(StandardCharsets.UTF_8),
                hash2.getBytes(StandardCharsets.UTF_8)
        );
    }
}
