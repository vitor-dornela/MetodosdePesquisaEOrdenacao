package br.faesa.C3.helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import br.faesa.C3.entidades.Reserva;
import br.faesa.C3.entidades.LCItem;

/**
 * Classe utilitária para escrever dados ordenados em arquivo.
 */
public class EscreveArquivo {

    /**
     * Salva uma LCItem em arquivo no formato CSV (separado por ponto e vírgula).
     * 
     * @param lista Lista a ser salva
     * @param caminhoArquivo Caminho completo do arquivo (não classpath, mas path do sistema)
     * @throws IOException Se houver erro ao escrever o arquivo
     */
    public static void salvarReservas(LCItem lista, String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            for (int i = 0; i < lista.getQuant(); i++) {
                Reserva item = lista.getItem(i);
                writer.write(item.toString());
                writer.newLine();
            }
        }
        System.out.println("Arquivo salvo com sucesso: " + caminhoArquivo);
    }

    /**
     * Salva uma LCItem em arquivo com cabeçalho opcional.
     * 
     * @param lista Lista a ser salva
     * @param caminhoArquivo Caminho completo do arquivo
     * @param incluirCabecalho Se true, adiciona linha de cabeçalho
     * @throws IOException Se houver erro ao escrever o arquivo
     */
    public static void salvarReservas(LCItem lista, String caminhoArquivo, boolean incluirCabecalho) 
            throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            
            if (incluirCabecalho) {
                writer.write("CHAVE;NOME;CODIGO_VOO;DATA;ASSENTO");
                writer.newLine();
            }
            
            for (int i = 0; i < lista.getQuant(); i++) {
                Reserva item = lista.getItem(i);
                writer.write(item.toString());
                writer.newLine();
            }
        }
        System.out.println("Arquivo salvo com sucesso: " + caminhoArquivo);
    }

    /**
     * Salva estatísticas sobre a ordenação em arquivo.
     * 
     * @param caminhoArquivo Caminho do arquivo de estatísticas
     * @param nomeDataset Nome do dataset
     * @param algoritmo Nome do algoritmo usado
     * @param media Média dos tempos de execução
     * @param numElementos Número de elementos ordenados
     * @param primeiraLinha Se true, cria um novo arquivo; se false, adiciona ao arquivo existente
     * @throws IOException Se houver erro ao escrever
     */
    public static void salvarEstatisticas(String caminhoArquivo, String nomeDataset, 
            String algoritmo, double media, int numElementos, boolean primeiraLinha) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo, !primeiraLinha))) {
            if (primeiraLinha) {
                // Cabeçalho
                writer.write("Dataset;Algoritmo;Elementos;Media(ms)");
                writer.newLine();
            }
            
            String linha = String.format("%s;%s;%d;%.2f", 
                nomeDataset, algoritmo, numElementos, media);
            writer.write(linha);
            writer.newLine();
        }
    }

    /**
     * Formata o resultado de uma pesquisa para um único nome.
     * 
     * @param nome Nome pesquisado
     * @param reservas Lista de reservas encontradas (ou null se não encontrado)
     * @return String formatada com o resultado da pesquisa
     */
    public static String formatarResultadoPesquisa(String nome, LCItem reservas) {
        StringBuilder sb = new StringBuilder();
        sb.append("NOME ").append(nome).append(":\n");
        
        if (reservas == null || reservas.eVazia()) {
            sb.append("NÃO TEM RESERVA\n");
        } else {
            for (int i = 0; i < reservas.getQuant(); i++) {
                Reserva item = reservas.getItem(i);
                sb.append(String.format("Reserva: %s Voo: %s Data: %s Assento: %s\n",
                    item.getChave(),
                    item.getCodigoVoo(),
                    item.getData(),
                    item.getAssento()));
            }
            sb.append(String.format("TOTAL: %d reserva%s\n", 
                reservas.getQuant(), 
                reservas.getQuant() > 1 ? "s" : ""));
        }
        
        return sb.toString();
    }

    /**
     * Salva os resultados de múltiplas pesquisas em arquivo.
     * 
     * @param caminhoArquivo Caminho do arquivo de saída
     * @param nomesPesquisados LCItem com os nomes pesquisados
     * @param resultados Array com as listas de reservas encontradas (pode conter nulls)
     * @throws IOException Se houver erro ao escrever
     */
    public static void salvarResultadosPesquisa(String caminhoArquivo, LCItem nomesPesquisados, 
            LCItem[] resultados) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            for (int i = 0; i < nomesPesquisados.getQuant(); i++) {
                String nome = nomesPesquisados.getItem(i).getNome();
                String resultado = formatarResultadoPesquisa(nome, resultados[i]);
                writer.write(resultado);
                writer.newLine(); // Linha em branco entre pesquisas
            }
        }
        System.out.println("Resultados salvos em: " + caminhoArquivo);
    }
}
