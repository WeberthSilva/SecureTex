package br.com.securetext.exception;

/**
 * Exceção específica para erros nas operações de criptografia.
 */
public class ErroCriptografiaException extends Exception {

    public ErroCriptografiaException(String mensagem) {
        super(mensagem);
    }

    public ErroCriptografiaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
