package br.faesa.C3.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.faesa.C3.entidades.Reserva;
import br.faesa.C3.entidades.LCItem;

/**
 * Classe para leitura de arquivos do sistema de arquivos.
 */
public class LeArquivo {

    /**
     * Lê todas as linhas de um arquivo e retorna uma lista.
     * @param caminho Caminho do arquivo no sistema de arquivos
     */
    public static List<String> lerLinhas(String caminho) {
        List<String> linhas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo: " + caminho, e);
        }

        return linhas;
    }

    /**
     * Lê um arquivo de reservas e retorna uma LCItem preenchida.
     * @param caminhoArquivo Caminho do arquivo (ex: "data/raw/Reserva1000alea.txt")
     * @return LCItem com todas as reservas carregadas
     */
    public static LCItem lerReservas(String caminhoArquivo) {
        List<String> linhas = lerLinhas(caminhoArquivo);
        
        // Cria lista com tamanho apropriado
        LCItem lista = new LCItem(linhas.size());
        
        for (String linha : linhas) {
            Reserva item = parseReserva(linha);
            if (item != null) {
                lista.insereFinal(item);
            }
        }
        
        return lista;
    }

    /**
     * Faz o parse de uma linha no formato: R000001;NOME;V947;21/04/2024;167C
     */
    private static Reserva parseReserva(String linha) {
        if (linha == null || linha.trim().isEmpty()) {
            return null;
        }
        
        String[] partes = linha.split(";");
        
        if (partes.length != 5) {
            System.err.println("Linha com formato inválido: " + linha);
            return null;
        }
        
        String chave = partes[0].trim();
        String nome = partes[1].trim();
        String codigo_voo = partes[2].trim();
        String data = partes[3].trim();
        String assento = partes[4].trim();
        
        return new Reserva(chave, nome, codigo_voo, data, assento);
    }

    /**
     * Lê um arquivo de nomes (um nome por linha) e retorna um array de strings.
     * @param caminhoArquivo Caminho do arquivo (ex: "data/raw/nome.txt")
     * @return Array com os nomes a serem pesquisados
     */
    public static String[] lerNomes(String caminhoArquivo) {
        List<String> linhas = lerLinhas(caminhoArquivo);
        
        // Remove linhas vazias e faz trim
        List<String> nomes = new ArrayList<>();
        for (String linha : linhas) {
            if (linha != null && !linha.trim().isEmpty()) {
                nomes.add(linha.trim());
            }
        }
        
        return nomes.toArray(new String[0]);
    }

    /**
     * Lê um arquivo de nomes (um nome por linha) e retorna uma LCItem.
     * Cada nome é armazenado como um Item com chave = índice.
     * @param caminhoArquivo Caminho do arquivo (ex: "data/raw/nome.txt")
     * @return LCItem contendo os nomes a serem pesquisados
     */
    public static LCItem lerNomesComoLCItem(String caminhoArquivo) {
        List<String> linhas = lerLinhas(caminhoArquivo);
        
        // Cria LCItem com capacidade inicial estimada
        LCItem nomes = new LCItem(linhas.size());
        
        // Adiciona cada nome como um Item (usando índice como chave)
        int indice = 0;
        for (String linha : linhas) {
            if (linha != null && !linha.trim().isEmpty()) {
                nomes.insereFinal(new Reserva(indice++, linha.trim()));
            }
        }
        
        return nomes;
    }
}
