package br.faesa.C3.algoritmos.pesquisa.AVL;

import br.faesa.C3.entidades.Item;
import br.faesa.C3.entidades.LCItem;

public class NoAVLItem {
    private String nome;
    private LCItem reservas;
    private NoAVLItem esq, dir;
    private int fatorBalanceamento;

    public NoAVLItem(Item item) {
        this.nome = item.getNome();
        this.reservas = new LCItem(5);
        this.reservas.insereFinal(item);
        this.fatorBalanceamento = 0;
        this.esq = null;
        this.dir = null;
    }

    // --- Métodos Get/Set
    public String getNome() { 
        return nome; 
    }
    
    public void setNome(String nome) { 
        this.nome = nome; 
    }
    
    public LCItem getReservas() { 
        return reservas; 
    }
    
    public NoAVLItem getEsq() { 
        return esq; 
    }
    
    public void setEsq(NoAVLItem esq) { 
        this.esq = esq; 
    }
    
    public NoAVLItem getDir() { 
        return dir; 
    }
    
    public void setDir(NoAVLItem dir) { 
        this.dir = dir; 
    }
    
    public int getFatorBalanceamento() { 
        return fatorBalanceamento; 
    }
    
    public void setFatorBalanceamento(int fatorBalanceamento) {
        this.fatorBalanceamento = fatorBalanceamento;
    }

    public String toString() {
        return "" + this.nome;
    }
}
