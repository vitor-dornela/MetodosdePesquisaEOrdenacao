# Estratégias de Estabilidade e Pesquisa em Chave Secundária

## 1. Estabilidade dos Algoritmos de Ordenação

Os algoritmos implementados (**HeapSort** e **QuickSort**) são **intrinsecamente instáveis**, ou seja, podem alterar a ordem relativa de elementos com chaves iguais. A estratégia utilizada para **garantir estabilidade** foi:

### Uso de Chave Composta no `compareTo()`

No `Item.java`, a comparação é feita em **dois níveis**:

```java
@Override
public int compareTo(Item outro) {
    // Primeiro compara por nome (chave primária de ordenação)
    int comparacaoNome = this.nome.compareToIgnoreCase(outro.nome);
    
    if (comparacaoNome != 0) {
        return comparacaoNome;
    }
    
    // Se os nomes são iguais, compara por chave (desempate determinístico)
    return this.chave.compareTo(outro.chave);
}
```

**Por que isso garante estabilidade?**
- Quando dois itens têm o **mesmo nome**, o desempate pela **chave (`R000001`, `R000002`, etc.)** garante uma ordenação **determinística e consistente**.
- Como a chave é única para cada registro, **nunca há ambiguidade** na comparação.
- O resultado é sempre o mesmo independentemente da implementação do algoritmo (HeapSort ou QuickSort).

---

## 2. Pesquisa em Chave Secundária

A **chave primária** dos dados é o campo `chave` (ex: `R000001`), porém a pesquisa é feita pelo **nome** (chave secundária). As estratégias implementadas nos três algoritmos de pesquisa são:

### a) ABB (Árvore Binária de Busca) - `ArvoreABBItem.java`

```java
// NoABBItem: armazena nome como chave + LCItem para múltiplas reservas
private String nome;
private LCItem reservas;  // Lista com todas as reservas desse nome

// Na inserção:
public boolean insere(Item item) {
    NoABBItem noExistente = pesquisa(item.getNome(), this.raiz);
    if (noExistente == null) {
        this.raiz = insere(item, this.raiz);
    } else {
        // Nome já existe, adiciona à lista de reservas do nó
        noExistente.getReservas().insereFinal(item);
    }
}
```

**Estratégia:** Cada nó indexa pelo **nome** (chave secundária) e mantém uma `LCItem` com **todas as reservas** que compartilham aquele nome.

---

### b) AVL (Árvore AVL) - `ArvoreAVLItem.java`

```java
// Mesma estratégia do ABB, com balanceamento automático
public void insere(Item item) {
    NoAVLItem noExistente = pesquisar(item.getNome(), this.raiz);
    if (noExistente != null) {
        // Nome já existe, adiciona à lista de reservas
        noExistente.getReservas().insereFinal(item);
    } else {
        this.raiz = inserir(item, this.raiz);
    }
}
```

**Estratégia:** Idêntica ao ABB, mas com rotações para manter O(log n) em todas as operações.

---

### c) Hashing Encadeado - `HashingEncadeado.java`

```java
// NoHash: armazena nome + LCItem para múltiplas reservas
private String nome;
private LCItem reservas;

// Na inserção:
public void inserir(Item item) {
    int indice = hashing(item.getNome());
    NoHash atual = tabela[indice];

    // Procura se o nome já existe na lista encadeada
    while (atual != null) {
        if (atual.getNome().equalsIgnoreCase(item.getNome())) {
            // Nome já existe, adiciona à lista de reservas
            atual.getReservas().insereFinal(item);
            return;
        }
        atual = atual.getProx();
    }
    // Se não existe, cria novo nó
    NoHash novoNo = new NoHash(item);
    ...
}
```

**Estratégia:** A função `hashing()` usa o **nome** para calcular o índice, e cada entrada na tabela hash armazena uma `LCItem` com todas as reservas daquele nome.

---

## Resumo das Estratégias

| Aspecto | Estratégia Implementada |
|---------|------------------------|
| **Estabilidade na Ordenação** | Chave composta no `compareTo()`: ordena por **nome** e desempata por **chave primária** |
| **Pesquisa por Chave Secundária** | Estruturas (ABB, AVL, Hash) indexam pelo **nome** e armazenam `LCItem` com múltiplas reservas do mesmo nome |
| **Tratamento de Duplicatas** | Nós únicos por nome; reservas com mesmo nome são agrupadas em `LCItem` no mesmo nó |
