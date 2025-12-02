# Árvore AVL - Documentação Detalhada

## Índice

1. [Visão Geral](#visão-geral)
2. [Conceitos Fundamentais](#conceitos-fundamentais)
3. [Arquitetura e Herança](#arquitetura-e-herança)
4. [Implementação do NoAVLItem](#implementação-do-noavlitem)
5. [Implementação da ArvoreAVLItem](#implementação-da-arvoreadvlitem)
6. [Rotações - O Coração da AVL](#rotações---o-coração-da-avl)
7. [Dependências e Integração](#dependências-e-integração)
8. [Uso no Projeto: PesquisaReservas.java](#uso-no-projeto-pesquisareservasjava)
9. [Análise de Complexidade](#análise-de-complexidade)
10. [Perguntas e Respostas](#perguntas-e-respostas)

---

## Visão Geral

A **Árvore AVL** (Adelson-Velsky e Landis, 1962) é uma Árvore Binária de Busca **auto-balanceada**. A cada inserção ou remoção, a árvore verifica e corrige automaticamente seu balanceamento através de **rotações**.

### Características da Implementação

| Aspecto | Descrição |
|---------|-----------|
| **Estrutura** | Árvore binária auto-balanceada |
| **Chave de busca** | Nome da reserva (String, case-insensitive) |
| **Valores** | Lista de reservas (`LCItem`) por nome |
| **Balanceamento** | Automático via rotações (FB: -1, 0, +1) |
| **Herança** | `NoAVLItem` estende `NoArvoreBase<NoAVLItem>` |
| **Diferencial** | Fator de balanceamento em cada nó |

### Localização no Projeto

```
src/br/faesa/C3/algoritmos/pesquisa/AVL/
├── ArvoreAVLItem.java    # Estrutura da árvore com rotações
└── NoAVLItem.java        # Nó da árvore (herda de NoArvoreBase + FB)
```

---

## Conceitos Fundamentais

### Fator de Balanceamento (FB)

O **Fator de Balanceamento** de um nó é:

```
FB(nó) = altura(subárvore direita) - altura(subárvore esquerda)
```

Em uma AVL válida, todo nó deve ter FB ∈ {-1, 0, +1}:

| FB | Significado |
|----|-------------|
| **-1** | Subárvore esquerda é 1 nível mais alta |
| **0** | Subárvores têm mesma altura |
| **+1** | Subárvore direita é 1 nível mais alta |

```
      FB=-1          FB=0           FB=+1
        A              A              A
       /              / \              \
      B              B   C              B
```

### Quando Rebalancear?

Após inserção, se algum nó ficar com FB ∈ {-2, +2}, a árvore está **desbalanceada** e precisa de rotação.

```
Inserção causou FB = -2:
        A (FB=-2)
       /
      B (FB=-1)
     /
    C (inserido)

→ Precisa de ROTAÇÃO À DIREITA
```

### Os 4 Casos de Desbalanceamento

| Caso | Padrão | Solução |
|------|--------|---------|
| **LL** | Inserção na esquerda da esquerda | Rotação Simples à Direita |
| **RR** | Inserção na direita da direita | Rotação Simples à Esquerda |
| **LR** | Inserção na direita da esquerda | Rotação Dupla à Direita |
| **RL** | Inserção na esquerda da direita | Rotação Dupla à Esquerda |

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
│  + getNome(), setNome()                                         │
│  + getReservas()                                                │
│  + getEsq(), setEsq(), getDir(), setDir()                       │
└─────────────────────────────────────────────────────────────────┘
                              △
                              │ extends
                              │
┌─────────────────────────────────────────────────────────────────┐
│                    NoAVLItem                                    │
│              extends NoArvoreBase<NoAVLItem>                    │
│                                                                 │
│  - fatorBalanceamento: int    ← ADICIONAL!                      │
│                                                                 │
│  + NoAVLItem(Reserva item)                                      │
│  + getFatorBalanceamento(): int                                 │
│  + setFatorBalanceamento(int): void                             │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ usa
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    ArvoreAVLItem                                │
│                                                                 │
│  - raiz: NoAVLItem                                              │
│  - quant: int                                                   │
│  - balancear: boolean    ← FLAG de propagação                   │
│                                                                 │
│  + pesquisa(String): LCItem                                     │
│  + insere(Reserva): void                                        │
│  + carregarDeLCItem(LCItem): void                               │
│  + pesquisarTodos(LCItem): LCItem[]                             │
│  + CamInOrdem(): LCItem                                         │
│  + mostrarEstrutura(): void                                     │
│                                                                 │
│  - inserir(Reserva, NoAVLItem): NoAVLItem                       │
│  - balancearDir(NoAVLItem): NoAVLItem                           │
│  - balancearEsq(NoAVLItem): NoAVLItem                           │
│  - rotaçãoDireita(NoAVLItem): NoAVLItem                         │
│  - rotaçãoEsquerda(NoAVLItem): NoAVLItem                        │
└─────────────────────────────────────────────────────────────────┘
```

### Diferença entre NoABBItem e NoAVLItem

```java
// NoABBItem - Apenas construtor
public class NoABBItem extends NoArvoreBase<NoABBItem> {
    public NoABBItem(Reserva item) {
        super(item);
    }
}

// NoAVLItem - Adiciona fator de balanceamento
public class NoAVLItem extends NoArvoreBase<NoAVLItem> {
    private int fatorBalanceamento;  // ← ADICIONAL!
    
    public NoAVLItem(Reserva item) {
        super(item);
        this.fatorBalanceamento = 0;  // Nó novo sempre tem FB=0
    }
    
    public int getFatorBalanceamento() { return fatorBalanceamento; }
    public void setFatorBalanceamento(int fb) { this.fatorBalanceamento = fb; }
}
```

---

## Implementação do NoAVLItem

### Código Completo

```java
package br.faesa.C3.algoritmos.pesquisa.AVL;

import br.faesa.C3.entidades.Reserva;
import br.faesa.C3.entidades.NoArvoreBase;

/**
 * Nó para Árvore AVL.
 * Herda todos os atributos e métodos de NoArvoreBase.
 * Adiciona fatorBalanceamento para controle de balanceamento.
 */
public class NoAVLItem extends NoArvoreBase<NoAVLItem> {
    private int fatorBalanceamento;

    public NoAVLItem(Reserva item) {
        super(item);
        this.fatorBalanceamento = 0;  // Nó novo: FB = 0
    }
    
    public int getFatorBalanceamento() { 
        return fatorBalanceamento; 
    }
    
    public void setFatorBalanceamento(int fatorBalanceamento) {
        this.fatorBalanceamento = fatorBalanceamento;
    }
}
```

### Por que FB = 0 inicialmente?

Um nó recém-criado é sempre uma **folha** (sem filhos):
- Altura da subárvore esquerda = 0
- Altura da subárvore direita = 0
- FB = 0 - 0 = **0**

---

## Implementação da ArvoreAVLItem

### Atributos da Classe

```java
public class ArvoreAVLItem {
    private NoAVLItem raiz;      // Raiz da árvore
    private boolean balancear;   // Flag: precisa propagar balanceamento?
    private int quant;           // Quantidade de nós únicos
}
```

**O atributo `balancear`** é crucial:
- `true`: A altura da subárvore mudou, ancestrais devem verificar FB
- `false`: A altura não mudou, não precisa mais verificar

### Método `pesquisa(String nome)`

```java
public LCItem pesquisa(String nome) {
    NoAVLItem no = pesquisar(nome, this.raiz);
    if (no == null) {
        return null;
    }
    return no.getReservas();
}

private NoAVLItem pesquisar(String nome, NoAVLItem noAtual) {
    if (noAtual == null) {
        return null;
    }

    int comparacao = nome.compareToIgnoreCase(noAtual.getNome());
    
    if (comparacao == 0) {
        return noAtual;  // Encontrou!
    } else if (comparacao < 0) {
        return pesquisar(nome, noAtual.getEsq());
    } else {
        return pesquisar(nome, noAtual.getDir());
    }
}
```

**Idêntico à ABB** - a pesquisa não muda, apenas a inserção.

### Método `insere(Reserva item)`

```java
public void insere(Reserva item) {
    // 1. Verifica se nome já existe
    NoAVLItem noExistente = pesquisar(item.getNome(), this.raiz);
    
    if (noExistente != null) {
        // Nome existe: adiciona à lista de reservas
        noExistente.getReservas().insereFinal(item);
    } else {
        // Nome novo: insere com balanceamento
        this.raiz = inserir(item, this.raiz);
        this.quant++;
    }
}
```

### Método Recursivo `inserir`

```java
private NoAVLItem inserir(Reserva item, NoAVLItem no) {
    if (no == null) {
        // Base: cria novo nó
        NoAVLItem novo = new NoAVLItem(item);
        this.balancear = true;  // Sinaliza: altura mudou!
        return novo;
    }
    
    int comparacao = item.getNome().compareToIgnoreCase(no.getNome());
    
    if (comparacao < 0) {
        // Insere à ESQUERDA → verifica balanceamento à DIREITA
        no.setEsq(inserir(item, no.getEsq()));
        no = balancearDir(no);  // Nome confuso, mas correto!
        return no;
    } else if (comparacao > 0) {
        // Insere à DIREITA → verifica balanceamento à ESQUERDA
        no.setDir(inserir(item, no.getDir()));
        no = balancearEsq(no);
        return no;
    } else {
        // Não deveria chegar aqui
        return no;
    }
}
```

**Nomenclatura "invertida"**:
- `balancearDir`: Inseriu na **esquerda**, pode precisar rotação à **direita**
- `balancearEsq`: Inseriu na **direita**, pode precisar rotação à **esquerda**

---

## Rotações - O Coração da AVL

### Método `balancearDir`

Chamado quando inserção foi na **subárvore esquerda**:

```java
private NoAVLItem balancearDir(NoAVLItem no) {
    if (this.balancear) {
        switch (no.getFatorBalanceamento()) {
            case 1:   // FB era +1, agora fica 0
                no.setFatorBalanceamento(0);
                this.balancear = false;  // Altura não mudou
                break;
            case 0:   // FB era 0, agora fica -1
                no.setFatorBalanceamento(-1);
                // balancear continua true (altura aumentou)
                break;
            case -1:  // FB era -1, agora seria -2 → ROTAÇÃO!
                no = rotaçãoDireita(no);
                // rotação já seta balancear = false
        }
    }
    return no;
}
```

### Diagrama do `balancearDir`

```
Inseriu na ESQUERDA. Como muda o FB?

Caso FB = +1 (direita era maior):
    Antes:          Depois:
       A (+1)          A (0)
      / \             / \
     B   C           B   C
         \          /     \
          D        E       D
                   ↑
              INSERIDO

    Altura total NÃO mudou! balancear = false

Caso FB = 0 (equilibrado):
    Antes:          Depois:
       A (0)           A (-1)
      / \             / \
     B   C           B   C
                    /
                   E
                   ↑
              INSERIDO

    Altura AUMENTOU! balancear continua true

Caso FB = -1 (esquerda já era maior):
    Antes:          Depois (SEM rotação):
       A (-1)          A (-2) ← INVÁLIDO!
      /               /
     B               B
                    /
                   E

    Precisa de ROTAÇÃO À DIREITA!
```

### Método `rotaçãoDireita` (Rotação Simples e Dupla)

```java
private NoAVLItem rotaçãoDireita(NoAVLItem no) {
    NoAVLItem temp1, temp2;
    temp1 = no.getEsq();

    if (temp1.getFatorBalanceamento() == -1) {
        // ═══════════════════════════════════════════════════════
        // ROTAÇÃO SIMPLES À DIREITA (Caso LL)
        // ═══════════════════════════════════════════════════════
        no.setEsq(temp1.getDir());
        temp1.setDir(no);
        no.setFatorBalanceamento(0);
        no = temp1;
    } else {
        // ═══════════════════════════════════════════════════════
        // ROTAÇÃO DUPLA À DIREITA (Caso LR)
        // ═══════════════════════════════════════════════════════
        temp2 = temp1.getDir();
        temp1.setDir(temp2.getEsq());
        temp2.setEsq(temp1);
        no.setEsq(temp2.getDir());
        temp2.setDir(no);

        // Recalcula FBs baseado no FB do temp2
        if (temp2.getFatorBalanceamento() == -1)
            no.setFatorBalanceamento(1);
        else
            no.setFatorBalanceamento(0);

        if (temp2.getFatorBalanceamento() == 1)
            temp1.setFatorBalanceamento(-1);
        else
            temp1.setFatorBalanceamento(0);

        no = temp2;
    }

    no.setFatorBalanceamento(0);
    this.balancear = false;  // Após rotação, altura volta ao normal
    return no;
}
```

### Rotação Simples à Direita (Caso LL) - Diagrama

```
ANTES (FB = -2, inseriu LL):           DEPOIS:
        A (-2)                            B (0)
       /                                 / \
      B (-1)                            C   A (0)
     /
    C (inserido)

Passos:
1. temp1 = A.esq = B
2. A.esq = temp1.dir = null
3. temp1.dir = A
4. no = temp1 = B

Código:
no.setEsq(temp1.getDir());  // A.esq = B.dir
temp1.setDir(no);           // B.dir = A
no = temp1;                 // raiz = B
```

### Rotação Dupla à Direita (Caso LR) - Diagrama

```
ANTES (FB = -2, inseriu LR):           DEPOIS:
        A (-2)                            D (0)
       /                                 / \
      B (+1)                            B   A
       \                               /     \
        D                             C       E
       / \
      C   E (inserido)

Passos:
1. temp1 = A.esq = B
2. temp2 = temp1.dir = D
3. B.dir = D.esq = C
4. D.esq = B
5. A.esq = D.dir = E
6. D.dir = A
7. no = D

É como se fizéssemos:
- Rotação à ESQUERDA em B
- Rotação à DIREITA em A
```

### Método `balancearEsq` e `rotaçãoEsquerda`

Simétricos aos anteriores, para inserções na direita:

```java
private NoAVLItem balancearEsq(NoAVLItem no) {
    if (this.balancear) {
        switch (no.getFatorBalanceamento()) {
            case -1:  // FB era -1, agora fica 0
                no.setFatorBalanceamento(0);
                this.balancear = false;
                break;
            case 0:   // FB era 0, agora fica +1
                no.setFatorBalanceamento(1);
                break;
            case 1:   // FB era +1, agora seria +2 → ROTAÇÃO!
                no = rotaçãoEsquerda(no);
        }
    }
    return no;
}
```

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
│                    ArvoreAVLItem                                │
│                                                                 │
│  carregarDeLCItem(reservas)  ────────────────────────┐          │
│  pesquisarTodos(nomes)       ────────────────────────┼────┐     │
│                                                      │    │     │
└──────────────────────────────────────────────────────┼────┼─────┘
                                                       │    │
                              ┌─────────────────────────    │
                              │                             │
                              ▼                             ▼
┌─────────────────────────────────────────┐  ┌────────────────────┐
│              NoAVLItem                  │  │      LCItem        │
│                                         │  │                    │
│  extends NoArvoreBase<NoAVLItem>        │  │  - lista: Reserva[]│
│  + fatorBalanceamento: int              │  │  - quant: int      │
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

### Comparação ABB vs AVL

| Aspecto | ABB | AVL |
|---------|-----|-----|
| **Nó** | `NoABBItem` | `NoAVLItem` (+ FB) |
| **Balanceamento** | Manual (`construirBalanceada`) | Automático (rotações) |
| **Inserção** | O(h), h pode ser n | O(log n) garantido |
| **Complexidade código** | Simples | Complexa (rotações) |
| **Uso no projeto** | `abb.construirBalanceada(reservas)` | `avl.carregarDeLCItem(reservas)` |

---

## Uso no Projeto: PesquisaReservas.java

### Fluxo de Execução

```java
// PesquisaReservas.java
private static void processarDataset(...) throws IOException {
    // 1. Carrega reservas
    LCItem reservas = LeArquivo.lerReservas(caminhoEntrada);
    
    // 2. Para cada execução (5 vezes)
    for (int exec = 0; exec < NUM_EXECUCOES; exec++) {
        long inicio = System.currentTimeMillis();
        
        // 3. Constrói AVL (auto-balanceada!)
        ArvoreAVLItem avl = new ArvoreAVLItem();
        avl.carregarDeLCItem(reservas);  // Insere com balanceamento
        
        // 4. Pesquisa todos os 400 nomes
        resultadosPesquisaAVL = avl.pesquisarTodos(nomesPesquisa);
        
        long fim = System.currentTimeMillis();
        tempoTotalAVL += (fim - inicio);
    }
    
    // 5. Calcula média e salva
    double mediaAVL = tempoTotalAVL / (double) NUM_EXECUCOES;
    EscreveArquivo.salvarResultadosPesquisa(caminhoSaidaAVL, nomesPesquisa, resultadosPesquisaAVL);
}
```

### Método `carregarDeLCItem`

```java
public void carregarDeLCItem(LCItem reservas) {
    for (int i = 0; i < reservas.getQuant(); i++) {
        insere(reservas.getItem(i));  // Cada inserção pode causar rotações
    }
}
```

**Diferença da ABB**:
- ABB: `construirBalanceada` assume dados ordenados
- AVL: `carregarDeLCItem` aceita **qualquer ordem** (auto-balanceia)

### Arquivos de Saída

| Dataset | Arquivo de Saída AVL |
|---------|---------------------|
| `Reserva1000alea` | `data/searched/AVLReserva1000alea.txt` |
| `Reserva5000ord` | `data/searched/AVLReserva5000ord.txt` |
| `Reserva10000inv` | `data/searched/AVLReserva10000inv.txt` |
| `Reserva50000alea` | `data/searched/AVLReserva50000alea.txt` |

### Cadeia de Chamadas na Inserção

```
avl.carregarDeLCItem(reservas)
    │
    └── for cada reserva:
        │
        └── avl.insere(reserva)
            │
            ├── pesquisar(nome, raiz) → existe?
            │   ├── SIM: noExistente.getReservas().insereFinal(reserva)
            │   └── NÃO: inserir(reserva, raiz)
            │
            └── inserir(reserva, no)
                │
                ├── no == null → cria NoAVLItem, balancear = true
                │
                ├── comparacao < 0 (vai para esquerda)
                │   ├── no.setEsq(inserir(item, no.getEsq()))
                │   └── no = balancearDir(no)
                │       │
                │       └── switch(FB)
                │           ├── 1 → FB=0, balancear=false
                │           ├── 0 → FB=-1
                │           └── -1 → rotaçãoDireita(no)
                │
                └── comparacao > 0 (vai para direita)
                    ├── no.setDir(inserir(item, no.getDir()))
                    └── no = balancearEsq(no)
```

---

## Análise de Complexidade

### Complexidade de Tempo

| Operação | Complexidade | Justificativa |
|----------|--------------|---------------|
| **Pesquisa** | O(log n) | Altura sempre ≤ 1.44 log₂(n) |
| **Inserção** | O(log n) | Descida + máximo 2 rotações |
| **Rotação** | O(1) | Apenas troca de ponteiros |
| **Construção** | O(n log n) | n inserções de O(log n) cada |
| **Caminhamento** | O(n) | Visita cada nó uma vez |

### Por que AVL garante O(log n)?

A altura máxima de uma AVL com n nós é:

```
h ≤ 1.44 × log₂(n + 2) - 0.328
```

Para n = 50.000:
- ABB desbalanceada: altura pode ser 50.000
- AVL: altura máxima ≈ 23

### Comparação no Benchmark

```
Dataset: Reserva50000 (resultados típicos)
┌────────────────────┬──────────────┬─────────────────┐
│ Estrutura          │ Tempo Médio  │ Altura Árvore   │
├────────────────────┼──────────────┼─────────────────┤
│ ABB (balanceada)   │ ~15 ms       │ ~16             │
│ AVL                │ ~12 ms       │ ~16 (garantido) │
│ Hashing            │ ~5 ms        │ N/A             │
└────────────────────┴──────────────┴─────────────────┘
```

### Complexidade de Espaço

| Tipo | Complexidade |
|------|--------------|
| **Estrutura** | O(n) nós + 1 int/nó para FB |
| **Recursão** | O(log n) pilha |

---

## Perguntas e Respostas

### P1: Por que usar uma flag `balancear` em vez de calcular FB diretamente?

**R:** **Eficiência e simplicidade!**

Calcular altura de cada subárvore recursivamente seria O(n) por nó. A flag `balancear` propaga informação de forma O(1):

```java
this.balancear = true;  // "A altura da minha subárvore aumentou!"
```

Quando ancestral recebe essa informação:
- Se FB muda para 0: altura total NÃO mudou → `balancear = false`
- Se FB muda para ±1: altura total MUDOU → `balancear` continua `true`
- Se FB seria ±2: rotação corrige → `balancear = false`

### P2: Por que `balancearDir` é chamado após inserir na ESQUERDA?

**R:** O nome indica a **direção da rotação potencial**, não da inserção:

```java
no.setEsq(inserir(item, no.getEsq()));  // Inseriu na ESQUERDA
no = balancearDir(no);  // Pode precisar rotação à DIREITA
```

Se inserir na esquerda desequilibra (FB = -2), a correção é **rotação à direita**.

**Alternativa de nomenclatura**: `verificarAposInsercaoEsquerda(no)` seria mais claro.

### P3: Qual a diferença entre rotação simples e dupla?

**R:** Depende da **forma do desbalanceamento**:

```
ROTAÇÃO SIMPLES (LL ou RR):
Desbalanceamento em "linha reta"

    A (-2)                B (0)
   /            →        / \
  B (-1)               C   A
 /
C

ROTAÇÃO DUPLA (LR ou RL):
Desbalanceamento em "zig-zag"

    A (-2)                D (0)
   /            →        / \
  B (+1)               B   A
   \
    D

Rotação simples NÃO funcionaria no caso LR!
```

### P4: Como o código decide entre rotação simples e dupla?

**R:** Pelo **FB do filho**:

```java
private NoAVLItem rotaçãoDireita(NoAVLItem no) {
    temp1 = no.getEsq();

    if (temp1.getFatorBalanceamento() == -1) {
        // Filho também pende para esquerda → SIMPLES (LL)
    } else {
        // Filho pende para direita → DUPLA (LR)
    }
}
```

- Pai FB = -2, Filho FB = -1: Mesma direção → **Simples**
- Pai FB = -2, Filho FB = +1: Direções opostas → **Dupla**

### P5: Por que não há método de remoção na AVL do projeto?

**R:** **Não é necessário para o benchmark!**

O projeto apenas:
1. Constrói a árvore (`carregarDeLCItem`)
2. Pesquisa nomes (`pesquisarTodos`)

Remoção em AVL é **complexa** (precisa rebalancear ao subir) e não foi implementada porque não é usada.

### P6: O que acontece se inserir dados já ordenados na AVL?

**R:** **A AVL lida perfeitamente!** Diferente da ABB:

```
Inserindo [1, 2, 3, 4, 5] em ABB:
    1
     \
      2
       \
        3           Altura = n (degenerada)
         \
          4
           \
            5

Inserindo [1, 2, 3, 4, 5] em AVL:
    1 → insere 2 → FB=-2 → rotação!
    
        2
       / \
      1   3 → insere 4 → FB=+2 → rotação!
      
            3
           / \
          2   4
         /     \
        1       5     Altura = O(log n) ✓
```

### P7: Por que FB usa valores -1, 0, +1 e não altura absoluta?

**R:** **Economia de espaço e simplicidade!**

- FB: 1 byte (-1, 0, +1) ou 2 bits
- Altura: int completo (4 bytes)

Para n = 50.000 nós:
- Com FB: ~50 KB extras
- Com altura: ~200 KB extras

E a lógica de balanceamento só precisa saber se está desbalanceado (|FB| > 1), não a altura exata.

### P8: O cast `(byte)` no `setFatorBalanceamento` é necessário?

**R:** No código atual, `fatorBalanceamento` é `int`, então o cast é **redundante**:

```java
no.setFatorBalanceamento((byte) 0);  // Cast desnecessário
no.setFatorBalanceamento(0);          // Funciona igual
```

O cast sugere que **originalmente** era `byte` (mais eficiente em memória). É um **resquício de implementação anterior**.

### P9: Por que pesquisa retorna `LCItem` e não `NoAVLItem`?

**R:** **Encapsulamento!** O usuário não precisa conhecer a estrutura interna:

```java
// Interface pública: retorna dados
public LCItem pesquisa(String nome) {
    NoAVLItem no = pesquisar(nome, this.raiz);
    return (no != null) ? no.getReservas() : null;
}

// Método interno: trabalha com nós
private NoAVLItem pesquisar(String nome, NoAVLItem noAtual) { ... }
```

O chamador quer as **reservas**, não o nó da árvore.

### P10: Como funciona o método `mostrarEstrutura`?

**R:** É um método de **debug** que imprime a árvore visualmente:

```java
avl.mostrarEstrutura();
// Saída:
// Estrutura da Árvore AVL:
// └── MARIA (FB: 0, Reservas: 3)
//     ├── PEDRO (FB: 0, Reservas: 1)
//     │   └── ZELIA (FB: 0, Reservas: 2)
//     └── CARLOS (FB: -1, Reservas: 1)
//         └── ANA (FB: 0, Reservas: 1)
```

Útil para verificar se a árvore está balanceada corretamente.

### P11: Quantas rotações podem ocorrer em uma inserção?

**R:** **No máximo 2 rotações** (uma dupla = 2 simples):

```
Uma rotação (simples ou dupla) sempre restaura a altura original da subárvore.
Após rotação, balancear = false, e nenhum ancestral precisa rotacionar.
```

Isso garante que inserção é O(log n), não O(n).

### P12: Por que AVL é mais rápida que ABB no benchmark se ambas têm mesma altura?

**R:** No nosso benchmark, a diferença é pequena porque:

1. ABB usa `construirBalanceada` (também resulta em árvore balanceada)
2. Ambas têm altura ~log n para os dados testados

A diferença apareceria mais se:
- ABB recebesse dados ordenados SEM `construirBalanceada`
- Houvesse muitas inserções/remoções dinâmicas

### P13: O que é o "custo" das rotações?

**R:** Rotações são **O(1)** - apenas movem ponteiros:

```java
// Rotação simples: 3 operações de ponteiro
no.setEsq(temp1.getDir());  // 1
temp1.setDir(no);           // 2
no = temp1;                 // 3

// Rotação dupla: 6 operações de ponteiro
```

O "custo" real é a **complexidade do código**, não performance.

---

## Diagrama Visual: Inserção com Rotação

```
Inserindo "ANA", "CARLOS", "MARIA", "PEDRO", "ZELIA" (ordem alfabética)

1. insere("ANA")
   ANA (FB=0)

2. insere("CARLOS")
   ANA (FB=+1)
     \
      CARLOS (FB=0)

3. insere("MARIA")
   ANA (FB=+2) → ROTAÇÃO ESQUERDA!
     \
      CARLOS (FB=+1)
        \
         MARIA

   Após rotação:
      CARLOS (FB=0)
      /     \
   ANA(0)  MARIA(0)

4. insere("PEDRO")
      CARLOS (FB=+1)
      /     \
   ANA(0)  MARIA(FB=+1)
              \
               PEDRO(0)

5. insere("ZELIA")
      CARLOS (FB=+2) → Precisa rotação, mas...
      /     \
   ANA(0)  MARIA(FB=+2)
              \
               PEDRO(+1)
                  \
                   ZELIA
   
   Rotação em MARIA primeiro:
      CARLOS (FB=+1)
      /     \
   ANA(0)  PEDRO(0)
           /    \
       MARIA(0)  ZELIA(0)

   Árvore final BALANCEADA!
```

---

## Referências

- Adelson-Velsky, G.; Landis, E. (1962). "An algorithm for the organization of information"
- Cormen, T. H., et al. "Introduction to Algorithms" - Capítulo 13: Red-Black Trees (conceitos similares)
- Knuth, D. "The Art of Computer Programming" - Volume 3: Sorting and Searching
- Sedgewick, R. "Algorithms" - Seção sobre Balanced Search Trees
