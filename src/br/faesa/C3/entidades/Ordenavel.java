package br.faesa.C3.entidades;

/**
 * Interface para estruturas de dados que podem ser ordenadas.
 */
public interface Ordenavel {
    void heapsort();
    void quicksort();
    void quicksortComInsercao();
    boolean eVazia();
}
