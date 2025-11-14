package br.faesa.C3.algoritmos.entidades;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArquivoUtil {

    // Abre um arquivo dentro do classpath
    public static BufferedReader abrirRecurso(String caminho) {
        InputStream input = ArquivoUtil.class.getResourceAsStream(caminho);

        if (input == null) {
            throw new RuntimeException("Arquivo não encontrado: " + caminho);
        }

        return new BufferedReader(new InputStreamReader(input));
    }

    // Lê todas as linhas e retorna uma lista
    public static List<String> lerArquivo(String caminho) {
        List<String> linhas = new ArrayList<>();

        try (BufferedReader br = abrirRecurso(caminho)) {
            String linha;

            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return linhas;
    }
}
