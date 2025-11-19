package br.faesa.C3.algoritmos.pesquisa.ABB;


public class NoABB
{
    private Integer num;
    private NoABB dir, esq;

    public NoABB(Integer num) {
        this.num = num;
        this.dir = null;
        this.esq = null;
    }

    public NoABB getDir() {
        return dir;
    }

    public void setDir(NoABB dir) {
        this.dir = dir;
    }

    public NoABB getEsq() {
        return esq;
    }

    public void setEsq(NoABB esq) {
        this.esq = esq;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum (int num) {
        this.num = num;
    }

    public String toString () {
        return ""+this.num;
    }



}

