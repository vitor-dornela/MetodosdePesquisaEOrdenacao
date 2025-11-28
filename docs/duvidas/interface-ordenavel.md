# Por que a interface `Ordenavel` foi útil?

A interface `Ordenavel` foi criada para trazer benefícios de **polimorfismo** e **organização** ao código.

## Definição da Interface

```java
public interface Ordenavel {
    void heapsort();
    void quicksort();
    void quicksortComInsercao();
    boolean eVazia();
}
```

## Classes que Implementam

- **`LCItem`** - lista de reservas (Item)
- **`LCInteiro`** - lista de inteiros

---

## Benefícios

### 1. Polimorfismo
Permite que os algoritmos de ordenação trabalhem com **qualquer estrutura** que implemente a interface:

```java
// HeapSort.java
public static void sort(Ordenavel lista) {
    if (lista != null && !lista.eVazia()) {
        lista.heapsort();  // Funciona com LCItem OU LCInteiro
    }
}
```

### 2. Contrato Garantido
Define que toda classe que implemente `Ordenavel` **obrigatoriamente** terá os métodos:
- `heapsort()`
- `quicksort()`
- `quicksortComInsercao()`
- `eVazia()`

### 3. Reutilização
Se você criar uma nova estrutura (ex: `LCString`), basta implementar `Ordenavel` e ela automaticamente funcionará com todos os algoritmos.

### 4. Desacoplamento
O código que usa ordenação não precisa saber se está trabalhando com `LCItem` ou `LCInteiro`:

```java
public void ordenarQualquerLista(Ordenavel lista) {
    lista.quicksort();  // Funciona independente do tipo concreto
}
```

---

## Resumo

A interface permite **tratar diferentes estruturas de dados de forma uniforme**, facilitando manutenção e extensão do código.
