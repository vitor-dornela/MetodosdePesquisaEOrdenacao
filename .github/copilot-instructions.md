# Copilot Instructions

## Project Overview
Academic project implementing classic search and sorting algorithms in Java. Core structures: `LCInteiro` for integer lists, `LCItem` for reservation data, with standalone sorting implementations and separate search structures (AVL tree).

## Architecture Pattern

### Data Structure Philosophy
- **`LCInteiro`** - Dynamic integer array with embedded sorting methods (`src/br/faesa/C3/algoritmos/entidades/LCInteiro.java`)
- **`LCItem`** - Dynamic array for `Item` objects (reservation records)
- **`Item`** - Represents reservation data (key, name, vehicle, date, seat) with `Comparable` interface
- **Sorting classes** - Standalone implementations in `br.faesa.C3.algoritmos.ordenacao` that operate on arrays
- **Search structures** - Separate classes (AVL tree) with their own node types

### Sorting Design Pattern
Sorting algorithms have **two implementations**:
1. **Standalone** - Static methods in `QuickSort`/`HeapSort` that work on `Item[]` arrays
2. **Legacy** - Methods embedded in `LCInteiro` for backward compatibility

```java
// Standalone pattern (NEW - preferred for Item arrays)
HeapSort.sort(itemArray, size);  // Works on Item[]

// Wrapper pattern (calls standalone)
LCItem lista = new LCItem();
lista.heapsort();  // Internally calls HeapSort.sort()

// Legacy pattern (for LCInteiro only)
LCInteiro listaInt = new LCInteiro();
listaInt.quicksort();  // Embedded in LCInteiro
```

## Code Conventions

### Package Structure
- `br.faesa.C3.algoritmos.entidades` - Data structures (`LCInteiro`, `LCItem`, `Item`) and utilities (`ArquivoUtil`, `ArquivoEscritor`, `ReservaReader`)
- `br.faesa.C3.algoritmos.ordenacao` - Standalone sorting implementations (`QuickSort`, `HeapSort`)
- `br.faesa.C3.algoritmos.pesquisa.AVL` - AVL tree implementation
- Data files in `src/br/faesa/C3/dados/` (accessed via classpath for reading, file system path for writing)

### Naming & Style
- Portuguese naming throughout (e.g., `insereFinal()`, `eVazia()`, `fatorBalanceamento`)
- Methods use Portuguese verbs: `insere`, `remove`, `pesquisa`, `ordenar`
- Compact commenting style with inline Portuguese explanations
- No external dependencies - pure Java stdlib

### File I/O Patterns
```java
// Reading (from classpath resources)
ArquivoUtil.lerArquivo("/br/faesa/C3/dados/Reserva1000alea.txt");

// Writing (to file system)
ArquivoEscritor.salvarReservas(lista, "src/br/faesa/C3/dados/output.txt");
```

## Working with Data

### Item Comparison Logic
`Item` implements `Comparable<Item>`:
1. Primary sort: by `nome` (case-insensitive alphabetical)
2. Secondary sort: by `chave` (if names are equal)

### Loading Reservation Datasets
```java
LCItem reservas = ReservaReader.lerReservas("/br/faesa/C3/dados/Reserva1000alea.txt");
```

### Sorting and Saving
```java
reservas.heapsort();  // Sort by name, then by key
ArquivoEscritor.salvarReservas(reservas, "output.txt");
```

## Working with Algorithms

### Modifying Sorting Algorithms
- Standalone sorting logic is in `QuickSort.java` and `HeapSort.java`
- These classes contain static `sort(Item[] array, int size)` methods
- `LCItem` has convenience methods (`.heapsort()`, `.quicksort()`) that delegate to standalone classes
- Legacy `LCInteiro` still has embedded sorting methods for backward compatibility

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
- `ExemploOrdenacaoReservas.java` - Batch processing of all reservation datasets with HeapSort

### Expected Output Pattern
```
=== ORDENAÇÃO DE RESERVAS COM HEAPSORT ===
Processando: Reserva1000alea
  - Carregadas 1000 reservas em X ms
  - Ordenado em Y ms
  - Primeiras 3 após ordenação:
    R000123    ADRIANA SILVA...
```

## Adding New Features

### Adding a New Sorting Algorithm
1. Create class in `br.faesa.C3.algoritmos.ordenacao`
2. Implement `public static void sort(Item[] array, int size)` method
3. Add convenience method to `LCItem` that calls your static method
4. Optionally add legacy method to `LCInteiro` for integers

### Adding Search Structures
Create new package under `br.faesa.C3.algoritmos.pesquisa` with separate node and tree classes (follow AVL structure)
