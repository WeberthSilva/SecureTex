package br.com.securetext.util;

import java.util.Base64;
import java.util.HexFormat;

/**
 * Reúne conversões utilizadas pelos serviços criptográficos.
 */
public final class ConversorUtil {

    private ConversorUtil() {
        // Classe utilitária: não deve ser instanciada.
    }

    public static String bytesParaHexadecimal(byte[] dados) {
        return HexFormat.of().formatHex(dados);
    }

    public static String bytesParaBase64(byte[] dados) {
        return Base64.getEncoder().encodeToString(dados);
    }

    public static byte[] base64ParaBytes(String dados) {
        return Base64.getDecoder().decode(dados);
    }

    public static byte[] hexadecimalParaBytes(String dados) {
        return HexFormat.of().parseHex(dados);
    }
}
