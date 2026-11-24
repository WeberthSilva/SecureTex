package br.com.securetext.util;

import java.util.Scanner;

/**
 * Centraliza a leitura de dados digitados no terminal.
 */
public class EntradaConsole {

    private final Scanner scanner;

    public EntradaConsole() {
        this.scanner = new Scanner(System.in);
    }

    public String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    /**
     * Continua solicitando a opção até que um número inteiro seja informado.
     */
    public int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine();

            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
            }
        }
    }

    public void fecharScanner() {
        scanner.close();
    }
}
