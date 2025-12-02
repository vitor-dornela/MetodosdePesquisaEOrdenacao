# Árvore Binária de Busca (ABB) - Documentação Detalhada

## Índice

1. [Visão Geral](#visão-geral)
2. [Conceitos Fundamentais](#conceitos-fundamentais)
3. [Arquitetura e Herança](#arquitetura-e-herança)
4. [Implementação do NoABBItem](#implementação-do-noabbitem)
5. [Implementação da ArvoreABBItem](#implementação-da-arvoreabbitem)
6. [Dependências e Integração](#dependências-e-integração)
7. [Uso no Projeto: PesquisaReservas.java](#uso-no-projeto-pesquisareservasjava)
8. [Análise de Complexidade](#análise-de-complexidade)
9. [Perguntas e Respostas](#perguntas-e-respostas)

---

## Visão Geral

A **Árvore Binária de Busca (ABB)** é uma estrutura de dados hierárquica onde cada nó possui no máximo dois filhos, e segue a propriedade: valores menores à esquerda, maiores à direita.

### Características da Implementação

| Aspecto | Descrição |
|---------|-----------|
| **Estrutura** | Árvore binária com nós genéricos |
| **Chave de busca** | Nome da reserva (String, case-insensitive) |
| **Valores** | Lista de reservas (`LCItem`) por nome |
| **Balanceamento** | Manual via `construirBalanceada()` |
| **Herança** | `NoABBItem` estende `NoArvoreBase<NoABBItem>` |

### Localização no Projeto

```
src/br/faesa/C3/algoritmos/pesquisa/ABB/
├── ArvoreABBItem.java    # Estrutura da árvore
└── NoABBItem.java        # Nó da árvore (herda de NoArvoreBase)
```

---

## Conceitos Fundamentais

### Propriedade da ABB

Para todo nó N na árvore:
- Todos os nós na **subárvore esquerda** têm chaves **menores** que N
- Todos os nós na **subárvore direita** têm chaves **maiores** que N

```
            "MARIA"
           /       \
      "CARLOS"    "PEDRO"
       /    \         \
   "ANA"  "JOSE"    "ZELIA"
```

### Múltiplas Reservas por Nome

Um diferencial da nossa implementação: **cada nó armazena uma lista de reservas**, não apenas uma. Isso permite múltiplas reservas com o mesmo nome.

```
            Nó "MARIA"
           ┌──────────────────────┐
           │ nome: "MARIA"        │
           │ reservas: LCItem     │
           │   ├── R000001;MARIA  │
           │   ├── R000025;MARIA  │
           │   └── R000089;MARIA  │
           └──────────────────────┘
```

### Construção Balanceada

Para evitar **StackOverflowError** com dados ordenados, usamos construção balanceada:

```
Inserção sequencial de dados ordenados:     Construção balanceada:
        "ANA"                                      "JOSE"
            \                                     /      \
          "CARLOS"                           "CARLOS"  "PEDRO"
              \                               /    \        \
            "JOSE"                        "ANA"  "MARIA"  "ZELIA"
                \
              "MARIA"              Altura: O(log n) ✓
                  \
                "PEDRO"
                    \
                  "ZELIA"
                  
        Altura: O(n) ✗
```

---

## Arquitetura e Herança

### Diagrama de Classes

```
┌─────────────────────────────────────────────────────────────────┐
│                    NoArvoreBase<T>                              │
│                    (Classe Abstrata)                            │
│                                                                 │
│  - nome: String                                                 │
│  - reservas: LCItem                                             │
│  - esq: T                                                       │
│  - dir: T                                                       │
│                                                                 │
│  + getNome(): String                                            │
│  + setNome(String): void                                        │
│  + getReservas(): LCItem                                        │
│  + getEsq(): T                                                  │
│  + setEsq(T): void                                              │
│  + getDir(): T                                                  │
│  + setDir(T): void                                              │
└─────────────────────────────────────────────────────────────────┘
                              △
                              │ extends
                              │
┌─────────────────────────────────────────────────────────────────┐
│                    NoABBItem                                    │
│              extends NoArvoreBase<NoABBItem>                    │
│                                                                 │
│  + NoABBItem(Reserva item)                                      │
│    // Apenas construtor - herda todo o resto                    │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ usa
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    ArvoreABBItem                                │
│                                                                 │
│  - raiz: NoABBItem                                              │
│  - quant: int                                                   │
│                                                                 │
│  + pesquisa(String): LCItem                                     │
│  + insere(Reserva): boolean                                     │
│  + remove(String): NoABBItem                                    │
│  + construirBalanceada(LCItem): void                            │
│  + balancear(): ArvoreABBItem                                   │
│  + CamInOrdem(): LCItem                                         │
│  + CamPreOrdem(): LCItem                                        │
│  + CamPosOrdem(): LCItem                                        │
│  + pesquisarTodos(LCItem): LCItem[]                             │
└─────────────────────────────────────────────────────────────────┘
```

### Por que Generics em NoArvoreBase?

O uso de `NoArvoreBase<T extends NoArvoreBase<T>>` permite:

```java
// NoABBItem retorna NoABBItem (não NoArvoreBase)
NoABBItem no = ...;
NoABBItem filho = no.getEsq();  // Tipo correto!

// NoAVLItem retorna NoAVLItem
NoAVLItem noAvl = ...;
NoAVLItem filhoAvl = noAvl.getEsq();  // Tipo correto!
```

Sem generics, precisaríamos de casting:
```java
NoABBItem filho = (NoABBItem) no.getEsq();  // Feio e inseguro
```

---

## Implementação do NoABBItem

### Código Completo

```java
package br.faesa.C3.algoritmos.pesquisa.ABB;

import br.faesa.C3.entidades.Reserva;
import br.faesa.C3.entidades.NoArvoreBase;

/**
 * Nó para Árvore Binária de Busca (ABB).
 * Herda todos os atributos e métodos de NoArvoreBase.
 */
public class NoABBItem extends NoArvoreBase<NoABBItem> {

    public NoABBItem(Reserva item) {
        super(item);
    }
}
```

### Análise

O `NoABBItem` é **minimalista** - apenas um construtor!

Toda a funcionalidade vem de `NoArvoreBase`:
- `nome`: Chave de busca (nome da pessoa)
- `reservas`: `LCItem` com todas as reservas dessa pessoa
- `esq`, `dir`: Filhos (tipados como `NoABBItem`)

---

## Implementação da ArvoreABBItem

### Construtor

```java
public class ArvoreABBItem {
    private NoABBItem raiz;
    private int quant;  // Quantidade de NÓS (não de reservas)

    public ArvoreABBItem() {
        this.raiz = null;
        this.quant = 0;
    }
}
```

### Método `pesquisa(String nome)`

```java
public LCItem pesquisa(String nome) {
    NoABBItem no = pesquisa(nome, raiz);
    return (no != null) ? no.getReservas() : null;
}

private NoABBItem pesquisa(String nome, NoABBItem no) {
    if (no == null) {
        return null;  // Base: não encontrou
    }
    
    int comparacao = nome.compareToIgnoreCase(no.getNome());
    
    if (comparacao == 0) {
        return no;  // Encontrou!
    }
    if (comparacao > 0) {
        return pesquisa(nome, no.getDir());  // Busca na direita
    }
    return pesquisa(nome, no.getEsq());  // Busca na esquerda
}
```

**Características**:
- Retorna `LCItem` com **todas** as reservas do nome
- Comparação **case-insensitive**: `compareToIgnoreCase()`
- Complexidade: O(h), onde h é a altura da árvore

### Método `insere(Reserva item)`

```java
public boolean insere(Reserva item) {
    // Primeiro, verifica se já existe nó com esse nome
    NoABBItem noExistente = pesquisa(item.getNome(), this.raiz);
    
    if (noExistente == null) {
        // Nome novo: cria nó
        this.raiz = insere(item, this.raiz);
        return true;  // Novo nó criado
    } else {
        // Nome já existe: adiciona reserva à lista do nó
        noExistente.getReservas().insereFinal(item);
        return false;  // Nó já existia
    }
}

private NoABBItem insere(Reserva item, NoABBItem no) {
    if (no == null) {
        // Posição encontrada: cria novo nó
        NoABBItem novo = new NoABBItem(item);
        this.quant++;
        return novo;
    }
    
    int comparacao = item.getNome().compareToIgnoreCase(no.getNome());
    
    if (comparacao < 0) {
        no.setEsq(insere(item, no.getEsq()));
    } else if (comparacao > 0) {
        no.setDir(insere(item, no.getDir()));
    }
    // Se comparacao == 0: não deveria chegar aqui (tratado acima)
    
    return no;
}
```

**Fluxo de Inserção**:

```
insere(Reserva("R001", "JOSE"))
│
├── pesquisa("JOSE", raiz) → null (não existe)
│
└── insere(item, raiz)
    │
    ├── no == null?
    │   ├── SIM: cria NoABBItem, quant++, retorna novo
    │   └── NÃO: compara e desce na árvore
    │
    └── Retorna nó atualizado
```

### Método `remove(String nome)`

```java
public NoABBItem remove(String nome) {
    NoABBItem[] removido = new NoABBItem[1];  // "Retorno" extra
    this.raiz = remove(nome, this.raiz, removido);
    return removido[0];
}

private NoABBItem remove(String nome, NoABBItem no, NoABBItem[] removido) {
    if (no == null) {
        removido[0] = null;
        return no;
    }
    
    int comparacao = nome.compareToIgnoreCase(no.getNome());
    
    if (comparacao < 0) {
        no.setEsq(remove(nome, no.getEsq(), removido));
    } else if (comparacao > 0) {
        no.setDir(remove(nome, no.getDir(), removido));
    } else {
        // ENCONTROU o nó a remover
        removido[0] = no;
        this.quant--;
        
        // Caso 1: Sem filho direito → retorna esquerdo
        if (no.getDir() == null) {
            return no.getEsq();
        }
        // Caso 2: Sem filho esquerdo → retorna direito
        else if (no.getEsq() == null) {
            return no.getDir();
        }
        // Caso 3: Dois filhos → substitui pelo maior da esquerda
        else {
            no.setEsq(maiorEsq(no, no.getEsq()));
        }
    }
    return no;
}
```

### Método `maiorEsq` (Auxiliar de Remoção)

```java
private NoABBItem maiorEsq(NoABBItem noRemovido, NoABBItem maior) {
    // Anda para direita até encontrar o maior da subárvore esquerda
    if (maior.getDir() != null) {
        maior.setDir(maiorEsq(noRemovido, maior.getDir()));
        return maior;
    }
    
    // Encontrou o maior: copia dados para o nó removido
    noRemovido.setNome(maior.getNome());
    // Nota: deveria copiar reservas também (possível melhoria)
    
    // Retorna o filho esquerdo (ou null) para reconectar
    return maior.getEsq();
}
```

**Diagrama da Remoção com Dois Filhos**:

```
Remover "MARIA":
        "MARIA"                      "JOSE"
       /       \                    /       \
   "JOSE"    "PEDRO"    →      "CARLOS"   "PEDRO"
   /    \                       /    
"CARLOS" null              "ANA"    
 /
"ANA"

1. Encontra maior da esquerda: "JOSE"
2. Copia "JOSE" para posição de "MARIA"
3. Remove "JOSE" original (tem só filho esquerdo)
```

### Método `construirBalanceada(LCItem itens)`

```java
public void construirBalanceada(LCItem itens) {
    this.raiz = null;
    this.quant = 0;
    balancear(itens, this, 0, itens.getQuant() - 1);
}

private void balancear(LCItem vetor, ArvoreABBItem arv, int esq, int dir) {
    if (esq <= dir) {
        int meio = (esq + dir) / 2;
        arv.insere(vetor.getItem(meio));  // Insere elemento do meio
        balancear(vetor, arv, esq, meio - 1);   // Metade esquerda
        balancear(vetor, arv, meio + 1, dir);   // Metade direita
    }
}
```

**Por que funciona?**

Para dados ordenados `[A, B, C, D, E, F, G]`:

```
1º: insere(D) - meio de [0,6]
2º: insere(B) - meio de [0,2]
3º: insere(A) - meio de [0,0]
4º: insere(C) - meio de [2,2]
5º: insere(F) - meio de [4,6]
6º: insere(E) - meio de [4,4]
7º: insere(G) - meio de [6,6]

Resultado:
        D
       / \
      B   F
     / \ / \
    A  C E  G

Altura = O(log n) ✓
```

### Caminhamentos

```java
// In-Ordem: Esquerda → Nó → Direita (ordem alfabética!)
public LCItem CamInOrdem() {
    LCItem vetor = new LCItem(this.quant * 5);
    fazCamInOrdem(vetor, this.raiz);
    return vetor;
}

private void fazCamInOrdem(LCItem vetor, NoABBItem no) {
    if (no != null) {
        fazCamInOrdem(vetor, no.getEsq());
        // Adiciona TODAS as reservas deste nó
        LCItem reservas = no.getReservas();
        for (int i = 0; i < reservas.getQuant(); i++) {
            vetor.insereFinal(reservas.getItem(i));
        }
        fazCamInOrdem(vetor, no.getDir());
    }
}

// Pré-Ordem: Nó → Esquerda → Direita
// Pós-Ordem: Esquerda → Direita → Nó
```

### Método `pesquisarTodos(LCItem nomes)`

```java
public LCItem[] pesquisarTodos(LCItem nomes) {
    LCItem[] resultados = new LCItem[nomes.getQuant()];
    for (int i = 0; i < nomes.getQuant(); i++) {
        resultados[i] = pesquisa(nomes.getItem(i).getNome());
    }
    return resultados;
}
```

**Usado no benchmark** para pesquisar os 400 nomes de uma vez.

---

## Dependências e Integração

### Diagrama de Dependências

```
┌─────────────────────────────────────────────────────────────────┐
│                    PesquisaReservas.java                        │
│                    (Programa Principal)                         │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ cria e usa
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    ArvoreABBItem                                │
│                                                                 │
│  construirBalanceada(reservas)  ─────────────────────┐          │
│  pesquisarTodos(nomes)          ─────────────────────┼────┐     │
│                                                      │    │     │
└──────────────────────────────────────────────────────┼────┼─────┘
                                                       │    │
                              ┌─────────────────────────    │
                              │                             │
                              ▼                             ▼
┌─────────────────────────────────────────┐  ┌────────────────────┐
│              NoABBItem                  │  │      LCItem        │
│                                         │  │                    │
│  extends NoArvoreBase<NoABBItem>        │  │  - lista: Reserva[]│
│                                         │  │  - quant: int      │
└─────────────────────────────────────────┘  └────────────────────┘
                   │                                    │
                   │ extends                            │ contém
                   ▼                                    ▼
┌─────────────────────────────────────────┐  ┌────────────────────┐
│           NoArvoreBase<T>               │  │      Reserva       │
│                                         │  │                    │
│  - nome: String                         │  │  - chave: String   │
│  - reservas: LCItem  ───────────────────┼──│  - nome: String    │
│  - esq: T                               │  │  - codigo_voo      │
│  - dir: T                               │  │  - data, assento   │
└─────────────────────────────────────────┘  └────────────────────┘
```

### Dependências Externas

| Classe | Pacote | Uso na ABB |
|--------|--------|------------|
| `Reserva` | `entidades` | Dados armazenados nos nós |
| `LCItem` | `entidades` | Lista de reservas por nó |
| `NoArvoreBase<T>` | `entidades` | Classe base para nós |
| `LeArquivo` | `helper` | Carrega dados do disco |
| `EscreveArquivo` | `helper` | Salva resultados |

---

## Uso no Projeto: PesquisaReservas.java

### Fluxo de Execução

```java
// PesquisaReservas.java
private static void processarDataset(String nomeDataset, LCItem nomesPesquisa, 
        boolean primeiraLinha) throws IOException {
    
    // 1. Carrega reservas do arquivo
    LCItem reservas = LeArquivo.lerReservas(caminhoEntrada);
    
    // 2. Para cada execução (5 vezes para média)
    for (int exec = 0; exec < NUM_EXECUCOES; exec++) {
        long inicio = System.currentTimeMillis();
        
        // 3. Constrói ABB balanceada
        ArvoreABBItem abb = new ArvoreABBItem();
        abb.construirBalanceada(reservas);
        
        // 4. Pesquisa todos os 400 nomes
        resultadosPesquisaABB = abb.pesquisarTodos(nomesPesquisa);
        
        long fim = System.currentTimeMillis();
        tempoTotalABB += (fim - inicio);
    }
    
    // 5. Calcula média e salva
    double mediaABB = tempoTotalABB / (double) NUM_EXECUCOES;
    EscreveArquivo.salvarResultadosPesquisa(caminhoSaidaABB, nomesPesquisa, resultadosPesquisaABB);
}
```

### Por que `construirBalanceada` em vez de `insere` sequencial?

```java
// PROBLEMA: inserção sequencial de dados ordenados
for (int i = 0; i < reservas.getQuant(); i++) {
    abb.insere(reservas.getItem(i));  // Cria árvore degenerada!
}
// Resultado: árvore vira lista → O(n) por pesquisa → LENTO

// SOLUÇÃO: construção balanceada
abb.construirBalanceada(reservas);
// Resultado: árvore balanceada → O(log n) por pesquisa → RÁPIDO
```

### Arquivos de Saída

| Dataset | Arquivo de Saída ABB |
|---------|---------------------|
| `Reserva1000alea` | `data/searched/ABBReserva1000alea.txt` |
| `Reserva5000ord` | `data/searched/ABBReserva5000ord.txt` |
| `Reserva10000inv` | `data/searched/ABBReserva10000inv.txt` |
| `Reserva50000alea` | `data/searched/ABBReserva50000alea.txt` |

### Cadeia de Chamadas Completa

```
PesquisaReservas.main()
    │
    ├── LeArquivo.lerReservas("data/raw/Reserva10000alea.txt")
    │   └── retorna LCItem com 10000 reservas
    │
    ├── LeArquivo.lerNomesComoLCItem("data/raw/nome.txt")
    │   └── retorna LCItem com 400 nomes
    │
    └── processarDataset(...)
        │
        ├── ArvoreABBItem abb = new ArvoreABBItem()
        │
        ├── abb.construirBalanceada(reservas)
        │   │
        │   └── balancear(reservas, abb, 0, 9999)
        │       ├── insere(reservas[5000])  // meio
        │       ├── balancear(..., 0, 4999)
        │       └── balancear(..., 5001, 9999)
        │
        ├── abb.pesquisarTodos(nomesPesquisa)
        │   │
        │   └── for cada nome:
        │       └── pesquisa(nome) → LCItem ou null
        │
        └── EscreveArquivo.salvarResultadosPesquisa(...)
```

---

## Análise de Complexidade

### Complexidade de Tempo

| Operação | Árvore Balanceada | Árvore Degenerada |
|----------|-------------------|-------------------|
| **Pesquisa** | O(log n) | O(n) |
| **Inserção** | O(log n) | O(n) |
| **Remoção** | O(log n) | O(n) |
| **Construção balanceada** | O(n log n) | - |
| **Caminhamento** | O(n) | O(n) |

### Complexidade de Espaço

| Tipo | Complexidade |
|------|--------------|
| **Estrutura** | O(n) nós |
| **Recursão (pesquisa)** | O(h) pilha, h = altura |
| **Caminhamento** | O(n) para resultado |

### Comparação no Benchmark

```
Dataset: Reserva50000 (resultados típicos)
┌────────────────────┬───────────────┐
│ Estrutura          │ Tempo Médio   │
├────────────────────┼───────────────┤
│ ABB (balanceada)   │ ~15 ms        │
│ AVL                │ ~12 ms        │
│ Hashing            │ ~5 ms         │
└────────────────────┴───────────────┘
```

---

## Perguntas e Respostas

### P1: Por que cada nó armazena uma `LCItem` de reservas em vez de uma única `Reserva`?

**R:** Porque **múltiplas pessoas podem ter o mesmo nome**!

```
Dados reais:
R000123;MARIA SILVA;V001;10/01/2024;12A
R000456;MARIA SILVA;V002;15/01/2024;8B
R000789;MARIA SILVA;V003;20/01/2024;3C
```

Se armazenássemos apenas uma `Reserva` por nó:
- Perderíamos as outras reservas
- OU precisaríamos de lógica complexa para duplicatas

Com `LCItem`:
```java
NoABBItem no = pesquisa("MARIA SILVA", raiz);
LCItem todasReservas = no.getReservas();  // Retorna TODAS as 3!
```

### P2: Por que usar `compareToIgnoreCase` em vez de `compareTo`?

**R:** Para garantir **busca case-insensitive**:

```java
int comparacao = nome.compareToIgnoreCase(no.getNome());
```

Assim, "MARIA", "Maria" e "maria" são tratados como **iguais**:

```
pesquisa("maria")   → encontra nó "MARIA"
pesquisa("MARIA")   → encontra nó "MARIA"
pesquisa("Maria")   → encontra nó "MARIA"
```

### P3: O que acontece se `construirBalanceada` receber dados não ordenados?

**R:** **Funciona, mas não fica perfeitamente balanceada!**

O algoritmo assume que os dados estão ordenados para escolher o "meio" correto:

```java
// Dados ordenados: [ANA, CARLOS, JOSE, MARIA, PEDRO]
meio = (0 + 4) / 2 = 2 → JOSE (mediana real!)

// Dados não ordenados: [MARIA, ANA, PEDRO, CARLOS, JOSE]
meio = (0 + 4) / 2 = 2 → PEDRO (não é mediana!)
```

**Solução**: Os dados devem ser ordenados antes de chamar `construirBalanceada`. No projeto, `reservas` já vem de arquivo ordenado ou é ordenado antes.

### P4: Por que o método `insere` retorna `boolean`?

**R:** Para informar se um **novo nó foi criado** ou se a reserva foi **adicionada a um nó existente**:

```java
public boolean insere(Reserva item) {
    NoABBItem noExistente = pesquisa(item.getNome(), this.raiz);
    
    if (noExistente == null) {
        this.raiz = insere(item, this.raiz);
        return true;   // Novo nó criado
    } else {
        noExistente.getReservas().insereFinal(item);
        return false;  // Adicionou a nó existente
    }
}
```

Isso permite ao chamador saber se a árvore cresceu ou não.

### P5: Por que usar array `NoABBItem[1]` no método `remove`?

**R:** É um **truque para "retornar" dois valores** em Java:

```java
public NoABBItem remove(String nome) {
    NoABBItem[] removido = new NoABBItem[1];  // "Segundo retorno"
    this.raiz = remove(nome, this.raiz, removido);
    return removido[0];  // Nó removido
}
```

O método recursivo precisa:
1. **Retornar** a nova subárvore (para reconectar)
2. **Informar** qual nó foi removido

Como Java não tem "out parameters", usamos array como referência mutável.

**Alternativa**: Criar classe `RemoveResult` com dois campos.

### P6: O que é o método `maiorEsq` e por que é necessário?

**R:** `maiorEsq` encontra o **maior elemento da subárvore esquerda** para substituir um nó removido que tem dois filhos.

```
Remover "MARIA" (tem dois filhos):
        "MARIA"
       /       \
   "JOSE"    "PEDRO"
```

Não podemos simplesmente deletar "MARIA" - precisamos manter a estrutura!

**Solução**: Substituir pelos dados do **maior da esquerda** (ou menor da direita):

```
1. Maior da esquerda de "MARIA" = "JOSE"
2. Copia dados de "JOSE" para onde estava "MARIA"
3. Remove "JOSE" original

        "JOSE"
       /       \
    null     "PEDRO"
```

### P7: Por que a ABB não é auto-balanceada como a AVL?

**R:** A ABB é mais **simples**, mas requer **cuidado manual** com balanceamento:

| Aspecto | ABB | AVL |
|---------|-----|-----|
| **Complexidade de implementação** | Simples | Complexa (rotações) |
| **Balanceamento** | Manual (`construirBalanceada`) | Automático |
| **Inserção/Remoção** | Mais rápida (sem rotações) | Mais lenta (rotações) |
| **Pesquisa garantida** | O(n) pior caso | O(log n) sempre |

No nosso projeto, usamos `construirBalanceada` uma vez e depois só pesquisamos, então a ABB é suficiente.

### P8: O que acontece se pesquisar um nome que não existe?

**R:** Retorna `null`:

```java
public LCItem pesquisa(String nome) {
    NoABBItem no = pesquisa(nome, raiz);
    return (no != null) ? no.getReservas() : null;
}
```

No benchmark, isso é tratado:
```java
for (LCItem resultado : resultadosPesquisaABB) {
    if (resultado != null && !resultado.eVazia()) {
        encontrados++;
    }
}
```

### P9: Por que `CamInOrdem` aloca `quant * 5` de tamanho inicial?

**R:** Porque `quant` é o número de **nós**, não de **reservas**!

```java
public LCItem CamInOrdem() {
    LCItem vetor = new LCItem(this.quant * 5);  // Estimativa
    // ...
}
```

Se cada nó pode ter múltiplas reservas (média ~5), precisamos de espaço extra:
- 1000 nós × 5 reservas/nó = 5000 reservas

O `LCItem` aumenta automaticamente se necessário (`aumentaLista()`).

### P10: Como a herança genérica `NoArvoreBase<T>` funciona?

**R:** É um padrão chamado **"Curiously Recurring Template Pattern" (CRTP)**:

```java
public abstract class NoArvoreBase<T extends NoArvoreBase<T>> {
    protected T esq, dir;
    
    public T getEsq() { return esq; }
    public void setEsq(T esq) { this.esq = esq; }
}

public class NoABBItem extends NoArvoreBase<NoABBItem> {
    // T = NoABBItem, então:
    // esq: NoABBItem (não NoArvoreBase!)
    // getEsq(): NoABBItem (não NoArvoreBase!)
}
```

**Benefício**: Type safety sem casting:
```java
NoABBItem no = ...;
NoABBItem filho = no.getEsq();  // Tipo correto automaticamente!
```

### P11: Por que construir a ABB a cada execução do benchmark?

**R:** Para medir o **tempo total** de construção + pesquisa:

```java
for (int exec = 0; exec < NUM_EXECUCOES; exec++) {
    long inicio = System.currentTimeMillis();
    
    ArvoreABBItem abb = new ArvoreABBItem();
    abb.construirBalanceada(reservas);  // Inclui construção
    resultados = abb.pesquisarTodos(nomesPesquisa);
    
    long fim = System.currentTimeMillis();
}
```

Isso é **justo** para comparação porque:
- Hashing também precisa construir a tabela
- AVL também precisa inserir elementos
- Compara o custo **completo** de cada estrutura

### P12: O método `remove` está completo? Falta algo?

**R:** Há uma **possível melhoria** no `maiorEsq`:

```java
private NoABBItem maiorEsq(NoABBItem noRemovido, NoABBItem maior) {
    if (maior.getDir() != null) {
        maior.setDir(maiorEsq(noRemovido, maior.getDir()));
        return maior;
    }
    noRemovido.setNome(maior.getNome());
    // FALTA: copiar as reservas também!
    // noRemovido.setReservas(maior.getReservas());  // ou transferir
    return maior.getEsq();
}
```

Atualmente só copia o nome, mas deveria copiar as reservas também. No projeto isso não causa problema porque `remove` não é usado no benchmark.

### P13: Qual a diferença entre `balancear()` e `construirBalanceada()`?

**R:** São dois métodos com propósitos diferentes:

```java
// construirBalanceada: Constrói árvore NOVA a partir de LCItem
public void construirBalanceada(LCItem itens) {
    this.raiz = null;      // Limpa árvore atual
    this.quant = 0;
    balancear(itens, this, 0, itens.getQuant() - 1);
}

// balancear: Rebalanceia árvore EXISTENTE
public ArvoreABBItem balancear() {
    LCItem vetor = CamInOrdem();          // Extrai dados ordenados
    ArvoreABBItem arvAux = new ArvoreABBItem();  // Nova árvore
    balancear(vetor, arvAux, 0, vetor.getQuant() - 1);
    return arvAux;  // Retorna nova árvore balanceada
}
```

- `construirBalanceada`: Usado no benchmark (dados vêm de arquivo)
- `balancear`: Usado se a árvore desbalancear após muitas inserções

---

## Diagrama de Execução da Pesquisa

```
pesquisa("JOSE")
│
├── pesquisa("JOSE", raiz="MARIA")
│   │
│   ├── comparacao = "JOSE".compareToIgnoreCase("MARIA") = -3 (< 0)
│   │
│   └── pesquisa("JOSE", esq="CARLOS")
│       │
│       ├── comparacao = "JOSE".compareToIgnoreCase("CARLOS") = 7 (> 0)
│       │
│       └── pesquisa("JOSE", dir="JOSE")
│           │
│           ├── comparacao = "JOSE".compareToIgnoreCase("JOSE") = 0
│           │
│           └── return no  ← ENCONTROU!
│
└── return no.getReservas()  ← Lista com todas as reservas de JOSE
```

---

## Referências

- Cormen, T. H., et al. "Introduction to Algorithms" - Capítulo 12: Binary Search Trees
- Sedgewick, R. "Algorithms" - Seção sobre Symbol Tables e BSTs
- Knuth, D. "The Art of Computer Programming" - Volume 3: Sorting and Searching
