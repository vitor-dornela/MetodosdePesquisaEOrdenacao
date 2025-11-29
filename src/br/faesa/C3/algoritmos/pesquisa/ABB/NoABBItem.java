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
