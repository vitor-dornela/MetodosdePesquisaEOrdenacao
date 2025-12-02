# 2. Desenvolvimento

## 2.1 Estratégias de Estabilidade e Pesquisa em Chave Secundária

### Estabilidade dos Algoritmos de Ordenação

Os algoritmos implementados (**HeapSort** e **QuickSort**) são **intrinsecamente instáveis**, ou seja, podem alterar a ordem relativa de elementos com chaves iguais. A estratégia utilizada para **garantir estabilidade** foi:

#### Uso de Chave Composta no `compareTo()`

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
- O resultado é sempre o mesmo independentemente da implementação do algoritmo.

### Pesquisa em Chave Secundária

A **chave primária** dos dados é o campo `chave` (ex: `R000001`), porém a pesquisa é feita pelo **nome** (chave secundária). As estratégias implementadas:

| Estrutura | Estratégia |
|-----------|------------|
| **ABB** | Cada nó indexa pelo nome e mantém uma `LCItem` com todas as reservas desse nome |
| **AVL** | Mesma estratégia do ABB, com balanceamento automático via rotações |
| **Hashing** | Função hash aplicada ao nome; cada entrada armazena `LCItem` com múltiplas reservas |

---

## 2.2 Ambiente de Execução

### Especificações da Máquina

| Componente | Especificação |
|------------|---------------|
| **Processador** | *(preencher com seu processador)* |
| **Memória RAM** | *(preencher com sua RAM)* |
| **Sistema Operacional** | Windows |
| **JDK** | Java 21 |
| **IDE** | Visual Studio Code |

---

## 2.3 Arquivos de Teste

Foram utilizados **12 arquivos** de entrada, variando em tamanho e ordenação inicial:

| Arquivo | Elementos | Ordenação |
|---------|-----------|-----------|
| `Reserva1000alea.txt` | 1.000 | Aleatória |
| `Reserva1000ord.txt` | 1.000 | Ordenada |
| `Reserva1000inv.txt` | 1.000 | Invertida |
| `Reserva5000alea.txt` | 5.000 | Aleatória |
| `Reserva5000ord.txt` | 5.000 | Ordenada |
| `Reserva5000inv.txt` | 5.000 | Invertida |
| `Reserva10000alea.txt` | 10.000 | Aleatória |
| `Reserva10000ord.txt` | 10.000 | Ordenada |
| `Reserva10000inv.txt` | 10.000 | Invertida |
| `Reserva50000alea.txt` | 50.000 | Aleatória |
| `Reserva50000ord.txt` | 50.000 | Ordenada |
| `Reserva50000inv.txt` | 50.000 | Invertida |

**Formato dos registros**: `chave;nome;voo;data;assento`

Para pesquisa, foi utilizado o arquivo `nome.txt` com **400 nomes** a serem buscados.

---

## 2.4 Resultados - Algoritmos de Ordenação

### Tabela de Tempos (em milissegundos)

| Dataset | HeapSort | QuickSort | QuickSort+Inserção | Mais Rápido | Mais Lento |
|---------|----------|-----------|-------------------|-------------|------------|
| Reserva1000alea | 4,80 | 4,00 | **2,60** | QuickSort+Inserção | HeapSort |
| Reserva1000ord | 1,20 | 1,00 | **0,60** | QuickSort+Inserção | HeapSort |
| Reserva1000inv | 1,80 | **0,80** | **0,80** | QuickSort / QuickSort+Inserção | HeapSort |
| Reserva5000alea | **5,00** | 6,60 | 5,80 | HeapSort | QuickSort |
| Reserva5000ord | 3,00 | **2,20** | **2,20** | QuickSort / QuickSort+Inserção | HeapSort |
| Reserva5000inv | 3,60 | 2,40 | **2,20** | QuickSort+Inserção | HeapSort |
| Reserva10000alea | 7,00 | 7,20 | **6,80** | QuickSort+Inserção | QuickSort |
| Reserva10000ord | **5,00** | 6,00 | **5,00** | HeapSort / QuickSort+Inserção | QuickSort |
| Reserva10000inv | 5,80 | 6,20 | **4,40** | QuickSort+Inserção | QuickSort |
| Reserva50000alea | 54,40 | 36,40 | **34,60** | QuickSort+Inserção | HeapSort |
| Reserva50000ord | 22,80 | 20,60 | **17,20** | QuickSort+Inserção | HeapSort |
| Reserva50000inv | 28,20 | 20,00 | **19,80** | QuickSort+Inserção | HeapSort |

### Análise dos Resultados de Ordenação

1. **QuickSort com Inserção foi o mais rápido** na maioria dos casos (9 de 12), confirmando a eficiência do algoritmo híbrido.

2. **HeapSort foi consistentemente o mais lento** para datasets grandes (50.000 elementos), devido ao maior overhead de manipulação do heap.

3. **Para dados já ordenados**, todos os algoritmos apresentam melhor desempenho, pois há menos trocas a serem realizadas.

4. **O QuickSort puro** teve desempenho intermediário, sendo superado pelo híbrido devido ao overhead de recursão em partições pequenas.

---

## 2.5 Resultados - Algoritmos de Pesquisa

### Tabela de Tempos (em milissegundos)

| Dataset | ABB | AVL | Hashing | Mais Rápido | Mais Lento |
|---------|-----|-----|---------|-------------|------------|
| Reserva1000alea | 2,20 | 2,00 | **1,80** | Hashing | ABB |
| Reserva1000ord | 0,60 | 0,40 | **0,20** | Hashing | ABB |
| Reserva1000inv | 0,40 | 0,40 | **0,20** | Hashing | ABB / AVL |
| Reserva5000alea | 3,20 | **2,40** | 3,40 | AVL | Hashing |
| Reserva5000ord | 1,60 | 1,60 | **1,00** | Hashing | ABB / AVL |
| Reserva5000inv | 1,40 | 1,40 | **0,80** | Hashing | ABB / AVL |
| Reserva10000alea | 6,40 | 6,20 | **4,40** | Hashing | ABB |
| Reserva10000ord | **3,00** | 2,60 | **3,00** | AVL | ABB / Hashing |
| Reserva10000inv | 2,80 | 2,60 | **2,20** | Hashing | ABB |
| Reserva50000alea | 52,60 | **38,20** | 40,60 | AVL | ABB |
| Reserva50000ord | 14,80 | **13,60** | 36,20 | AVL | Hashing |
| Reserva50000inv | 15,00 | **13,00** | 30,80 | AVL | Hashing |

### Análise dos Resultados de Pesquisa

1. **Hashing foi o mais rápido** para datasets menores (1.000 a 10.000 elementos) com dados aleatórios, devido à complexidade O(1) no caso médio.

2. **AVL superou o Hashing** em datasets grandes (50.000 elementos), especialmente com dados ordenados/invertidos. Isso pode ser explicado pelo melhor aproveitamento de cache e menor número de colisões comparado ao hashing.

3. **ABB foi consistentemente mais lento** que AVL, demonstrando a importância do balanceamento automático da AVL.

4. **O Hashing teve pior desempenho** em datasets grandes ordenados (36,20ms vs 13,60ms da AVL), possivelmente devido a padrões de colisão na função hash.

5. **Dados ordenados e invertidos** geralmente resultam em tempos menores para ABB e AVL, pois a construção balanceada é mais eficiente.
