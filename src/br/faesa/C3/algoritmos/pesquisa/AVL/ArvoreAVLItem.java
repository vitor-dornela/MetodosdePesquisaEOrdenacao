package br.faesa.C3.algoritmos.pesquisa.AVL;

import br.faesa.C3.entidades.Item;
import br.faesa.C3.entidades.LCItem;

public class ArvoreAVLItem {
    private NoAVLItem raiz;
    private boolean balancear;
    private int quant;

    public ArvoreAVLItem() {
        this.raiz = null;
        this.quant = 0;
        this.balancear = false;
    }

    /**
     * Pesquisa um nome na árvore e retorna a lista de reservas associadas
     */
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
            return noAtual;
        } else if (comparacao < 0) {
            return pesquisar(nome, noAtual.getEsq());
        } else {
            return pesquisar(nome, noAtual.getDir());
        }
    }

    /**
     * Insere um item na árvore AVL
     * Se o nome já existe, adiciona à lista de reservas do nó
     */
    public void insere(Item item) {
        // Verifica se o nome já existe
        NoAVLItem noExistente = pesquisar(item.getNome(), this.raiz);
        if (noExistente != null) {
            // Nome já existe, adiciona à lista de reservas
            noExistente.getReservas().insereFinal(item);
        } else {
            // Nome não existe, insere novo nó
            this.raiz = inserir(item, this.raiz);
            this.quant++;
        }
    }

    private NoAVLItem inserir(Item item, NoAVLItem no) {
        if (no == null) {
            NoAVLItem novo = new NoAVLItem(item);
            this.balancear = true;
            return novo;
        } else {
            int comparacao = item.getNome().compareToIgnoreCase(no.getNome());
            
            if (comparacao < 0) {
                // Insere à esquerda e verifica se precisa balancear à direita
                no.setEsq(inserir(item, no.getEsq()));
                no = balancearDir(no);
                return no;
            } else if (comparacao > 0) {
                // Insere à direita e verifica se precisa balancear à esquerda
                no.setDir(inserir(item, no.getDir()));
                no = balancearEsq(no);
                return no;
            } else {
                // Nome igual não deveria chegar aqui (já tratado no método público)
                return no;
            }
        }
    }

    // Função para verificar se é necessário o balanceamento para direita do nó
    private NoAVLItem balancearDir(NoAVLItem no) {
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
                    no = rotaçãoDireita(no);
            }
        }
        return no;
    }

    // Função para verificar se é necessário o balanceamento para esquerda do nó
    private NoAVLItem balancearEsq(NoAVLItem no) {
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
                    no = rotaçãoEsquerda(no);
            }
        }
        return no;
    }

    // Função para realizar ROTAÇÃO à DIREITA (RD) e ROTAÇÃO DUPLA à DIREITA (RDD)
    private NoAVLItem rotaçãoDireita(NoAVLItem no) {
        NoAVLItem temp1, temp2;
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

    // Função para realizar ROTAÇÃO à ESQUERDA (RE) e ROTAÇÃO DUPLA à ESQUERDA (RDE)
    private NoAVLItem rotaçãoEsquerda(NoAVLItem no) {
        NoAVLItem temp1, temp2;
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

    /**
     * Coleta todos os itens da árvore em ordem
     */
    public LCItem CamInOrdem() {
        LCItem resultado = new LCItem();
        camInOrdemRecursivo(this.raiz, resultado);
        return resultado;
    }

    private void camInOrdemRecursivo(NoAVLItem no, LCItem resultado) {
        if (no != null) {
            camInOrdemRecursivo(no.getEsq(), resultado);
            // Adiciona todas as reservas deste nó
            LCItem reservas = no.getReservas();
            for (int i = 0; i < reservas.getQuant(); i++) {
                resultado.insereFinal(reservas.getItem(i));
            }
            camInOrdemRecursivo(no.getDir(), resultado);
        }
    }

    // Função para verificar se a árvore está vazia
    public boolean estaVazia() {
        return this.raiz == null;
    }

    // Retorna quantidade de nós únicos (não de itens totais)
    public int getQuant() {
        return quant;
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

    private void mostrarEstruturaRecursivo(NoAVLItem no, String prefixo, boolean isUltimo) {
        if (no != null) {
            System.out.println(prefixo + (isUltimo ? "└── " : "├── ") + 
                             no.getNome() + " (FB: " + no.getFatorBalanceamento() + 
                             ", Reservas: " + no.getReservas().getQuant() + ")");

            String novoPrefixo = prefixo + (isUltimo ? "    " : "│   ");

            // Primeiro mostra o filho direito (aparece em cima)
            if (no.getDir() != null || no.getEsq() != null) {
                mostrarEstruturaRecursivo(no.getDir(), novoPrefixo, no.getEsq() == null);
                mostrarEstruturaRecursivo(no.getEsq(), novoPrefixo, true);
            }
        }
    }
}
