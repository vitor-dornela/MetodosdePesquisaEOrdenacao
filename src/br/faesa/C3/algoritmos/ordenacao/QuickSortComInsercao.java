package br.faesa.C3.algoritmos.ordenacao;

import br.faesa.C3.entidades.Reserva;

public class QuickSortComInsercao {

    /**
     * Ordena um array de Item usando QuickSort com InsertionSort para partições pequenas.
     * Quando a partição tem 20 ou menos elementos, usa InsertionSort.
     */
    public static void sort(Reserva[] array, int size) {
        quicksortComInsercao(array, 0, size - 1);
    }

    private static void quicksortComInsercao(Reserva[] array, int esq, int dir) {
        // Se a partição tem 20 ou menos elementos, usa InsertionSort
        if (dir - esq <= 20) {
            InsertionSort.sortRange(array, esq, dir);
            return;
        }

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
            quicksortComInsercao(array, esq, j);
        }
        if (i < dir) {
            quicksortComInsercao(array, i, dir);
        }
    }

    /**
     * Ordena um array de Integer usando QuickSort com InsertionSort para partições pequenas.
     * Quando a partição tem 20 ou menos elementos, usa InsertionSort.
     */
    public static void sort(Integer[] array, int size) {
        quicksortComInsercao(array, 0, size - 1);
    }

    private static void quicksortComInsercao(Integer[] array, int esq, int dir) {
        // Se a partição tem 20 ou menos elementos, usa InsertionSort
        if (dir - esq <= 20) {
            InsertionSort.sortRange(array, esq, dir);
            return;
        }

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
            quicksortComInsercao(array, esq, j);
        }
        if (i < dir) {
            quicksortComInsercao(array, i, dir);
        }
    }
}
