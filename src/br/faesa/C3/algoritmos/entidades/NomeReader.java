package br.faesa.C3.algoritmos.entidades;

import java.util.List;

public class NomeReader {

    // Lê o arquivo nome.txt que está no classpath
    public static List<String> ler() {
        return ArquivoUtil.lerArquivo("/br/faesa/C3/dados/nome.txt");
    }

    public static void main(String[] args) {

        // Carrega as linhas do arquivo em uma lista
        List<String> dados = ler();

        // Imprime cada linha lida
        for (String linha : dados) {
            System.out.println(linha);
        }
    }
}
