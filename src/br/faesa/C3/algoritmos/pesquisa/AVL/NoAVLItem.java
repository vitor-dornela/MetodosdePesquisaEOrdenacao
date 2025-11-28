package br.faesa.C3.algoritmos.pesquisa.AVL;

import br.faesa.C3.entidades.Item;
import br.faesa.C3.entidades.NoArvoreBase;

/**
 * Nó para Árvore AVL.
 * Herda todos os atributos e métodos de NoArvoreBase.
 * Adiciona fatorBalanceamento para controle de balanceamento.
 */
public class NoAVLItem extends NoArvoreBase<NoAVLItem> {
    private int fatorBalanceamento;

    public NoAVLItem(Item item) {
        super(item);
        this.fatorBalanceamento = 0;
    }
    
    public int getFatorBalanceamento() { 
        return fatorBalanceamento; 
    }
    
    public void setFatorBalanceamento(int fatorBalanceamento) {
        this.fatorBalanceamento = fatorBalanceamento;
    }
}
