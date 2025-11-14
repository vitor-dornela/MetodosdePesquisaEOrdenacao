package br.faesa.C3.algoritmos.ordenacao;
import br.faesa.C3.algoritmos.entidades.LCInteiro;


public class QuickSort {
    public static void sortList(LCInteiro lista) {
        if (lista != null && !lista.eVazia()) {
            lista.quicksort();
        }
    }
}
