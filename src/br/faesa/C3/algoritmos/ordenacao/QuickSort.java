package br.faesa.C3.algoritmos.ordenacao;

import br.faesa.C3.algoritmos.entidades.Item;
import br.faesa.C3.algoritmos.entidades.LCInteiro;

public class QuickSort {
    
    // Método legado para LCInteiro
    public static void sortList(LCInteiro lista) {
        if (lista != null && !lista.eVazia()) {
            lista.quicksort();
        }
    }

    /**
     * QuickSort para array de Items.
     * Ordena por nome e, em caso de empate, por chave.
     */
    public static void sort(Item[] array, int tamanho) {
        if (array == null || tamanho <= 1) {
            return;
        }
        quicksortRecursivo(array, 0, tamanho - 1);
    }

    /**
     * Método recursivo que implementa o QuickSort
     */
    private static void quicksortRecursivo(Item[] array, int esquerda, int direita) {
        if (esquerda < direita) {
            int indicePivo = particionar(array, esquerda, direita);
            
            // Ordena recursivamente os elementos antes e depois da partição
            quicksortRecursivo(array, esquerda, indicePivo - 1);
            quicksortRecursivo(array, indicePivo + 1, direita);
        }
    }

    /**
     * Particiona o array em torno do pivô
     */
    private static int particionar(Item[] array, int esquerda, int direita) {
        // Escolhe o elemento do meio como pivô
        Item pivo = array[(esquerda + direita) / 2];
        int i = esquerda;
        int j = direita;

        while (i <= j) {
            // Encontra elemento à esquerda que deveria estar à direita
            while (array[i].compareTo(pivo) < 0) {
                i++;
            }

            // Encontra elemento à direita que deveria estar à esquerda
            while (array[j].compareTo(pivo) > 0) {
                j--;
            }

            // Se os índices não se cruzaram, troca os elementos
            if (i <= j) {
                Item temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }

        return i;
    }
}
