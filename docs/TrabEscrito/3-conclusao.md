# 3. Conclusão

## 3.1 Verificação dos Resultados com a Teoria

### Algoritmos de Ordenação

Os resultados obtidos estão **de acordo com a teoria** estudada em sala de aula:

| Teoria | Resultado Observado | Conforme? |
|--------|---------------------|-----------|
| HeapSort tem complexidade O(n log n) constante | HeapSort manteve desempenho previsível em todos os casos | ✅ |
| QuickSort tem O(n log n) médio, mas pode degradar | QuickSort teve bom desempenho, sem degradação significativa | ✅ |
| Inserção Direta é eficiente para poucos elementos | QuickSort+Inserção superou QuickSort puro em quase todos os casos | ✅ |
| Algoritmos híbridos combinam vantagens | O híbrido foi o mais rápido em 9 de 12 testes | ✅ |

**Observação importante**: O HeapSort, apesar de ter complexidade garantida O(n log n), foi mais lento que QuickSort na prática. Isso ocorre porque:
- O QuickSort tem melhor localidade de cache (acessa elementos próximos)
- O HeapSort faz muitos saltos na memória (pai/filhos no heap)
- A constante multiplicativa do HeapSort é maior

### Algoritmos de Pesquisa

| Teoria | Resultado Observado | Conforme? |
|--------|---------------------|-----------|
| Hashing tem O(1) no caso médio | Hashing foi o mais rápido para datasets menores | ✅ |
| AVL garante O(log n) | AVL teve desempenho consistente e previsível | ✅ |
| ABB pode degradar sem balanceamento | ABB foi mais lento que AVL em vários casos | ✅ |
| Colisões prejudicam o Hashing | Hashing perdeu desempenho em datasets grandes ordenados | ✅ |

**Observação**: O comportamento do Hashing em dados ordenados demonstra a importância de uma boa função hash. Dados com padrões podem gerar mais colisões.

---

## 3.2 Conclusões Gerais

1. **QuickSort com Inserção é a melhor escolha** para ordenação geral, combinando a eficiência do QuickSort para grandes partições com a simplicidade do InsertionSort para pequenas.

2. **AVL é a estrutura de pesquisa mais equilibrada**, oferecendo desempenho O(log n) garantido sem os riscos de degradação do ABB ou colisões do Hashing.

3. **Hashing é ideal para datasets pequenos a médios** com distribuição uniforme, oferecendo O(1) no caso médio.

4. **A ordenação inicial dos dados afeta significativamente o desempenho**, especialmente para estruturas como ABB que dependem da ordem de inserção.

5. **A estratégia de chave composta** (nome + código) garantiu ordenação estável e determinística mesmo usando algoritmos intrinsecamente instáveis.

---

## 3.3 Dificuldades Encontradas

Durante o desenvolvimento do trabalho, as principais dificuldades foram:

1. **StackOverflowError na ABB**: Ao inserir 50.000 elementos em ordem, a ABB degenerava em uma lista encadeada, causando estouro de pilha na recursão. **Solução**: implementar construção balanceada a partir de dados pré-ordenados.

2. **Pesquisa por chave secundária**: Os dados possuem chave primária (`R000001`) mas a pesquisa é feita por nome. **Solução**: criar nós que armazenam o nome como chave e uma lista `LCItem` com todas as reservas desse nome.

3. **Função hash para strings**: A função hash inicial (soma de chars) gerava muitas colisões para nomes similares. **Solução**: usar `Character.getNumericValue()` conforme especificação do material de aula.

4. **Medição de tempo precisa**: Tempos muito pequenos (< 1ms) geravam inconsistências. **Solução**: executar cada teste 5 vezes e calcular a média.

5. **Organização do código**: Com muitos algoritmos e estruturas, o código ficou complexo. **Solução**: separar algoritmos em classes standalone com padrão de delegação.

---

## 3.4 Trabalhos Futuros

Possíveis melhorias e extensões:

- Implementar outros algoritmos de ordenação (MergeSort, TimSort)
- Testar com diferentes funções hash (multiplicação, divisão, universal)
- Implementar árvore Rubro-Negra como alternativa à AVL
- Adicionar testes com datasets ainda maiores (100.000+)
- Implementar visualização gráfica dos tempos de execução
