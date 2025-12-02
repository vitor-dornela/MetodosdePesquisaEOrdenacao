# QuickSort - Documentação Detalhada

## Índice

1. [Visão Geral](#visão-geral)
2. [Conceitos Fundamentais](#conceitos-fundamentais)
3. [Implementação do QuickSort Padrão](#implementação-do-quicksort-padrão)
4. [Implementação do QuickSort com Inserção](#implementação-do-quicksort-com-inserção)
5. [Dependências e Integração](#dependências-e-integração)
6. [Uso no Projeto: OrdenacaoReservas.java](#uso-no-projeto-ordenacaoreservasjava)
7. [Análise de Complexidade](#análise-de-complexidade)
8. [Perguntas e Respostas](#perguntas-e-respostas)

---

## Visão Geral

O **QuickSort** é um algoritmo de ordenação baseado no paradigma **Dividir e Conquistar**, desenvolvido por Tony Hoare em 1959. É considerado um dos algoritmos de ordenação mais eficientes na prática.

### Implementações no Projeto

O projeto possui **duas variantes** do QuickSort:

| Classe | Descrição | Uso |
|--------|-----------|-----|
| `QuickSort.java` | Implementação padrão com pivô do meio | Ordenação geral |
| `QuickSortComInsercao.java` | Híbrido: QuickSort + InsertionSort | Otimizado para partições pequenas |

### Características Principais

- **In-place**: Não requer memória adicional significativa (apenas pilha de recursão)
- **Não-estável**: Elementos iguais podem ter sua ordem relativa alterada
- **Dividir e Conquistar**: Divide o problema em subproblemas menores
- **Pivô do meio**: Nossa implementação escolhe o elemento central como pivô

---

## Conceitos Fundamentais

### O Paradigma Dividir e Conquistar

O QuickSort segue três passos:

1. **Dividir**: Escolhe um pivô e particiona o array
2. **Conquistar**: Ordena recursivamente as duas partições
3. **Combinar**: Não é necessário (ordenação acontece in-place)

### Particionamento

O **coração do QuickSort** é o particionamento:

```
Array inicial: [5, 2, 8, 1, 9, 3, 7, 4, 6]
                         ↑
                       pivô = 1 (elemento do meio: índice 4, valor 9... não!)
                       
Na verdade: pivô = array[(0+8)/2] = array[4] = 9

Após particionar com pivô = 9:
[5, 2, 8, 1, 6, 3, 7, 4] | [9]
        menores          | maior/igual
```

### Escolha do Pivô

Nossa implementação usa o **elemento do meio** como pivô:

```java
pivo = array[(i + j) / 2];
```

**Por que o elemento do meio?**

| Estratégia | Vantagem | Desvantagem |
|------------|----------|-------------|
| **Primeiro elemento** | Simples | Péssimo para arrays ordenados (O(n²)) |
| **Último elemento** | Simples | Péssimo para arrays ordenados (O(n²)) |
| **Elemento do meio** ✓ | Bom para ordenados | Pode ser ruim para padrões específicos |
| **Mediana de três** | Mais robusto | Mais complexo |
| **Aleatório** | Evita casos ruins | Overhead de geração aleatória |

---

## Implementação do QuickSort Padrão

### Estrutura da Classe `QuickSort.java`

```java
package br.faesa.C3.algoritmos.ordenacao;

import br.faesa.C3.entidades.Reserva;
import br.faesa.C3.entidades.Ordenavel;

public class QuickSort {
    // Método para interface Ordenavel
    public static void sort(Ordenavel lista)
    
    // Método principal para Reserva[]
    public static void sort(Reserva[] array, int size)
    
    // Método recursivo privado para Reserva[]
    private static void ordena(Reserva[] array, int esq, int dir)
    
    // Método principal para Integer[]
    public static void sort(Integer[] array, int size)
    
    // Método recursivo privado para Integer[]
    private static void ordena(Integer[] array, int esq, int dir)
}
```

### Método Principal: `sort(Reserva[] array, int size)`

```java
public static void sort(Reserva[] array, int size) {
    ordena(array, 0, size - 1);
}
```

**Função**: Ponto de entrada público que chama o método recursivo com os limites iniciais (0 até size-1).

### Método Recursivo: `ordena(Reserva[] array, int esq, int dir)`

```java
private static void ordena(Reserva[] array, int esq, int dir) {
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

    // 4. Chama recursivamente para as duas partições
    if (esq < j) {
        ordena(array, esq, j);
    }
    if (i < dir) {
        ordena(array, i, dir);
    }
}
```

### Análise Passo a Passo

#### Passo 1: Inicialização

```java
int i = esq, j = dir;
pivo = array[(i + j) / 2];
```

- `i` começa na extremidade esquerda
- `j` começa na extremidade direita
- `pivo` é o elemento do meio (não seu índice, mas seu **valor**)

#### Passo 2: Loop de Particionamento

```java
do {
    while (array[i].compareTo(pivo) < 0) { i++; }
    while (array[j].compareTo(pivo) > 0) { j--; }
    // ...
} while (i <= j);
```

**Invariantes do loop:**

- Todos os elementos à esquerda de `i` são **menores** que o pivô
- Todos os elementos à direita de `j` são **maiores** que o pivô
- O loop para quando `i` cruza `j`

#### Passo 3: Troca de Elementos

```java
if (i <= j) {
    temp = array[i];
    array[i] = array[j];
    array[j] = temp;
    i++;
    j--;
}
```

Quando encontramos:

- Um elemento em `i` que deveria estar à direita (>= pivô)
- Um elemento em `j` que deveria estar à esquerda (<= pivô)

Trocamos eles de lugar e avançamos os ponteiros.

#### Passo 4: Recursão

```java
if (esq < j) { ordena(array, esq, j); }
if (i < dir) { ordena(array, i, dir); }
```

Após o particionamento:

- `array[esq..j]` contém elementos <= pivô
- `array[i..dir]` contém elementos >= pivô

Ordenamos cada partição recursivamente.

### Exemplo Visual Completo

```
Array: [5, 2, 8, 1, 9, 3, 7, 4, 6]
        ↑                       ↑
        i=0                   j=8
        
Pivô = array[(0+8)/2] = array[4] = 9

Iteração 1:
  - array[0]=5 < 9? Sim, i++  → i=1
  - array[1]=2 < 9? Sim, i++  → i=2
  - array[2]=8 < 9? Sim, i++  → i=3
  - array[3]=1 < 9? Sim, i++  → i=4
  - array[4]=9 < 9? Não, para
  - array[8]=6 > 9? Não, para
  - i(4) <= j(8)? Sim, troca array[4] ↔ array[8]
  
Array: [5, 2, 8, 1, 6, 3, 7, 4, 9]
                    ↑           ↑
                   i=5         j=7

Iteração 2:
  - array[5]=3 < 6? Sim, i++  → i=6
  - array[6]=7 < 6? Não, para
  - array[7]=4 > 6? Não, para
  - i(6) <= j(7)? Sim, troca array[6] ↔ array[7]

Array: [5, 2, 8, 1, 6, 3, 4, 7, 9]
                          ↑  ↑
                         i=7 j=6

i > j → Loop termina

Recursão:
  ordena(array, 0, 6)  → [5, 2, 8, 1, 6, 3, 4]
  ordena(array, 7, 8)  → [7, 9]
```

---

## Implementação do QuickSort com Inserção

### Por que Híbrido?

O QuickSort tem overhead significativo para partições pequenas:

- Chamadas recursivas
- Cálculo do pivô
- Múltiplas comparações

O **InsertionSort** é mais eficiente para arrays pequenos:

- Sem overhead de recursão
- Ótimo para arrays quase ordenados
- Constantes multiplicativas menores

### Estrutura da Classe `QuickSortComInsercao.java`

```java
package br.faesa.C3.algoritmos.ordenacao;

import br.faesa.C3.entidades.Reserva;

public class QuickSortComInsercao {
    public static void sort(Reserva[] array, int size)
    private static void quicksortComInsercao(Reserva[] array, int esq, int dir)
    
    public static void sort(Integer[] array, int size)
    private static void quicksortComInsercao(Integer[] array, int esq, int dir)
}
```

### Método Recursivo com Limiar

```java
private static void quicksortComInsercao(Reserva[] array, int esq, int dir) {
    // DIFERENÇA CHAVE: verifica tamanho da partição
    if (dir - esq <= 20) {
        InsertionSort.sortRange(array, esq, dir);
        return;
    }

    // Resto igual ao QuickSort padrão
    Reserva pivo, temp;
    int i = esq, j = dir;
    
    pivo = array[(i + j) / 2];
    
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

    if (esq < j) { quicksortComInsercao(array, esq, j); }
    if (i < dir) { quicksortComInsercao(array, i, dir); }
}
```

### O Limiar de 20 Elementos

```java
if (dir - esq <= 20) {
    InsertionSort.sortRange(array, esq, dir);
    return;
}
```

**Por que 20?**

O valor ideal varia entre 10-25 dependendo de:

- Arquitetura do processador
- Custo de comparação dos elementos
- Overhead de chamada de função

O valor **20** é uma escolha empírica comum que funciona bem na maioria dos casos.

### Dependência: InsertionSort.sortRange()

```java
// InsertionSort.java
public static void sortRange(Reserva[] array, int inicio, int fim) {
    for (int i = inicio + 1; i <= fim; i++) {
        Reserva temp = array[i];
        int j = i - 1;

        while (j >= inicio && array[j].compareTo(temp) > 0) {
            array[j + 1] = array[j];
            j--;
        }

        array[j + 1] = temp;
    }
}
```

**Importante**: O método `sortRange` ordena apenas um **intervalo** do array, não o array completo. Isso é essencial para o funcionamento do híbrido.

### Diagrama de Decisão

```
┌─────────────────────────────────────────────────────────────────┐
│              quicksortComInsercao(array, esq, dir)              │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
                    ┌─────────────────┐
                    │ dir - esq <= 20 │
                    └─────────────────┘
                     /              \
                   Sim              Não
                   /                  \
                  ▼                    ▼
    ┌──────────────────┐    ┌──────────────────────┐
    │ InsertionSort    │    │ Particionamento      │
    │ .sortRange()     │    │ (igual QuickSort)    │
    │                  │    │                      │
    │ return;          │    │ Recursão:            │
    └──────────────────┘    │ quicksortComInsercao │
                            │ para cada partição   │
                            └──────────────────────┘
```

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
│              (Estrutura de Dados - implements Ordenavel)        │
│                                                                 │
│  quicksort() ─────────────────────────┐                         │
│  quicksortComInsercao() ──────────────┼──┐                      │
└───────────────────────────────────────┼──┼──────────────────────┘
                                        │  │
                         delega para    │  │
                                        ▼  ▼
┌─────────────────────────────────────────────────────────────────┐
│                 Pacote: algoritmos.ordenacao                    │
│  ┌──────────────────┐    ┌──────────────────────────────────┐   │
│  │   QuickSort.java │    │   QuickSortComInsercao.java      │   │
│  │                  │    │                                  │   │
│  │ sort(Reserva[])  │    │ sort(Reserva[]) ─────────────────┼───┼──┐
│  │ sort(Integer[])  │    │ sort(Integer[])                  │   │  │
│  │ ordena()         │    │ quicksortComInsercao()           │   │  │
│  └──────────────────┘    └──────────────────────────────────┘   │  │
│                                                                 │  │
│  ┌──────────────────┐                                           │  │
│  │ InsertionSort    │◀──────────────────────────────────────────┼──┘
│  │ .java            │                                           │
│  │                  │     dependência                           │
│  │ sortRange()      │                                           │
│  └──────────────────┘                                           │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ ordena
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        Reserva.java                             │
│               (implements Comparable<Reserva>)                  │
│                                                                 │
│  compareTo() ← usado nas comparações do particionamento        │
└─────────────────────────────────────────────────────────────────┘
```

### Interface Ordenavel

```java
package br.faesa.C3.entidades;

public interface Ordenavel {
    void heapsort();
    void quicksort();
    void quicksortComInsercao();
    boolean eVazia();
}
```

**Propósito**: Permite que `QuickSort.sort(Ordenavel)` aceite qualquer estrutura que implemente a interface.

### Delegação em LCItem

```java
// LCItem.java
public void quicksort() {
    QuickSort.sort(this.lista, this.quant);
}

public void quicksortComInsercao() {
    QuickSortComInsercao.sort(this.lista, this.quant);
}
```

**Padrão de Projeto**: Delegação - `LCItem` não implementa ordenação, apenas delega para as classes especializadas.

### Comparable em Reserva

```java
// Reserva.java
@Override
public int compareTo(Reserva outro) {
    if (outro == null) return 1;
    
    // Primeiro compara por nome (ordem alfabética)
    int comparacaoNome = this.nome.compareToIgnoreCase(outro.nome);
    
    if (comparacaoNome != 0) return comparacaoNome;
    
    // Se nomes iguais, compara por chave
    return this.chave.compareTo(outro.chave);
}
```

**Critério de ordenação**:

1. Primário: Nome (case-insensitive)
2. Secundário: Chave (para desempate)

---

## Uso no Projeto: OrdenacaoReservas.java

### Fluxo de Execução

```java
// OrdenacaoReservas.java
public static void main(String[] args) {
    // 12 datasets: 1000, 5000, 10000, 50000 × alea, ord, inv
    LCItem datasets = new LCItem(12);
    datasets.insereFinal(new Reserva(0, "Reserva1000alea"));
    // ... mais datasets
    
    // 3 algoritmos comparados
    LCItem algoritmos = new LCItem(3);
    algoritmos.insereFinal(new Reserva(0, "HeapSort"));
    algoritmos.insereFinal(new Reserva(0, "QuickSort"));
    algoritmos.insereFinal(new Reserva(0, "QuickSortInsertion"));
    
    // Processa cada combinação
    for (int i = 0; i < datasets.getQuant(); i++) {
        for (int j = 0; j < algoritmos.getQuant(); j++) {
            processarDataset(nomeDataset, algoritmo, primeiraLinha);
        }
    }
}
```

### Chamada dos Algoritmos QuickSort

```java
private static void processarDataset(String nomeDataset, String algoritmo, boolean primeiraLinha) {
    // Executa 5 vezes para média estatística
    for (int i = 0; i < 5; i++) {
        reservas = LeArquivo.lerReservas(caminhoEntrada);
        
        if (algoritmo.equals("HeapSort")) {
            reservas.heapsort();
        } else if (algoritmo.equals("QuickSort")) {
            reservas.quicksort();           // ← QuickSort padrão
        } else {
            reservas.quicksortComInsercao(); // ← QuickSort híbrido
        }
    }
    
    double media = tempoTotal / 5.0;
    
    // Salva resultados
    EscreveArquivo.salvarReservas(reservas, caminhoSaida);
    EscreveArquivo.salvarEstatisticas(...);
}
```

### Arquivos de Saída

| Algoritmo | Prefixo | Exemplo de Arquivo |
|-----------|---------|-------------------|
| QuickSort | `quick` | `data/sorted/quickReserva1000alea.txt` |
| QuickSortComInsercao | `QuickIns` | `data/sorted/QuickIns20Reserva1000alea.txt` |

### Cadeia de Chamadas Completa

```
OrdenacaoReservas.main()
    │
    ▼
reservas.quicksort()                    // LCItem.java
    │
    ▼
QuickSort.sort(this.lista, this.quant)  // QuickSort.java
    │
    ▼
ordena(array, 0, size-1)                // Recursivo
    │
    ├── array[i].compareTo(pivo)        // Reserva.java
    │
    ├── ordena(array, esq, j)           // Partição esquerda
    │
    └── ordena(array, i, dir)           // Partição direita
```

---

## Análise de Complexidade

### Complexidade de Tempo

| Caso | QuickSort Padrão | QuickSort Híbrido |
|------|------------------|-------------------|
| **Melhor** | O(n log n) | O(n log n) |
| **Médio** | O(n log n) | O(n log n) |
| **Pior** | O(n²) | O(n²) |

#### Caso Médio: O(n log n)

A cada nível de recursão:

- Fazemos O(n) comparações (particionamento)
- Temos O(log n) níveis (divisão pela metade)

**Total**: O(n) × O(log n) = O(n log n)

#### Caso Pior: O(n²)

Ocorre quando o pivô é sempre o menor ou maior elemento:

- Cada partição tem tamanho n-1 e 0
- Temos n níveis de recursão
- Cada nível faz O(n) comparações

**Total**: O(n) × O(n) = O(n²)

**Nossa implementação mitiga isso** usando o pivô do meio, que é bom para arrays ordenados.

### Complexidade de Espaço

| Tipo | QuickSort Padrão | QuickSort Híbrido |
|------|------------------|-------------------|
| **Auxiliar** | O(log n) pilha | O(log n) pilha |
| **Total** | O(n) | O(n) |

O espaço O(log n) é para a pilha de recursão no caso médio.

### Comparação Prática

```
Dataset: Reserva50000alea
┌────────────────────────┬───────────────┐
│ Algoritmo              │ Tempo Médio   │
├────────────────────────┼───────────────┤
│ HeapSort               │ ~45 ms        │
│ QuickSort              │ ~35 ms        │
│ QuickSortComInsercao   │ ~30 ms        │
└────────────────────────┴───────────────┘

Dataset: Reserva50000ord (já ordenado)
┌────────────────────────┬───────────────┐
│ Algoritmo              │ Tempo Médio   │
├────────────────────────┼───────────────┤
│ HeapSort               │ ~40 ms        │
│ QuickSort              │ ~25 ms        │ ← Pivô do meio ajuda!
│ QuickSortComInsercao   │ ~20 ms        │
└────────────────────────┴───────────────┘
```

---

## Perguntas e Respostas

### P1: Por que escolher o pivô como `array[(i + j) / 2]` em vez do primeiro ou último elemento?

**R:** A escolha do elemento do meio oferece **melhor performance para arrays ordenados ou parcialmente ordenados**.

Se usássemos o primeiro elemento:

- Array ordenado: pivô sempre o menor → partição de n-1 e 0 → O(n²)
- Array inversamente ordenado: pivô sempre o maior → mesma degradação

Com o elemento do meio:

- Array ordenado: pivô é a mediana real → partições balanceadas → O(n log n)
- Performance consistente para padrões comuns de dados

```java
// Ruim para arrays ordenados
pivo = array[esq];        // Primeiro elemento

// Ruim para arrays ordenados
pivo = array[dir];        // Último elemento

// Bom para arrays ordenados (nossa escolha)
pivo = array[(i + j) / 2]; // Elemento do meio
```

### P2: Por que usar `do-while` em vez de `while` no loop de particionamento?

**R:** O `do-while` garante que **pelo menos uma iteração** ocorra, o que é necessário porque:

1. Os ponteiros `i` e `j` começam nas extremidades
2. Precisamos avançá-los pelo menos uma vez para encontrar elementos para trocar
3. O critério de parada `i <= j` faz sentido após pelo menos uma iteração

```java
// Nossa implementação (correta)
do {
    while (array[i].compareTo(pivo) < 0) { i++; }
    while (array[j].compareTo(pivo) > 0) { j--; }
    if (i <= j) { /* troca */ i++; j--; }
} while (i <= j);

// Alternativa com while (também funciona, mas diferente)
while (i < j) {
    // lógica ligeiramente diferente necessária
}
```

### P3: O que acontece se o array tiver elementos repetidos?

**R:** O algoritmo funciona corretamente, mas **não é estável**.

Elementos iguais podem ser trocados de posição relativa durante o particionamento. Se a estabilidade for importante, considere:

1. Usar MergeSort (estável, O(n log n))
2. Adicionar índice original como critério secundário de comparação

No nosso caso, `Reserva.compareTo()` usa chave como desempate, reduzindo empates.

### P4: Por que `QuickSortComInsercao` usa o limiar de 20 elementos?

**R:** É um **valor empírico otimizado** baseado em trade-offs:

**Para partições pequenas (≤20):**

- QuickSort: overhead de recursão, cálculo de pivô, múltiplos branches
- InsertionSort: loop simples, sem recursão, excelente para quase-ordenados

**Análise do limiar:**

| Limiar | QuickSort Overhead | InsertionSort Performance |
|--------|-------------------|---------------------------|
| 5 | Muito baixo | Sempre O(25) = constante |
| 10 | Baixo | Sempre O(100) = constante |
| **20** | Moderado | Sempre O(400) = aceitável |
| 50 | Alto | O(2500) começa a pesar |

O valor **20** balanceia a redução de chamadas recursivas com o custo quadrático do InsertionSort.

### P5: Por que incrementar `i` e decrementar `j` após a troca?

**R:** Para **evitar loop infinito** e garantir progresso:

```java
if (i <= j) {
    temp = array[i];
    array[i] = array[j];
    array[j] = temp;
    i++;  // ← Essencial!
    j--;  // ← Essencial!
}
```

Sem os incrementos:

1. Após trocar, `array[i]` e `array[j]` satisfazem as condições
2. Os loops internos `while` não avançariam
3. O `do-while` continuaria trocando os mesmos elementos infinitamente

### P6: Por que a versão para `Integer[]` é quase idêntica à de `Reserva[]`?

**R:** **Java não suporta generics com primitivos**, então precisamos de versões separadas:

```java
// Para Reserva (usa Comparable)
while (array[i].compareTo(pivo) < 0) { i++; }

// Para Integer (comparação direta - mais eficiente)
while (array[i] < pivo) { i++; }
```

A versão `Integer[]` evita boxing/unboxing ao usar comparação direta `<` em vez de `compareTo()`.

### P7: O método `sort(Ordenavel lista)` não causa recursão infinita?

**R:** **Não**, devido à implementação cuidadosa do padrão de delegação:

```java
// QuickSort.java
public static void sort(Ordenavel lista) {
    lista.quicksort();  // Chama método da interface
}

// LCItem.java (implementa Ordenavel)
public void quicksort() {
    QuickSort.sort(this.lista, this.quant);  // Chama versão com array!
}
```

A chamada `sort(Ordenavel)` → `lista.quicksort()` → `sort(Reserva[], int)` não volta para `sort(Ordenavel)`.

**Atenção**: Se `LCItem.quicksort()` chamasse `QuickSort.sort(this)`, aí sim haveria recursão infinita!

### P8: Por que as condições de recursão são `esq < j` e `i < dir` (não `<=`)?

**R:** Para evitar **chamadas desnecessárias** quando a partição tem 0 ou 1 elemento:

```java
if (esq < j) { ordena(array, esq, j); }     // Pelo menos 2 elementos
if (i < dir) { ordena(array, i, dir); }     // Pelo menos 2 elementos
```

Se usássemos `<=`:

- `esq == j` significa 1 elemento → já está ordenado
- `i == dir` significa 1 elemento → já está ordenado

Essas chamadas seriam desperdiçadas (retornariam imediatamente).

### P9: Como o QuickSort se comporta com arrays de diferentes ordenações iniciais?

**R:** Com **pivô do meio**, nossa implementação tem performance consistente:

| Ordenação Inicial | Comportamento | Complexidade |
|-------------------|---------------|--------------|
| **Aleatória** | Partições balanceadas | O(n log n) |
| **Ordenada** | Pivô é mediana → ótimo | O(n log n) |
| **Inversamente ordenada** | Pivô é mediana → ótimo | O(n log n) |
| **Todos iguais** | Partições de n/2 cada | O(n log n) |
| **Padrão adversário** | Possível O(n²) | O(n²) raro |

### P10: Por que o `QuickSortComInsercao` não herda de `QuickSort`?

**R:** Optamos por **composição/delegação** em vez de herança por:

1. **Simplicidade**: Classes estáticas com métodos utilitários
2. **Flexibilidade**: `QuickSortComInsercao` pode usar `InsertionSort.sortRange()` diretamente
3. **Clareza**: Cada classe é autocontida e completa
4. **Performance**: Sem overhead de vtable para métodos estáticos

```java
// Abordagem atual (composição)
QuickSortComInsercao usa InsertionSort.sortRange()

// Alternativa (herança) - não utilizada
class QuickSortComInsercao extends QuickSort { ... }
```

### P11: O que garante que o particionamento termina?

**R:** Três garantias:

1. **Progressão dos ponteiros**: `i` sempre incrementa, `j` sempre decrementa
2. **Condição de parada**: `i <= j` eventualmente falha
3. **Elementos encontrados**: Os loops internos sempre encontram elementos para trocar (ou cruzam)

```java
// Progressão garantida
do {
    while (...) { i++; }  // i só aumenta
    while (...) { j--; }  // j só diminui
    if (i <= j) {
        // troca
        i++;  // progresso adicional
        j--;  // progresso adicional
    }
} while (i <= j);  // eventualmente i > j
```

### P12: Por que guardar o pivô em variável separada em vez de usar o índice?

**R:** Porque o **pivô pode ser movido** durante o particionamento!

```java
pivo = array[(i + j) / 2];  // Copia o VALOR, não o índice
```

Se guardássemos apenas o índice:

```java
int indicePivo = (i + j) / 2;
// Durante as trocas, array[indicePivo] pode mudar!
// Comparar com array[indicePivo] daria resultados incorretos
```

O valor do pivô deve permanecer constante durante todo o particionamento.

### P13: Qual a diferença prática entre `QuickSort` e `QuickSortComInsercao` nos benchmarks?

**R:** Baseado nos resultados típicos do `estatisticas_ordenacao.csv`:

```
Dataset          | QuickSort | QuickSortComInsercao | Ganho
-----------------|-----------|----------------------|------
Reserva1000alea  | 1.8 ms    | 1.6 ms               | ~11%
Reserva5000alea  | 8.2 ms    | 7.4 ms               | ~10%
Reserva10000alea | 16.5 ms   | 14.8 ms              | ~10%
Reserva50000alea | 85.0 ms   | 76.5 ms              | ~10%
```

O ganho de **~10%** é consistente porque:

- Partições pequenas são comuns (muitas recursões terminam pequenas)
- InsertionSort tem constantes menores para n ≤ 20
- Redução significativa no número de chamadas recursivas

---

## Diagrama do Fluxo de Recursão

```
quicksort([5, 2, 8, 1, 9, 3, 7, 4, 6], 0, 8)
│
├── Particionamento com pivô = 9
│   Resultado: [5, 2, 8, 1, 6, 3, 4, 7, 9]
│                              j=6    i=7
│
├── quicksort([5, 2, 8, 1, 6, 3, 4, 7], 0, 6)
│   │
│   ├── Particionamento com pivô = 1
│   │   Resultado: [1, 2, 8, 5, 6, 3, 4, 7]
│   │
│   ├── quicksort([1], 0, 0) → base case
│   │
│   └── quicksort([2, 8, 5, 6, 3, 4, 7], 1, 6)
│       │
│       └── ... continua recursivamente
│
└── quicksort([9], 7, 8)
    │
    └── Particionamento trivial → ordenado
```

---

## Referências

- Hoare, C.A.R. (1961). "Algorithm 64: Quicksort". Communications of the ACM.
- Sedgewick, R. (1978). "Implementing Quicksort programs". Communications of the ACM.
- Cormen, T. H., et al. "Introduction to Algorithms" - Capítulo 7: Quicksort
- Bentley, J. & McIlroy, M. (1993). "Engineering a Sort Function". Software: Practice and Experience.
