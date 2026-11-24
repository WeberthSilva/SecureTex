# SecureText — versão simples (sem Maven)

Projeto Java de terminal, sem Maven, Gradle ou bibliotecas externas.

## Requisito
- JDK 17 ou superior.

## Estrutura
```text
SecureText-Simples/
├── README.md
└── src/main/java/br/com/securetext/
    ├── app/Main.java
    ├── controller/MenuController.java
    ├── exception/ErroCriptografiaException.java
    ├── model/ResultadoCriptografia.java
    ├── service/CriptografiaService.java
    ├── service/HashService.java
    └── util/ConversorUtil.java
        EntradaConsole.java
```

## Compilar e executar
Abra o terminal na pasta `SecureText-Simples`.

### Windows (PowerShell)
```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out br.com.securetext.app.Main
```

### Linux / macOS
```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out br.com.securetext.app.Main
```

## Funcionalidades
- Criptografia e descriptografia com AES-GCM.
- Derivação de chave por senha usando PBKDF2WithHmacSHA256.
- Geração e comparação de hash SHA-256.

## Observações
- Não existe `pom.xml` e não há dependências externas.
- O projeto utiliza classes nativas do Java.
- Salt e IV são gerados aleatoriamente e guardados junto ao texto criptografado.
- SHA-256 é uma função hash, não uma criptografia reversível.
