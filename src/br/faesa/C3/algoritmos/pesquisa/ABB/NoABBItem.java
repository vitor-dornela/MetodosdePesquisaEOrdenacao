package br.faesa.C3.algoritmos.pesquisa.ABB;

import br.faesa.C3.entidades.Item;
import br.faesa.C3.entidades.LCItem;

public class NoABBItem {
    private String nome;
    private LCItem reservas;
    private NoABBItem dir, esq;

    public NoABBItem(Item item) {
        this.nome = item.getNome();
        this.reservas = new LCItem(5);
        this.reservas.insereFinal(item);
        this.dir = null;
        this.esq = null;
    }

    public NoABBItem getDir() {
        return dir;
    }

    public void setDir(NoABBItem dir) {
        this.dir = dir;
    }

    public NoABBItem getEsq() {
        return esq;
    }

    public void setEsq(NoABBItem esq) {
        this.esq = esq;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LCItem getReservas() {
        return reservas;
    }

    public String toString() {
        return "" + this.nome;
    }
}
