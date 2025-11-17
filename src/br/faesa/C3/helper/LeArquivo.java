package br.faesa.C3.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.faesa.C3.entidades.Item;
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
            Item item = parseReserva(linha);
            if (item != null) {
                lista.insereFinal(item);
            }
        }
        
        return lista;
    }

    /**
     * Faz o parse de uma linha no formato: R000001;NOME;V947;21/04/2024;167C
     */
    private static Item parseReserva(String linha) {
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
        
        return new Item(chave, nome, codigo_voo, data, assento);
    }
}
