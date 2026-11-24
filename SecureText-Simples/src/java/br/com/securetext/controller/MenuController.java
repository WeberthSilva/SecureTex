package br.com.securetext.controller;

import br.com.securetext.exception.ErroCriptografiaException;
import br.com.securetext.model.ResultadoCriptografia;
import br.com.securetext.service.CriptografiaService;
import br.com.securetext.service.HashService;
import br.com.securetext.util.EntradaConsole;

/**
 * Controla o menu e encaminha as operações para os serviços especializados.
 */
public class MenuController {

    private final EntradaConsole entrada;
    private final CriptografiaService criptografiaService;
    private final HashService hashService;
    private boolean executando;

    public MenuController(EntradaConsole entrada,
                          CriptografiaService criptografiaService,
                          HashService hashService) {
        this.entrada = entrada;
        this.criptografiaService = criptografiaService;
        this.hashService = hashService;
        this.executando = true;
    }

    public void iniciar() {
        System.out.println("======================================");
        System.out.println("             SECURETEXT");
        System.out.println("       Criptografia e Hash");
        System.out.println("======================================");

        while (executando) {
            exibirMenu();
            int opcao = entrada.lerInteiro("Escolha uma opção: ");
            executarOpcao(opcao);
        }
    }

    public void exibirMenu() {
        System.out.println();
        System.out.println("--------------- MENU ----------------");
        System.out.println("1. Criptografia e descriptografia");
        System.out.println("2. Geração e comparação de Hash");
        System.out.println("0. Sair");
        System.out.println("--------------------------------------");
    }

    public void executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> executarCriptografia();
            case 2 -> executarComparacaoHash();
            case 0 -> encerrarSistema();
            default -> System.out.println("Opção inválida. Tente novamente.");
        }
    }

    /**
     * Criptografa uma mensagem e, em seguida, demonstra sua recuperação.
     */
    public void executarCriptografia() {
        System.out.println("\n--- CRIPTOGRAFIA E DESCRIPTOGRAFIA ---");

        String mensagem = entrada.lerTexto("Digite a mensagem: ");
        if (mensagem.isBlank()) {
            System.out.println("A mensagem não pode estar vazia.");
            return;
        }

        String senha = entrada.lerTexto("Defina uma senha: ");
        if (senha.isBlank()) {
            System.out.println("A senha não pode estar vazia.");
            return;
        }

        try {
            ResultadoCriptografia resultado =
                    criptografiaService.criptografar(mensagem, senha);

            System.out.println("\nMensagem original: " + mensagem);
            System.out.println("Texto criptografado (Base64):");
            System.out.println(resultado.getTextoCriptografado());
            System.out.println("\nSalt (Base64): " + resultado.getSalt());
            System.out.println("IV (Base64): " + resultado.getIv());

            System.out.println("\n--- DESCRIPTOGRAFIA ---");
            String senhaDigitada =
                    entrada.lerTexto("Digite novamente a senha para descriptografar: ");

            String mensagemRecuperada =
                    criptografiaService.descriptografar(resultado, senhaDigitada);

            System.out.println("Mensagem recuperada: " + mensagemRecuperada);

            boolean recuperacaoCorreta =
                    criptografiaService.validarRecuperacao(mensagem, mensagemRecuperada);

            if (recuperacaoCorreta) {
                System.out.println("Verificação: sucesso! O texto original foi recuperado.");
            } else {
                System.out.println("Verificação: os textos são diferentes.");
            }

        } catch (ErroCriptografiaException e) {
            System.out.println("Não foi possível concluir a operação: " + e.getMessage());
        }
    }

    /**
     * Gera SHA-256 para duas mensagens e compara os resultados.
     */
    public void executarComparacaoHash() {
        System.out.println("\n--- GERAÇÃO E COMPARAÇÃO DE HASH ---");

        String mensagem1 = entrada.lerTexto("Digite a primeira mensagem: ");
        String hash1 = hashService.gerarHash(mensagem1);

        System.out.println("\nHash SHA-256 da primeira mensagem:");
        System.out.println(hash1);

        String mensagem2 = entrada.lerTexto("\nDigite a segunda mensagem: ");
        String hash2 = hashService.gerarHash(mensagem2);

        System.out.println("\nHash SHA-256 da segunda mensagem:");
        System.out.println(hash2);

        System.out.println("\n--- COMPARAÇÃO ---");
        if (hashService.compararHashes(hash1, hash2)) {
            System.out.println("Os hashes são IGUAIS.");
            System.out.println("As mensagens possuem o mesmo conteúdo.");
        } else {
            System.out.println("Os hashes são DIFERENTES.");
            System.out.println("As mensagens possuem conteúdos diferentes.");
        }
    }

    public void encerrarSistema() {
        executando = false;
        System.out.println("\nSistema encerrado.");
    }
}
