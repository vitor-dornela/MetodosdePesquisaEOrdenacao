package br.faesa.C3;
import br.faesa.C3.entidades.*;
import br.faesa.C3.helper.EscreveArquivo;
import br.faesa.C3.helper.LeArquivo;

import java.io.IOException;

/**
 * Exemplo de uso: Carregar datasets, ordenar com HeapSort e QuickSort e salvar resultados.
 */
public class OrdenacaoReservas {

    public static void main(String[] args) {
        // Lista com todos os datasets disponíveis
        LCItem datasets = new LCItem(12);
        datasets.insereFinal(new Item(0, "Reserva1000alea"));
        datasets.insereFinal(new Item(0, "Reserva1000ord"));
        datasets.insereFinal(new Item(0, "Reserva1000inv"));
        datasets.insereFinal(new Item(0, "Reserva5000alea"));
        datasets.insereFinal(new Item(0, "Reserva5000ord"));
        datasets.insereFinal(new Item(0, "Reserva5000inv"));
        datasets.insereFinal(new Item(0, "Reserva10000alea"));
        datasets.insereFinal(new Item(0, "Reserva10000ord"));
        datasets.insereFinal(new Item(0, "Reserva10000inv"));
        datasets.insereFinal(new Item(0, "Reserva50000alea"));
        datasets.insereFinal(new Item(0, "Reserva50000ord"));
        datasets.insereFinal(new Item(0, "Reserva50000inv"));

        // Lista com todos os algoritmos
        LCItem algoritmos = new LCItem(3);
        algoritmos.insereFinal(new Item(0, "HeapSort"));
        algoritmos.insereFinal(new Item(0, "QuickSort"));
        algoritmos.insereFinal(new Item(0, "QuickSortInsertion"));

        // Processa cada dataset com todos os algoritmos
        for (int i = 0; i < datasets.getQuant(); i++) {
            String nomeDataset = datasets.getItem(i).getNome();
            System.out.println("\n=== PROCESSANDO: " + nomeDataset + " ===\n");
            
            for (int j = 0; j < algoritmos.getQuant(); j++) {
                String algoritmo = algoritmos.getItem(j).getNome();
                boolean primeiraLinha = (i == 0 && j == 0); // Só a primeira linha do primeiro dataset
                processarDataset(nomeDataset, algoritmo, primeiraLinha);
            }
        }

        System.out.println("\n=== PROCESSAMENTO CONCLUÍDO ===");
    }

    /**
     * Carrega, ordena e salva um dataset específico.
     * @param nomeDataset Nome do dataset
     * @param algoritmo Nome do algoritmo ("HeapSort", "QuickSort" ou "QuickSortInsertion")
     * @param primeiraLinha Se true, escreve o cabeçalho do CSV
     */
    private static void processarDataset(String nomeDataset, String algoritmo, boolean primeiraLinha) {
        String caminhoEntrada = "data/raw/" + nomeDataset + ".txt";
        String prefixo;
        if (algoritmo.equals("HeapSort")) {
            prefixo = "heap";
        } else if (algoritmo.equals("QuickSort")) {
            prefixo = "quick";
        } else {
            prefixo = "QuickIns";
        }
        String caminhoSaida = "data/sorted/" + prefixo + nomeDataset + ".txt";

        try {
            System.out.print("  " + algoritmo + ": ");

            // Marca o tempo inicial
            long inicio = System.currentTimeMillis();
            
            // Executa 5 vezes
            LCItem reservas = null;
            for (int i = 0; i < 5; i++) {
                // Carrega o dataset
                reservas = LeArquivo.lerReservas(caminhoEntrada);
                
                // Chama o método apropriado
                if (algoritmo.equals("HeapSort")) {
                    reservas.heapsort();
                } else if (algoritmo.equals("QuickSort")) {
                    reservas.quicksort();
                } else {
                    reservas.quicksortComInsercao();
                }
            }
            
            // Marca o tempo final e calcula a média
            long tempoTotal = System.currentTimeMillis() - inicio;
            double media = tempoTotal / 5.0;
            
            System.out.println(String.format("%.2f", media) + " ms");

            // Salva o resultado
            EscreveArquivo.salvarReservas(reservas, caminhoSaida);

            // Salva estatísticas
            EscreveArquivo.salvarEstatisticas(
                "data/estatisticas.csv",
                nomeDataset,
                algoritmo,
                media,
                reservas.getQuant(),
                primeiraLinha
            );

        } catch (IOException e) {
            System.err.println("Erro ao processar " + nomeDataset + ": " + e.getMessage());
        }
    }
}
