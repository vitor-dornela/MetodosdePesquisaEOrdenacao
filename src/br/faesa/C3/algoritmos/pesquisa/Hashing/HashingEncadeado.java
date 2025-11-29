package br.faesa.C3.algoritmos.pesquisa.Hashing;

import br.faesa.C3.entidades.Reserva;
import br.faesa.C3.entidades.LCItem;

/**
 * Implementação de Hashing Encadeado (com lista encadeada para colisões).
 * Usa função hash baseada no nome da reserva para distribuição.
 */
public class HashingEncadeado {
    private NoHash[] tabela;
    private int tamanho;
    private int numElementos;

    /**
     * Construtor que define o tamanho da tabela hash.
     * Recomenda-se usar um número primo para melhor distribuição.
     */
    public HashingEncadeado(int tamanho) {
        this.tamanho = tamanho;
        this.tabela = new NoHash[tamanho];
        this.numElementos = 0;
    }

    /**
     * Função hash para chave alfanumérica.
     * Transforma a chave String em valor numérico somando os valores dos caracteres.
     * Retorna o índice na tabela usando módulo do tamanho (M).
     */
    public int hashing(String chave) {
        char carac;
        int i, soma = 0;
        for (i = 0; i < chave.length(); i++) {
            carac = chave.charAt(i);
            soma += Character.getNumericValue(carac);
        }
        return soma % tamanho;
    }

    /**
     * Insere um item na tabela hash.
     * Se o nome já existe, adiciona à lista de reservas existente.
     * Caso contrário, cria um novo nó no início da lista encadeada.
     */
    public void inserir(Reserva item) {
        if (item == null || estaVazia(item.getNome())) {
            return;
        }

        int indice = hashing(item.getNome());
        NoHash atual = tabela[indice];

        // Procura se o nome já existe na lista encadeada
        while (atual != null) {
            if (atual.getNome().equalsIgnoreCase(item.getNome())) {
                // Nome já existe, adiciona à lista de reservas
                atual.getReservas().insereFinal(item);
                return;
            }
            atual = atual.getProx();
        }

        // Nome não existe, cria novo nó no início da lista
        NoHash novoNo = new NoHash(item);
        novoNo.setProx(tabela[indice]);
        tabela[indice] = novoNo;
        numElementos++;
    }

    /**
     * Pesquisa todos os itens com o nome especificado.
     * Retorna LCItem com todas as reservas encontradas (pode haver múltiplas).
     */
    public LCItem pesquisar(String nome) {
        if (estaVazia(nome)) {
            return new LCItem();
        }

        int indice = hashing(nome);
        NoHash atual = tabela[indice];

        // Percorre a lista encadeada naquela posição
        while (atual != null) {
            if (atual.getNome().equalsIgnoreCase(nome)) {
                // Retorna a lista de reservas do nó encontrado
                return atual.getReservas();
            }
            atual = atual.getProx();
        }

        // Nome não encontrado
        return new LCItem();
    }

    /**
     * Carrega todos os itens de uma LCItem para a tabela hash.
     */
    public void carregarDeLCItem(LCItem lista) {
        if (lista == null) {
            return;
        }

        for (int i = 0; i < lista.getQuant(); i++) {
            inserir(lista.getItem(i));
        }
    }

    // Getters para estatísticas
    public int getTamanho() {
        return tamanho;
    }

    public int getNumElementos() {
        return numElementos;
    }

    /**
     * Verifica se a tabela está vazia.
     */
    public boolean estaVazia() {
        return numElementos == 0;
    }

    /**
     * Verifica se uma string está vazia (nula ou sem caracteres).
     */
    private boolean estaVazia(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * Calcula o fator de carga (load factor) da tabela.
     */
    public double getFatorCarga() {
        return (double) numElementos / tamanho;
    }

    /**
     * Calcula estatísticas sobre colisões na tabela.
     */
    public void imprimirEstatisticas() {
        int posicoesVazias = 0;
        int maxColisoes = 0;
        int totalColisoes = 0;

        for (int i = 0; i < tamanho; i++) {
            if (tabela[i] == null) {
                posicoesVazias++;
            } else {
                int tamLista = 0;
                NoHash atual = tabela[i];
                while (atual != null) {
                    tamLista++;
                    atual = atual.getProx();
                }
                if (tamLista > 1) {
                    totalColisoes += (tamLista - 1);
                }
                maxColisoes = Math.max(maxColisoes, tamLista);
            }
        }

        System.out.println("=== Estatísticas do Hashing ===");
        System.out.println("Tamanho da tabela: " + tamanho);
        System.out.println("Elementos inseridos: " + numElementos);
        System.out.printf("Fator de carga: %.2f\n", getFatorCarga());
        System.out.println("Posições vazias: " + posicoesVazias + " (" + 
            (posicoesVazias * 100 / tamanho) + "%)");
        System.out.println("Máximo em uma lista: " + maxColisoes);
        System.out.println("Total de colisões: " + totalColisoes);
    }

    // ========== Métodos estáticos para cálculo de tamanho primo ==========

    /**
     * Calcula o próximo número primo maior ou igual ao valor fornecido.
     * Usado para definir tamanho ótimo da tabela hash.
     * 
     * @param n Número de elementos esperados
     * @return Número primo próximo a n * 1.1 (fator de carga ~0.9)
     */
    public static int calcularTamanhoPrimo(int n) {
        // Ajusta para um valor próximo (fator 1.1 para lista encadeada)
        int candidato = (int) (n * 1.1);
        
        // Garante que seja ímpar
        if (candidato % 2 == 0) {
            candidato++;
        }
        
        // Busca o próximo primo
        while (!ehPrimo(candidato)) {
            candidato += 2;
        }
        
        return candidato;
    }

    /**
     * Verifica se um número é primo.
     */
    public static boolean ehPrimo(int n) {
        if (n < 2) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }
        
        int raiz = (int) Math.sqrt(n);
        for (int i = 3; i <= raiz; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Pesquisa todos os nomes de um LCItem e retorna os resultados.
     * @param nomes LCItem contendo os nomes a pesquisar
     * @return Array de LCItem com os resultados de cada pesquisa
     */
    public LCItem[] pesquisarTodos(LCItem nomes) {
        LCItem[] resultados = new LCItem[nomes.getQuant()];
        for (int i = 0; i < nomes.getQuant(); i++) {
            resultados[i] = pesquisar(nomes.getItem(i).getNome());
        }
        return resultados;
    }
}
