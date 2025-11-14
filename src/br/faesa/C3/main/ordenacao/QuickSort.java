package br.faesa.C3.main.ordenacao;
import br.faesa.C3.main.entidades.LCInteiro;


public class QuickSort {
    public static void sortList(LCInteiro lista) {
        if (lista != null && !lista.eVazia()) {
            lista.quicksort();
        }
    }
}
