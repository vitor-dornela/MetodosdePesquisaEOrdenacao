package br.faesa.C3.entidades;

/**
 * Classe base genérica para nós de árvores de busca (ABB e AVL).
 * Usa Generics para permitir que esq/dir sejam do tipo correto em cada subclasse.
 * 
 * @param <T> O tipo do nó (própria classe que estende NoArvoreBase)
 */
public abstract class NoArvoreBase<T extends NoArvoreBase<T>> {
    protected String nome;
    protected LCItem reservas;
    protected T esq, dir;

    public NoArvoreBase(Reserva item) {
        this.nome = item.getNome();
        this.reservas = new LCItem(5);
        this.reservas.insereFinal(item);
        this.esq = null;
        this.dir = null;
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

    public T getEsq() {
        return esq;
    }

    public void setEsq(T esq) {
        this.esq = esq;
    }

    public T getDir() {
        return dir;
    }

    public void setDir(T dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "" + this.nome;
    }
}
