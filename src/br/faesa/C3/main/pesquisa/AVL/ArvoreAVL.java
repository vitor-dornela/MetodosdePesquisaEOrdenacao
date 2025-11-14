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

    // a) função pesquisar
    public NoAVL pesquisar(int num) {
        // Inicia a busca recursiva a partir da raiz
        return pesquisarRecursivo(num, this.raiz);
    }

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

    // b) função inserir
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

    // c) função para verificar se é necessário o balanceamento para direita do nó
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

    // d) função para verificar se é necessário o balanceamento para esquerda do nó
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

    // e) função para realizar ROTAÇÃO à DIREITA
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

    // f) função para realizar ROTAÇÃO à
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

    // Métodos para visualização da árvore

    // Traversal em ordem (esquerda, raiz, direita)
    public void emOrdem() {
        System.out.print("Em ordem: ");
        emOrdemRecursivo(this.raiz);
        System.out.println();
    }

    private void emOrdemRecursivo(NoAVL no) {
        if (no != null) {
            emOrdemRecursivo(no.getEsq());
            System.out.print(no.getNum() + " ");
            emOrdemRecursivo(no.getDir());
        }
    }

    // Traversal pré-ordem (raiz, esquerda, direita)
    public void preOrdem() {
        System.out.print("Pré-ordem: ");
        preOrdemRecursivo(this.raiz);
        System.out.println();
    }

    private void preOrdemRecursivo(NoAVL no) {
        if (no != null) {
            System.out.print(no.getNum() + " ");
            preOrdemRecursivo(no.getEsq());
            preOrdemRecursivo(no.getDir());
        }
    }

    // Traversal pós-ordem (esquerda, direita, raiz)
    public void posOrdem() {
        System.out.print("Pós-ordem: ");
        posOrdemRecursivo(this.raiz);
        System.out.println();
    }

    private void posOrdemRecursivo(NoAVL no) {
        if (no != null) {
            posOrdemRecursivo(no.getEsq());
            posOrdemRecursivo(no.getDir());
            System.out.print(no.getNum() + " ");
        }
    }

    // Exibe a estrutura da árvore de forma visual
    public void mostrarEstrutura() {
        System.out.println("Estrutura da Árvore AVL:");
        if (this.raiz == null) {
            System.out.println("Árvore vazia");
        } else {
            mostrarEstruturaRecursivo(this.raiz, "", true);
        }
    }

    private void mostrarEstruturaRecursivo(NoAVL no, String prefixo, boolean isUltimo) {
        if (no != null) {
            System.out.println(prefixo + (isUltimo ? "└── " : "├── ") + 
                             no.getNum() + " (FB: " + no.getFatorBalanceamento() + ")");

            String novoPrefixo = prefixo + (isUltimo ? "    " : "│   ");

            // Primeiro mostra o filho direito (aparece em cima)
            if (no.getDir() != null || no.getEsq() != null) {
                mostrarEstruturaRecursivo(no.getDir(), novoPrefixo, no.getEsq() == null);
                mostrarEstruturaRecursivo(no.getEsq(), novoPrefixo, true);
            }
        }
    }

    // função para verificar se a árvore está vazia
    public boolean estaVazia() {
        return this.raiz == null;
    }

    // função para obter a altura da árvore
    public int getAltura() {
        if (this.raiz == null) {
            return 0; // Árvore vazia tem altura 0
        }
        return getAlturaRecursivo(this.raiz);
    }

    private int getAlturaRecursivo(NoAVL no) {
        if (no == null) {
            return 0;
        }
        int alturaEsq = getAlturaRecursivo(no.getEsq());
        int alturaDir = getAlturaRecursivo(no.getDir());

        // Se é uma folha (sem filhos), altura = 0
        if (no.getEsq() == null && no.getDir() == null) {
            return 0;
        }

        return Math.max(alturaEsq, alturaDir) + 1;
    }
}