# 1. Introdução

Este trabalho tem como objetivo implementar e comparar o desempenho de algoritmos clássicos de **ordenação** e **pesquisa** aplicados a um conjunto de dados de reservas de voos.

## 1.1 Algoritmos de Ordenação

### HeapSort
O HeapSort é um algoritmo de ordenação baseado em uma estrutura de dados chamada *heap* (árvore binária completa). O algoritmo funciona em duas fases:
1. **Construção do heap**: transforma o array em um heap máximo
2. **Ordenação**: remove repetidamente o maior elemento (raiz) e reconstrói o heap

**Complexidade**: O(n log n) em todos os casos (melhor, médio e pior).

### QuickSort
O QuickSort é um algoritmo de ordenação por divisão e conquista. Ele seleciona um elemento como pivô e particiona o array em dois sub-arrays: elementos menores que o pivô e elementos maiores. O processo é repetido recursivamente.

**Complexidade**: O(n log n) no caso médio, O(n²) no pior caso (quando o pivô é sempre o menor ou maior elemento).

### QuickSort com Inserção (Híbrido)
Esta variação combina o QuickSort com o InsertionSort. Quando uma partição atinge **20 ou menos elementos**, o algoritmo troca para InsertionSort, que é mais eficiente para pequenas quantidades de dados devido ao menor overhead.

**Complexidade**: O(n log n) no caso médio, com constante menor que o QuickSort puro para a maioria dos casos práticos.

## 1.2 Algoritmos de Pesquisa

### Árvore Binária de Busca (ABB)
A ABB é uma estrutura de dados em árvore onde cada nó possui no máximo dois filhos. Para cada nó, todos os elementos da subárvore esquerda são menores e todos da subárvore direita são maiores. A ABB foi construída de forma **balanceada** a partir dos dados ordenados.

**Complexidade**: O(log n) para busca em árvore balanceada, O(n) no pior caso (árvore degenerada).

### Árvore AVL
A AVL é uma árvore binária de busca **auto-balanceada**. Após cada inserção, o fator de balanceamento de cada nó é verificado e, se necessário, rotações são realizadas para manter a árvore balanceada.

**Complexidade**: O(log n) garantido para todas as operações.

### Hashing Encadeado
O Hashing utiliza uma função hash para mapear chaves a posições em uma tabela. Colisões são tratadas através de **listas encadeadas** (encadeamento). A função hash utilizada soma os valores numéricos dos caracteres da chave e aplica módulo pelo tamanho da tabela.

**Complexidade**: O(1) no caso médio para busca, O(n) no pior caso (todas as chaves colidem).

## 1.3 Metodologia de Implementação

A implementação seguiu os seguintes princípios:

1. **Separação de responsabilidades**: Os algoritmos de ordenação foram implementados em classes standalone (`HeapSort.java`, `QuickSort.java`, `QuickSortComInsercao.java`), separados das estruturas de dados.

2. **Estruturas de dados customizadas**: Foram implementadas as classes `LCItem` (lista dinâmica de itens) e `Item` (registro de reserva) para armazenar os dados.

3. **Padrão de delegação**: As estruturas de dados (`LCItem`) delegam a ordenação para as classes de algoritmos, mantendo o código organizado.

4. **Pesquisa por chave secundária**: As estruturas de pesquisa (ABB, AVL, Hashing) indexam pelo **nome** do passageiro (chave secundária), agrupando múltiplas reservas do mesmo nome em um único nó.

5. **Benchmarking padronizado**: Cada algoritmo foi executado **5 vezes** para cada dataset, calculando a **média** do tempo de execução para resultados mais confiáveis.
