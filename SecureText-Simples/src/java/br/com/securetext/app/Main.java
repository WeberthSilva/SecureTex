package br.com.securetext.app;

import br.com.securetext.controller.MenuController;
import br.com.securetext.service.CriptografiaService;
import br.com.securetext.service.HashService;
import br.com.securetext.util.EntradaConsole;

/**
 * Ponto de entrada da aplicação SecureText.
 */
public class Main {

    public static void main(String[] args) {
        EntradaConsole entrada = new EntradaConsole();
        CriptografiaService criptografiaService = new CriptografiaService();
        HashService hashService = new HashService();

        MenuController menuController =
                new MenuController(entrada, criptografiaService, hashService);

        try {
            menuController.iniciar();
        } finally {
            entrada.fecharScanner();
        }
    }
}
