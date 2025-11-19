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
- **Estruturas de Pesquisa**: Árvore AVL e ABB (Árvore Binária de Busca)

### Características Principais

✅ **4 Algoritmos de Ordenação** com análise comparativa de desempenho  
✅ **Pesquisa ABB Otimizada** com solução para StackOverflowError em grandes datasets  
✅ **Sobrecarga de Métodos** para trabalhar com `Item[]` e `Integer[]`  
✅ **Padrão de Delegação** entre estruturas de dados e algoritmos  
✅ **Medição Automática** de tempo de execução (média de 5 rodadas)  
✅ **Exportação de Resultados** ordenados, pesquisas e estatísticas em CSV  
✅ **Suporte a Nomes Duplicados** com múltiplas reservas por nome na ABB  

## 📁 Estrutura do Projeto

```
MetodosdePesquisaEOrdenacao/
├── src/
│   └── br/faesa/C3/
│       ├── Main.java                      # Testes básicos
│       ├── OrdenacaoReservas.java         # Benchmark de ordenação
│       ├── PesquisaReservas.java          # Benchmark de pesquisa ABB
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
│           ├── helper/
│           │   ├── LeArquivo.java         # Leitura de arquivos
│           │   └── EscreveArquivo.java    # Escrita de arquivos
│           └── pesquisa/
│               ├── AVL/
│               │   ├── ArvoreAVL.java     # Árvore AVL (inteiros)
│               │   └── NoAVL.java         # Nó da árvore AVL
│               └── ABB/
│                   ├── ArvoreABB.java     # ABB para inteiros
│                   ├── ArvoreABBItem.java # ABB para reservas
│                   ├── NoABB.java         # Nó ABB (inteiro)
│                   └── NoABBItem.java     # Nó ABB (Item)
├── data/
│   ├── raw/                               # Datasets de entrada
│   │   ├── Reserva1000alea.txt
│   │   ├── Reserva1000ord.txt
│   │   ├── Reserva1000inv.txt
│   │   ├── nome.txt                       # 400 nomes para pesquisa
│   │   └── ... (12 arquivos no total)
│   ├── sorted/                            # Resultados ordenados
│   ├── searched/                          # Resultados de pesquisa
│   ├── estatisticas.csv                   # Estatísticas de ordenação
│   └── estatisticas_pesquisa.csv          # Estatísticas de pesquisa
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

### Benchmark de Pesquisa ABB

```java
// Executar PesquisaReservas.java
// Processa automaticamente:
// - Carrega cada dataset (12 arquivos)
// - Constrói ABB balanceada 5 vezes
// - Pesquisa 400 nomes em cada execução
// - Calcula tempo médio
// - Salva resultados de pesquisa e estatísticas

// Exemplo de saída:
// ABBReserva1000alea.txt - resultados para cada nome pesquisado
// Nomes encontrados: 140 de 400 (35.0%)
// Total de reservas: 195
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

### Arquivo de Estatísticas

O arquivo `data/estatisticas.csv` contém:

```csv
Dataset;Algoritmo;Elementos;Media(ms)
Reserva1000alea;HeapSort;1000;12.40
Reserva1000alea;QuickSort;1000;10.20
Reserva1000alea;QuickSortInsertion;1000;9.80
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

# Benchmark de pesquisa (ABB)
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
