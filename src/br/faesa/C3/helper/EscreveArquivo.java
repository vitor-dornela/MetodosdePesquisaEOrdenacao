package br.faesa.C3.helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import br.faesa.C3.entidades.Item;
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
}
