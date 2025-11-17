package br.faesa.C3.algoritmos.ordenacao;

import br.faesa.C3.entidades.Item;

public class QuickSortComInsercaoExato {

    /**
     * Ordena um array de Item usando QuickSort com InsertionSort APENAS quando a partição
     * tem exatamente 20 elementos.
     */
    public static void sort(Item[] array, int size) {
        quicksortComInsercaoExato(array, 0, size - 1);
    }

    private static void quicksortComInsercaoExato(Item[] array, int esq, int dir) {
        // Se a partição tem EXATAMENTE 20 elementos, usa InsertionSort
        if (dir - esq == 20) {
            InsertionSort.sortRange(array, esq, dir);
            return;
        }

        // Se a partição tem menos de 2 elementos, já está ordenada
        if (esq >= dir) {
            return;
        }

        Item pivo, temp;
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
            quicksortComInsercaoExato(array, esq, j);
        }
        if (i < dir) {
            quicksortComInsercaoExato(array, i, dir);
        }
    }

    /**
     * Ordena um array de Integer usando QuickSort com InsertionSort APENAS quando a partição
     * tem exatamente 20 elementos.
     */
    public static void sort(Integer[] array, int size) {
        quicksortComInsercaoExato(array, 0, size - 1);
    }

    private static void quicksortComInsercaoExato(Integer[] array, int esq, int dir) {
        // Se a partição tem EXATAMENTE 20 elementos, usa InsertionSort
        if (dir - esq == 20) {
            InsertionSort.sortRange(array, esq, dir);
            return;
        }

        // Se a partição tem menos de 2 elementos, já está ordenada
        if (esq >= dir) {
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
            quicksortComInsercaoExato(array, esq, j);
        }
        if (i < dir) {
            quicksortComInsercaoExato(array, i, dir);
        }
    }
}
