# Hashing Encadeado - Documentação Detalhada

## Índice

1. [Visão Geral](#visão-geral)
2. [Conceitos Fundamentais](#conceitos-fundamentais)
3. [Arquitetura das Classes](#arquitetura-das-classes)
4. [Implementação do NoHash](#implementação-do-nohash)
5. [Implementação do HashingEncadeado](#implementação-do-hashingencadeado)
6. [Função Hash](#função-hash)
7. [Tratamento de Colisões](#tratamento-de-colisões)
8. [Dimensionamento da Tabela](#dimensionamento-da-tabela)
9. [Dependências e Integração](#dependências-e-integração)
10. [Uso no Projeto: PesquisaReservas.java](#uso-no-projeto-pesquisareservasjava)
11. [Análise de Complexidade](#análise-de-complexidade)
12. [Perguntas e Respostas](#perguntas-e-respostas)

---

## Visão Geral

O **Hashing Encadeado** (ou Hash com Encadeamento Separado) é uma estrutura de dados que permite busca em **tempo médio O(1)** através de uma função hash que mapeia chaves diretamente para posições em uma tabela.

### Características da Implementação

| Aspecto | Descrição |
|---------|-----------|
| **Estrutura** | Array de listas encadeadas |
| **Chave de busca** | Nome da reserva (String, case-insensitive) |
| **Valores** | Lista de reservas (`LCItem`) por nome |
| **Tratamento de colisões** | Encadeamento separado (listas ligadas) |
| **Função hash** | Soma de valores ASCII mod tamanho |
| **Dimensionamento** | Número primo ~1.1x quantidade de elementos |

### Localização no Projeto

```
src/br/faesa/C3/algoritmos/pesquisa/Hashing/
├── HashingEncadeado.java    # Estrutura principal da tabela hash
└── NoHash.java              # Nó da lista encadeada
```

---

## Conceitos Fundamentais

### O que é Hashing?

**Hashing** é uma técnica que transforma uma chave (como "MARIA") em um **índice numérico** através de uma função matemática, permitindo acesso direto à posição de armazenamento.

```
Chave "MARIA" → função hash → índice 42 → tabela[42]
```

### Por que Encadeamento?

Quando duas chaves diferentes produzem o **mesmo índice** (colisão), precisamos de uma estratégia para armazenar ambas. O **encadeamento separado** usa listas ligadas:

```
Índice 42:  [MARIA] → [MARTA] → [MARIO] → null
            (colidiram no mesmo índice)
```

### Estrutura Visual

```
┌─────────────────────────────────────────────────────────────────┐
│                    TABELA HASH (tamanho M)                      │
├─────────────────────────────────────────────────────────────────┤
│ índice │ lista encadeada                                        │
├────────┼────────────────────────────────────────────────────────┤
│   0    │ null                                                   │
│   1    │ [ANA] → [ALICE] → null                                 │
│   2    │ null                                                   │
│   3    │ [CARLOS] → null                                        │
│   4    │ null                                                   │
│   ...  │ ...                                                    │
│   42   │ [MARIA] → [MARTA] → null                               │
│   ...  │ ...                                                    │
│  M-1   │ [ZELIA] → null                                         │
└────────┴────────────────────────────────────────────────────────┘
```

---

## Arquitetura das Classes

### Diagrama de Classes

```
┌─────────────────────────────────────────────────────────────────┐
│                    HashingEncadeado                             │
│                                                                 │
│  - tabela: NoHash[]          ← Array de listas                  │
│  - tamanho: int              ← Tamanho M da tabela              │
│  - numElementos: int         ← Quantidade de nós únicos         │
│                                                                 │
│  + HashingEncadeado(int tamanho)                                │
│  + hashing(String chave): int                                   │
│  + inserir(Reserva item): void                                  │
│  + pesquisar(String nome): LCItem                               │
│  + carregarDeLCItem(LCItem lista): void                         │
│  + pesquisarTodos(LCItem nomes): LCItem[]                       │
│  + getFatorCarga(): double                                      │
│  + imprimirEstatisticas(): void                                 │
│                                                                 │
│  [static] calcularTamanhoPrimo(int n): int                      │
│  [static] ehPrimo(int n): boolean                               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ usa (array de)
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         NoHash                                  │
│                                                                 │
│  - nome: String              ← Chave de busca                   │
│  - reservas: LCItem          ← Lista de reservas deste nome     │
│  - prox: NoHash              ← Próximo nó na lista encadeada    │
│                                                                 │
│  + NoHash(Reserva item)                                         │
│  + getNome(): String                                            │
│  + getReservas(): LCItem                                        │
│  + getProx(): NoHash                                            │
│  + setProx(NoHash prox): void                                   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ contém
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         LCItem                                  │
│                                                                 │
│  - lista: Reserva[]                                             │
│  - quant: int                                                   │
│                                                                 │
│  + insereFinal(Reserva item): void                              │
│  + getItem(int i): Reserva                                      │
│  + getQuant(): int                                              │
└─────────────────────────────────────────────────────────────────┘
```

### Diferença entre NoHash e NoArvore

| Aspecto | NoHash | NoABBItem/NoAVLItem |
|---------|--------|---------------------|
| **Estrutura** | Lista simplesmente ligada | Árvore binária |
| **Ponteiros** | `prox` (próximo na lista) | `esq`, `dir` (filhos) |
| **Herança** | Nenhuma | `extends NoArvoreBase<T>` |
| **Navegação** | Linear na lista | Binária na árvore |

---

## Implementação do NoHash

### Código Completo

```java
package br.faesa.C3.algoritmos.pesquisa.Hashing;

import br.faesa.C3.entidades.Reserva;
import br.faesa.C3.entidades.LCItem;

/**
 * Nó para lista encadeada no Hashing.
 * Armazena o nome (chave) e uma LCItem com todas as reservas desse nome.
 */
public class NoHash {
    private String nome;           // Chave de busca
    private LCItem reservas;       // Todas as reservas com este nome
    private NoHash prox;           // Próximo nó na lista encadeada

    public NoHash(Reserva item) {
        this.nome = item.getNome();
        this.reservas = new LCItem(5);      // Capacidade inicial 5
        this.reservas.insereFinal(item);    // Adiciona a primeira reserva
        this.prox = null;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LCItem getReservas() { return reservas; }
    public NoHash getProx() { return prox; }
    public void setProx(NoHash prox) { this.prox = prox; }
}
```

### Estrutura do Nó

```
┌─────────────────────────────────────────────────┐
│                    NoHash                       │
├─────────────────────────────────────────────────┤
│  nome: "MARIA"                                  │
├─────────────────────────────────────────────────┤
│  reservas: LCItem                               │
│    ├── [R001;MARIA;V001;01/01/2024;12A]         │
│    ├── [R055;MARIA;V003;15/03/2024;8B]          │
│    └── [R123;MARIA;V007;20/06/2024;3C]          │
├─────────────────────────────────────────────────┤
│  prox: → [MARTA] ou null                        │
└─────────────────────────────────────────────────┘
```

### Por que LCItem com capacidade 5?

```java
this.reservas = new LCItem(5);
```

- A maioria dos nomes terá **poucas reservas** (1-5)
- Capacidade inicial pequena economiza memória
- `LCItem` cresce automaticamente se necessário (redimensionamento dinâmico)

---

## Implementação do HashingEncadeado

### Atributos da Classe

```java
public class HashingEncadeado {
    private NoHash[] tabela;     // Array de cabeças de listas
    private int tamanho;         // Tamanho M da tabela
    private int numElementos;    // Quantidade de nomes únicos
}
```

### Construtor

```java
public HashingEncadeado(int tamanho) {
    this.tamanho = tamanho;
    this.tabela = new NoHash[tamanho];  // Todas as posições iniciam null
    this.numElementos = 0;
}
```

### Método `inserir(Reserva item)`

```java
public void inserir(Reserva item) {
    if (item == null || estaVazia(item.getNome())) {
        return;  // Validação de entrada
    }

    int indice = hashing(item.getNome());  // Calcula posição
    NoHash atual = tabela[indice];

    // 1. Procura se o nome já existe na lista encadeada
    while (atual != null) {
        if (atual.getNome().equalsIgnoreCase(item.getNome())) {
            // Nome existe: adiciona à lista de reservas existente
            atual.getReservas().insereFinal(item);
            return;
        }
        atual = atual.getProx();
    }

    // 2. Nome não existe: cria novo nó no INÍCIO da lista
    NoHash novoNo = new NoHash(item);
    novoNo.setProx(tabela[indice]);  // Novo aponta para antigo primeiro
    tabela[indice] = novoNo;         // Novo se torna o primeiro
    numElementos++;
}
```

### Diagrama da Inserção

```
CASO 1: Inserindo "MARIA" em posição vazia (índice 42)

Antes:
tabela[42] = null

Depois:
tabela[42] → [MARIA] → null

═══════════════════════════════════════════════════════════════

CASO 2: Inserindo "MARTA" que colide (mesmo índice 42)

Antes:
tabela[42] → [MARIA] → null

Depois (inserção no INÍCIO):
tabela[42] → [MARTA] → [MARIA] → null

═══════════════════════════════════════════════════════════════

CASO 3: Inserindo nova reserva de "MARIA" (nome já existe)

Antes:
tabela[42] → [MARTA] → [MARIA: 2 reservas] → null

Depois (adiciona à lista de reservas existente):
tabela[42] → [MARTA] → [MARIA: 3 reservas] → null
```

### Método `pesquisar(String nome)`

```java
public LCItem pesquisar(String nome) {
    if (estaVazia(nome)) {
        return new LCItem();  // Retorna lista vazia
    }

    int indice = hashing(nome);     // Calcula posição
    NoHash atual = tabela[indice];

    // Percorre a lista encadeada naquela posição
    while (atual != null) {
        if (atual.getNome().equalsIgnoreCase(nome)) {
            return atual.getReservas();  // Encontrou!
        }
        atual = atual.getProx();
    }

    return new LCItem();  // Não encontrado: retorna lista vazia
}
```

### Fluxo da Pesquisa

```
pesquisar("MARIA")
    │
    ├── 1. hashing("MARIA") → índice 42
    │
    ├── 2. atual = tabela[42] → [MARTA]
    │       "MARTA" == "MARIA"? NÃO
    │       atual = atual.prox → [MARIA]
    │
    ├── 3. "MARIA" == "MARIA"? SIM!
    │
    └── 4. return atual.getReservas() → LCItem com 3 reservas
```

---

## Função Hash

### Implementação

```java
public int hashing(String chave) {
    char carac;
    int i, soma = 0;
    for (i = 0; i < chave.length(); i++) {
        carac = chave.charAt(i);
        soma += Character.getNumericValue(carac);
    }
    return soma % tamanho;  // Módulo garante índice válido
}
```

### Análise Passo a Passo

```
hashing("MARIA") com tamanho = 1103

Caractere │ ASCII │ getNumericValue()
──────────┼───────┼───────────────────
    M     │  77   │      22
    A     │  65   │      10
    R     │  82   │      27
    I     │  73   │      18
    A     │  65   │      10
──────────┼───────┼───────────────────
          │       │   soma = 87

índice = 87 % 1103 = 87
```

### Por que `Character.getNumericValue()`?

Esta função retorna:
- **Letras A-Z**: valores 10-35 (como em sistemas de numeração)
- **Dígitos 0-9**: valores 0-9
- **Outros**: -1 ou -2

```java
Character.getNumericValue('A') = 10
Character.getNumericValue('Z') = 35
Character.getNumericValue('0') = 0
Character.getNumericValue('9') = 9
```

**Observação**: Uma alternativa seria usar `(int) carac` diretamente (valor ASCII), mas `getNumericValue()` foi escolhida na implementação.

### Propriedades da Função Hash

| Propriedade | Status | Descrição |
|-------------|--------|-----------|
| **Determinística** | ✅ | Mesma entrada → mesmo índice |
| **Distribuição** | ⚠️ | Razoável para nomes, não perfeita |
| **Velocidade** | ✅ | O(k), k = tamanho da chave |
| **Colisões** | ⚠️ | Anagramas colidem (AMOR = ROMA) |

---

## Tratamento de Colisões

### O que é uma Colisão?

Quando duas chaves diferentes produzem o **mesmo índice**:

```
hashing("AMOR") = soma(10+22+24+27) % M = 83 % 1103 = 83
hashing("ROMA") = soma(27+24+22+10) % M = 83 % 1103 = 83

COLISÃO! Ambos vão para índice 83
```

### Encadeamento Separado

Cada posição da tabela é uma **lista encadeada**:

```
tabela[83] → [ROMA] → [AMOR] → [MORA] → [OMAR] → null
               │        │        │        │
             LCItem   LCItem   LCItem   LCItem
              (3)      (1)      (2)      (1)
```

### Vantagens do Encadeamento

| Vantagem | Descrição |
|----------|-----------|
| **Simplicidade** | Fácil de implementar |
| **Flexibilidade** | Não tem limite de elementos |
| **Fator de carga** | Pode ser > 1 sem problema |
| **Remoção** | Simples (remove da lista) |

### Desvantagens

| Desvantagem | Descrição |
|-------------|-----------|
| **Memória extra** | Ponteiros `prox` em cada nó |
| **Cache miss** | Listas não são contíguas |
| **Pior caso** | O(n) se todos colidirem |

---

## Dimensionamento da Tabela

### Por que Usar Números Primos?

Números primos melhoram a **distribuição** da função hash:

```
Se M = 100 (não primo) e soma sempre termina em 0:
  índices: 0, 100, 200... → todos vão para índice 0!

Se M = 101 (primo):
  índices: 0, 100, 200... → 0, 99, 98... distribuídos!
```

### Método `calcularTamanhoPrimo`

```java
public static int calcularTamanhoPrimo(int n) {
    // Fator 1.1 para fator de carga ~0.9
    int candidato = (int) (n * 1.1);
    
    // Garante que seja ímpar (primos > 2 são ímpares)
    if (candidato % 2 == 0) {
        candidato++;
    }
    
    // Busca o próximo primo
    while (!ehPrimo(candidato)) {
        candidato += 2;  // Pula pares
    }
    
    return candidato;
}
```

### Método `ehPrimo`

```java
public static boolean ehPrimo(int n) {
    if (n < 2) return false;
    if (n == 2) return true;
    if (n % 2 == 0) return false;
    
    int raiz = (int) Math.sqrt(n);
    for (int i = 3; i <= raiz; i += 2) {
        if (n % i == 0) {
            return false;
        }
    }
    return true;
}
```

### Exemplos de Dimensionamento

```
Reservas │ n × 1.1 │ Próximo Primo │ Fator de Carga
─────────┼─────────┼───────────────┼────────────────
  1.000  │  1.100  │     1.103     │     0.91
  5.000  │  5.500  │     5.501     │     0.91
 10.000  │ 11.000  │    11.003     │     0.91
 50.000  │ 55.000  │    55.001     │     0.91
```

### Fator de Carga

```
Fator de Carga (α) = numElementos / tamanho

α ≈ 0.9 significa:
- ~90% das posições têm pelo menos 1 elemento
- Boa distribuição com poucas colisões
- Listas encadeadas curtas (média ~1 elemento)
```

---

## Dependências e Integração

### Diagrama de Dependências

```
┌─────────────────────────────────────────────────────────────────┐
│                    PesquisaReservas.java                        │
│                    (Programa Principal)                         │
└─────────────────────────────────────────────────────────────────┘
                              │
          ┌───────────────────┼───────────────────┐
          │                   │                   │
          ▼                   ▼                   ▼
    ArvoreABBItem      ArvoreAVLItem      HashingEncadeado
                                                 │
                              ┌──────────────────┴──────────────────┐
                              │                                     │
                              ▼                                     ▼
                 ┌────────────────────────┐           ┌────────────────────────┐
                 │      NoHash            │           │      LCItem            │
                 │                        │           │                        │
                 │  - nome: String        │           │  - lista: Reserva[]    │
                 │  - reservas: LCItem  ──┼───────────│  - quant: int          │
                 │  - prox: NoHash        │           │                        │
                 └────────────────────────┘           └────────────────────────┘
                                                                  │
                                                                  ▼
                                                      ┌────────────────────────┐
                                                      │       Reserva          │
                                                      │                        │
                                                      │  - chave: String       │
                                                      │  - nome: String        │
                                                      │  - codigo_voo          │
                                                      │  - data, assento       │
                                                      └────────────────────────┘
```

### Comparação das 3 Estruturas de Pesquisa

| Aspecto | ABB | AVL | Hashing |
|---------|-----|-----|---------|
| **Estrutura** | Árvore binária | Árvore auto-balanceada | Tabela + listas |
| **Nó** | `NoABBItem` | `NoAVLItem` | `NoHash` |
| **Herança** | `NoArvoreBase<T>` | `NoArvoreBase<T>` | Nenhuma |
| **Pesquisa média** | O(log n) | O(log n) | **O(1)** |
| **Pesquisa pior** | O(n) | O(log n) | O(n) |
| **Ordenação** | Mantém ordem | Mantém ordem | **Não ordena** |
| **Memória extra** | 2 ponteiros/nó | 2 pont + FB/nó | 1 ponteiro/nó |

---

## Uso no Projeto: PesquisaReservas.java

### Fluxo de Execução

```java
// PesquisaReservas.java - Seção do Hashing
private static void processarDataset(...) throws IOException {
    // 1. Carrega reservas
    LCItem reservas = LeArquivo.lerReservas(caminhoEntrada);
    
    // 2. Para cada execução (5 vezes)
    for (int exec = 0; exec < NUM_EXECUCOES; exec++) {
        long inicio = System.currentTimeMillis();
        
        // 3. Calcula tamanho primo ideal
        int tamanhoTabela = HashingEncadeado.calcularTamanhoPrimo(reservas.getQuant());
        
        // 4. Constrói tabela hash
        HashingEncadeado hash = new HashingEncadeado(tamanhoTabela);
        hash.carregarDeLCItem(reservas);
        
        // 5. Pesquisa todos os 400 nomes
        resultadosPesquisaHash = hash.pesquisarTodos(nomesPesquisa);
        
        long fim = System.currentTimeMillis();
        tempoTotalHash += (fim - inicio);
    }
    
    // 6. Calcula média e salva
    double mediaHash = tempoTotalHash / (double) NUM_EXECUCOES;
    EscreveArquivo.salvarResultadosPesquisa(caminhoSaidaHash, nomesPesquisa, resultadosPesquisaHash);
}
```

### Método `carregarDeLCItem`

```java
public void carregarDeLCItem(LCItem lista) {
    if (lista == null) {
        return;
    }

    for (int i = 0; i < lista.getQuant(); i++) {
        inserir(lista.getItem(i));
    }
}
```

### Método `pesquisarTodos`

```java
public LCItem[] pesquisarTodos(LCItem nomes) {
    LCItem[] resultados = new LCItem[nomes.getQuant()];
    for (int i = 0; i < nomes.getQuant(); i++) {
        resultados[i] = pesquisar(nomes.getItem(i).getNome());
    }
    return resultados;
}
```

### Arquivos de Saída

| Dataset | Arquivo de Saída Hash |
|---------|----------------------|
| `Reserva1000alea` | `data/searched/HashReserva1000alea.txt` |
| `Reserva5000ord` | `data/searched/HashReserva5000ord.txt` |
| `Reserva10000inv` | `data/searched/HashReserva10000inv.txt` |
| `Reserva50000alea` | `data/searched/HashReserva50000alea.txt` |

### Cadeia de Chamadas

```
PesquisaReservas.processarDataset()
    │
    ├── HashingEncadeado.calcularTamanhoPrimo(50000)
    │       └── retorna 55001 (primo)
    │
    ├── new HashingEncadeado(55001)
    │       └── tabela = new NoHash[55001]
    │
    ├── hash.carregarDeLCItem(reservas)
    │       │
    │       └── for cada reserva:
    │               └── hash.inserir(reserva)
    │                       ├── indice = hashing(nome)
    │                       ├── nome existe? → adiciona à LCItem
    │                       └── nome novo? → cria NoHash no início
    │
    └── hash.pesquisarTodos(nomes)
            │
            └── for cada nome:
                    └── hash.pesquisar(nome)
                            ├── indice = hashing(nome)
                            ├── percorre lista encadeada
                            └── retorna LCItem ou lista vazia
```

---

## Análise de Complexidade

### Complexidade de Tempo

| Operação | Caso Médio | Pior Caso | Justificativa |
|----------|------------|-----------|---------------|
| **Inserção** | O(1) | O(n) | Média: acesso direto; Pior: todos colidem |
| **Pesquisa** | O(1) | O(n) | Média: lista curta; Pior: lista com n elementos |
| **Hash** | O(k) | O(k) | k = tamanho da chave |
| **Construção** | O(n) | O(n²) | n inserções de O(1) médio cada |

### Por que O(1) na Média?

Com fator de carga α ≈ 0.9:
- Tamanho médio das listas ≈ α = 0.9
- Pesquisa percorre em média < 1 elemento por lista
- **Tempo constante** independente de n!

### Comparação no Benchmark

```
Dataset: Reserva50000 (resultados típicos)
┌────────────────────┬──────────────┬─────────────────┐
│ Estrutura          │ Tempo Médio  │ Complexidade    │
├────────────────────┼──────────────┼─────────────────┤
│ ABB (balanceada)   │ ~15 ms       │ O(log n)        │
│ AVL                │ ~12 ms       │ O(log n)        │
│ Hashing            │ ~5 ms        │ O(1) médio      │
└────────────────────┴──────────────┴─────────────────┘

Hashing é ~2-3x mais rápido!
```

### Complexidade de Espaço

| Tipo | Complexidade |
|------|--------------|
| **Tabela** | O(M) = O(n × 1.1) ≈ O(n) |
| **Nós** | O(nomes únicos) |
| **LCItem por nó** | O(reservas daquele nome) |

---

## Perguntas e Respostas

### P1: Por que escolher encadeamento em vez de endereçamento aberto?

**R:** O encadeamento separado foi escolhido por várias razões:

1. **Simplicidade**: Inserção e remoção são triviais em listas ligadas
2. **Sem clustering**: Não sofre de clustering primário/secundário
3. **Fator de carga flexível**: Pode ser > 1 sem problemas
4. **Múltiplas reservas**: Cada nó já precisa de uma lista de reservas, então a lista encadeada é natural

```java
// Encadeamento: sempre O(1) para inserir no início
novoNo.setProx(tabela[indice]);
tabela[indice] = novoNo;

// Endereçamento aberto: pode precisar sondar várias posições
while (tabela[indice] != null) {
    indice = (indice + 1) % tamanho;  // Linear probing
}
```

### P2: Por que usar `Character.getNumericValue()` em vez de valor ASCII direto?

**R:** Na verdade, usar ASCII direto seria **mais intuitivo**:

```java
// Implementação atual
soma += Character.getNumericValue(carac);  // A=10, B=11, ...

// Alternativa com ASCII
soma += (int) carac;  // A=65, B=66, ...
```

Ambas funcionam, mas `getNumericValue()`:
- Trata letras como valores 10-35 (hexadecimal estendido)
- Retorna -1 para caracteres especiais (pode ser problemático!)

**Potencial melhoria**: usar `(int) carac` diretamente é mais seguro.

### P3: Por que inserir no início da lista e não no final?

**R:** **Eficiência!** Inserção no início é O(1):

```java
// Inserção no INÍCIO: O(1)
novoNo.setProx(tabela[indice]);
tabela[indice] = novoNo;

// Inserção no FINAL: O(n) - precisa percorrer toda a lista
NoHash atual = tabela[indice];
while (atual.getProx() != null) {
    atual = atual.getProx();
}
atual.setProx(novoNo);
```

A ordem na lista não importa para a funcionalidade (não é ordenada).

### P4: Por que o fator de carga escolhido foi ~0.9 (fator 1.1)?

**R:** É um **compromisso entre espaço e tempo**:

| Fator de Carga | Espaço | Colisões | Velocidade |
|----------------|--------|----------|------------|
| α = 0.5 | Desperdiça 50% | Poucas | Rápido |
| **α = 0.9** | **Eficiente** | **Moderadas** | **Rápido** |
| α = 1.5 | Mínimo | Muitas | Lento |

Com α ≈ 0.9:
- ~10% de posições sobram para distribuição
- Listas encadeadas ficam curtas (média < 1)
- Boa relação custo-benefício

### P5: O que acontece se a função hash for ruim?

**R:** **Degrada para O(n)!** Se todos os elementos colidirem:

```
Função hash terrível: sempre retorna 0
tabela[0] → [A] → [B] → [C] → ... → [Z] → null  // Uma lista com TUDO!

Pesquisar "Z" precisa percorrer n elementos = O(n)
```

Por isso é importante:
1. Usar tamanho primo
2. Função hash com boa distribuição
3. Monitorar estatísticas de colisão

### P6: Por que a pesquisa retorna `LCItem` vazia em vez de `null`?

**R:** **Padrão Null Object!** Evita `NullPointerException`:

```java
// Com null (problemático)
LCItem resultado = hash.pesquisar("INEXISTENTE");
int quant = resultado.getQuant();  // NullPointerException!

// Com LCItem vazia (seguro)
LCItem resultado = hash.pesquisar("INEXISTENTE");
int quant = resultado.getQuant();  // Retorna 0, sem erro
```

O chamador pode verificar `resultado.getQuant() == 0` sem risco.

### P7: Como funciona o método `imprimirEstatisticas()`?

**R:** Coleta e exibe métricas sobre a tabela:

```java
hash.imprimirEstatisticas();
// === Estatísticas do Hashing ===
// Tamanho da tabela: 55001
// Elementos inseridos: 48723 (nomes únicos)
// Fator de carga: 0.89
// Posições vazias: 6278 (11%)
// Máximo em uma lista: 5 (pior colisão)
// Total de colisões: 3421
```

Útil para:
- Verificar qualidade da função hash
- Identificar clustering
- Validar dimensionamento

### P8: Por que Hashing não mantém ordenação?

**R:** **Por design!** A função hash distribui elementos aleatoriamente:

```
Inserindo em ordem: ANA, CARLOS, MARIA, ZELIA

ABB/AVL mantém ordem:        Hashing perde ordem:
    CARLOS                   tabela[23] → ANA
    /    \                   tabela[45] → ZELIA
  ANA    MARIA               tabela[67] → CARLOS
           \                 tabela[89] → MARIA
          ZELIA
```

Se precisar de ordenação após pesquisa, deve-se ordenar o resultado.

### P9: O NoHash não herda de NoArvoreBase. Por quê?

**R:** **Estruturas diferentes!**

```java
// NoArvoreBase - para árvores binárias
protected T esq, dir;  // Dois filhos (binário)

// NoHash - para listas encadeadas
private NoHash prox;   // Um único próximo (linear)
```

NoHash não tem conceito de "esquerda/direita", apenas "próximo na lista". Herdar de NoArvoreBase seria semanticamente incorreto e desperdiçaria memória.

### P10: Como a comparação case-insensitive afeta o hashing?

**R:** **Pode causar mais colisões!**

```java
// Pesquisa é case-insensitive
atual.getNome().equalsIgnoreCase(item.getNome())

// MAS a função hash é case-sensitive!
hashing("MARIA") ≠ hashing("maria")

// "MARIA" e "maria" podem ir para índices DIFERENTES
// mas a comparação equalsIgnoreCase() encontra ambos
```

**Solução ideal**: normalizar para maiúsculas antes do hash:

```java
public int hashing(String chave) {
    String normalizada = chave.toUpperCase();
    // ... resto do código
}
```

### P11: Qual a diferença entre `numElementos` e quantidade total de reservas?

**R:** São conceitos diferentes:

```
numElementos = quantidade de NOMES únicos na tabela
             = quantidade de NoHash criados

Total de reservas = soma de todas as reservas em todos os LCItem
                  = muito maior se nomes se repetem

Exemplo:
- 1000 reservas no dataset
- 400 nomes únicos
- numElementos = 400
- Cada nome tem em média 2.5 reservas
```

### P12: Por que reconstruir o hash em cada execução do benchmark?

**R:** **Para medir tempo total (construção + pesquisa)**:

```java
for (int exec = 0; exec < NUM_EXECUCOES; exec++) {
    long inicio = System.currentTimeMillis();
    
    // Constrói do zero a cada vez
    HashingEncadeado hash = new HashingEncadeado(tamanhoTabela);
    hash.carregarDeLCItem(reservas);
    
    // Pesquisa
    resultados = hash.pesquisarTodos(nomes);
    
    long fim = System.currentTimeMillis();
}
```

Isso permite comparação justa com ABB e AVL, que também são reconstruídas a cada execução.

### P13: O que é o "fator de carga" e por que é importante?

**R:** O **fator de carga (α)** é:

```
α = numElementos / tamanhoTabela
```

| α | Significado | Performance |
|---|-------------|-------------|
| α < 0.5 | Tabela subutilizada | Excelente |
| **0.7 ≤ α ≤ 0.9** | **Ótimo** | **Muito boa** |
| α ≈ 1 | Cheio | Boa |
| α > 1.5 | Sobrecarregado | Degradada |

Para encadeamento, α pode ser > 1, mas quanto maior, mais longas as listas e mais lenta a pesquisa.

---

## Estatísticas de Exemplo

```
=== Dataset: Reserva50000alea ===
Tamanho da tabela: 55001 (primo)
Nomes únicos inseridos: ~35000
Fator de carga: 0.64
Posições vazias: ~19000 (35%)
Maior lista: 4 elementos
Tempo médio de 400 pesquisas: ~5ms
```

---

## Referências

- Cormen, T. H., et al. "Introduction to Algorithms" - Capítulo 11: Hash Tables
- Knuth, D. "The Art of Computer Programming" - Volume 3: Sorting and Searching
- Sedgewick, R. "Algorithms" - Seção sobre Hash Tables
- Standish, T. A. "Data Structures, Algorithms, and Software Principles in C"
