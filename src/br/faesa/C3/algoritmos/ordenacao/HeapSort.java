package br.faesa.C3.algoritmos.ordenacao;

import br.faesa.C3.algoritmos.entidades.Item;
import br.faesa.C3.algoritmos.entidades.LCInteiro;

public class HeapSort {
    
    // Método legado para LCInteiro
    public static void sortList(LCInteiro lista) {
        if (lista != null && !lista.eVazia()) {
            lista.heapsort();
        }
    }

    /**
     * HeapSort para array de Items.
     * Ordena por nome e, em caso de empate, por chave.
     */
    public static void sort(Item[] array, int tamanho) {
        if (array == null || tamanho <= 1) {
            return;
        }

        // Constrói o heap (reorganiza o array)
        for (int i = tamanho / 2 - 1; i >= 0; i--) {
            heapify(array, tamanho, i);
        }

        // Extrai elementos do heap um por um
        for (int i = tamanho - 1; i > 0; i--) {
            // Move a raiz atual para o final
            Item temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            // Chama heapify na heap reduzida
            heapify(array, i, 0);
        }
    }

    /**
     * Reorganiza uma subárvore com raiz no índice i.
     * @param array Array a ser heapificado
     * @param tamanho Tamanho do heap
     * @param i Índice da raiz da subárvore
     */
    private static void heapify(Item[] array, int tamanho, int i) {
        int maior = i;           // Inicializa maior como raiz
        int esquerda = 2 * i + 1;  // Filho esquerdo
        int direita = 2 * i + 2;   // Filho direito

        // Se o filho esquerdo é maior que a raiz
        if (esquerda < tamanho && array[esquerda].compareTo(array[maior]) > 0) {
            maior = esquerda;
        }

        // Se o filho direito é maior que o maior até agora
        if (direita < tamanho && array[direita].compareTo(array[maior]) > 0) {
            maior = direita;
        }

        // Se o maior não é a raiz
        if (maior != i) {
            Item swap = array[i];
            array[i] = array[maior];
            array[maior] = swap;

            // Recursivamente heapifica a subárvore afetada
            heapify(array, tamanho, maior);
        }
    }
}
