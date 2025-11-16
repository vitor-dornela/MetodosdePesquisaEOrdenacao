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
        String caminhoSaida = "src/br/faesa/C3/dados/ordenados/" + "heap" +nomeDataset + ".txt";

        try {
            System.out.println("Processando: " + nomeDataset);

            // 1. Carrega o dataset
            long inicio = System.currentTimeMillis();
            LCItem reservas = LeArquivo.lerReservas(caminhoEntrada);
            long tempoCarregamento = System.currentTimeMillis() - inicio;

            System.out.println("  - Carregadas " + reservas.getQuant() + " reservas em " + 
                               tempoCarregamento + " ms");

            // 2. Ordena usando HeapSort (por nome, depois por chave)
            inicio = System.currentTimeMillis();
            reservas.heapsort();
            long tempoOrdenacao = System.currentTimeMillis() - inicio;

            System.out.println("  - Ordenado em " + tempoOrdenacao + " ms");

            // 3. Mostra primeiras 3 reservas ordenadas
            System.out.println("  - Primeiras 3 após ordenação:");
            for (int i = 0; i < Math.min(3, reservas.getQuant()); i++) {
                System.out.println("    " + reservas.getItem(i).toStringFormatado());
            }

            // 4. Salva o resultado
            EscreveArquivo.salvarReservas(reservas, caminhoSaida);

            // 5. Salva estatísticas
            EscreveArquivo.salvarEstatisticas(
                "src/br/faesa/C3/dados/estatisticas.csv",
                nomeDataset,
                "HeapSort",
                tempoOrdenacao,
                reservas.getQuant()
            );

            System.out.println();

        } catch (IOException e) {
            System.err.println("Erro ao processar " + nomeDataset + ": " + e.getMessage());
        }
    }
}
