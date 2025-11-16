package br.faesa.C3.algoritmos.entidades;

import java.util.List;

/**
 * Classe para carregar arquivos de reservas do formato:
 * R000001;NOME;V947;21/04/2024;167C
 */
public class ReservaReader {

    /**
     * Lê um arquivo de reservas e retorna uma LCItem preenchida.
     * @param caminhoArquivo Caminho do arquivo no classpath (ex: "/br/faesa/C3/dados/Reserva1000alea.txt")
     * @return LCItem com todas as reservas carregadas
     */
    public static LCItem lerReservas(String caminhoArquivo) {
        List<String> linhas = ArquivoUtil.lerArquivo(caminhoArquivo);
        
        // Cria lista com tamanho apropriado
        LCItem lista = new LCItem(linhas.size());
        
        for (String linha : linhas) {
            Item item = parseLinha(linha);
            if (item != null) {
                lista.insereFinal(item);
            }
        }
        
        return lista;
    }

    /**
     * Faz o parse de uma linha no formato: R000001;NOME;V947;21/04/2024;167C
     */
    private static Item parseLinha(String linha) {
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

    /**
     * Método de teste
     */
    public static void main(String[] args) {
        System.out.println("Carregando arquivo de reservas...");
        
        LCItem reservas = lerReservas("/br/faesa/C3/dados/Reserva1000alea.txt");
        
        System.out.println("Total de reservas carregadas: " + reservas.getQuant());
        System.out.println("\nPrimeiras 5 reservas:");
        
        for (int i = 0; i < Math.min(5, reservas.getQuant()); i++) {
            System.out.println(reservas.getItem(i).toStringFormatado());
        }
    }
}
