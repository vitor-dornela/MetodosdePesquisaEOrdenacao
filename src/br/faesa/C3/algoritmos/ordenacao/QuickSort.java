package br.faesa.C3.algoritmos.ordenacao;

import br.faesa.C3.algoritmos.entidades.Ordenavel;

public class QuickSort {
    
    /**
     * Método genérico para ordenar qualquer lista que implemente Ordenavel.
     */
    public static void sort(Ordenavel lista) {
        if (lista != null && !lista.eVazia()) {
            lista.quicksort();
        }
    }
}
