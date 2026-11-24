package br.com.securetext.service;

import br.com.securetext.exception.ErroCriptografiaException;
import br.com.securetext.model.ResultadoCriptografia;
import br.com.securetext.util.ConversorUtil;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * Implementa criptografia autenticada AES-GCM e derivação de chave por PBKDF2.
 */
public class CriptografiaService {

    private static final String ALGORITMO = "AES/GCM/NoPadding";
    private static final String ALGORITMO_DERIVACAO = "PBKDF2WithHmacSHA256";
    private static final int TAMANHO_SALT = 16;
    private static final int TAMANHO_IV = 12;
    private static final int ITERACOES = 210_000;
    private static final int TAMANHO_CHAVE_BITS = 256;
    private static final int TAMANHO_TAG_BITS = 128;

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Gera salt e IV novos para cada operação e criptografa a mensagem.
     */
    public ResultadoCriptografia criptografar(String mensagem, String senha)
            throws ErroCriptografiaException {

        validarEntrada(mensagem, senha);

        byte[] salt = gerarSalt();
        byte[] iv = gerarIv();
        byte[] chaveBytes = null;

        try {
            chaveBytes = derivarChave(senha, salt);

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            SecretKeySpec chave = new SecretKeySpec(chaveBytes, "AES");
            GCMParameterSpec parametros = new GCMParameterSpec(TAMANHO_TAG_BITS, iv);

            cipher.init(Cipher.ENCRYPT_MODE, chave, parametros);

            byte[] textoCifrado = cipher.doFinal(
                    mensagem.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            return new ResultadoCriptografia(
                    ConversorUtil.bytesParaBase64(textoCifrado),
                    ConversorUtil.bytesParaBase64(salt),
                    ConversorUtil.bytesParaBase64(iv)
            );

        } catch (GeneralSecurityException e) {
            throw new ErroCriptografiaException(
                    "Falha durante a criptografia.", e);
        } finally {
            limpar(chaveBytes);
        }
    }

    /**
     * Recupera o texto original. Senha incorreta ou dados adulterados
     * fazem a autenticação GCM falhar.
     */
    public String descriptografar(ResultadoCriptografia resultado, String senha)
            throws ErroCriptografiaException {

        if (resultado == null) {
            throw new ErroCriptografiaException("O resultado criptografado não foi informado.");
        }
        if (senha == null || senha.isBlank()) {
            throw new ErroCriptografiaException("A senha não pode estar vazia.");
        }

        byte[] salt = null;
        byte[] iv = null;
        byte[] textoCifrado = null;
        byte[] chaveBytes = null;

        try {
            salt = ConversorUtil.base64ParaBytes(resultado.getSalt());
            iv = ConversorUtil.base64ParaBytes(resultado.getIv());
            textoCifrado = ConversorUtil.base64ParaBytes(
                    resultado.getTextoCriptografado());

            if (salt.length != TAMANHO_SALT || iv.length != TAMANHO_IV) {
                throw new ErroCriptografiaException("Salt ou IV possui tamanho inválido.");
            }

            chaveBytes = derivarChave(senha, salt);

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            SecretKeySpec chave = new SecretKeySpec(chaveBytes, "AES");
            GCMParameterSpec parametros = new GCMParameterSpec(TAMANHO_TAG_BITS, iv);

            cipher.init(Cipher.DECRYPT_MODE, chave, parametros);
            byte[] textoOriginal = cipher.doFinal(textoCifrado);

            return new String(textoOriginal,
                    java.nio.charset.StandardCharsets.UTF_8);

        } catch (AEADBadTagException e) {
            throw new ErroCriptografiaException(
                    "Senha incorreta ou conteúdo criptografado alterado.", e);
        } catch (IllegalArgumentException e) {
            throw new ErroCriptografiaException(
                    "Os dados informados não estão em um formato Base64 válido.", e);
        } catch (GeneralSecurityException e) {
            throw new ErroCriptografiaException(
                    "Falha durante a descriptografia.", e);
        } finally {
            limpar(chaveBytes);
        }
    }

    public boolean validarRecuperacao(String original, String recuperado) {
        return original != null && original.equals(recuperado);
    }

    /**
     * Gera um salt aleatório de 16 bytes.
     */
    private byte[] gerarSalt() {
        byte[] salt = new byte[TAMANHO_SALT];
        secureRandom.nextBytes(salt);
        return salt;
    }

    /**
     * Gera um IV aleatório de 12 bytes, tamanho recomendado para GCM.
     */
    private byte[] gerarIv() {
        byte[] iv = new byte[TAMANHO_IV];
        secureRandom.nextBytes(iv);
        return iv;
    }

    /**
     * Deriva uma chave AES de 256 bits a partir da senha e do salt.
     */
    private byte[] derivarChave(String senha, byte[] salt)
            throws GeneralSecurityException {

        PBEKeySpec especificacao = new PBEKeySpec(
                senha.toCharArray(),
                salt,
                ITERACOES,
                TAMANHO_CHAVE_BITS
        );

        try {
            SecretKeyFactory fabrica =
                    SecretKeyFactory.getInstance(ALGORITMO_DERIVACAO);
            return fabrica.generateSecret(especificacao).getEncoded();
        } finally {
            especificacao.clearPassword();
        }
    }

    private void validarEntrada(String mensagem, String senha)
            throws ErroCriptografiaException {

        if (mensagem == null || mensagem.isBlank()) {
            throw new ErroCriptografiaException("A mensagem não pode estar vazia.");
        }
        if (senha == null || senha.isBlank()) {
            throw new ErroCriptografiaException("A senha não pode estar vazia.");
        }
    }

    private void limpar(byte[] dados) {
        if (dados != null) {
            Arrays.fill(dados, (byte) 0);
        }
    }
}
