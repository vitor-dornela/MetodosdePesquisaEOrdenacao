package br.faesa.C3.algoritmos.pesquisa.ABB;

import br.faesa.C3.entidades.Reserva;
import br.faesa.C3.entidades.LCItem;

public class ArvoreABBItem {

    // Atributos
    private NoABBItem raiz;
    private int quant;

    public ArvoreABBItem() {
        this.raiz = null;
        this.quant = 0;
    }

    public LCItem pesquisa(String nome) {
        NoABBItem no = pesquisa(nome, raiz);
        return (no != null) ? no.getReservas() : null;
    }

    private NoABBItem pesquisa(String nome, NoABBItem no) {
        if (no == null) {
            return null;
        }
        int comparacao = nome.compareToIgnoreCase(no.getNome());
        if (comparacao == 0) {
            return no;
        }
        if (comparacao > 0) {
            return pesquisa(nome, no.getDir());
        }
        return pesquisa(nome, no.getEsq());
    }

    public boolean insere(Reserva item) {
        NoABBItem noExistente = pesquisa(item.getNome(), this.raiz);
        if (noExistente == null) {
            this.raiz = insere(item, this.raiz);
            return true;
        } else {
            // Nome já existe, adiciona à lista de reservas
            noExistente.getReservas().insereFinal(item);
            return false;
        }
    }

    private NoABBItem insere(Reserva item, NoABBItem no) {
        if (no == null) {
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
        // Se comparacao == 0, já existe - apenas retorna o nó sem modificar
        return no;
    }

    public NoABBItem remove(String nome) {
        NoABBItem[] removido = new NoABBItem[1];
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
            removido[0] = no;
            this.quant--;
            if (no.getDir() == null) {
                return no.getEsq();
            } else if (no.getEsq() == null) {
                return no.getDir();
            } else {
                no.setEsq(maiorEsq(no, no.getEsq()));
            }
        }

        return no;
    }

    private NoABBItem maiorEsq(NoABBItem noRemovido, NoABBItem maior) {
        /*
         * andar para a direita o maximo possivel, procurando o maior
         * da esquerda.
         */
        if (maior.getDir() != null) {
            maior.setDir(maiorEsq(noRemovido, maior.getDir()));
            return maior;
        }
        noRemovido.setNome(maior.getNome());
        return maior.getEsq();
    }

    public LCItem CamInOrdem() {
        LCItem vetor = new LCItem(this.quant * 5);
        fazCamInOrdem(vetor, this.raiz);
        return (vetor);
    }

    private void fazCamInOrdem(LCItem vetor, NoABBItem no) {
        if (no != null) {
            fazCamInOrdem(vetor, no.getEsq());
            // Adiciona todas as reservas deste nó
            LCItem reservas = no.getReservas();
            for (int i = 0; i < reservas.getQuant(); i++) {
                vetor.insereFinal(reservas.getItem(i));
            }
            fazCamInOrdem(vetor, no.getDir());
        }
    }

    public LCItem CamPreOrdem() {
        LCItem vetor = new LCItem(this.quant * 5);
        fazCamPreOrdem(vetor, this.raiz);
        return (vetor);
    }

    private void fazCamPreOrdem(LCItem vetor, NoABBItem no) {
        if (no != null) {
            // Adiciona todas as reservas deste nó
            LCItem reservas = no.getReservas();
            for (int i = 0; i < reservas.getQuant(); i++) {
                vetor.insereFinal(reservas.getItem(i));
            }
            fazCamPreOrdem(vetor, no.getEsq());
            fazCamPreOrdem(vetor, no.getDir());
        }
    }

    public LCItem CamPosOrdem() {
        LCItem vetor = new LCItem(this.quant * 5);
        fazCamPosOrdem(vetor, this.raiz);
        return (vetor);
    }

    private void fazCamPosOrdem(LCItem vetor, NoABBItem no) {
        if (no != null) {
            fazCamPosOrdem(vetor, no.getEsq());
            fazCamPosOrdem(vetor, no.getDir());
            // Adiciona todas as reservas deste nó
            LCItem reservas = no.getReservas();
            for (int i = 0; i < reservas.getQuant(); i++) {
                vetor.insereFinal(reservas.getItem(i));
            }
        }
    }

    /**
     * Constrói uma árvore balanceada diretamente a partir de uma lista de itens
     * Evita StackOverflowError que ocorreria ao inserir dados ordenados um por um
     */
    public void construirBalanceada(LCItem itens) {
        this.raiz = null;
        this.quant = 0;
        balancear(itens, this, 0, itens.getQuant() - 1);
    }

    public ArvoreABBItem balancear() {
        LCItem vetor = CamInOrdem();
        ArvoreABBItem arvAux = new ArvoreABBItem();
        balancear(vetor, arvAux, 0, vetor.getQuant() - 1);
        return arvAux;
    }

    private void balancear(LCItem vetor, ArvoreABBItem arv, int esq, int dir) {
        int meio;

        if (esq <= dir) {
            meio = (esq + dir) / 2;
            arv.insere(vetor.getItem(meio));
            balancear(vetor, arv, esq, meio - 1);
            balancear(vetor, arv, meio + 1, dir);
        }
    }

    public int getQuant() {
        return quant;
    }

    /**
     * Pesquisa todos os nomes de um LCItem e retorna os resultados.
     * @param nomes LCItem contendo os nomes a pesquisar
     * @return Array de LCItem com os resultados de cada pesquisa
     */
    public LCItem[] pesquisarTodos(LCItem nomes) {
        LCItem[] resultados = new LCItem[nomes.getQuant()];
        for (int i = 0; i < nomes.getQuant(); i++) {
            resultados[i] = pesquisa(nomes.getItem(i).getNome());
        }
        return resultados;
    }
}
