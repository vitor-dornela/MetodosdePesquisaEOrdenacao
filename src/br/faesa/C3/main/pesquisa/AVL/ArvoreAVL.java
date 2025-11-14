package br.faesa.C3.main.pesquisa.AVL;

public class ArvoreAVL {
    private NoAVL raiz;
    private boolean balancear;
    private int quant;

    public ArvoreAVL() {
        this.raiz = null;
        this.quant = 0;
        this.balancear = false;
    }

    // a) Método pesquisar

    /**
     * Pesquisa publicamente por um número na árvore.
     *
     * @param num O número (chave) a ser pesquisado.
     * @return O nó (NoAVL) que contém o número, ou null se não for encontrado.
     */
    public NoAVL pesquisar(int num) {
        // Inicia a busca recursiva a partir da raiz
        return pesquisarRecursivo(num, this.raiz);
    }

    /**
     * Método auxiliar privado que implementa a lógica da busca recursiva.
     */
    private NoAVL pesquisarRecursivo(int num, NoAVL noAtual) {
        // Caso base 1: A árvore está vazia ou o nó não foi encontrado (chegou a um nulo)
        if (noAtual == null) {
            return null;
        }

        // Caso base 2: O número foi encontrado no nó atual
        if (num == noAtual.getNum()) {
            return noAtual;
        }

        // Passo recursivo: Decide para qual lado da árvore continuar a busca
        if (num < noAtual.getNum()) {
            // Se o número for menor, busca na subárvore esquerda
            return pesquisarRecursivo(num, noAtual.getEsq());
        } else {
            // Se o número for maior, busca na subárvore direita
            return pesquisarRecursivo(num, noAtual.getDir());
        }
    }

    // b) Método inserir
    public void inserir(Integer num) {
        this.raiz = this.inserir(num, this.raiz);
    }

    private NoAVL inserir(Integer num, NoAVL no) {
        if (no == null) {
            NoAVL novo = new NoAVL(num);
            this.balancear = true;
            return novo;
        } else {
            if (num < no.getNum()) {
                // Insere à esquerda e verifica se precisa balancear à direita
                no.setEsq(this.inserir(num, no.getEsq()));
                no = this.balancearDir(no);
                return no;
            } else {
                // Insere à direita e verifica se precisa balancear à esquerda
                no.setDir(this.inserir(num, no.getDir()));
                no = this.balancearEsq(no);
                return no;
            }
        }
    }

    // c) Método para verificar se é necessário o balanceamento para direita do nó
    private NoAVL balancearDir(NoAVL no) {
        if (this.balancear) {
            switch (no.getFatorBalanceamento()) {
                case 1:
                    no.setFatorBalanceamento(0);
                    this.balancear = false;
                    break;
                case 0:
                    no.setFatorBalanceamento(-1);
                    break;
                case -1:
                    no = this.rotaçãoDireita(no);
            }
        }
        return no;
    }

    // d) Método para verificar se é necessário o balanceamento para esquerda do nó
    private NoAVL balancearEsq(NoAVL no) {
        if (this.balancear) {
            switch (no.getFatorBalanceamento()) {
                case -1:
                    no.setFatorBalanceamento(0);
                    this.balancear = false;
                    break;
                case 0:
                    no.setFatorBalanceamento(1);
                    break;
                case 1:
                    no = this.rotaçãoEsquerda(no);
            }
        }
        return no;
    }

    // e) Método para realizar ROTAÇÃO à DIREITA
    // (RD) e ROTAÇÃO DUPLA à DIREITA (RDD)
    private NoAVL rotaçãoDireita(NoAVL no) {
        NoAVL temp1, temp2;
        temp1 = no.getEsq();

        if (temp1.getFatorBalanceamento() == -1) { // Rotação Simples
            no.setEsq(temp1.getDir());
            temp1.setDir(no);
            no.setFatorBalanceamento((byte) 0);
            no = temp1;
        } else { // Rotação Dupla
            temp2 = temp1.getDir();
            temp1.setDir(temp2.getEsq());
            temp2.setEsq(temp1);
            no.setEsq(temp2.getDir());
            temp2.setDir(no);

            // Recalcula o FB do nó à direita na nova árvore
            if (temp2.getFatorBalanceamento() == -1)
                no.setFatorBalanceamento((byte) 1);
            else
                no.setFatorBalanceamento((byte) 0);

            // Recalcula o FB do nó à esquerda na nova árvore
            if (temp2.getFatorBalanceamento() == 1)
                temp1.setFatorBalanceamento((byte) -1);
            else
                temp1.setFatorBalanceamento((byte) 0);

            no = temp2;
        }

        no.setFatorBalanceamento((byte) 0);
        this.balancear = false;
        return no;
    }

    // f) Método para realizar ROTAÇÃO à
    // ESQUERDA (RE) e ROTAÇÃO DUPLA à
    // ESQUERDA (RDE)
    private NoAVL rotaçãoEsquerda(NoAVL no) {
        NoAVL temp1, temp2;
        temp1 = no.getDir();

        if (temp1.getFatorBalanceamento() == 1) { // Rotação Simples
            no.setDir(temp1.getEsq());
            temp1.setEsq(no);
            no.setFatorBalanceamento((byte) 0);
            no = temp1;
        } else { // Rotação Dupla
            temp2 = temp1.getEsq();
            temp1.setEsq(temp2.getDir());
            temp2.setDir(temp1);
            no.setDir(temp2.getEsq());
            temp2.setEsq(no);

            if (temp2.getFatorBalanceamento() == 1)
                no.setFatorBalanceamento((byte) -1);
            else
                no.setFatorBalanceamento((byte) 0);

            if (temp2.getFatorBalanceamento() == -1)
                temp1.setFatorBalanceamento((byte) 1);
            else
                temp1.setFatorBalanceamento((byte) 0);

            no = temp2;
        }

        no.setFatorBalanceamento((byte) 0);
        this.balancear = false;
        return no;
    }
}