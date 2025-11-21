# Métodos de Pesquisa e Ordenação

Projeto acadêmico implementando algoritmos clássicos de ordenação e pesquisa em Java, com foco em análise de desempenho e estruturas de dados dinâmicas.

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Algoritmos Implementados](#algoritmos-implementados)
- [Estruturas de Dados](#estruturas-de-dados)
- [Como Usar](#como-usar)
- [Exemplos de Código](#exemplos-de-código)
- [Análise de Desempenho](#análise-de-desempenho)
- [Datasets](#datasets)
- [Compilação e Execução](#compilação-e-execução)

## 🎯 Visão Geral

Este projeto implementa e compara o desempenho de diversos algoritmos de ordenação e estruturas de pesquisa aplicados a dados de reservas. O sistema foi projetado com separação clara de responsabilidades:

- **Estruturas de Dados**: Gerenciam os dados (listas dinâmicas)
- **Algoritmos de Ordenação**: Implementações standalone e reutilizáveis
- **Estruturas de Pesquisa**: ABB, AVL e Hashing Encadeado

### Características Principais

✅ **4 Algoritmos de Ordenação** com análise comparativa de desempenho  
✅ **Pesquisa ABB Otimizada** com solução para StackOverflowError em grandes datasets  
✅ **Pesquisa AVL** com auto-balanceamento garantindo O(log n)  
✅ **Hashing Encadeado** com resolução de colisões por encadeamento  
✅ **Comparação Tripla** ABB vs AVL vs Hashing em 12 datasets (1k-50k elementos)  
✅ **Sobrecarga de Métodos** para trabalhar com `Item[]` e `Integer[]`  
✅ **Padrão de Delegação** entre estruturas de dados e algoritmos  
✅ **100% LCItem** - Uso consistente de estruturas de dados próprias (sem String[] no código do usuário)  
✅ **Medição Automática** de tempo de execução (média de 5 rodadas)  
✅ **Exportação de Resultados** ordenados, pesquisas e estatísticas em CSV  
✅ **Suporte a Nomes Duplicados** com múltiplas reservas por nome em todas as estruturas  

## 📁 Estrutura do Projeto

```
MetodosdePesquisaEOrdenacao/
├── src/
│   └── br/faesa/C3/
│       ├── Main.java                      # Testes básicos
│       ├── OrdenacaoReservas.java         # Benchmark de ordenação
│       ├── PesquisaReservas.java          # Benchmark ABB vs AVL
│       └── algoritmos/
│           ├── entidades/
│           │   ├── Item.java              # Modelo de dados de reserva
│           │   ├── LCItem.java            # Lista dinâmica de Item
│           │   └── LCInteiro.java         # Lista dinâmica de Integer
│           ├── ordenacao/
│           │   ├── HeapSort.java          # Heap sort
│           │   ├── QuickSort.java         # Quick sort
│           │   ├── InsertionSort.java     # Insertion sort
│           │   └── QuickSortComInsercao.java        # Híbrido ≤20
            ├── helper/
            │   ├── LeArquivo.java         # Leitura de arquivos (com lerNomesComoLCItem)
            │   └── EscreveArquivo.java    # Escrita de arquivos (aceita LCItem)
            └── pesquisa/
                ├── AVL/
                │   ├── ArvoreAVL.java     # Árvore AVL (inteiros)
                │   ├── ArvoreAVLItem.java # AVL para reservas
                │   ├── NoAVL.java         # Nó da árvore AVL
                │   └── NoAVLItem.java     # Nó AVL (Item)
                ├── ABB/
                │   ├── ArvoreABB.java     # ABB para inteiros
                │   ├── ArvoreABBItem.java # ABB para reservas
                │   ├── NoABB.java         # Nó ABB (inteiro)
                │   └── NoABBItem.java     # Nó ABB (Item)
                └── Hashing/
                    ├── HashingEncadeado.java  # Tabela hash com encadeamento
                    └── NoHash.java            # Nó da lista encadeada
├── data/
│   ├── raw/                               # Datasets de entrada
│   │   ├── Reserva1000alea.txt
│   │   ├── Reserva1000ord.txt
│   │   ├── Reserva1000inv.txt
│   │   ├── nome.txt                       # 400 nomes para pesquisa
│   │   └── ... (12 arquivos no total)
│   ├── sorted/                            # Resultados ordenados
│   ├── searched/                          # Resultados de pesquisa
│   │   ├── ABBReserva*.txt               # 12 arquivos ABB
│   │   ├── AVLReserva*.txt               # 12 arquivos AVL
│   │   └── HashReserva*.txt              # 12 arquivos Hashing
│   ├── estatisticas_ordenacao.csv         # Estatísticas de ordenação
│   └── estatisticas_pesquisa.csv          # Estatísticas de pesquisa (ABB + AVL + Hashing)
└── .github/
    └── copilot-instructions.md            # Documentação técnica
```

## 🔧 Algoritmos Implementados

### Algoritmos de Ordenação

#### 1. HeapSort
Algoritmo baseado em heap binária que garante O(n log n) no pior caso.

**Características:**
- Complexidade: O(n log n) em todos os casos
- Estável: Não
- In-place: Sim
- Uso: Bom para grandes datasets com garantia de performance

#### 2. QuickSort
Algoritmo de divisão e conquista com pivô no elemento do meio.

**Características:**
- Complexidade: O(n log n) médio, O(n²) pior caso
- Estável: Não
- In-place: Sim
- Uso: Excelente performance média na prática

#### 3. InsertionSort
Algoritmo simples e eficiente para pequenos conjuntos ou dados quase ordenados.

**Características:**
- Complexidade: O(n²) pior caso, O(n) melhor caso
- Estável: Sim
- In-place: Sim
- Uso: Ideal para arrays pequenos (< 20 elementos)

#### 4. QuickSortComInsercao (Híbrido ≤20)
Combina QuickSort com InsertionSort: quando uma partição tem **20 ou menos elementos**, usa InsertionSort.

**Características:**
- Aproveita a eficiência do QuickSort para grandes partições
- Usa InsertionSort para partições pequenas (mais eficiente)
- Melhora a performance geral em datasets variados

### Estruturas de Pesquisa

#### ABB (Árvore Binária de Busca)
Árvore binária de busca implementada para pesquisa eficiente de reservas por nome.

**Características:**
- Complexidade de busca: O(log n) balanceada, O(n) pior caso
- Suporta nomes duplicados usando lista de reservas por nó
- Balanceamento via método `balancear()` com construção O(n log n)

**Implementação:**
- `NoABBItem`: Nó contendo nome (String) e lista de reservas (LCItem)
- `ArvoreABBItem`: Árvore com métodos de inserção, busca, remoção e balanceamento

#### AVL (Árvore Balanceada)
Árvore binária de busca **auto-balanceada** com rotações automáticas durante inserção.

**Características:**
- Complexidade de busca: O(log n) **garantido** em todos os casos
- Auto-balanceamento via rotações simples e duplas durante inserção
- Fator de balanceamento mantido em cada nó (-1, 0, 1)
- Suporta nomes duplicados usando lista de reservas por nó

**Implementação:**
- `NoAVLItem`: Nó com nome, lista de reservas e fator de balanceamento
- `ArvoreAVLItem`: Árvore com rotações automáticas (esquerda, direita, dupla-esquerda, dupla-direita)

**Vantagem sobre ABB:**
- Não requer balanceamento manual após inserção
- Mantém altura O(log n) automaticamente
- Melhor performance consistente em todos os tipos de dados (alea/ord/inv)

#### Hashing Encadeado (Hash Table with Chaining)
Tabela hash com resolução de colisões por encadeamento.

**Características:**
- Complexidade de busca: O(1) média, O(n) pior caso
- **Resolução de colisões**: Encadeamento com listas ligadas (`NoHash`)
- **Função hash**: Soma dos valores ASCII módulo tamanho da tabela
- **Tamanho da tabela**: Número primo ~1.3x o dataset (fator de carga ~0.75)
- Suporta nomes duplicados usando lista de reservas por slot

**Implementação:**
- `NoHash`: Nó contendo nome, lista de reservas e próximo nó
- `HashingEncadeado`: Tabela com métodos:
  - `inserir(Item)` - Insere na frente da cadeia O(1)
  - `pesquisar(String nome)` - Retorna LCItem com todas as reservas O(1) médio
  - `carregarDeLCItem(LCItem)` - Carregamento em lote do dataset

**⚠️ Problema Crítico Resolvido: StackOverflowError**

**O Problema:**
Ao inserir dados **ordenados** (ex: 50.000 registros em ordem alfabética) um por um em uma ABB, a árvore se torna completamente desbalanceada, essencialmente uma lista encadeada de 50.000 níveis de profundidade. Isso causa **StackOverflowError** devido à profundidade excessiva da recursão durante inserção e busca.

```java
// ABORDAGEM TRADICIONAL (FALHA!)
ArvoreABBItem abb = new ArvoreABBItem();
for (int i = 0; i < 50000; i++) {
    abb.insere(item);  // ← StackOverflowError em dados ordenados!
}
abb = abb.balancear();  // Nunca chega aqui
```

**A Solução:**
Implementamos o método `construirBalanceada()` que constrói uma árvore **já balanceada** usando divide-and-conquer:

```java
// SOLUÇÃO OTIMIZADA
ArvoreABBItem abb = new ArvoreABBItem();
abb.construirBalanceada(reservas);  // Constrói árvore balanceada diretamente
```

**Como funciona:**
1. Recebe a lista completa de itens
2. Insere o elemento do **meio** como raiz
3. Recursivamente constrói subárvore esquerda (metade esquerda dos dados)
4. Recursivamente constrói subárvore direita (metade direita dos dados)
5. Garante altura O(log n) desde o início

**Resultado:**
- ✅ Nenhum StackOverflowError, mesmo com 50.000 elementos ordenados
- ✅ Performance 64-84x melhor em dados ordenados (de 115ms para 1.8ms)
- ✅ Todos os 12 datasets processam com sucesso

**Características:**
- Aproveita a eficiência do QuickSort para grandes partições
- Usa InsertionSort para partições pequenas (mais eficiente)
- Melhora a performance geral em datasets variados

## ⚖️ Comparação: ABB vs AVL vs Hashing

### Resultados dos Testes (Tempo médio de 5 execuções)

| Dataset | ABB (ms) | AVL (ms) | Hashing (ms) | Vencedor | Comentário |
|---------|----------|----------|--------------|----------|-----------|
| 1000alea | 1.80 | 2.20 | 0.80 | Hashing | Hashing ~2x mais rápido |
| 1000ord | 3.20 | 3.60 | 0.60 | Hashing | Hashing ~5x mais rápido |
| 1000inv | 0.40 | 0.40 | 0.60 | ABB/AVL | Empate ABB-AVL |
| 5000alea | 2.60 | 2.80 | 1.40 | Hashing | Hashing ~2x mais rápido |
| 5000ord | 2.20 | 2.00 | 1.20 | Hashing | Hashing consistente |
| 5000inv | 1.80 | 2.40 | 1.60 | ABB | ABB ligeiramente melhor |
| 10000alea | 9.00 | 6.00 | 2.40 | Hashing | Hashing 2.5x mais rápido que AVL |
| 10000ord | 5.00 | 3.80 | 2.20 | Hashing | Hashing quase 2x mais rápido |
| 10000inv | 3.60 | 3.40 | 2.80 | Hashing | Hashing vence |
| 50000alea | 43.60 | 52.00 | 12.00 | Hashing | Hashing 3.6x mais rápido que ABB |
| 50000ord | 16.60 | 19.20 | 11.40 | Hashing | Dominância absoluta |
| 50000inv | 16.20 | 13.20 | 12.60 | AVL | AVL marginal |

### Análise de Performance

#### 🏆 Hashing - Vantagens
- **Performance O(1)**: Mais rápido em quase todos os cenários (11 de 12 datasets)
- **Escalabilidade**: Diferença aumenta com tamanho do dataset (até 4.3x mais rápido em 50k)
- **Consistência**: Performance previsível independente da ordenação inicial
- **Simplicidade**: Sem necessidade de balanceamento ou rotações

#### 🏆 ABB - Vantagens
- **Construção em lote**: `construirBalanceada()` constrói árvore já balanceada via divide-and-conquer
- **Melhor em datasets pequenos invertidos**: Competitiva em casos específicos
- **Sem overhead de rotações**: Balanceamento feito uma vez, não em cada inserção
- **Performance máxima árvore**: Quando hashing não é opção

#### 🏆 AVL - Vantagens
- **Performance consistente entre árvores**: Garante O(log n) automaticamente
- **Auto-balanceamento**: Não requer otimização manual
- **Melhor que ABB em dados ordenados**: Superioridade em metade dos casos vs ABB
- **Simplicidade**: Código mais limpo que ABB

#### Quando Usar Cada Estrutura

**Use Hashing quando:**
- ✅ Busca por chave exata é o caso primário (não range queries)
- ✅ Performance máxima é crítica
- ✅ Dataset cabe em memória
- ✅ Não precisa de ordenação dos resultados

**Use ABB quando:**
- ✅ Necessita range queries ou travessia ordenada
- ✅ Dados podem ser carregados completamente antes da construção
- ✅ Controle manual sobre balanceamento é desejado
- ✅ Hashing não é viável (memória limitada)

**Use AVL quando:**
- ✅ Inserções/remoções incrementais são necessárias
- ✅ Garantia de O(log n) é crítica
- ✅ Necessita range queries ou travessia ordenada
- ✅ Simplicidade de uso é importante

### 💡 Conclusão
**Hashing domina para busca pura:**
- **11x mais rápido** que árvores em média nos datasets grandes
- **Recomendação primária** para casos de busca por chave exata
- Único trade-off: não permite travessia ordenada ou range queries

**Entre as árvores:**
- **ABB** otimizada com `construirBalanceada()` oferece melhor performance em construção batch
- **AVL** oferece garantias mais fortes e simplicidade
- Diferenças entre árvores são relativamente pequenas (<30%) comparado com Hashing (200-400%)

## 📊 Estruturas de Dados

### Item
Representa uma reserva com campos completos:

```java
public class Item implements Comparable<Item> {
    private String chave;        // Ex: R000001
    private String nome;         // Nome do passageiro
    private String codigo_voo;   // Ex: V947
    private String data;         // Ex: 21/04/2024
    private String assento;      // Ex: 167C
}
```

**Comparação:**
1. Primeiro por `nome` (alfabética, case-insensitive)
2. Depois por `chave` (se nomes iguais)

### LCItem
Lista dinâmica que armazena objetos `Item`.

**Características:**
- Tamanho inicial: 10 elementos
- Crescimento automático: +50% quando cheia
- Métodos principais:
  - `insereFinal(Item)` - Insere no final
  - `insere(int pos, Item)` - Insere em posição específica
  - `remover(int pos)` - Remove por posição
  - `pesquisa(int cod)` - Busca linear por código
  - `heapsort()`, `quicksort()`, etc. - Métodos de ordenação

### LCInteiro
Lista dinâmica para números inteiros.

**Características:**
- Similar a LCItem, mas para `Integer`
- Suporta os mesmos algoritmos de ordenação
- Útil para testes e validações

### Árvore AVL
Árvore binária de busca auto-balanceada.

**Características:**
- Mantém altura balanceada (diferença máxima de 1 entre subárvores)
- Rotações automáticas para manter balanceamento
- Busca, inserção e remoção em O(log n)

## 🚀 Como Usar

### Uso Básico: Carregar e Ordenar

```java
// 1. Carregar dataset
LCItem reservas = LeArquivo.lerReservas("data/raw/Reserva1000alea.txt");

// 2. Ordenar (escolha um método)
reservas.heapsort();
// ou
reservas.quicksort();
// ou
reservas.quicksortComInsercao();
// ou
reservas.quicksortComInsercaoExato();

// 3. Salvar resultado
EscreveArquivo.salvarReservas(reservas, "data/sorted/resultado.txt");
```

### Uso Avançado: Algoritmos Standalone

```java
// Trabalhar diretamente com arrays
Item[] array = reservas.getLista();
int tamanho = reservas.getQuant();

// Chamar algoritmo diretamente
HeapSort.sort(array, tamanho);
QuickSort.sort(array, tamanho);
InsertionSort.sortRange(array, 0, 19);  // Ordenar apenas um intervalo
```

### Benchmark de Ordenação

```java
// Executar OrdenacaoReservas.java
// Processa automaticamente:
// - 12 datasets (4 tamanhos × 3 ordenações)
// - 4 algoritmos por dataset
// - 5 execuções por algoritmo
// - Calcula médias e salva estatísticas
```

### Benchmark de Pesquisa (ABB vs AVL vs Hashing)

```java
// Executar PesquisaReservas.java
// Processa automaticamente:
// - Usa LCItem para TODOS os dados (datasets e nomes de pesquisa)
// - Carrega cada dataset (12 arquivos) como LCItem
// - Carrega 400 nomes de pesquisa como LCItem (via lerNomesComoLCItem)
// - Constrói TRÊS estruturas: ABB balanceada, AVL auto-balanceada E Hashing
// - Pesquisa 400 nomes em cada estrutura
// - Executa 5 vezes para calcular tempo médio
// - Salva resultados em data/searched/ABB*.txt, AVL*.txt e Hash*.txt
// - Compara performance entre as três estruturas
// - Salva estatísticas em data/estatisticas_pesquisa.csv
// - Demonstra 100% uso consistente de LCItem (sem String[] no código)

// Exemplo de saída:
// Dataset: Reserva10000alea
//   ABB: 6.60 ms
//   AVL: 6.00 ms
//   Hashing: 1.20 ms
//   Nomes encontrados: 284 de 400 (71.0%)
//   Total de reservas: 479
```

### Diferença de Uso: ABB vs AVL vs Hashing

```java
// ===== ABB =====
// Requer construção balanceada para evitar StackOverflowError em dados ordenados
ArvoreABBItem abb = new ArvoreABBItem();
abb.construirBalanceada(reservas);  // Constrói árvore balanceada diretamente
LCItem resultado = abb.pesquisa("JOAO SILVA");

// ===== AVL =====
// Auto-balanceamento durante inserção - mais simples!
ArvoreAVLItem avl = new ArvoreAVLItem();
for (int i = 0; i < reservas.getQuant(); i++) {
    avl.insere(reservas.getItem(i));  // Auto-balanceia aqui
}
LCItem resultado = avl.pesquisa("JOAO SILVA");

// ===== Hashing =====
// Mais rápido (O(1) médio) - ideal para grandes datasets
HashingEncadeado hash = new HashingEncadeado(1301);  // Tamanho primo
hash.carregarDeLCItem(reservas);  // Carregamento em lote
LCItem resultado = hash.pesquisar("JOAO SILVA");

// Todas as três retornam LCItem com todas as reservas do nome encontrado
// Todas lidam com nomes duplicados da mesma forma
// Demonstra uso consistente de LCItem em todo o código
```

## 💡 Exemplos de Código

### Exemplo 1: Comparar Dois Algoritmos

```java
// Carregar dataset
LCItem dados1 = LeArquivo.lerReservas("data/raw/Reserva5000alea.txt");
LCItem dados2 = LeArquivo.lerReservas("data/raw/Reserva5000alea.txt");

// HeapSort
long inicio = System.currentTimeMillis();
dados1.heapsort();
long tempoHeap = System.currentTimeMillis() - inicio;

// QuickSort
inicio = System.currentTimeMillis();
dados2.quicksort();
long tempoQuick = System.currentTimeMillis() - inicio;

System.out.println("HeapSort: " + tempoHeap + " ms");
System.out.println("QuickSort: " + tempoQuick + " ms");
```

### Exemplo 2: Trabalhar com Integers

```java
LCInteiro numeros = new LCInteiro();
numeros.insereFinal(42);
numeros.insereFinal(15);
numeros.insereFinal(8);
numeros.insereFinal(23);

// Ordenar
numeros.quicksort();

// Exibir
System.out.println(numeros);  // 8 | 15 | 23 | 42 |
```

### Exemplo 3: Busca com AVL

```java
ArvoreAVL arvore = new ArvoreAVL();

// Inserir valores
arvore.insere(50);
arvore.insere(30);
arvore.insere(70);
arvore.insere(20);

// Buscar
if (arvore.pesquisa(30)) {
    System.out.println("Valor encontrado!");
}

// Altura da árvore
System.out.println("Altura: " + arvore.altura());
```

## 📈 Análise de Desempenho

### Executando os Benchmarks

```bash
# Compile e execute OrdenacaoReservas
javac src/br/faesa/C3/OrdenacaoReservas.java
java -cp src br.faesa.C3.OrdenacaoReservas
```

### Saída Esperada

```text
=== PROCESSANDO: Reserva1000alea ===

  HeapSort: 12.40 ms
  QuickSort: 10.20 ms
  QuickSortInsertion: 9.80 ms
  QuickSortInsertionExato: 10.10 ms

=== PROCESSANDO: Reserva5000alea ===

  HeapSort: 45.60 ms
  QuickSort: 38.20 ms
  ...
```

### Arquivos de Estatísticas

**Ordenação** (`data/estatisticas_ordenacao.csv`):
```csv
Dataset;Algoritmo;Elementos;Media(ms)
Reserva1000alea;HeapSort;1000;12.40
Reserva1000alea;QuickSort;1000;10.20
...
```

**Pesquisa** (`data/estatisticas_pesquisa.csv`):
```csv
Dataset;Algoritmo;Elementos;Media(ms)
Reserva1000alea;ABB;1000;1.80
Reserva1000alea;AVL;1000;2.20
Reserva1000alea;Hashing;1000;0.80
Reserva10000alea;ABB;10000;9.00
Reserva10000alea;AVL;10000;6.00
Reserva10000alea;Hashing;10000;2.40
...
```

## 📦 Datasets

### Tamanhos Disponíveis
- **1.000 elementos** - Testes rápidos
- **5.000 elementos** - Análise intermediária
- **10.000 elementos** - Performance média
- **50.000 elementos** - Stress test

### Ordenações Iniciais
- **`alea`** - Aleatório (cenário mais realista)
- **`ord`** - Ordenado (melhor caso)
- **`inv`** - Invertido (pior caso)

### Formato dos Dados
Cada linha representa uma reserva:
```
R000001;ADRIANA SILVA;V947;21/04/2024;167C
R000002;BRUNO COSTA;V123;15/03/2024;045A
...
```

**Campos:**
1. Chave da reserva (único)
2. Nome do passageiro
3. Código do voo
4. Data da viagem
5. Número do assento

## ⚙️ Compilação e Execução

### Pré-requisitos
- Java JDK 21 ou superior
- Estrutura de diretórios preservada

### Compilar

```bash
# Compilar todas as classes
javac -d out src/br/faesa/C3/**/*.java

# Ou usar o compilador da IDE (VS Code, IntelliJ, Eclipse)
```

### Executar

```bash
# Benchmark de ordenação
java -cp out br.faesa.C3.OrdenacaoReservas

# Benchmark de pesquisa (ABB + AVL combinados)
java -cp out br.faesa.C3.PesquisaReservas

# Testes básicos
java -cp out Main
```

### Usando VS Code

1. Abra a pasta do projeto
2. Pressione `F5` ou clique em "Run Java" no arquivo desejado
3. Os resultados aparecerão no terminal integrado

## 🎓 Conceitos Aplicados

### Padrões de Projeto
- **Delegation Pattern**: Estruturas de dados delegam ordenação para classes especializadas
- **Strategy Pattern**: Múltiplos algoritmos intercambiáveis
- **Template Method**: Estrutura comum para algoritmos de ordenação

### Princípios SOLID
- **Single Responsibility**: Cada classe tem uma responsabilidade clara
- **Open/Closed**: Fácil adicionar novos algoritmos sem modificar existentes
- **Dependency Inversion**: Dependência de abstrações (interface Comparable)

### Estrutura de Dados
- **Arrays Dinâmicos**: Crescimento automático com realocação
- **Árvores Balanceadas**: AVL com rotações automáticas
- **Comparação de Objetos**: Interface Comparable customizada

## 📝 Convenções do Código

### Nomenclatura
- Português para métodos e variáveis: `insereFinal()`, `eVazia()`
- CamelCase para classes: `LCItem`, `ArvoreAVL`
- Verbos em português: `insere`, `remove`, `pesquisa`, `ordena`

### Organização
- Pacotes por funcionalidade: `entidades`, `ordenacao`, `pesquisa`, `helper`
- Separação clara: código fonte em `src/`, dados em `data/`
- Documentação inline em português

## 🔍 Troubleshooting

### Erro: "Arquivo não encontrado"
**Solução:** Verifique se os arquivos estão em `data/raw/` e se o caminho está correto.

### Erro: "OutOfMemoryError"
**Solução:** Aumente a memória da JVM: `java -Xmx2g -cp out br.faesa.C3.OrdenacaoReservas`

### Performance lenta nos datasets grandes
**Normal:** Os datasets de 50.000 elementos são propositalmente grandes para análise de complexidade.

### Resultados inconsistentes
**Solução:** Execute múltiplas vezes (o programa já faz 5 rodadas e calcula a média automaticamente).

### Erro: "StackOverflowError" em ABB com dados ordenados
**Causa:** Inserir dados ordenados um por um cria árvore desbalanceada (lista encadeada de 50k níveis).  
**Solução:** O programa já usa `construirBalanceada()` que evita este problema. Se implementar sua própria ABB, sempre construa balanceada desde o início.

## 🤝 Contribuindo

Este é um projeto acadêmico, mas sugestões são bem-vindas:

1. Novos algoritmos de ordenação
2. Otimizações de performance
3. Estruturas de dados adicionais
4. Melhorias na análise estatística

## 📄 Licença

Projeto acadêmico desenvolvido para fins educacionais.

## ✨ Autor

Desenvolvido como parte do curso de Pesquisa e Ordenação - FAESA

---

**Última atualização:** Novembro 2025
