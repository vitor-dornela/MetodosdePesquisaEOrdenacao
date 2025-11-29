# Copilot Instructions

## Project Overview
Academic project implementing classic search and sorting algorithms in Java. Core structures: `LCInteiro` for integer lists, `LCItem` for reservation data, with standalone sorting implementations and separate search structures (ABB, AVL, Hashing).

## Architecture Pattern

### Data Structure Philosophy
- **`LCInteiro`** - Dynamic integer array, delegates sorting to standalone classes
- **`LCItem`** - Dynamic array for `Reserva` objects (reservation records), delegates sorting to standalone classes
- **`Reserva`** - Represents reservation data (chave, nome, codigoVoo, data, assento) with `Comparable` interface
- **`NoArvoreBase<T>`** - Abstract generic base class for tree nodes (ABB and AVL inherit from this)
- **Sorting classes** - Standalone implementations in `br.faesa.C3.algoritmos.ordenacao` with overloaded methods for `Reserva[]` and `Integer[]`
- **Search structures** - Separate packages (ABB, AVL, Hashing) with their own node and tree classes

### Sorting Design Pattern
All sorting algorithms are in **standalone classes** with method overloading:

```java
// Standalone sorting (works on arrays directly)
HeapSort.sort(reservaArray, size);        // For Reserva[]
HeapSort.sort(integerArray, size);        // For Integer[]
InsertionSort.sortRange(array, 0, 10);    // Range sorting

// Data structure delegation (recommended)
LCItem lista = new LCItem();
lista.heapsort();  // Calls HeapSort.sort(this.lista, this.quant)

LCInteiro listaInt = new LCInteiro();
listaInt.quicksort();  // Calls QuickSort.sort(this.lista, this.quant)
```

**Sorting Classes:**
- `HeapSort` - Heap sort for `Reserva[]` and `Integer[]`
- `QuickSort` - Quick sort for `Reserva[]` and `Integer[]`
- `InsertionSort` - Insertion sort with `sort()` and `sortRange()` methods
- `QuickSortComInsercao` - Hybrid: QuickSort switching to InsertionSort for partitions ≤20 elements

### Search Structure Inheritance
Tree nodes use generic inheritance to avoid code duplication:

```java
// Base class with generic self-referential type
public abstract class NoArvoreBase<T extends NoArvoreBase<T>> {
    protected String nome;
    protected LCItem reservas;
    protected T esq, dir;
    // getters, setters, constructor
}

// ABB node (minimal - only constructor)
public class NoABBItem extends NoArvoreBase<NoABBItem> {
    public NoABBItem(String nome) { super(nome); }
}

// AVL node (adds balance factor)
public class NoAVLItem extends NoArvoreBase<NoAVLItem> {
    private int fatorBalanceamento;
    // constructor, getter, setter
}
```

## Code Conventions

### Package Structure
```
src/br/faesa/C3/
├── OrdenacaoReservas.java          # Sorting benchmark program
├── PesquisaReservas.java           # Search benchmark program
├── entidades/                      # Core data structures
│   ├── LCInteiro.java              # Dynamic integer array
│   ├── LCItem.java                 # Dynamic Reserva array
│   ├── Reserva.java                # Reservation record (Comparable)
│   ├── NoArvoreBase.java           # Generic abstract tree node base
│   └── Ordenavel.java              # Sorting interface
├── helper/                         # File I/O utilities
│   ├── LeArquivo.java              # Read reservations/names
│   └── EscreveArquivo.java         # Write results/statistics
└── algoritmos/
    ├── ordenacao/                  # Standalone sorting algorithms
    │   ├── HeapSort.java
    │   ├── QuickSort.java
    │   ├── InsertionSort.java
    │   └── QuickSortComInsercao.java
    └── pesquisa/                   # Search structures
        ├── ABB/
        │   ├── ArvoreABBItem.java   # Binary search tree
        │   └── NoABBItem.java       # ABB node (extends NoArvoreBase)
        ├── AVL/
        │   ├── ArvoreAVLItem.java   # Self-balancing tree
        │   └── NoAVLItem.java       # AVL node (extends NoArvoreBase)
        └── Hashing/
            ├── HashingEncadeado.java # Hash table with chaining
            └── NoHash.java           # Hash chain node
```

### Data Directory Structure
```
data/
├── raw/                            # Input datasets
│   ├── nome.txt                    # 400 search names
│   └── Reserva*.txt                # 12 datasets (1k, 5k, 10k, 50k × alea, ord, inv)
├── sorted/                         # Sorted output (36 files)
│   ├── heap*.txt
│   ├── quick*.txt
│   └── QuickIns*.txt
├── searched/                       # Search results (36 files)
│   ├── ABB*.txt
│   ├── AVL*.txt
│   └── Hash*.txt
├── estatisticas_ordenacao.csv      # Sorting benchmark results
└── estatisticas_pesquisa.csv       # Search benchmark results
```

### Naming & Style
- Portuguese naming throughout (e.g., `insereFinal()`, `eVazia()`, `fatorBalanceamento`)
- Methods use Portuguese verbs: `insere`, `remove`, `pesquisa`, `ordenar`
- Compact commenting style with inline Portuguese explanations
- No external dependencies - pure Java stdlib

### File I/O Patterns
```java
// Reading reservations (from file system)
LCItem reservas = LeArquivo.lerReservas("data/raw/Reserva1000alea.txt");

// Reading names as LCItem (NEW - fully integrated with LCItem)
LCItem nomes = LeArquivo.lerNomesComoLCItem("data/raw/nome.txt");

// Writing reservations (to file system)
EscreveArquivo.salvarReservas(lista, "data/sorted/output.txt");

// Writing search results (accepts LCItem directly - no String[] conversion needed)
EscreveArquivo.salvarResultadosPesquisa("output.txt", nomesPesquisa, resultados);

// Statistics
EscreveArquivo.salvarEstatisticas("data/estatisticas_ordenacao.csv", dataset, algo, media, count, firstLine);
```

## Working with Data

### Item Comparison Logic
`Reserva` implements `Comparable<Reserva>`:
1. Primary sort: by `nome` (case-insensitive alphabetical)
2. Secondary sort: by `chave` (if names are equal)

### Loading Reservation Datasets
```java
LCItem reservas = LeArquivo.lerReservas("data/raw/Reserva1000alea.txt");
```

### Sorting and Saving
```java
// Option 1: Use data structure methods (delegates to sorting classes)
reservas.heapsort();  // Sort by name, then by key
reservas.quicksort();
reservas.quicksortComInsercao();      // Hybrid ≤20

// Option 2: Use sorting classes directly
HeapSort.sort(reservas.getLista(), reservas.getQuant());
QuickSort.sort(reservas.getLista(), reservas.getQuant());

// Save results
EscreveArquivo.salvarReservas(reservas, "data/sorted/output.txt");
```

## Working with Algorithms

### Sorting Algorithm Architecture
All sorting logic is in `br.faesa.C3.algoritmos.ordenacao` package:
- **Standalone classes** with static methods that work on arrays
- **Method overloading** for both `Reserva[]` and `Integer[]` types
- **Data structures delegate** to these classes (separation of concerns)

Example structure:
```java
// HeapSort.java
public static void sort(Reserva[] array, int size) { ... }      // For Reserva[]
public static void sort(Integer[] array, int size) { ... }   // For Integer[]

// LCItem.java (delegates)
public void heapsort() {
    HeapSort.sort(this.lista, this.quant);
}
```

### Available Algorithms
1. **HeapSort** - Classic heap sort
2. **QuickSort** - Standard quicksort with middle pivot
3. **InsertionSort** - Insertion sort with range support
4. **QuickSortComInsercao** - Hybrid: switches to insertion sort when partition ≤20

### Hybrid Sorting Strategy
The hybrid algorithm improves performance on small partitions:
- Use QuickSort for large partitions (efficient for large data)
- Switch to InsertionSort for small partitions ≤20 elements (efficient for small data)

### Search Algorithm Architecture

#### AVL Tree Implementation
- Balancing handled via `fatorBalanceamento` (-1, 0, 1) tracked in each `NoAVL`
- Rotations are recursive and update balance factors: `rotaçãoDireita()`, `rotaçãoEsquerda()`
- Insert triggers auto-balancing through `balancearDir()` / `balancearEsq()` checks

#### ABB (Binary Search Tree) Implementation
- Balanced construction from sorted `LCItem` via `construirBalanceada()`
- Search by name returns `LCItem` with all matching reservations

#### Hashing Encadeado (Chained Hashing) Implementation
- **Collision resolution**: Linked list chaining (`NoHash` nodes)
- **Hash function**: Sum of ASCII values modulo table size
- **Table sizing**: Prime number ~1.3x dataset size for load factor ~0.75
- **Utility methods** (static in `HashingEncadeado`):
  - `calcularTamanhoPrimo(int n)` - Finds next prime ≥ n*1.3
  - `ehPrimo(int n)` - Primality check
- **Key operations**:
  - `inserir(Reserva)` - Adds to front of chain (O(1))
  - `pesquisar(String nome)` - Returns `LCItem` with all matches (O(1) average)
  - `carregarDeLCItem(LCItem)` - Bulk load from dataset

### Test Data Files
Multiple dataset sizes with three orderings each:
- Sizes: 1000, 5000, 10000, 50000
- Orderings: `alea` (random), `ord` (sorted), `inv` (reverse sorted)
- Format: `CHAVE;NOME;VEICULO;DATA;ASSENTO` (e.g., `R000001;NOME;V947;21/04/2024;167C`)

## Running the Project

### Main Programs

#### `OrdenacaoReservas.java` - Sorting Performance Comparison
- Compares 3 sorting algorithms across 12 datasets
- Runs each algorithm 5 times per dataset
- Calculates mean execution time
- Saves sorted results to `data/sorted/`
- Appends statistics to `data/estatisticas.csv`

**Expected Output:**
```
=== PROCESSANDO: Reserva1000alea ===
  HeapSort: 12.40 ms
  QuickSort: 10.20 ms
  QuickSortInsertion: 9.80 ms
```

#### `PesquisaReservas.java` - Search Performance Comparison
- Compares 3 search structures (ABB, AVL, Hashing) across 12 datasets
- **Uses LCItem throughout** - no String[] arrays (except Java main signature)
- For each dataset:
  1. Loads reservas from file as LCItem
  2. Loads search names as LCItem (via `lerNomesComoLCItem()`)
  3. Builds all 3 search structures
  4. Searches 400 names from `nome.txt` in each structure
  5. Repeats 5 times for average timing
  6. Saves results to `data/searched/` (ABB*, AVL*, Hash* prefixes)
- Appends statistics to `data/estatisticas_pesquisa.csv`
- Demonstrates 100% consistent use of LCItem data structure

**Expected Output:**
```
=== PROCESSANDO: Reserva1000alea ===
  ABB: 2.00 ms
  AVL: 1.60 ms
  Hashing: 0.80 ms
  Nomes encontrados: 140 de 400 (35.0%)
```

### Output Files
**Sorting:**
- `data/sorted/heap*.txt` - HeapSort results
- `data/sorted/quick*.txt` - QuickSort results
- `data/sorted/QuickIns*.txt` - QuickSortComInsercao results
- `data/estatisticas_ordenacao.csv` - CSV: Dataset, Algoritmo, Elementos, Media(ms)

**Searching:**
- `data/searched/ABB*.txt` - ABB search results (12 files)
- `data/searched/AVL*.txt` - AVL search results (12 files)
- `data/searched/Hash*.txt` - Hashing search results (12 files)
- `data/estatisticas_pesquisa.csv` - CSV: Dataset, Algoritmo, Elementos, Media(ms)
  - Contains results for all 3 structures (ABB, AVL, Hashing)
  - Single unified statistics file for search comparison

## Adding New Features

### Adding a New Sorting Algorithm
1. Create class in `br.faesa.C3.algoritmos.ordenacao` (e.g., `MergeSort.java`)
2. Implement overloaded static methods:
   ```java
   public static void sort(Reserva[] array, int size) { ... }
   public static void sort(Integer[] array, int size) { ... }
   ```
3. Add delegation method to `LCItem`:
   ```java
   public void mergesort() {
       MergeSort.sort(this.lista, this.quant);
   }
   ```
4. Optionally add delegation method to `LCInteiro` for integers
5. Add to `Ordenavel` interface if needed for polymorphism
6. Update `OrdenacaoReservas.java` to include in benchmarks

### Adding Search Structures
Create new package under `br.faesa.C3.algoritmos.pesquisa` with:
- Node class extending `NoArvoreBase<T>` (for tree structures)
- Tree/structure class with insert and search methods
- Follow existing patterns (ABB, AVL, Hashing)

### Code Organization Principles
- **Separation of concerns**: Data structures manage data, sorting classes implement algorithms
- **Method overloading**: Same method names handle different types (`Reserva[]` vs `Integer[]`)
- **Delegation pattern**: Data structures delegate to standalone algorithm classes
- **Generic inheritance**: `NoArvoreBase<T>` provides shared fields/methods for tree nodes
- **No code duplication**: Shared logic extracted to reusable classes
- **Clean architecture**: `src/` for code, `data/` for datasets and results
- **100% LCItem integration**: All user-facing data uses LCItem structures
  - File I/O methods accept LCItem directly
  - No String[] arrays except Java main(String[] args) requirement
  - Demonstrates practical mastery of implemented data structures
