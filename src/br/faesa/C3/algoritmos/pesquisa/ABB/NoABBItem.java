package br.faesa.C3.algoritmos.pesquisa.ABB;

import br.faesa.C3.entidades.Item;
import br.faesa.C3.entidades.NoArvoreBase;

/**
 * Nó para Árvore Binária de Busca (ABB).
 * Herda todos os atributos e métodos de NoArvoreBase.
 */
public class NoABBItem extends NoArvoreBase<NoABBItem> {

    public NoABBItem(Item item) {
        super(item);
    }
}
