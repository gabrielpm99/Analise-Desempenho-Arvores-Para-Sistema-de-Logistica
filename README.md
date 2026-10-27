# Análise do desempenho de árvores para sistema de logística

![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)

## Sumário

- [Introdução](#introdução)
- [Dependências](#dependências)
- [Estrutura dos arquivos](#estrutura-dos-arquivos)
- [Execução do projeto](#execução-do-projeto)
- [Funcionalidades implementadas](#funcionalidades-implementadas)
- [Exemplo de uso](#exemplo-de-uso)
- [Comentários sobre o funcionamento](#comentários-sobre-o-funcionamento)
- [Resultados Experimentais](#resultados-experimentais)
- [Autor do trabalho](#autor-do-trabalho)
- [Referências Bibliográficas](#referências-bibliográficas)

---

## Introdução

Trabalho de implementação e análise de árvores balanceadas **AVL** e **Rubro-Negra (LLRB)** referente à disciplina de **Estruturas de Dados Básicas II (EDBII)** do curso de **BTI/UFRN**.

O projeto simula um sistema de logística para o contexto de tráfego intenso da **Black Friday da Amazon**, no qual é necessário registrar, localizar e remover identificadores de encomendas de maneira eficiente.

O objetivo principal é implementar as duas estruturas de dados, analisar seu comportamento e comparar seu desempenho sob alta carga (centenas de milhares de operações), permitindo avaliar qual estrutura apresenta melhores características para o cenário proposto.

---

## Dependências

| Item | Requisito |
|---|---|
| **Java** | **JDK 8 ou superior** |
| **Bibliotecas externas** | Apenas as bibliotecas padrão do Java (`java.util`, `java.io`) |
| **Terminal** | Suporte a UTF-8 para exibição correta da representação geométrica das árvores |

### Observações sobre o terminal

- **Windows:** recomenda-se utilizar o Windows Terminal, PowerShell moderno ou o console integrado do VS Code.
- Caso os caracteres de desenho (`┌ ─ ┴ ┐ │`) sejam exibidos incorretamente, configure o seu terminal para utilizar codificação **UTF-8**.
- A representação visual geométrica (*Top-Down*) utiliza caracteres Unicode para garantir o alinhamento dos nós.

---

## Estrutura dos arquivos

```text
Trabalho-de-EDBII/
├── src/
│   ├── No.java
│   ├── NoAVL.java
│   ├── NoRN.java
│   ├── Arvore.java
│   ├── AVL.java
│   └── RN.java
├── Main.java
├── bin/                        (criada pela compilação)
├── README.md
└── arvores.jar                 (versão compilada e executável)
```

| Arquivo | Descrição |
|---|---|
| `Main.java` | Classe principal. Contém o menu interativo, validação de inserções em lote, testes randômicos e medição analítica de tempos. |
| `src/No.java` | Classe base `No`, responsável por armazenar o valor do nó e as referências aos filhos. |
| `src/NoAVL.java` | Especialização utilizada pela árvore AVL. Acrescenta a variável métrica de altura do nó. |
| `src/NoRN.java` | Especialização utilizada pela árvore Rubro-Negra. Acrescenta o enumerador de cor e a referência para o nó pai. |
| `src/Arvore.java` | Classe abstrata que define as operações comuns (busca e percurso em ordem) e contém o algoritmo de renderização visual em caixas de texto (*Top-Down*). |
| `src/AVL.java` | Implementação da árvore AVL, com cálculo do fator de balanceamento e aplicação das rotações LL, RR, LR e RL. |
| `src/RN.java` | Implementação da variante LLRB (*Left-Leaning Red-Black Tree*), com regras de inclinação à esquerda e inversões de cor. |

---

## Execução do projeto

### Opção 1 — Executável `.jar` (Recomendado)

```bash
# 1) Clonar o projeto
git clone [Cole aqui o link do seu repositório]

# 2) Entrar na pasta do projeto
cd [Nome da pasta do repositório]

# 3) Executar o programa
java -jar arvores.jar
```

### Opção 2 — Compilar e executar do zero

```bash
# 1) Clonar o projeto
git clone [Cole aqui o link do seu repositório]

# 2) Entrar na pasta do projeto
cd [Nome da pasta do repositório]

# 3) Criar o diretório de compilação e compilar
mkdir -p bin
javac -encoding UTF-8 -d bin src/*.java Main.java

# 4) Gerar o arquivo JAR
jar cfe arvores.jar Main -C bin .

# 5) Executar o programa
java -jar arvores.jar
```

> **Observação:** no Windows, o comando `mkdir -p bin` pode ser substituído por `mkdir bin` caso a pasta não exista.

---

## Funcionalidades implementadas

### Menu Unificado (AVL e Rubro-Negra LLRB)

O programa executa as operações simultaneamente em instâncias limpas de ambas as árvores, permitindo comparações diretas de desempenho.

### Inserção e Remoção Avançada

Suporte a processamento em lote. O usuário pode inserir múltiplos valores separados por espaços em uma única operação.

### Busca Cronometrada

Retorna o status de presença do identificador e o tempo despendido por cada árvore, medido em milissegundos.

### Visualização da Estrutura (Top-Down)

Imprime a árvore utilizando linhas retas para representar suas conexões. Na Rubro-Negra, as cores dos nós são evidenciadas por `(V)` ou `(P)`.

### Geração de Arquivos Automática (Proteção de Terminal)

Sempre que a árvore exceder 10 nós, o sistema bloqueia a impressão massiva no terminal para evitar problemas de visualização.

Em vez disso, sugere a exportação do conteúdo para arquivos `.txt` formatados:

- `percurso.txt`
- `estrutura_arvore.txt`

### Teste Randômico de Estresse

O menu (opção 8) automatiza centenas de milhares de inserções, buscas e remoções, extraindo quadros completos de métricas comparativas.

---

## Exemplo de uso

### Sessão interativa via terminal

```text
1. Inserir elemento(s)
2. Remover elemento(s)
3. Buscar elemento
4. Percurso em ordem
5. Imprimir estrutura da arvore
...

Opcao: 1
Quantidade de elementos: 3
Digite os 3 valores separados por espaco: 50 30 70

  [Insercao de 3 elemento(s)]
  Arvore  Altura    FB Raiz    Rotacoes    Comparacoes   Trocas Cor    Tempo (ms)
  -------------------------------------------------------------------------------
  AVL     2         0          0           3             N/A           -
  RN      2         0          0           3             0             -

Opcao: 5

  ARVORE AVL:

      ┌───(50)───┐
      │          │
    (30)       (70)

  ARVORE RUBRO-NEGRA:

      ┌───(50(P))───┐
      │             │
   (30(V))       (70(P))
```

---

## Comentários sobre o funcionamento

Em estrita conformidade com os requisitos da disciplina, a implementação não utiliza estruturas de dados ou algoritmos de árvores balanceadas fornecidos por bibliotecas externas.

### AVL

A implementação mantém uma variável de altura individual em cada nó. Após uma inserção ou remoção, ao retornar recursivamente pelo caminho percorrido, o código atualiza as alturas e verifica o balanceamento de cada nó.

O fator de balanceamento é calculado por:

```text
FB = altura(esq) - altura(dir)
```

Para uma árvore AVL balanceada, deve ser satisfeita a condição:

```text
-1 ≤ FB ≤ 1
```

Caso `|FB| > 1`, são aplicadas rotações para restaurar o balanceamento.

Os quatro casos possíveis são:

| Caso | Operação |
|---|---|
| **LL** | Rotação simples à direita |
| **RR** | Rotação simples à esquerda |
| **LR** | Rotação à esquerda seguida de rotação à direita |
| **RL** | Rotação à direita seguida de rotação à esquerda |

Cada rotação individual possui complexidade **O(1)**. Como a altura da árvore AVL é **O(log n)**, as operações de busca, inserção e remoção apresentam complexidade **O(log n)**.

A árvore mantém um balanceamento mais rígido, o que contribui para uma menor altura global.

### Rubro-Negra (LLRB)

Segue os preceitos da **LLRB (*Left-Leaning Red-Black Tree*)**, formulada por **Robert Sedgewick**, utilizando a correspondência com árvores 2-3.

Na inserção, um novo nó é inicialmente criado na cor vermelha. Durante o retorno da recursão, são realizadas correções para preservar as propriedades da estrutura.

As principais correções são:

1. **Nó vermelho à direita:** realiza uma **rotação à esquerda**, mantendo a propriedade *Left-Leaning*.
2. **Dois nós vermelhos consecutivos à esquerda:** realiza uma **rotação à direita**.
3. **Ambos os filhos vermelhos:** realiza uma **inversão de cores**, dividindo o agrupamento correspondente a um nó 4.

Assim como na AVL, a altura da estrutura permanece **O(log n)**, garantindo operações de busca, inserção e remoção com complexidade **O(log n)**.

---

## Resultados Experimentais

A ferramenta randômica integrada foi submetida a um teste massivo (**Cenário Black Friday**) na máquina do autor (**Processador Ryzen 5 5600GT / 16GB RAM**) para validar empiricamente a eficiência das estruturas perante picos de acesso logístico.

### Cenário de Estresse: 500.000 Inserções (Registros de Encomendas)

| Métrica | Árvore AVL | Árvore Rubro-Negra (LLRB) |
|---|---:|---:|
| **Altura Final** | 23 | 26 |
| **Rotações Efetuadas** | 331.010 | 563.601 |
| **Comparações Executadas** | 48.464.664 | 13.082.668 |
| **Trocas de Cor** | N/A | 2.191.461 |

### Análise

Apesar da AVL garantir uma estrutura de altura mais enxuta e realizar menos rotações estruturais, a Rubro-Negra LLRB mostrou-se extraordinariamente superior na navegação lógica durante a estruturação de grandes volumes.

No pico de **500 mil registros**, a AVL exigiu mais de **48 milhões de comparações**, enquanto a LLRB resolveu o mesmo mapeamento logístico com apenas **13 milhões de comparações**, consolidando sua eficiência computacional como a escolha recomendada para a recepção de dados da Amazon no contexto proposto.

---

## Autor do trabalho

**Gabriel Pinheiro Maia**

**Matrícula:** 20250036420

---

## Referências Bibliográficas

**[1]** ADELSON-VELSKY, G. M.; LANDIS, E. M. *An algorithm for the organization of information*. Proceedings of the USSR Academy of Sciences, 146, 263-266, 1962.

**[2]** SEDGEWICK, Robert. *Left-leaning Red-Black Trees*. Department of Computer Science, Princeton University, 2008.

**[3]** GUIBAS, Leonidas J.; SEDGEWICK, Robert. *A dichromatic framework for balanced trees*. In: 19th Annual Symposium on Foundations of Computer Science (FOCS). IEEE, 1978. p. 8-21.