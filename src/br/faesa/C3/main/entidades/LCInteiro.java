package br.faesa.C3.main.entidades;

public class LCInteiro {
    private Integer[] lista;
    private int quant;

    public LCInteiro() {
        this.lista = new Integer[10];
        this.quant = 0;
    }

    public LCInteiro(int tamanho) {
        this.lista = new Integer[tamanho];
        this.quant = 0;
    }

    public int tamanho () {
        return this.lista.length;
    }

    public int getQuant() {
        return this.quant;
    }
    /*
     * eVazia() => retorna verdadeiro se a lista estiver vazia e
     *             falso caso contrário.
     */
    public boolean eVazia() {
        if (this.quant==0) {
            return true;
        }
        return false;
    }

    public boolean eCheia() {
        if (this.quant==this.lista.length) {
            return true;
        }
        return false;
    }

    public Integer get (int pos) {
        if (pos>=0 && pos<this.quant) {
            return this.lista[pos];
        }
        return null;
    }

    public int pesquisa (int cod) {
        for (int i = 0; i<quant; i++) {
            if (this.lista[i] == cod) {
                return i;
            }
        }
        return -1;
    }

    public boolean insere (Integer item, int pos) {
        if (pos<0 || pos>quant) {// posicao invalida
            return false;
        }
        if (quant==this.lista.length) {
            aumenta();
        }
        for (int i = quant; i> pos; i--) {
            this.lista[i] = this.lista[i-1];
        }
        this.lista[pos] = item;
        this.quant++;
        return true;
    }

    public void insereInicio (Integer item) {
        insere (item, 0);
    }

    public void insereFinal (Integer item) {
        if (quant==this.lista.length) {
            aumenta();
        }
        this.lista[quant] = item;
        this.quant++;
    }

    private void aumenta() {
        Integer[] novo;
        novo = new Integer[lista.length+lista.length/2];
        for (int i=0; i<this.quant; i++) {
            novo[i] = this.lista[i];
        }
        this.lista = novo;
    }

    public Integer remove (int cod) {
        Integer aux;
        int pos = this.pesquisa(cod);
        if (pos==-1) {
            return null;
        }
        aux = this.lista[pos];
        for (int i = pos; i<quant-1; i++) {
            this.lista[i] = this.lista[i+1];
        }
        this.lista[quant-1] = null;
        this.quant--;
        return aux;
    }

    public Integer removePos (int pos) {
        Integer aux;
        if (pos<0 || pos>=quant) {
            return null;
        }
        aux = this.lista[pos];
        for (int i = pos; i<quant-1; i++) {
            this.lista[i] = this.lista[i+1];
        }
        this.lista[quant-1] = null;
        this.quant--;
        return aux;
    }

    public String toString () {
        String aux = "";
        for (int i=0; i<this.quant; i++) {
            aux += this.lista[i]+" | ";
        }
        return aux;
    }


    public LCInteiro concatena(LCInteiro outra) {
        // Cria uma nova lista com capacidade para todos os elementos
        LCInteiro nova = new LCInteiro(this.getQuant() + outra.getQuant());

        // Copia os elementos da lista objeto
        for (int i = 0; i < this.getQuant(); i++) {
            nova.insereFinal(this.get(i));
        }

        // Copia os elementos da lista passada como parâmetro
        for (int i = 0; i < outra.getQuant(); i++) {
            nova.insereFinal(outra.get(i));
        }

        return nova;
    }


    public boolean igual(LCInteiro outra) {
        if (outra == null) {
            return false;
        }
        // Se a quantidade de elementos for diferente, as listas não são iguais.
        if (this.getQuant() != outra.getQuant()) {
            return false;
        }
        // Compara cada elemento na mesma posição.
        for (int i = 0; i < this.getQuant(); i++) {
            Integer elementoThis = this.get(i);
            Integer elementoOutra = outra.get(i);
            // Verifica se os elementos são ambos nulos ou se são iguais.
            if (elementoThis == null) {
                if (elementoOutra != null) {
                    return false;
                }
            } else if (!elementoThis.equals(elementoOutra)) {
                return false;
            }
        }
        return true;
    }


    public boolean estaContida(LCInteiro outra) {
        // Para cada elemento da lista objeto
        for (int i = 0; i < this.getQuant(); i++) {
            // Obtém o elemento da posição i
            int valor = this.get(i);
            // Se o elemento não for encontrado na lista parâmetro, retorna false
            if (outra.pesquisa(valor) == -1) {
                return false;
            }
        }
        // Se todos os elementos foram encontrados, retorna true
        return true;
    }

    public void quicksort () {
        // Chama o método recursivo 'ordena' para ordenar
        // o array inteiro (da posição 0 até quant-1)
        ordena (0, this.quant-1);
    }

    /**
     * Método privado e recursivo que implementa a lógica
     * "dividir para conquistar" do Quick Sort.
     */
    private void ordena (int esq, int dir) {
        int pivo, i = esq, j = dir, temp;

        // 1. Escolhe o pivô (elemento do meio)
        pivo = this.lista[(i + j) / 2];

        // 2. Particiona o array
        do {
            // Encontra um elemento à esquerda que é >= pivô
            while (this.lista[i] < pivo) {
                i++;
            }

            // Encontra um elemento à direita que é <= pivô
            while (this.lista[j] > pivo) {
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
            ordena (esq, j);
        }
        if (i < dir) {
            ordena (i, dir);
        }
    }









}
