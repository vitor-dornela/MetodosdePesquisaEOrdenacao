package br.faesa.C3.algoritmos.entidades;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe utilitária para escrever dados ordenados em arquivo.
 */
public class ArquivoEscritor {

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
                Item item = lista.getItem(i);
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
                Item item = lista.getItem(i);
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
     * @param tempoMs Tempo de execução em milissegundos
     * @param numElementos Número de elementos ordenados
     * @throws IOException Se houver erro ao escrever
     */
    public static void salvarEstatisticas(String caminhoArquivo, String nomeDataset, 
            String algoritmo, long tempoMs, int numElementos) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            String linha = String.format("%s;%s;%d;%d ms", 
                nomeDataset, algoritmo, numElementos, tempoMs);
            writer.write(linha);
            writer.newLine();
        }
    }
}
