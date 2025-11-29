package br.faesa.C3.algoritmos.ordenacao;

import br.faesa.C3.entidades.Reserva;

public class InsertionSort {

    /**
     * InsertionSort para um array completo de Item.
     */
    public static void sort(Reserva[] array, int size) {
        sortRange(array, 0, size - 1);
    }

    /**
     * InsertionSort para um intervalo específico de um array de Item.
     */
    public static void sortRange(Reserva[] array, int inicio, int fim) {
        for (int i = inicio + 1; i <= fim; i++) {
            Reserva temp = array[i];
            int j = i - 1;

            // Desloca os elementos maiores para a direita
            while (j >= inicio && array[j].compareTo(temp) > 0) {
                array[j + 1] = array[j];
                j--;
            }

            // Insere o elemento na posição correta
            array[j + 1] = temp;
        }
    }

    /**
     * InsertionSort para um array completo de Integer.
     */
    public static void sort(Integer[] array, int size) {
        sortRange(array, 0, size - 1);
    }

    /**
     * InsertionSort para um intervalo específico de um array de Integer.
     */
    public static void sortRange(Integer[] array, int inicio, int fim) {
        for (int i = inicio + 1; i <= fim; i++) {
            int temp = array[i];
            int j = i - 1;

            // Desloca os elementos maiores para a direita
            while (j >= inicio && array[j] > temp) {
                array[j + 1] = array[j];
                j--;
            }

            // Insere o elemento na posição correta
            array[j + 1] = temp;
        }
    }
}
