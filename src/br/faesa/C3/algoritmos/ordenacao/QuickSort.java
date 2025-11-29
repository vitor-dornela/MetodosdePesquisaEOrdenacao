package br.faesa.C3.algoritmos.ordenacao;

import br.faesa.C3.entidades.Reserva;
import br.faesa.C3.entidades.Ordenavel;

public class QuickSort {
    
    /**
     * Método genérico para ordenar qualquer lista que implemente Ordenavel.
     */
    public static void sort(Ordenavel lista) {
        if (lista != null && !lista.eVazia()) {
            lista.quicksort();
        }
    }

    /**
     * Ordena um array de Item usando QuickSort.
     */
    public static void sort(Reserva[] array, int size) {
        ordena(array, 0, size - 1);
    }

    private static void ordena(Reserva[] array, int esq, int dir) {
        Reserva pivo, temp;
        int i = esq, j = dir;

        // 1. Escolhe o pivô (elemento do meio)
        pivo = array[(i + j) / 2];

        // 2. Particiona o array
        do {
            // Encontra um elemento à esquerda que é >= pivô
            while (array[i].compareTo(pivo) < 0) {
                i++;
            }

            // Encontra um elemento à direita que é <= pivô
            while (array[j].compareTo(pivo) > 0) {
                j--;
            }

            // 3. Se os ponteiros não se cruzaram, troca os elementos
            if (i <= j) {
                temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        } while (i <= j);

        // 4. Chama a si mesmo recursivamente para as duas sub-listas
        if (esq < j) {
            ordena(array, esq, j);
        }
        if (i < dir) {
            ordena(array, i, dir);
        }
    }

    /**
     * Ordena um array de Integer usando QuickSort.
     */
    public static void sort(Integer[] array, int size) {
        ordena(array, 0, size - 1);
    }

    private static void ordena(Integer[] array, int esq, int dir) {
        int pivo, temp;
        int i = esq, j = dir;

        // 1. Escolhe o pivô (elemento do meio)
        pivo = array[(i + j) / 2];

        // 2. Particiona o array
        do {
            // Encontra um elemento à esquerda que é >= pivô
            while (array[i] < pivo) {
                i++;
            }

            // Encontra um elemento à direita que é <= pivô
            while (array[j] > pivo) {
                j--;
            }

            // 3. Se os ponteiros não se cruzaram, troca os elementos
            if (i <= j) {
                temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        } while (i <= j);

        // 4. Chama a si mesmo recursivamente para as duas sub-listas
        if (esq < j) {
            ordena(array, esq, j);
        }
        if (i < dir) {
            ordena(array, i, dir);
        }
    }
}
