import br.faesa.C3.algoritmos.entidades.*;

import java.io.IOException;

/**
 * Exemplo de uso: Carregar datasets, ordenar com HeapSort e salvar resultados.
 */
public class OrdenacaoReservas {

    public static void main(String[] args) {
        // Array com todos os datasets disponíveis
        String[] datasets = {
            "Reserva1000alea", "Reserva1000ord", "Reserva1000inv",
            "Reserva5000alea", "Reserva5000ord", "Reserva5000inv",
            "Reserva10000alea", "Reserva10000ord", "Reserva10000inv",
            "Reserva50000alea", "Reserva50000ord", "Reserva50000inv"
        };

        System.out.println("=== ORDENAÇÃO DE RESERVAS COM HEAPSORT ===\n");

        // Processa cada dataset
        for (String nomeDataset : datasets) {
            processarDataset(nomeDataset);
        }

        System.out.println("\n=== PROCESSAMENTO CONCLUÍDO ===");
    }

    /**
     * Carrega, ordena e salva um dataset específico.
     */
    private static void processarDataset(String nomeDataset) {
        String caminhoEntrada = "/br/faesa/C3/dados/brutos/" + nomeDataset + ".txt";
        String caminhoSaida = "src/br/faesa/C3/dados/ordenados/" + "heap" + nomeDataset + ".txt";

        try {
            System.out.println("Processando: " + nomeDataset);

            // 1. Carrega o dataset
            long inicio = System.currentTimeMillis();
            LCItem reservas = LeArquivo.lerReservas(caminhoEntrada);
            long tempoCarregamento = System.currentTimeMillis() - inicio;

            System.out.println("  - Carregadas " + reservas.getQuant() + " reservas em " + 
                               tempoCarregamento + " ms");

            // 2. Ordena usando HeapSort 5 vezes e calcula a média
            long[] tempos = new long[5];
            System.out.println("  - Executando HeapSort 5 vezes:");
            
            for (int i = 0; i < 5; i++) {
                // Recarrega o dataset para cada execução
                LCItem reservasCopia = LeArquivo.lerReservas(caminhoEntrada);
                
                inicio = System.currentTimeMillis();
                reservasCopia.heapsort();
                tempos[i] = System.currentTimeMillis() - inicio;
                
                System.out.println("    T" + (i+1) + ": " + tempos[i] + " ms");
                
                // Usa a última execução para salvar
                if (i == 4) {
                    reservas = reservasCopia;
                }
            }
            
            // Calcula a média
            double media = 0;
            for (long tempo : tempos) {
                media += tempo;
            }
            media = media / 5.0;
            
            System.out.println("  - Média: " + String.format("%.2f", media) + " ms");

            // 3. Salva o resultado
            EscreveArquivo.salvarReservas(reservas, caminhoSaida);

            // 4. Salva estatísticas (primeira linha cria o arquivo, demais adicionam)
            boolean primeiraLinha = nomeDataset.equals("Reserva1000alea");
            EscreveArquivo.salvarEstatisticas(
                "src/br/faesa/C3/dados/estatisticas.csv",
                nomeDataset,
                "HeapSort",
                tempos,
                media,
                reservas.getQuant(),
                primeiraLinha
            );

            System.out.println();

        } catch (IOException e) {
            System.err.println("Erro ao processar " + nomeDataset + ": " + e.getMessage());
        }
    }
}
