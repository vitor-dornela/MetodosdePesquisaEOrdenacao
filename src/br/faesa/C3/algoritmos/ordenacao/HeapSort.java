package br.faesa.C3.algoritmos.ordenacao;

import br.faesa.C3.algoritmos.entidades.LCInteiro;

public class HeapSort {
    public static void sortList(LCInteiro lista) {
        if (lista != null && !lista.eVazia()) {
            lista.heapsort();
        }
    }

}
