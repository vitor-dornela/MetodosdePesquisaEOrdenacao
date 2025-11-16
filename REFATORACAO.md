# Refatoração Concluída - Algoritmos de Ordenação

## Mudanças Realizadas

### 1. **Classe `Item` Expandida** 
- **Arquivo**: `src/br/faesa/C3/algoritmos/entidades/Item.java`
- Agora representa dados completos de reserva:
  - `chave` (ex: R000001)
  - `nome` (ex: MARIA SILVA)
  - `veiculo` (ex: V947)
  - `data` (ex: 21/04/2024)
  - `assento` (ex: 167C)
- Implementa `Comparable<Item>`:
  - Ordena primeiro por **nome** (alfabético, case-insensitive)
  - Em caso de empate, ordena por **chave**
- Mantém compatibilidade com código antigo (construtor `Item(int codigo, String nome)`)

### 2. **HeapSort e QuickSort Independentes**
- **Arquivos**: 
  - `src/br/faesa/C3/algoritmos/ordenacao/HeapSort.java`
  - `src/br/faesa/C3/algoritmos/ordenacao/QuickSort.java`
  
- Agora contêm implementações **standalone** completas:
  ```java
  HeapSort.sort(Item[] array, int tamanho)
  QuickSort.sort(Item[] array, int tamanho)
  ```
- Não dependem mais de métodos embutidos em `LCInteiro`
- Mantém métodos legados `sortList(LCInteiro)` para compatibilidade

### 3. **LCItem Atualizada**
- **Arquivo**: `src/br/faesa/C3/algoritmos/entidades/LCItem.java`
- Novos métodos adicionados:
  ```java
  public void heapsort()   // Usa HeapSort.sort()
  public void quicksort()  // Usa QuickSort.sort()
  public Item[] getLista() // Acesso ao array interno
  ```
- Esses métodos **delegam** para as implementações standalone

### 4. **Utilitários Criados**

#### `ReservaReader.java`
- Carrega arquivos de reservas do classpath
- Faz parsing do formato: `R000001;NOME;V947;21/04/2024;167C`
- Retorna `LCItem` preenchida
- Uso:
  ```java
  LCItem reservas = ReservaReader.lerReservas("/br/faesa/C3/dados/Reserva1000alea.txt");
  ```

#### `ArquivoEscritor.java`
- Salva `LCItem` em arquivo (caminho do sistema de arquivos)
- Suporta salvamento com ou sem cabeçalho
- Salva estatísticas de ordenação
- Uso:
  ```java
  ArquivoEscritor.salvarReservas(reservas, "src/br/faesa/C3/dados/saida.txt");
  ```

### 5. **Programas de Exemplo**

#### `TesteReservas.java`
- Demonstra todas as funcionalidades básicas
- Testa comparação de itens
- Carrega, ordena e salva um dataset
- Ideal para testes rápidos

#### `ExemploOrdenacaoReservas.java`
- Processa **todos os 12 datasets** automaticamente
- Para cada um:
  1. Carrega do classpath
  2. Ordena com HeapSort
  3. Salva resultado ordenado
  4. Registra estatísticas de desempenho
- Gera arquivo `estatisticas.csv` com tempos de execução

## Como Usar

### 1. Carregar e Ordenar um Dataset
```java
// Carregar
LCItem reservas = ReservaReader.lerReservas("/br/faesa/C3/dados/Reserva1000alea.txt");

// Ordenar (por nome, depois por chave)
reservas.heapsort();

// Salvar
ArquivoEscritor.salvarReservas(reservas, "src/br/faesa/C3/dados/ordenado.txt");
```

### 2. Usar Algoritmos Diretamente
```java
Item[] array = lista.getLista();
int tamanho = lista.getQuant();

// HeapSort
HeapSort.sort(array, tamanho);

// Ou QuickSort
QuickSort.sort(array, tamanho);
```

### 3. Executar Todos os Datasets
```bash
java ExemploOrdenacaoReservas
```

### 4. Teste Rápido
```bash
java TesteReservas
```

## Datasets Disponíveis
- `Reserva1000alea.txt`, `Reserva1000ord.txt`, `Reserva1000inv.txt`
- `Reserva5000alea.txt`, `Reserva5000ord.txt`, `Reserva5000inv.txt`
- `Reserva10000alea.txt`, `Reserva10000ord.txt`, `Reserva10000inv.txt`
- `Reserva50000alea.txt`, `Reserva50000ord.txt`, `Reserva50000inv.txt`

Sufixos:
- `alea` = aleatório
- `ord` = ordenado
- `inv` = invertido

## Arquivos de Saída
Quando executar `ExemploOrdenacaoReservas`:
- `src/br/faesa/C3/dados/*_ordenado.txt` - Datasets ordenados
- `src/br/faesa/C3/dados/estatisticas.csv` - Tempos de execução

## Compatibilidade
✅ **Código antigo continua funcionando**:
- `LCInteiro` mantém seus métodos `.quicksort()` e `.heapsort()` embutidos
- `Item(int codigo, String nome)` ainda funciona
- `Main.java` não precisa ser alterado

## Arquitetura Final

```
Sorting Algorithms (Standalone)
├── HeapSort.sort(Item[], int)
└── QuickSort.sort(Item[], int)
        ↑
        │ delegação
        │
LCItem (Convenience Methods)
├── .heapsort()
└── .quicksort()
```

Esta arquitetura permite:
- ✅ Algoritmos independentes e reutilizáveis
- ✅ Fácil adição de novos algoritmos
- ✅ Métodos convenientes em `LCItem`
- ✅ Compatibilidade com código legado
