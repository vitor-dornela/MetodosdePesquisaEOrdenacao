# Por que `Item` implementa `Comparable<Item>`?

A classe `Item` implementa `Comparable<Item>` para permitir que os **algoritmos de ordenação** comparem dois itens diretamente.

## Implementação

```java
public class Item implements Comparable<Item> {
    
    @Override
    public int compareTo(Item outro) {
        // Primeiro compara por nome
        int comparacaoNome = this.nome.compareToIgnoreCase(outro.nome);
        
        if (comparacaoNome != 0) {
            return comparacaoNome;
        }
        
        // Se nomes iguais, compara por chave
        return this.chave.compareTo(outro.chave);
    }
}
```

---

## Por que é Necessário?

Nos algoritmos de ordenação (HeapSort, QuickSort), a comparação é feita assim:

```java
// QuickSort.java
while (array[i].compareTo(pivo) < 0) {  // Usa compareTo!
    i++;
}
```

Sem `Comparable`, o Java **não saberia** como comparar dois objetos `Item`.

---

## Retorno do `compareTo`

| Retorno | Significado |
|---------|-------------|
| `< 0` | `this` vem **antes** de `outro` |
| `= 0` | São **iguais** |
| `> 0` | `this` vem **depois** de `outro` |

---

## Benefícios

1. **Ordenação genérica** - qualquer algoritmo pode ordenar Items
2. **Critério customizado** - ordena por nome, desempata por chave
3. **Estabilidade** - o desempate por chave garante resultado determinístico

---

## Resumo

`Comparable<Item>` define **como dois Items devem ser comparados**, permitindo ordenação por nome com desempate por chave primária.
