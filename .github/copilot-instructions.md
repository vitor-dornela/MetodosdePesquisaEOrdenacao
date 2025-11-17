# Copilot Instructions

## Project Overview
Academic project implementing classic search and sorting algorithms in Java. Core structures: `LCInteiro` for integer lists, `LCItem` for reservation data, with standalone sorting implementations and separate search structures (AVL tree).

## Architecture Pattern

### Data Structure Philosophy
- **`LCInteiro`** - Dynamic integer array, delegates sorting to standalone classes
- **`LCItem`** - Dynamic array for `Item` objects (reservation records), delegates sorting to standalone classes
- **`Item`** - Represents reservation data (chave, nome, codigo_voo, data, assento) with `Comparable` interface
- **Sorting classes** - Standalone implementations in `br.faesa.C3.algoritmos.ordenacao` with overloaded methods for `Item[]` and `Integer[]`
- **Search structures** - Separate classes (AVL tree) with their own node types

### Sorting Design Pattern
All sorting algorithms are now in **standalone classes** with method overloading:

```java
// Standalone sorting (works on arrays directly)
HeapSort.sort(itemArray, size);        // For Item[]
HeapSort.sort(integerArray, size);     // For Integer[]
InsertionSort.sortRange(array, 0, 10); // Range sorting

// Data structure delegation (recommended)
LCItem lista = new LCItem();
lista.heapsort();  // Calls HeapSort.sort(this.lista, this.quant)

LCInteiro listaInt = new LCInteiro();
listaInt.quicksort();  // Calls QuickSort.sort(this.lista, this.quant)
```

**Sorting Classes:**
- `HeapSort` - Heap sort for `Item[]` and `Integer[]`
- `QuickSort` - Quick sort for `Item[]` and `Integer[]`
- `InsertionSort` - Insertion sort with `sort()` and `sortRange()` methods
- `QuickSortComInsercao` - Hybrid: QuickSort switching to InsertionSort for partitions ≤20 elements
- `QuickSortComInsercaoExato` - Hybrid: QuickSort switching to InsertionSort for partitions ==20 elements

## Code Conventions

### Package Structure
- `br.faesa.C3.algoritmos.entidades` - Data structures (`LCInteiro`, `LCItem`, `Item`)
- `br.faesa.C3.algoritmos.ordenacao` - Standalone sorting implementations with overloaded methods:
  - `HeapSort`, `QuickSort`, `InsertionSort`
  - `QuickSortComInsercao` (≤20), `QuickSortComInsercaoExato` (==20)
- `br.faesa.C3.algoritmos.helper` - File I/O utilities (`LeArquivo`, `EscreveArquivo`)
- `br.faesa.C3.algoritmos.pesquisa.AVL` - AVL tree implementation
- `data/` - Data files (separate from source code):
  - `data/raw/` - Input datasets (read via file system)
  - `data/sorted/` - Sorted output files
  - `data/estatisticas.csv` - Performance statistics

### Naming & Style
- Portuguese naming throughout (e.g., `insereFinal()`, `eVazia()`, `fatorBalanceamento`)
- Methods use Portuguese verbs: `insere`, `remove`, `pesquisa`, `ordenar`
- Compact commenting style with inline Portuguese explanations
- No external dependencies - pure Java stdlib

### File I/O Patterns
```java
// Reading (from file system)
LCItem reservas = LeArquivo.lerReservas("data/raw/Reserva1000alea.txt");

// Writing (to file system)
EscreveArquivo.salvarReservas(lista, "data/sorted/output.txt");

// Statistics
EscreveArquivo.salvarEstatisticas("data/estatisticas.csv", dataset, algo, media, count, firstLine);
```

## Working with Data

### Item Comparison Logic
`Item` implements `Comparable<Item>`:
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
reservas.quicksortComInsercaoExato(); // Hybrid ==20

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
- **Method overloading** for both `Item[]` and `Integer[]` types
- **Data structures delegate** to these classes (separation of concerns)

Example structure:
```java
// HeapSort.java
public static void sort(Item[] array, int size) { ... }      // For Item[]
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
5. **QuickSortComInsercaoExato** - Hybrid: switches to insertion sort when partition ==20

### Hybrid Sorting Strategy
The hybrid algorithms improve performance on small partitions:
- Use QuickSort for large partitions (efficient for large data)
- Switch to InsertionSort for small partitions (efficient for small data)
- Threshold variants: `≤20` vs `==20` for performance comparison

### AVL Tree Implementation
- Balancing handled via `fatorBalanceamento` (-1, 0, 1) tracked in each `NoAVL`
- Rotations are recursive and update balance factors: `rotaçãoDireita()`, `rotaçãoEsquerda()`
- Insert triggers auto-balancing through `balancearDir()` / `balancearEsq()` checks

### Test Data Files
Multiple dataset sizes with three orderings each:
- Sizes: 1000, 5000, 10000, 50000
- Orderings: `alea` (random), `ord` (sorted), `inv` (reverse sorted)
- Format: `CHAVE;NOME;VEICULO;DATA;ASSENTO` (e.g., `R000001;NOME;V947;21/04/2024;167C`)

## Running the Project

### Main Programs
- `Main.java` - Original tests for `LCInteiro` and AVL tree
- `OrdenacaoReservas.java` - Performance comparison of all 4 algorithms across 12 datasets
  - Runs each algorithm 5 times per dataset
  - Calculates mean execution time
  - Saves sorted results to `data/sorted/`
  - Appends statistics to `data/estatisticas.csv`

### Expected Output Pattern
```
=== PROCESSANDO: Reserva1000alea ===

  HeapSort: 12.40 ms
  QuickSort: 10.20 ms
  QuickSortInsertion: 9.80 ms
  QuickSortInsertionExato: 10.10 ms
```

### Output Files
- `data/sorted/heap*.txt` - HeapSort results
- `data/sorted/quick*.txt` - QuickSort results
- `data/sorted/QuickIns*.txt` - QuickSortComInsercao results
- `data/sorted/QuickIns20*.txt` - QuickSortComInsercaoExato results
- `data/estatisticas.csv` - CSV with columns: Dataset, Algoritmo, Elementos, Media(ms)

## Adding New Features

### Adding a New Sorting Algorithm
1. Create class in `br.faesa.C3.algoritmos.ordenacao` (e.g., `MergeSort.java`)
2. Implement overloaded static methods:
   ```java
   public static void sort(Item[] array, int size) { ... }
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
Create new package under `br.faesa.C3.algoritmos.pesquisa` with separate node and tree classes (follow AVL structure)

### Code Organization Principles
- **Separation of concerns**: Data structures manage data, sorting classes implement algorithms
- **Method overloading**: Same method names handle different types (`Item[]` vs `Integer[]`)
- **Delegation pattern**: Data structures delegate to standalone algorithm classes
- **No code duplication**: Shared logic extracted to reusable classes
- **Clean architecture**: `src/` for code, `data/` for datasets and results
