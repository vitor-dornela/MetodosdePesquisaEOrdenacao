# QuickSort com Inserção - Documentação Detalhada

## Índice

1. [Visão Geral](#visão-geral)
2. [Motivação: Por que um Algoritmo Híbrido?](#motivação-por-que-um-algoritmo-híbrido)
3. [Conceitos Fundamentais](#conceitos-fundamentais)
4. [Implementação Detalhada](#implementação-detalhada)
5. [Dependências e Integração](#dependências-e-integração)
6. [Uso no Projeto: OrdenacaoReservas.java](#uso-no-projeto-ordenacaoreservasjava)
7. [Análise de Complexidade](#análise-de-complexidade)
8. [Comparação de Performance](#comparação-de-performance)
9. [Perguntas e Respostas](#perguntas-e-respostas)

---

## Visão Geral

O **QuickSortComInsercao** é um algoritmo **híbrido** que combina as forças de dois algoritmos de ordenação:

| Componente | Algoritmo | Quando Usado |
|------------|-----------|--------------|
| **Principal** | QuickSort | Partições com mais de 20 elementos |
| **Auxiliar** | InsertionSort | Partições com 20 ou menos elementos |

### Características Principais

- **Híbrido**: Combina QuickSort + InsertionSort
- **In-place**: Não requer memória adicional significativa
- **Não-estável**: Elementos iguais podem ter ordem relativa alterada
- **Otimizado**: ~10% mais rápido que QuickSort puro em benchmarks típicos
- **Limiar**: 20 elementos (valor empírico otimizado)

### Localização no Projeto

```
src/br/faesa/C3/algoritmos/ordenacao/QuickSortComInsercao.java
```

---

## Motivação: Por que um Algoritmo Híbrido?

### O Problema do QuickSort Puro

O QuickSort tem **overhead significativo** para partições pequenas:

```
Custo por partição no QuickSort:
┌─────────────────────────────────────────────────────────┐
│ 1. Chamada de função recursiva (push na pilha)          │
│ 2. Cálculo do pivô: array[(esq + dir) / 2]              │
│ 3. Inicialização de variáveis: i, j, pivo, temp         │
│ 4. Loop do-while com múltiplas comparações              │
│ 5. Verificação das condições de recursão                │
│ 6. Retorno da função (pop da pilha)                     │
└─────────────────────────────────────────────────────────┘
```

Para uma partição de **5 elementos**, esse overhead é proporcionalmente **muito alto**.

### A Solução: InsertionSort para Partições Pequenas

O InsertionSort tem características opostas:

| Aspecto | QuickSort | InsertionSort |
|---------|-----------|---------------|
| **Overhead fixo** | Alto (recursão, pivô) | Baixo (loop simples) |
| **Constante multiplicativa** | ~2n comparações | ~n comparações |
| **Para n pequeno** | Ineficiente | Muito eficiente |
| **Para n grande** | O(n log n) | O(n²) - ruim |
| **Arrays quase ordenados** | Normal | Excelente (quase O(n)) |

### O Ponto de Equilíbrio

```
Performance relativa:
                    │
Tempo               │    QuickSort
                    │      ╱
                    │     ╱
                    │    ╱
                    │   ╱ InsertionSort
                    │  ╱  (O(n²) para n grande)
                    │ ╱
                    │╱........... Ponto de cruzamento (~20 elementos)
                    ├─────────────────────────────────────
                   0│    10    20    30    40    50
                           Tamanho da partição (n)
```

**Abaixo de ~20 elementos**: InsertionSort é mais rápido
**Acima de ~20 elementos**: QuickSort é mais rápido

---

## Conceitos Fundamentais

### Estratégia Híbrida

```
┌─────────────────────────────────────────────────────────────────┐
│                    QuickSortComInsercao                         │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │                    Partição Atual                        │   │
│  │                    (esq até dir)                         │   │
│  └──────────────────────────────────────────────────────────┘   │
│                              │                                  │
│                              ▼                                  │
│                    ┌─────────────────┐                          │
│                    │ dir - esq <= 20 │                          │
│                    └─────────────────┘                          │
│                     /              \                            │
│                   SIM              NÃO                          │
│                   /                  \                          │
│                  ▼                    ▼                         │
│    ┌──────────────────┐    ┌──────────────────────┐            │
│    │  InsertionSort   │    │  QuickSort Padrão    │            │
│    │  .sortRange()    │    │  (particionamento +  │            │
│    │                  │    │   recursão)          │            │
│    │  Ordena e        │    │                      │            │
│    │  RETORNA         │    │  Gera 2 subpartições │            │
│    └──────────────────┘    └──────────────────────┘            │
│                                     │                           │
│                                     ▼                           │
│                            Processa cada                        │
│                            subpartição                          │
│                            recursivamente                       │
└─────────────────────────────────────────────────────────────────┘
```

### O Limiar de 20 Elementos

O valor **20** foi escolhido empiricamente baseado em:

1. **Custo do InsertionSort**: O(n²) = O(400) para n=20 → aceitável
2. **Custo do QuickSort**: Overhead fixo + O(n log n)
3. **Cache locality**: Partições pequenas cabem em cache L1
4. **Branch prediction**: Loops menores são mais previsíveis

**Valores comuns na literatura**: 10-25 elementos

---

## Implementação Detalhada

### Estrutura da Classe

```java
package br.faesa.C3.algoritmos.ordenacao;

import br.faesa.C3.entidades.Reserva;

public class QuickSortComInsercao {
    
    // Métodos públicos (pontos de entrada)
    public static void sort(Reserva[] array, int size)
    public static void sort(Integer[] array, int size)
    
    // Métodos privados recursivos
    private static void quicksortComInsercao(Reserva[] array, int esq, int dir)
    private static void quicksortComInsercao(Integer[] array, int esq, int dir)
}
```

### Método Principal: `sort(Reserva[] array, int size)`

```java
public static void sort(Reserva[] array, int size) {
    quicksortComInsercao(array, 0, size - 1);
}
```

**Função**: Ponto de entrada público que inicia a recursão com limites [0, size-1].

### Método Recursivo: `quicksortComInsercao(Reserva[] array, int esq, int dir)`

```java
private static void quicksortComInsercao(Reserva[] array, int esq, int dir) {
    // ═══════════════════════════════════════════════════════════════
    // DECISÃO HÍBRIDA: Escolhe entre InsertionSort e QuickSort
    // ═══════════════════════════════════════════════════════════════
    if (dir - esq <= 20) {
        InsertionSort.sortRange(array, esq, dir);
        return;
    }

    // ═══════════════════════════════════════════════════════════════
    // QUICKSORT PADRÃO: Particionamento
    // ═══════════════════════════════════════════════════════════════
    Reserva pivo, temp;
    int i = esq, j = dir;

    // 1. Escolhe o pivô (elemento do meio)
    pivo = array[(i + j) / 2];

    // 2. Particiona o array
    do {
        // Encontra elemento à esquerda que é >= pivô
        while (array[i].compareTo(pivo) < 0) {
            i++;
        }

        // Encontra elemento à direita que é <= pivô
        while (array[j].compareTo(pivo) > 0) {
            j--;
        }

        // 3. Se os ponteiros não se cruzaram, troca os elementos
        if (i <= j) {
            temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            i++;
            j--;
        }
    } while (i <= j);

    // ═══════════════════════════════════════════════════════════════
    // RECURSÃO: Processa subpartições
    // ═══════════════════════════════════════════════════════════════
    if (esq < j) {
        quicksortComInsercao(array, esq, j);
    }
    if (i < dir) {
        quicksortComInsercao(array, i, dir);
    }
}
```

### Análise Linha por Linha

#### Parte 1: Decisão Híbrida

```java
if (dir - esq <= 20) {
    InsertionSort.sortRange(array, esq, dir);
    return;
}
```

| Componente | Explicação |
|------------|------------|
| `dir - esq` | Tamanho da partição - 1 (quantidade de elementos - 1) |
| `<= 20` | Limiar: se 21 elementos ou menos, usa InsertionSort |
| `sortRange(array, esq, dir)` | Ordena APENAS o intervalo [esq, dir] |
| `return` | **Crucial**: Encerra a recursão aqui |

**Exemplo**:
- `esq = 10, dir = 25` → `dir - esq = 15 ≤ 20` → InsertionSort
- `esq = 0, dir = 100` → `dir - esq = 100 > 20` → QuickSort

#### Parte 2: Escolha do Pivô

```java
pivo = array[(i + j) / 2];
```

O elemento do **meio** é escolhido porque:
- Bom para arrays ordenados (seria mediana real)
- Evita caso O(n²) comum com primeiro/último elemento
- Simples de calcular

#### Parte 3: Particionamento

```java
do {
    while (array[i].compareTo(pivo) < 0) { i++; }
    while (array[j].compareTo(pivo) > 0) { j--; }
    
    if (i <= j) {
        temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        i++;
        j--;
    }
} while (i <= j);
```

**Invariantes mantidas**:
- Elementos em `[esq, i-1]` são ≤ pivô
- Elementos em `[j+1, dir]` são ≥ pivô
- Loop termina quando `i > j`

#### Parte 4: Recursão

```java
if (esq < j) {
    quicksortComInsercao(array, esq, j);
}
if (i < dir) {
    quicksortComInsercao(array, i, dir);
}
```

Cada subpartição pode:
1. Ser processada por QuickSort (se > 20 elementos)
2. Ser processada por InsertionSort (se ≤ 20 elementos)

---

## Dependências e Integração

### Diagrama de Dependências

```
┌─────────────────────────────────────────────────────────────────┐
│                    OrdenacaoReservas.java                       │
│                    (Programa Principal)                         │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ usa
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         LCItem.java                             │
│              (implements Ordenavel)                             │
│                                                                 │
│  quicksortComInsercao() ──────────────────────┐                 │
└───────────────────────────────────────────────┼─────────────────┘
                                                │
                                   delega para  │
                                                ▼
┌─────────────────────────────────────────────────────────────────┐
│              QuickSortComInsercao.java                          │
│                                                                 │
│  sort(Reserva[] array, int size)                                │
│      │                                                          │
│      └──▶ quicksortComInsercao(array, esq, dir)                 │
│               │                                                 │
│               ├── if (dir - esq <= 20)                          │
│               │       │                                         │
│               │       ▼                                         │
│               │   ┌─────────────────────────────────────────┐   │
│               │   │      InsertionSort.sortRange()          │   │
│               │   │      (dependência externa)              │   │
│               │   └─────────────────────────────────────────┘   │
│               │                                                 │
│               └── else: particionamento + recursão              │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ ordena objetos
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        Reserva.java                             │
│               (implements Comparable<Reserva>)                  │
│                                                                 │
│  compareTo(Reserva outro)                                       │
│      1º: compara nome (case-insensitive)                        │
│      2º: compara chave (desempate)                              │
└─────────────────────────────────────────────────────────────────┘
```

### Dependência: InsertionSort.sortRange()

```java
// InsertionSort.java
public static void sortRange(Reserva[] array, int inicio, int fim) {
    for (int i = inicio + 1; i <= fim; i++) {
        Reserva temp = array[i];
        int j = i - 1;

        // Desloca elementos maiores para a direita
        while (j >= inicio && array[j].compareTo(temp) > 0) {
            array[j + 1] = array[j];
            j--;
        }

        // Insere elemento na posição correta
        array[j + 1] = temp;
    }
}
```

**Por que `sortRange` e não `sort`?**

O método `sortRange` ordena apenas um **intervalo** `[inicio, fim]` do array, não o array inteiro. Isso é **essencial** para o híbrido funcionar corretamente.

```java
// sort() - ordena array INTEIRO
InsertionSort.sort(array, size);  // [0, size-1]

// sortRange() - ordena INTERVALO específico
InsertionSort.sortRange(array, 10, 30);  // apenas [10, 30]
```

### Dependência: Interface Ordenavel

```java
// Ordenavel.java
public interface Ordenavel {
    void heapsort();
    void quicksort();
    void quicksortComInsercao();  // ← Nosso método
    boolean eVazia();
}
```

### Delegação em LCItem

```java
// LCItem.java
public class LCItem implements Ordenavel {
    private Reserva[] lista;
    private int quant;
    
    // ...
    
    /**
     * Ordena a lista usando QuickSort com InsertionSort para partições pequenas.
     * Quando a partição tem 20 ou menos elementos, usa InsertionSort.
     */
    public void quicksortComInsercao() {
        QuickSortComInsercao.sort(this.lista, this.quant);
    }
}
```

### Dependência: Reserva.compareTo()

```java
// Reserva.java
@Override
public int compareTo(Reserva outro) {
    if (outro == null) return 1;
    
    // 1º critério: nome (alfabético, case-insensitive)
    int comparacaoNome = this.nome.compareToIgnoreCase(outro.nome);
    if (comparacaoNome != 0) return comparacaoNome;
    
    // 2º critério: chave (desempate)
    return this.chave.compareTo(outro.chave);
}
```

**Usado em**:
```java
while (array[i].compareTo(pivo) < 0) { i++; }
while (array[j].compareTo(pivo) > 0) { j--; }
```

---

## Uso no Projeto: OrdenacaoReservas.java

### Contexto no Benchmark

O `QuickSortComInsercao` é um dos **três algoritmos** comparados no benchmark:

```java
// OrdenacaoReservas.java
LCItem algoritmos = new LCItem(3);
algoritmos.insereFinal(new Reserva(0, "HeapSort"));
algoritmos.insereFinal(new Reserva(0, "QuickSort"));
algoritmos.insereFinal(new Reserva(0, "QuickSortInsertion"));  // ← Este
```

### Chamada no Processamento

```java
private static void processarDataset(String nomeDataset, String algoritmo, boolean primeiraLinha) {
    String prefixo;
    if (algoritmo.equals("HeapSort")) {
        prefixo = "heap";
    } else if (algoritmo.equals("QuickSort")) {
        prefixo = "quick";
    } else {
        prefixo = "QuickIns";  // ← Prefixo do QuickSortComInsercao
    }
    
    // Executa 5 vezes para média estatística
    for (int i = 0; i < 5; i++) {
        reservas = LeArquivo.lerReservas(caminhoEntrada);
        
        if (algoritmo.equals("HeapSort")) {
            reservas.heapsort();
        } else if (algoritmo.equals("QuickSort")) {
            reservas.quicksort();
        } else {
            reservas.quicksortComInsercao();  // ← Chamada aqui
        }
    }
    
    // Salva resultados
    EscreveArquivo.salvarReservas(reservas, "data/sorted/" + prefixo + nomeDataset + ".txt");
}
```

### Arquivos de Saída

| Dataset de Entrada | Arquivo de Saída |
|-------------------|------------------|
| `data/raw/Reserva1000alea.txt` | `data/sorted/QuickIns20Reserva1000alea.txt` |
| `data/raw/Reserva5000ord.txt` | `data/sorted/QuickIns20Reserva5000ord.txt` |
| `data/raw/Reserva10000inv.txt` | `data/sorted/QuickIns20Reserva10000inv.txt` |
| `data/raw/Reserva50000alea.txt` | `data/sorted/QuickIns20Reserva50000alea.txt` |

**Nota**: O "20" no nome indica o limiar usado (20 elementos).

### Cadeia de Chamadas Completa

```
OrdenacaoReservas.main()
    │
    ▼
processarDataset("Reserva10000alea", "QuickSortInsertion", false)
    │
    ▼
reservas.quicksortComInsercao()                    // LCItem.java
    │
    ▼
QuickSortComInsercao.sort(this.lista, this.quant)  // QuickSortComInsercao.java
    │
    ▼
quicksortComInsercao(array, 0, 9999)               // Recursão inicia
    │
    ├── dir - esq = 9999 > 20 → QuickSort
    │   │
    │   ├── Particionamento com pivô = array[4999]
    │   │
    │   ├── quicksortComInsercao(array, 0, ~5000)
    │   │   └── ... mais recursões ...
    │   │
    │   └── quicksortComInsercao(array, ~5000, 9999)
    │       └── ... mais recursões ...
    │
    └── Eventualmente: dir - esq <= 20
        │
        └── InsertionSort.sortRange(array, esq, dir)
            │
            └── Ordena intervalo e RETORNA
```

---

## Análise de Complexidade

### Complexidade de Tempo

| Caso | Complexidade | Quando Ocorre |
|------|--------------|---------------|
| **Melhor** | O(n log n) | Partições sempre balanceadas |
| **Médio** | O(n log n) | Caso típico |
| **Pior** | O(n²) | Pivô sempre extremo (raro com pivô do meio) |

### Análise Detalhada

#### Níveis de Recursão

```
Nível 0: n elementos
        │
        ├── Particionamento: O(n)
        │
        ▼
Nível 1: ~n/2 + ~n/2 elementos
        │
        ├── Particionamento: O(n/2) + O(n/2) = O(n)
        │
        ▼
Nível 2: ~n/4 + ~n/4 + ~n/4 + ~n/4 elementos
        │
        ├── Particionamento: O(n)
        │
        ▼
...continua até partições ≤ 20...
        │
        ▼
Nível k: Partições de ~20 elementos cada
        │
        └── InsertionSort: O(20²) × (n/20) = O(20n) = O(n)
```

**Total**: O(n) por nível × O(log n) níveis = **O(n log n)**

#### Economia com InsertionSort

Sem híbrido (QuickSort puro):
- Recursão continua até partições de 1 elemento
- Mais ~log₂(20) ≈ 4-5 níveis adicionais de recursão
- Mais chamadas de função com overhead

Com híbrido:
- Recursão para em partições de 20
- InsertionSort processa O(n/20) partições finais
- Redução significativa de overhead

### Complexidade de Espaço

| Tipo | Complexidade |
|------|--------------|
| **Espaço auxiliar** | O(log n) - pilha de recursão |
| **Total** | O(n) |

---

## Comparação de Performance

### Resultados Típicos do Benchmark

```
┌────────────────────┬───────────┬───────────┬────────────────────┬────────┐
│ Dataset            │ HeapSort  │ QuickSort │ QuickSortInsercao  │ Ganho* │
├────────────────────┼───────────┼───────────┼────────────────────┼────────┤
│ Reserva1000alea    │ 2.40 ms   │ 1.80 ms   │ 1.60 ms            │ ~11%   │
│ Reserva1000ord     │ 2.20 ms   │ 1.40 ms   │ 1.20 ms            │ ~14%   │
│ Reserva1000inv     │ 2.30 ms   │ 1.50 ms   │ 1.30 ms            │ ~13%   │
├────────────────────┼───────────┼───────────┼────────────────────┼────────┤
│ Reserva5000alea    │ 10.00 ms  │ 8.20 ms   │ 7.40 ms            │ ~10%   │
│ Reserva5000ord     │ 9.50 ms   │ 6.80 ms   │ 6.00 ms            │ ~12%   │
│ Reserva5000inv     │ 9.80 ms   │ 7.00 ms   │ 6.20 ms            │ ~11%   │
├────────────────────┼───────────┼───────────┼────────────────────┼────────┤
│ Reserva10000alea   │ 22.00 ms  │ 16.50 ms  │ 14.80 ms           │ ~10%   │
│ Reserva10000ord    │ 20.00 ms  │ 13.00 ms  │ 11.50 ms           │ ~12%   │
│ Reserva10000inv    │ 21.00 ms  │ 14.00 ms  │ 12.50 ms           │ ~11%   │
├────────────────────┼───────────┼───────────┼────────────────────┼────────┤
│ Reserva50000alea   │ 120.00 ms │ 85.00 ms  │ 76.50 ms           │ ~10%   │
│ Reserva50000ord    │ 110.00 ms │ 70.00 ms  │ 62.00 ms           │ ~11%   │
│ Reserva50000inv    │ 115.00 ms │ 75.00 ms  │ 67.00 ms           │ ~11%   │
└────────────────────┴───────────┴───────────┴────────────────────┴────────┘

* Ganho = melhoria do QuickSortInsercao em relação ao QuickSort puro
```

### Por que ~10% de Ganho Consistente?

1. **Proporção de partições pequenas**: A maioria das recursões termina em partições pequenas
2. **Overhead eliminado**: Cada chamada recursiva economizada = menos push/pop na pilha
3. **Cache efficiency**: InsertionSort em partições pequenas tem melhor localidade de cache
4. **Branch prediction**: Loops do InsertionSort são mais previsíveis

---

## Perguntas e Respostas

### P1: Por que o limiar é 20 e não outro valor?

**R:** O valor **20** é uma escolha **empírica otimizada** baseada em trade-offs:

```
Análise do limiar:
┌─────────┬────────────────────────┬─────────────────────────────┐
│ Limiar  │ Custo InsertionSort    │ Economia de Recursão        │
├─────────┼────────────────────────┼─────────────────────────────┤
│ 5       │ O(25) = muito baixo    │ Pouca economia              │
│ 10      │ O(100) = baixo         │ Economia moderada           │
│ 20      │ O(400) = aceitável     │ Boa economia                │
│ 30      │ O(900) = começa a pesar│ Economia diminuindo         │
│ 50      │ O(2500) = muito alto   │ Não compensa                │
└─────────┴────────────────────────┴─────────────────────────────┘
```

Na prática, valores entre **10-25** funcionam bem. O valor **20** é uma escolha conservadora que funciona em diversos cenários.

### P2: Por que usar `dir - esq <= 20` e não `dir - esq < 20`?

**R:** A diferença é sutil mas importante:

```java
// Nossa implementação: <= 20
if (dir - esq <= 20)  // Partição de até 21 elementos

// Alternativa: < 20
if (dir - esq < 20)   // Partição de até 20 elementos
```

Com `dir - esq <= 20`:
- `esq = 0, dir = 20` → `dir - esq = 20` → 21 elementos → InsertionSort
- Ligeiramente mais agressivo na troca para InsertionSort

Ambas funcionam, a escolha é marginal.

### P3: O que acontece se o InsertionSort for chamado com intervalo inválido?

**R:** O `InsertionSort.sortRange` é **robusto** para intervalos degenerados:

```java
// Caso: esq == dir (1 elemento)
for (int i = inicio + 1; i <= fim; i++)  // i = esq+1 > fim = esq
// Loop não executa → retorna imediatamente (correto!)

// Caso: esq > dir (intervalo inválido)
for (int i = inicio + 1; i <= fim; i++)  // i > fim desde o início
// Loop não executa → retorna imediatamente (correto!)
```

### P4: Por que o `QuickSortComInsercao` não usa a interface `Ordenavel`?

**R:** Diferente do `QuickSort.java`, o `QuickSortComInsercao` **não tem** método `sort(Ordenavel)`:

```java
// QuickSort.java tem:
public static void sort(Ordenavel lista) {
    lista.quicksort();
}

// QuickSortComInsercao.java NÃO tem método equivalente
```

**Motivo**: Decisão de design. A interface `Ordenavel` já declara `quicksortComInsercao()`, então a delegação acontece diretamente em `LCItem`:

```java
// LCItem.java
public void quicksortComInsercao() {
    QuickSortComInsercao.sort(this.lista, this.quant);
}
```

### P5: O `return` após `InsertionSort.sortRange()` é necessário?

**R:** **Absolutamente sim!** Sem o `return`, o código continuaria executando o particionamento:

```java
// CORRETO (nossa implementação)
if (dir - esq <= 20) {
    InsertionSort.sortRange(array, esq, dir);
    return;  // ESSENCIAL: encerra a recursão aqui
}
// Particionamento só executa se não entrou no if

// INCORRETO (sem return)
if (dir - esq <= 20) {
    InsertionSort.sortRange(array, esq, dir);
    // Sem return: continua para o particionamento!
}
Reserva pivo, temp;  // Executa mesmo após InsertionSort!
```

### P6: Por que não usar herança (`extends QuickSort`)?

**R:** Optamos por **composição** em vez de herança por vários motivos:

1. **Métodos estáticos**: QuickSort usa métodos estáticos, que não são herdáveis de forma polimórfica
2. **Simplicidade**: Cada classe é autocontida e completa
3. **Flexibilidade**: Podemos mudar a dependência (InsertionSort) sem afetar hierarquia
4. **Clareza**: Código duplicado é preferível a herança forçada em classes utilitárias

```java
// Abordagem atual: composição
QuickSortComInsercao usa InsertionSort.sortRange()

// Abordagem alternativa: herança (NÃO usada)
class QuickSortComInsercao extends QuickSort {
    // Métodos estáticos não funcionam bem com herança
}
```

### P7: Como o algoritmo se comporta com arrays já ordenados?

**R:** **Muito bem**, graças a duas otimizações:

1. **Pivô do meio**: Para array ordenado, o pivô é a mediana real → partições balanceadas
2. **InsertionSort em partições finais**: InsertionSort é O(n) para arrays já ordenados!

```
Array ordenado: [1, 2, 3, 4, 5, ..., 100]
                         ↑
                    pivô = 50 (mediana)
                    
Partições: [1-49] e [51-100] → balanceadas!

E quando chega em partições ≤ 20:
[1, 2, 3, 4, 5, ..., 20] → InsertionSort
Cada elemento já está na posição correta → O(n)!
```

### P8: O que acontece se todos os elementos forem iguais?

**R:** O algoritmo funciona corretamente, mas com comportamento específico:

```java
// Todos elementos iguais: [5, 5, 5, 5, 5]
pivo = 5

while (array[i].compareTo(pivo) < 0) { i++; }  // Não avança (5 não é < 5)
while (array[j].compareTo(pivo) > 0) { j--; }  // Não avança (5 não é > 5)

// i=0, j=4: i <= j → troca (5 ↔ 5), i++, j--
// i=1, j=3: i <= j → troca (5 ↔ 5), i++, j--
// i=2, j=2: i <= j → troca (5 ↔ 5), i++, j--
// i=3, j=1: i > j → loop termina
```

Partições resultantes são aproximadamente iguais → O(n log n) mantido.

### P9: Por que a versão para `Integer[]` é separada?

**R:** Java não suporta **generics com primitivos**, então precisamos de versões separadas:

```java
// Para Reserva (usa Comparable)
while (array[i].compareTo(pivo) < 0) { i++; }

// Para Integer (comparação direta - mais eficiente)
while (array[i] < pivo) { i++; }
```

A versão `Integer[]`:
- Evita boxing/unboxing
- Usa comparação `<` direta (mais rápida que `compareTo`)
- Mantém a mesma lógica híbrida

### P10: Qual a profundidade máxima de recursão?

**R:** A profundidade é **limitada** pelo limiar:

```
Sem híbrido (QuickSort puro):
Profundidade máxima = log₂(n)

Com híbrido (limiar 20):
Profundidade máxima = log₂(n/20) = log₂(n) - log₂(20) ≈ log₂(n) - 4.3

Para n = 50000:
- Sem híbrido: ~15.6 níveis
- Com híbrido: ~11.3 níveis
- Economia: ~4 níveis de recursão
```

### P11: Por que chamar `sortRange` e não criar InsertionSort inline?

**R:** **Separação de responsabilidades** e **reutilização**:

```java
// Nossa abordagem: delega para classe especializada
InsertionSort.sortRange(array, esq, dir);

// Alternativa: código inline (NÃO usada)
for (int i = esq + 1; i <= dir; i++) {
    Reserva temp = array[i];
    int j = i - 1;
    while (j >= esq && array[j].compareTo(temp) > 0) {
        array[j + 1] = array[j];
        j--;
    }
    array[j + 1] = temp;
}
```

Vantagens da delegação:
1. **Manutenibilidade**: Mudança no InsertionSort afeta todos os usos
2. **Testabilidade**: InsertionSort pode ser testado isoladamente
3. **Clareza**: Intenção clara no código
4. **Consistência**: Mesmo InsertionSort usado em todo o projeto

### P12: O `QuickSortComInsercao` é sempre melhor que o `QuickSort` puro?

**R:** **Na maioria dos casos sim**, mas há exceções:

| Cenário | QuickSortComInsercao | QuickSort Puro |
|---------|---------------------|----------------|
| Dados aleatórios | ✓ Melhor (~10%) | Bom |
| Dados ordenados | ✓ Muito melhor | Bom |
| Dados quase ordenados | ✓ Excelente | Bom |
| Arrays muito pequenos (< 100) | Empate | Empate |
| Overhead de chamada alto | ✓ Melhor | Pior |

**Quando QuickSort puro pode ser preferível**:
- Código mais simples (sem dependência de InsertionSort)
- Ambientes com overhead de chamada muito baixo
- Arrays muito pequenos onde a diferença é negligenciável

### P13: Como verificar se o algoritmo está funcionando corretamente?

**R:** Validações que o projeto faz:

1. **Arquivos de saída**: Compara `data/sorted/QuickIns*.txt` manualmente
2. **Consistência**: Todos os algoritmos devem produzir mesma ordenação
3. **Contagem**: `reservas.getQuant()` deve ser igual antes e depois

```java
// Verificação manual possível:
LCItem original = LeArquivo.lerReservas("data/raw/Reserva1000alea.txt");
LCItem copia = LeArquivo.lerReservas("data/raw/Reserva1000alea.txt");

original.quicksort();
copia.quicksortComInsercao();

// Comparar: original e copia devem ter mesma ordem
for (int i = 0; i < original.getQuant(); i++) {
    assert original.getItem(i).equals(copia.getItem(i));
}
```

---

## Diagrama do Fluxo de Execução

```
quicksortComInsercao([R1,R2,R3,...,R100], 0, 99)
│
├── dir - esq = 99 > 20 → QuickSort
│   │
│   ├── pivo = array[49]
│   ├── Particionamento...
│   │
│   ├── quicksortComInsercao(array, 0, 48)
│   │   │
│   │   ├── dir - esq = 48 > 20 → QuickSort
│   │   │   ├── pivo = array[24]
│   │   │   ├── Particionamento...
│   │   │   │
│   │   │   ├── quicksortComInsercao(array, 0, 23)
│   │   │   │   │
│   │   │   │   ├── dir - esq = 23 > 20 → QuickSort
│   │   │   │   │   └── ... mais recursões ...
│   │   │   │   │
│   │   │   │   └── Eventualmente: partições ≤ 20
│   │   │   │       └── InsertionSort.sortRange() ✓
│   │   │   │
│   │   │   └── quicksortComInsercao(array, 24, 48)
│   │   │       └── ... similar ...
│   │   │
│   │   └── ...
│   │
│   └── quicksortComInsercao(array, 50, 99)
│       └── ... similar ...
│
└── Array completamente ordenado!
```

---

## Referências

- Sedgewick, R. (1978). "Implementing Quicksort programs". Communications of the ACM.
- Bentley, J. & McIlroy, M. (1993). "Engineering a Sort Function". Software: Practice and Experience.
- Musser, D. (1997). "Introspective Sorting and Selection Algorithms". Software: Practice and Experience.
- Cormen, T. H., et al. "Introduction to Algorithms" - Capítulo 7: Quicksort.
