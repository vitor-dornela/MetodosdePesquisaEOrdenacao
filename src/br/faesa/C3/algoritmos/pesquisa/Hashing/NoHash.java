package br.faesa.C3.algoritmos.pesquisa.Hashing;

import br.faesa.C3.entidades.Reserva;
import br.faesa.C3.entidades.LCItem;

/**
 * Nó para lista encadeada no Hashing.
 * Armazena o nome (chave) e uma LCItem com todas as reservas desse nome.
 */
public class NoHash {
    private String nome;
    private LCItem reservas;
    private NoHash prox;

    public NoHash(Reserva item) {
        this.nome = item.getNome();
        this.reservas = new LCItem(5);
        this.reservas.insereFinal(item);
        this.prox = null;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LCItem getReservas() {
        return reservas;
    }

    public NoHash getProx() {
        return prox;
    }

    public void setProx(NoHash prox) {
        this.prox = prox;
    }
}
