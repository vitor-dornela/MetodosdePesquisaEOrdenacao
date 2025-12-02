# HeapSort - Documentação Detalhada

## Índice
1. [Visão Geral](#visão-geral)
2. [Conceitos Fundamentais](#conceitos-fundamentais)
3. [Implementação](#implementação)
4. [Análise de Complexidade](#análise-de-complexidade)
5. [Perguntas e Respostas](#perguntas-e-respostas)
6. [Uso no Projeto: OrdenacaoReservas.java](#uso-no-projeto-ordenacaoreservasjava)

---

## Visão Geral

O **HeapSort** é um algoritmo de ordenação baseado em comparação que utiliza uma estrutura de dados chamada **heap** (especificamente um **max-heap**) para ordenar elementos. Foi desenvolvido por J.W.J. Williams em 1964.

### Características Principais
- **In-place**: Não requer memória adicional significativa (O(1) de espaço auxiliar)
- **Não-estável**: Elementos iguais podem ter sua ordem relativa alterada
- **Complexidade garantida**: O(n log n) em todos os casos (melhor, médio e pior)

---

## Conceitos Fundamentais

### O que é um Heap?

Um **heap** é uma árvore binária completa que satisfaz a **propriedade de heap**:
- **Max-Heap**: O valor de cada nó é maior ou igual aos valores de seus filhos
- **Min-Heap**: O valor de cada nó é menor ou igual aos valores de seus filhos

### Representação em Array

A grande vantagem do heap é que pode ser representado eficientemente em um array:

```
        [0]
       /   \
     [1]   [2]
    /  \   /  \
  [3] [4] [5] [6]
```

**Relações de índices** (para índice base 0):
- **Pai** do nó `i`: `(i - 1) / 2`
- **Filho esquerdo** do nó `i`: `2 * i + 1`
- **Filho direito** do nó `i`: `2 * i + 2`

### Exemplo Visual

Array: `[4, 10, 3, 5, 1]`

```
Árvore inicial:          Max-Heap resultante:
      4                        10
     / \                      /  \
   10   3        →           5    3
   / \                      / \
  5   1                    4   1

Array após heapify: [10, 5, 3, 4, 1]
```

---

## Implementação

### Estrutura da Classe

Nossa implementação possui **três métodos sobrecarregados** para diferentes tipos de dados:

```java
public class HeapSort {
    // 1. Método genérico para Ordenavel
    public static void sort(Ordenavel lista)
    
    // 2. Método para array de Reserva
    public static void sort(Reserva[] array, int size)
    
    // 3. Método para array de Integer
    public static void sort(Integer[] array, int size)
}
```

### Método Principal: `sort(Reserva[] array, int size)`

```java
public static void sort(Reserva[] array, int size) {
    int dir = size - 1, esq = (dir - 1) / 2;
    Reserva temp;

    // FASE 1: Construção do Max-Heap (bottom-up)
    while (esq >= 0) {
        refazheap(array, esq, size - 1);
        esq--;
    }

    // FASE 2: Extração ordenada
    while (dir > 0) {
        temp = array[0];           // Guarda o maior (raiz)
        array[0] = array[dir];     // Move último para raiz
        array[dir] = temp;         // Coloca maior na posição final
        dir--;                     // Reduz heap
        refazheap(array, 0, dir);  // Restaura propriedade
    }
}
```

#### Fase 1: Construção do Max-Heap

**Objetivo**: Transformar o array em um max-heap válido.

**Estratégia Bottom-Up**:
- Começa pelo **último nó não-folha**: `(size - 1 - 1) / 2 = (dir - 1) / 2`
- Processa cada nó de baixo para cima até a raiz
- Folhas (metade direita do array) já são heaps válidos trivialmente

**Por que começar do último nó não-folha?**
- Nós folha não têm filhos, então já satisfazem a propriedade de heap
- Economiza processamento desnecessário

```
Array: [4, 10, 3, 5, 1]  (size = 5)
dir = 4
esq = (4 - 1) / 2 = 1  ← Último nó não-folha

Índices: [0, 1, 2, 3, 4]
              ↑
         Começa aqui
```

#### Fase 2: Extração Ordenada

**Processo repetitivo**:
1. O maior elemento está sempre na raiz (índice 0)
2. Troca raiz com o último elemento do heap
3. Reduz o tamanho lógico do heap (`dir--`)
4. Restaura a propriedade de max-heap com `refazheap`

```
Heap:    [10, 5, 3, 4, 1]    Ordenado: []
Troca:   [1, 5, 3, 4, | 10]  Ordenado: [10]
Refaz:   [5, 4, 3, 1, | 10]  
Troca:   [1, 4, 3, | 5, 10]  Ordenado: [5, 10]
Refaz:   [4, 1, 3, | 5, 10]
...continua até ordenar completamente
```

### Método `refazheap` (Heapify/Sift-Down)

```java
private static void refazheap(Reserva[] array, int esq, int dir) {
    int i = esq, maiorFilho = 2 * i + 1;
    Reserva raiz = array[i];
    boolean heap = false;

    while ((maiorFilho <= dir) && (!heap)) {
        // Encontra o maior filho
        if (maiorFilho < dir)
            if (array[maiorFilho].compareTo(array[maiorFilho + 1]) < 0)
                maiorFilho++;
        
        // Compara raiz com maior filho
        if (raiz.compareTo(array[maiorFilho]) < 0) {
            array[i] = array[maiorFilho];  // Sobe o filho
            i = maiorFilho;                 // Desce para próximo nível
            maiorFilho = 2 * i + 1;         // Calcula novo filho esquerdo
        } else
            heap = true;  // Propriedade satisfeita
    }
    array[i] = raiz;  // Coloca raiz na posição correta
}
```

#### Parâmetros:
- `esq`: Índice do nó a ser "afundado" (sift-down)
- `dir`: Limite direito do heap (elementos após `dir` já estão ordenados)

#### Lógica Detalhada:

1. **Guarda o valor da raiz** temporariamente
2. **Loop de descida**: enquanto houver filhos dentro do heap
   - Encontra o **maior dos dois filhos**
   - Se raiz < maior filho: "sobe" o filho e continua descendo
   - Senão: para (propriedade de heap restaurada)
3. **Posiciona a raiz** no local correto encontrado

#### Por que guardar a raiz e só atribuir no final?

**Otimização importante**: Em vez de fazer trocas completas a cada nível (3 operações), fazemos apenas atribuições (1 operação) e uma atribuição final. Reduz operações de ~3n para ~n+1.

```java
// Versão NÃO otimizada (mais trocas):
if (raiz < filho) {
    temp = array[i];
    array[i] = array[maiorFilho];
    array[maiorFilho] = temp;  // 3 operações
}

// Versão otimizada (nossa implementação):
if (raiz < filho) {
    array[i] = array[maiorFilho];  // 1 operação
}
// ... no final:
array[i] = raiz;  // 1 operação adicional
```

---

## Análise de Complexidade

### Complexidade de Tempo

| Caso | Complexidade | Explicação |
|------|-------------|------------|
| **Melhor** | O(n log n) | Mesmo com array ordenado |
| **Médio** | O(n log n) | Comportamento consistente |
| **Pior** | O(n log n) | Garantido, sem degradação |

#### Detalhamento:

**Construção do Heap (Fase 1)**: O(n)
- Surpreendentemente linear, não O(n log n)!
- A maioria dos nós está perto das folhas e requer poucos "afundamentos"

**Extração (Fase 2)**: O(n log n)
- n extrações, cada uma com refazheap de O(log n)

### Complexidade de Espaço

| Tipo | Complexidade |
|------|-------------|
| **Espaço Auxiliar** | O(1) |
| **Total** | O(n) |

O HeapSort é **in-place**: usa apenas variáveis temporárias, sem arrays auxiliares.

### Comparação com Outros Algoritmos

| Algoritmo | Melhor | Médio | Pior | Espaço | Estável |
|-----------|--------|-------|------|--------|---------|
| **HeapSort** | O(n log n) | O(n log n) | O(n log n) | O(1) | Não |
| QuickSort | O(n log n) | O(n log n) | O(n²) | O(log n) | Não |
| MergeSort | O(n log n) | O(n log n) | O(n log n) | O(n) | Sim |

---

## Perguntas e Respostas

### P1: Por que usar `(dir - 1) / 2` como ponto de partida na construção do heap?

**R:** Este cálculo encontra o **último nó não-folha** do array. Em um heap representado em array:
- Nós folha ocupam aproximadamente a metade direita do array (índices `⌊n/2⌋` até `n-1`)
- Começar pelas folhas seria desperdício, pois elas já são heaps válidos (não têm filhos)
- `(size - 1 - 1) / 2 = (dir - 1) / 2` nos dá exatamente o índice do último nó que tem pelo menos um filho

### P2: Por que a variável se chama `esq` se ela representa o índice atual?

**R:** A nomenclatura `esq` (esquerda) e `dir` (direita) referem-se aos **limites lógicos do heap**:
- `esq`: limite esquerdo - até onde já processamos na construção
- `dir`: limite direito - até onde o heap ainda é válido

Na **Fase 1**, `esq` decresce de `(dir-1)/2` até 0, processando cada subárvore.
Na **Fase 2**, `dir` decresce indicando que elementos após ele já estão ordenados.

### P3: O que acontece se dois elementos forem iguais? O algoritmo é estável?

**R:** **Não, o HeapSort não é estável.** Elementos com chaves iguais podem ter sua ordem relativa alterada durante as operações de troca e reconstrução do heap.

No nosso caso com `Reserva`, usamos `compareTo()` que compara primeiro por `nome` e depois por `chave`, minimizando empates. Porém, se dois objetos tiverem mesmo nome E mesma chave, a estabilidade não é garantida.

### P4: Por que guardar `raiz` no início de `refazheap` e só atribuir no final?

**R:** **Otimização de performance!** 

Comparando as abordagens:
- **Com trocas**: Cada nível descido requer 3 operações (temp = A, A = B, B = temp)
- **Nossa abordagem**: Cada nível requer 1 operação (A = B), mais 1 atribuição final

Para um heap de altura h, economizamos aproximadamente 2h operações por chamada de `refazheap`. Com n chamadas, a economia é significativa.

### P5: Por que verificar `maiorFilho < dir` antes de comparar os filhos?

**R:** Para evitar **acesso fora dos limites do array**!

```java
if (maiorFilho < dir)  // Verifica se existe filho direito
    if (array[maiorFilho].compareTo(array[maiorFilho + 1]) < 0)
        maiorFilho++;
```

- `maiorFilho` é o filho esquerdo: `2 * i + 1`
- `maiorFilho + 1` seria o filho direito: `2 * i + 2`
- Se `maiorFilho == dir`, só existe o filho esquerdo (o direito estaria fora do heap)

### P6: Qual a diferença entre as versões para `Reserva[]` e `Integer[]`?

**R:** A lógica é **idêntica**, apenas o método de comparação muda:

```java
// Para Reserva (usa Comparable)
if (array[maiorFilho].compareTo(array[maiorFilho + 1]) < 0)

// Para Integer (comparação direta)
if (array[maiorFilho] < array[maiorFilho + 1])
```

`Reserva` implementa `Comparable<Reserva>`, então usamos `compareTo()`. Para `Integer`, a comparação direta com `<` é mais eficiente.

### P7: Por que a construção do heap é O(n) e não O(n log n)?

**R:** Análise matemática surpreendente!

Intuitivamente parece O(n log n): n nós × log n altura. Porém:
- **Metade** dos nós são folhas (0 operações)
- **1/4** dos nós têm altura 1 (máximo 1 comparação)
- **1/8** dos nós têm altura 2 (máximo 2 comparações)
- E assim por diante...

A soma converge para: `n * Σ(h/2^h)` para h de 0 a log n, que resulta em **O(n)**.

### P8: Como o HeapSort se comporta com arrays já ordenados?

**R:** **Sempre O(n log n)**, independente da ordenação inicial!

Diferente do QuickSort (que pode degradar para O(n²) com escolha ruim de pivô), o HeapSort:
- Sempre constrói o heap completo
- Sempre faz n extrações com log n operações cada

Isso é uma **vantagem** para dados adversários, mas **desvantagem** se os dados já estiverem quase ordenados (outros algoritmos como InsertionSort seriam mais rápidos).

### P9: Por que usar `while` com flag `heap` em vez de `break`?

**R:** Questão de estilo e clareza. Ambas abordagens funcionam:

```java
// Nossa implementação (com flag)
boolean heap = false;
while ((maiorFilho <= dir) && (!heap)) {
    // ...
    if (condição) { ... } 
    else heap = true;
}

// Alternativa (com break)
while (maiorFilho <= dir) {
    // ...
    if (condição) { ... }
    else break;
}
```

O uso de flag torna explícito que há duas condições de parada: limite do array E propriedade de heap satisfeita.

### P10: O método `sort(Ordenavel lista)` não causa recursão infinita?

**R:** **Não**, desde que `LCItem.heapsort()` chame `HeapSort.sort(array, size)` diretamente!

```java
// Em HeapSort.java
public static void sort(Ordenavel lista) {
    lista.heapsort();  // Chama o método da interface
}

// Em LCItem.java
public void heapsort() {
    HeapSort.sort(this.lista, this.quant);  // Chama versão com array
}
```

A chamada `sort(Ordenavel)` → `lista.heapsort()` → `sort(Reserva[], int)` não volta para `sort(Ordenavel)`.

**Cuidado**: Se `LCItem.heapsort()` chamasse `HeapSort.sort(this)`, aí sim teríamos recursão infinita!

---

## Diagrama do Fluxo de Execução

```
┌─────────────────────────────────────────────────────────────────┐
│                    HeapSort.sort(array, size)                   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  FASE 1: Construção do Max-Heap                                 │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ for esq = (size-2)/2 até 0:                             │    │
│  │     refazheap(array, esq, size-1)                       │    │
│  └─────────────────────────────────────────────────────────┘    │
│  Resultado: Array transformado em Max-Heap válido               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  FASE 2: Extração Ordenada                                      │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │ for dir = size-1 até 1:                                 │    │
│  │     troca(array[0], array[dir])                         │    │
│  │     refazheap(array, 0, dir-1)                          │    │
│  └─────────────────────────────────────────────────────────┘    │
│  Resultado: Array completamente ordenado                        │
└─────────────────────────────────────────────────────────────────┘
```

---

## Uso no Projeto: OrdenacaoReservas.java

O HeapSort é utilizado no programa principal de benchmark `OrdenacaoReservas.java` para comparar performance entre algoritmos de ordenação.

### Estrutura do Benchmark

```java
// Lista de datasets processados
String[] datasets = {
    "Reserva1000alea", "Reserva1000ord", "Reserva1000inv",
    "Reserva5000alea", "Reserva5000ord", "Reserva5000inv",
    "Reserva10000alea", "Reserva10000ord", "Reserva10000inv",
    "Reserva50000alea", "Reserva50000ord", "Reserva50000inv"
};

// Algoritmos comparados
String[] algoritmos = {"HeapSort", "QuickSort", "QuickSortInsertion"};
```

### Fluxo de Execução

```
┌─────────────────────────────────────────────────────────────────┐
│                    OrdenacaoReservas.main()                     │
└─────────────────────────────────────────────────────────────────┘
                              │
          ┌───────────────────┼───────────────────┐
          ▼                   ▼                   ▼
    ┌──────────┐        ┌──────────┐        ┌──────────┐
    │ HeapSort │        │ QuickSort│        │QuickIns20│
    └──────────┘        └──────────┘        └──────────┘
          │                   │                   │
          └───────────────────┼───────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  Para cada dataset (12 total):                                  │
│  1. Carrega reservas do arquivo                                 │
│  2. Executa ordenação 5 vezes                                   │
│  3. Calcula média de tempo                                      │
│  4. Salva resultado ordenado                                    │
│  5. Registra estatísticas em CSV                                │
└─────────────────────────────────────────────────────────────────┘
```

### Código de Chamada do HeapSort

```java
private static void processarDataset(String nomeDataset, String algoritmo, boolean primeiraLinha) {
    // ...
    
    // Executa 5 vezes para calcular média
    for (int i = 0; i < 5; i++) {
        // Carrega o dataset (cria nova instância a cada iteração)
        reservas = LeArquivo.lerReservas(caminhoEntrada);
        
        // Chama o método apropriado via delegação
        if (algoritmo.equals("HeapSort")) {
            reservas.heapsort();  // LCItem delega para HeapSort.sort(array, size)
        } else if (algoritmo.equals("QuickSort")) {
            reservas.quicksort();
        } else {
            reservas.quicksortComInsercao();
        }
    }
    
    // Calcula média
    double media = tempoTotal / 5.0;
    
    // Salva resultados
    EscreveArquivo.salvarReservas(reservas, caminhoSaida);
    EscreveArquivo.salvarEstatisticas("data/estatisticas_ordenacao.csv", ...);
}
```

### Por que Executar 5 Vezes?

A execução múltipla é uma **técnica de benchmarking** para obter medições mais confiáveis:

1. **Warm-up da JVM**: A primeira execução pode ser mais lenta devido ao JIT (Just-In-Time compilation)
2. **Variação do sistema**: Processos em background podem afetar medições individuais
3. **Cache effects**: Acessos subsequentes à memória podem ser mais rápidos
4. **Média estatística**: Reduz o impacto de outliers

### Arquivos de Saída

| Entrada | Saída HeapSort |
|---------|----------------|
| `data/raw/Reserva1000alea.txt` | `data/sorted/heapReserva1000alea.txt` |
| `data/raw/Reserva5000ord.txt` | `data/sorted/heapReserva5000ord.txt` |
| `data/raw/Reserva10000inv.txt` | `data/sorted/heapReserva10000inv.txt` |
| ... | ... |

### Estatísticas Geradas

O arquivo `data/estatisticas_ordenacao.csv` contém:

```csv
Dataset,Algoritmo,Elementos,Media(ms)
Reserva1000alea,HeapSort,1000,2.40
Reserva1000alea,QuickSort,1000,1.80
Reserva1000alea,QuickSortInsertion,1000,1.60
...
```

### Padrão de Delegação

O benchmark usa o **padrão de delegação** implementado no projeto:

```
OrdenacaoReservas.java          LCItem.java                   HeapSort.java
        │                            │                              │
        │  reservas.heapsort()       │                              │
        ├──────────────────────────▶ │                              │
        │                            │  HeapSort.sort(lista, quant) │
        │                            ├─────────────────────────────▶│
        │                            │                              │
        │                            │◀─────────────────────────────┤
        │◀───────────────────────────┤                              │
```

Este padrão permite:
- **Encapsulamento**: `LCItem` esconde detalhes de implementação
- **Flexibilidade**: Fácil trocar algoritmos sem alterar código cliente
- **Testabilidade**: Cada componente pode ser testado isoladamente

---

## Perguntas Adicionais sobre o Benchmark

### P11: Por que recarregar o dataset a cada iteração do loop de 5 execuções?

**R:** Porque a ordenação **modifica o array in-place**! Se não recarregarmos:
- Na 2ª execução, o array já estaria ordenado
- Isso mediria o tempo de "ordenar algo já ordenado"
- Os resultados não refletiriam o caso de uso real

```java
for (int i = 0; i < 5; i++) {
    reservas = LeArquivo.lerReservas(caminhoEntrada);  // ESSENCIAL!
    reservas.heapsort();
}
```

### P12: O tempo de leitura do arquivo está incluído na medição?

**R:** **Sim**, o tempo de `LeArquivo.lerReservas()` está incluído no benchmark. Isso pode ser considerado uma limitação metodológica, mas:
- Reflete o cenário real de uso (carregar + ordenar)
- A leitura de arquivo é consistente entre algoritmos
- Para datasets maiores, o tempo de ordenação domina

Uma versão mais precisa mediria apenas a ordenação:
```java
// Versão mais precisa (não implementada)
reservas = LeArquivo.lerReservas(caminhoEntrada);
long inicio = System.currentTimeMillis();
reservas.heapsort();
long fim = System.currentTimeMillis();
```

### P13: Por que usar `LCItem` para armazenar nomes de datasets e algoritmos?

**R:** Demonstra o uso consistente da estrutura de dados do projeto! Mesmo para listas simples de strings, usamos `LCItem` com objetos `Reserva`:

```java
LCItem datasets = new LCItem(12);
datasets.insereFinal(new Reserva(0, "Reserva1000alea"));
```

Isso é um pouco "over-engineering" para strings simples, mas:
- Mantém consistência com o resto do projeto
- Demonstra domínio da estrutura `LCItem`
- Evita misturar `String[]` com `LCItem` no código

---

## Referências

- Williams, J.W.J. (1964). "Algorithm 232: Heapsort". Communications of the ACM.
- Cormen, T. H., et al. "Introduction to Algorithms" - Capítulo 6: Heapsort
- Sedgewick, R. "Algorithms" - Seção sobre Priority Queues e Heapsort
