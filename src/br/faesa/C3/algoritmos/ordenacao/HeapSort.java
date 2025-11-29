package br.faesa.C3.algoritmos.ordenacao;

import br.faesa.C3.entidades.Reserva;
import br.faesa.C3.entidades.Ordenavel;

public class HeapSort {
    
    /**
     * Método genérico para ordenar qualquer lista que implemente Ordenavel.
     */
    public static void sort(Ordenavel lista) {
        if (lista != null && !lista.eVazia()) {
            lista.heapsort();
        }
    }

    /**
     * Ordena um array de Item usando HeapSort.
     */
    public static void sort(Reserva[] array, int size) {
        int dir = size - 1, esq = (dir - 1) / 2;
        Reserva temp;

        while (esq >= 0) {
            refazheap(array, esq, size - 1);
            esq--;
        }

        while (dir > 0) {
            temp = array[0];
            array[0] = array[dir];
            array[dir] = temp;
            dir--;
            refazheap(array, 0, dir);
        }
    }

    private static void refazheap(Reserva[] array, int esq, int dir) {
        int i = esq, maiorFilho = 2 * i + 1;
        Reserva raiz = array[i];
        boolean heap = false;

        while ((maiorFilho <= dir) && (!heap)) {
            if (maiorFilho < dir)
                if (array[maiorFilho].compareTo(array[maiorFilho + 1]) < 0)
                    maiorFilho++;
            if (raiz.compareTo(array[maiorFilho]) < 0) {
                array[i] = array[maiorFilho];
                i = maiorFilho;
                maiorFilho = 2 * i + 1;
            } else
                heap = true;
        }
        array[i] = raiz;
    }

    /**
     * Ordena um array de Integer usando HeapSort.
     */
    public static void sort(Integer[] array, int size) {
        int dir = size - 1, esq = (dir - 1) / 2, temp;

        while (esq >= 0) {
            refazheap(array, esq, size - 1);
            esq--;
        }

        while (dir > 0) {
            temp = array[0];
            array[0] = array[dir];
            array[dir] = temp;
            dir--;
            refazheap(array, 0, dir);
        }
    }

    private static void refazheap(Integer[] array, int esq, int dir) {
        int i = esq, maiorFilho = 2 * i + 1;
        int raiz = array[i];
        boolean heap = false;

        while ((maiorFilho <= dir) && (!heap)) {
            if (maiorFilho < dir)
                if (array[maiorFilho] < array[maiorFilho + 1])
                    maiorFilho++;
            if (raiz < array[maiorFilho]) {
                array[i] = array[maiorFilho];
                i = maiorFilho;
                maiorFilho = 2 * i + 1;
            } else
                heap = true;
        }
        array[i] = raiz;
    }
}
