package br.com.securetext.model;

/**
 * Armazena os dados necessários para descriptografar uma mensagem.
 * Salt e IV não são secretos, mas precisam ser preservados.
 */
public class ResultadoCriptografia {

    private final String textoCriptografado;
    private final String salt;
    private final String iv;

    public ResultadoCriptografia(String textoCriptografado, String salt, String iv) {
        if (textoCriptografado == null || salt == null || iv == null) {
            throw new IllegalArgumentException("Os dados do resultado não podem ser nulos.");
        }
        this.textoCriptografado = textoCriptografado;
        this.salt = salt;
        this.iv = iv;
    }

    public String getTextoCriptografado() {
        return textoCriptografado;
    }

    public String getSalt() {
        return salt;
    }

    public String getIv() {
        return iv;
    }
}
