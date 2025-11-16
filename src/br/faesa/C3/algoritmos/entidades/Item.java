package br.faesa.C3.algoritmos.entidades;

public class Item implements Comparable<Item> {
    private String chave;      // Ex: R000001
    private String nome;       // Ex: RUI CUNHA CASTRO MARTINHO
    private String codigo_voo;    // Ex: V947
    private String data;       // Ex: 21/04/2024
    private String assento;    // Ex: 167C

    // Construtor completo para reservas
    public Item(String chave, String nome, String codigo_voo, String data, String assento) {
        this.chave = chave;
        this.nome = nome;
        this.codigo_voo = codigo_voo;
        this.data = data;
        this.assento = assento;
    }

    // Construtor simplificado (compatibilidade)
    public Item(int codigo, String nome) {
        this.chave = String.format("R%06d", codigo);
        this.nome = nome;
        this.codigo_voo = "";
        this.data = "";
        this.assento = "";
    }

    // Getters e Setters
    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoVoo() {
        return codigo_voo;
    }

    public void setCodigoVoo(String codigo_voo) {
        this.codigo_voo = codigo_voo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAssento() {
        return assento;
    }

    public void setAssento(String assento) {
        this.assento = assento;
    }

    // Compatibilidade com código antigo
    public int getCodigo() {
        if (chave != null && chave.startsWith("R")) {
            return Integer.parseInt(chave.substring(1));
        }
        return 0;
    }

    public void setCodigo(int codigo) {
        this.chave = String.format("R%06d", codigo);
    }

    /**
     * Compara itens por nome (ordem alfabética) e, em caso de empate, por chave.
     */
    @Override
    public int compareTo(Item outro) {
        if (outro == null) {
            return 1;
        }
        
        // Primeiro compara por nome
        int comparacaoNome = this.nome.compareToIgnoreCase(outro.nome);
        
        if (comparacaoNome != 0) {
            return comparacaoNome;
        }
        
        // Se os nomes são iguais, compara por chave
        return this.chave.compareTo(outro.chave);
    }

    @Override
    public String toString() {
        return chave + ";" + nome + ";" + codigo_voo + ";" + data + ";" + assento;
    }

    // Formato legível para debug
    public String toStringFormatado() {
        return String.format("%-10s %-40s %-6s %-12s %-6s", 
            chave, nome, codigo_voo, data, assento);
    }
}
