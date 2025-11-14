package br.faesa.C3.main.pesquisa.AVL;

public class NoAVL {
    private Integer num;
    private NoAVL esq, dir;
    private int fatorBalanceamento;

    public NoAVL (Integer num) {
        this.num = num;
        this.fatorBalanceamento = 0;
    }

    // --- Métodos Get/Set
    public Integer getNum() { return num; }
    public NoAVL getEsq() { return esq; }
    public void setEsq(NoAVL esq) { this.esq = esq; }
    public NoAVL getDir() { return dir; }
    public void setDir(NoAVL dir) { this.dir = dir; }
    public int getFatorBalanceamento() { return fatorBalanceamento; }
    public void setFatorBalanceamento(int fatorBalanceamento) {
        this.fatorBalanceamento = fatorBalanceamento;
    }
}