package br.faesa.C3.algoritmos.entidades;

public class LCItem implements Ordenavel {

    private Item[] lista;
    private int quant;

    public LCItem() {
        this.lista = new Item[10];
        this.quant = 0;
    }
    public LCItem(int tamanho) {
        this.lista = new Item[tamanho];
        this.quant = 0;
    }

    public int tamanho() {
        return this.lista.length;
    }

    public int getQuant() {
        return this.quant;
    }

    public boolean eVazia() {
        return this.quant == 0;
    }

    public boolean eCheia() {
        return this.getQuant() == this.tamanho();
    }

    public Item getItem(int posicao) {
        if (posicao < 0 || posicao >= this.tamanho()) {
            return null;
        }
        return this.lista[posicao];
    }

    public int pesquisa(int cod) {
        for (int i = 0; i < this.quant; i++) {
            if (this.lista[i].getCodigo() == cod) {
                return i; //achou o codigo e retorna posicao
            }
        }
        return -1; //terminou for e nao achou
    }
    /*
    insereFim (Item) => insere um Item no final da lista. Se ela estiver cheia, deve aumentar o tamanho em 50%.
     */

    private void aumentaLista() {
        int novoTamanho = (int) (this.quant * 1.5);
        Item[] novaLista = new Item[novoTamanho];
        for (int i = 0; i < quant; i++) {
            novaLista[i] = this.lista[i];
        }
        this.lista = novaLista;
    }

    public void insereFinal (Item item) {
        if (eCheia()) {
            aumentaLista();
        }
        this.lista[quant] = item;
        this.quant++;
    }

    /*
    insere (Item, posicao) => insere um Item em uma posição da lista, deslocando todos os elementos que estiverem após essa posição para o índice seguinte, retornando true. Se a lista estiver cheia, deve aumentar a lista em 50%. Se a posição for inválida (menor que zero ou maior que quant), deve retornar false.
     */

    public boolean insere (int position, Item item) {
        if (position < 0 || position > this.quant) { //a lista só pode ser preenchida continuamente
            return false;
        }
        if (eCheia()) {
            aumentaLista();
        }
        //deslocar itens da lista
        for (int i = this.quant - 1; i >= position ; i--) {
            this.lista[i+1] = this.lista[i];
        }
        this.lista[position] = item;
        this.quant++;
        return true;
    }

    public void insereInicio (Item item) {
        insere(0, item);
    }

    /*
    remove (pos) => deve remover um elemento que está na posição pos da lista. Se conseguir, deve retornar o Item removido, se não conseguir, retorna null.
     */
    public Item remover(int posicao) {
        Item aux;
        if (posicao < 0 || posicao >= this.quant) {
            return null;
        }
        aux = this.lista[posicao];
        for (int i = posicao; i < this.quant - 1; i++) {
            this.lista[i] = this.lista[i+1];
        }
        quant--;
        this.lista[this.quant - 1] = null; //limpa a última pos

        return aux;
    }

    /*
    remove (cod) => deve remover da lista o elemento cujo código é cod. Se conseguir, deve retornar o Item removido, se não conseguir, retorna null.
     */

    public Item removerCod(int cod) {
        int posicao = pesquisa(cod);
        if (posicao == -1) {
            return null;
        }
        return remover(posicao);
    }
    /*
    toString() => retornar uma String contendo todos os elementos da lista, do primeiro até o último.
     */

    public String toString () {
        String aux = "";
        for (int i=0; i<this.quant; i++) {
            aux += this.lista[i].toString()+"\n";
        }
        return aux;
    }

    /**
     * Ordena a lista usando HeapSort.
     */
    public void heapsort() {
        int dir = quant - 1, esq = (dir - 1) / 2;
        Item temp;

        while (esq >= 0) {
            refazheap(esq, this.quant - 1);
            esq--;
        }
        
        while (dir > 0) {
            temp = this.lista[0];
            this.lista[0] = this.lista[dir];
            this.lista[dir] = temp;
            dir--;
            refazheap(0, dir);
        }
    }

    private void refazheap(int esq, int dir) {
        int i = esq, maiorFilho = 2 * i + 1;
        Item raiz = this.lista[i];
        boolean heap = false;

        while ((maiorFilho <= dir) && (!heap)) {
            if (maiorFilho < dir)
                if (this.lista[maiorFilho].compareTo(this.lista[maiorFilho + 1]) < 0)
                    maiorFilho++;
            if (raiz.compareTo(this.lista[maiorFilho]) < 0) {
                this.lista[i] = this.lista[maiorFilho];
                i = maiorFilho;
                maiorFilho = 2 * i + 1;
            } else
                heap = true;
        }
        this.lista[i] = raiz;
    }

    /**
     * Ordena a lista usando QuickSort.
     */
    public void quicksort() {
        ordena(0, this.quant - 1);
    }

    private void ordena(int esq, int dir) {
        Item pivo, temp;
        int i = esq, j = dir;

        // 1. Escolhe o pivô (elemento do meio)
        pivo = this.lista[(i + j) / 2];

        // 2. Particiona o array
        do {
            // Encontra um elemento à esquerda que é >= pivô
            while (this.lista[i].compareTo(pivo) < 0) {
                i++;
            }

            // Encontra um elemento à direita que é <= pivô
            while (this.lista[j].compareTo(pivo) > 0) {
                j--;
            }

            // 3. Se os ponteiros não se cruzaram, troca os elementos
            if (i <= j) {
                temp = this.lista[i];
                this.lista[i] = this.lista[j];
                this.lista[j] = temp;
                i++;
                j--;
            }
        } while (i <= j);

        // 4. Chama a si mesmo recursivamente para as duas sub-listas
        if (esq < j) {
            ordena(esq, j);
        }
        if (i < dir) {
            ordena(i, dir);
        }
    }

    /**
     * Retorna o array interno (útil para operações diretas).
     */
    public Item[] getLista() {
        return this.lista;
    }

}

