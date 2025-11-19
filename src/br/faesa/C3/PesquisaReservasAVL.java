package br.faesa.C3;

import java.io.IOException;

import br.faesa.C3.algoritmos.pesquisa.AVL.ArvoreAVLItem;
import br.faesa.C3.entidades.LCItem;
import br.faesa.C3.helper.EscreveArquivo;
import br.faesa.C3.helper.LeArquivo;

/**
 * Programa para realizar pesquisas de reservas usando Árvore AVL.
 * 
 * Para cada um dos 12 datasets de reservas:
 * 1. Carrega as reservas do arquivo
 * 2. Constrói uma AVL indexada por nome (auto-balanceada)
 * 3. Pesquisa os 400 nomes do arquivo nome.txt
 * 4. Repete 5 vezes para calcular tempo médio
 * 5. Salva os resultados em data/searched/AVL*.txt
 */
public class PesquisaReservasAVL {

    private static final int NUM_EXECUCOES = 5;
    private static final String CAMINHO_NOMES = "data/raw/nome.txt";
    private static final String CAMINHO_ESTATISTICAS = "data/estatisticas_pesquisa_avl.csv";

    public static void main(String[] args) {
        // Datasets a processar
        String[] datasets = {
            "Reserva1000alea", "Reserva1000ord", "Reserva1000inv",
            "Reserva5000alea", "Reserva5000ord", "Reserva5000inv",
            "Reserva10000alea", "Reserva10000ord", "Reserva10000inv",
            "Reserva50000alea", "Reserva50000ord", "Reserva50000inv"
        };

        // Carrega os nomes a pesquisar (uma única vez)
        String[] nomesPesquisa = LeArquivo.lerNomes(CAMINHO_NOMES);
        System.out.println("Total de nomes para pesquisa: " + nomesPesquisa.length);

        boolean primeiraLinha = true;

        // Processa cada dataset
        for (String nomeDataset : datasets) {
            System.out.println("\n=== PROCESSANDO: " + nomeDataset + " ===");

            try {
                processarDataset(nomeDataset, nomesPesquisa, primeiraLinha);
                primeiraLinha = false;
            } catch (IOException e) {
                System.err.println("Erro ao processar " + nomeDataset + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("\n=== PROCESSAMENTO CONCLUÍDO ===");
        System.out.println("Resultados salvos em: data/searched/");
        System.out.println("Estatísticas salvas em: " + CAMINHO_ESTATISTICAS);
    }

    /**
     * Processa um dataset: constrói AVL, pesquisa nomes e salva resultados.
     */
    private static void processarDataset(String nomeDataset, String[] nomesPesquisa, 
            boolean primeiraLinha) throws IOException {
        
        String caminhoEntrada = "data/raw/" + nomeDataset + ".txt";
        String caminhoSaida = "data/searched/AVL" + nomeDataset + ".txt";

        // Carrega o arquivo uma vez (fora do tempo)
        LCItem reservas = LeArquivo.lerReservas(caminhoEntrada);
        System.out.println("  Reservas carregadas: " + reservas.getQuant());

        // Variáveis para armazenar resultados da última execução
        LCItem[] resultadosPesquisa = null;
        long tempoTotal = 0;

        // Executa NUM_EXECUCOES vezes para calcular média
        for (int exec = 0; exec < NUM_EXECUCOES; exec++) {
            long inicio = System.currentTimeMillis();

            // 5.1) Carrega em AVL (auto-balanceada durante inserção)
            ArvoreAVLItem avl = new ArvoreAVLItem();
            for (int i = 0; i < reservas.getQuant(); i++) {
                avl.insere(reservas.getItem(i));
            }

            // 5.2) Realiza as pesquisas nos 400 nomes
            resultadosPesquisa = new LCItem[nomesPesquisa.length];
            for (int i = 0; i < nomesPesquisa.length; i++) {
                resultadosPesquisa[i] = avl.pesquisa(nomesPesquisa[i]);
            }

            long fim = System.currentTimeMillis();
            tempoTotal += (fim - inicio);
        }
        // 5.3) Após rodar 5 vezes, termina de contar o tempo

        // 5.4) Calcula média (diferença entre tempo final e inicial dividida por 5)
        double media = tempoTotal / (double) NUM_EXECUCOES;
        System.out.printf("  AVL: %.2f ms (média de %d execuções)\n", media, NUM_EXECUCOES);

        // Salva os resultados da última execução
        EscreveArquivo.salvarResultadosPesquisa(caminhoSaida, nomesPesquisa, resultadosPesquisa);

        // Salva estatísticas
        EscreveArquivo.salvarEstatisticas(
            CAMINHO_ESTATISTICAS,
            nomeDataset,
            "AVL",
            media,
            reservas.getQuant(),
            primeiraLinha
        );

        // Conta quantos nomes foram encontrados
        int encontrados = 0;
        int totalReservasEncontradas = 0;
        for (LCItem resultado : resultadosPesquisa) {
            if (resultado != null && !resultado.eVazia()) {
                encontrados++;
                totalReservasEncontradas += resultado.getQuant();
            }
        }
        System.out.printf("  Nomes encontrados: %d de %d (%.1f%%)\n", 
            encontrados, nomesPesquisa.length, 
            (encontrados * 100.0) / nomesPesquisa.length);
        System.out.println("  Total de reservas encontradas: " + totalReservasEncontradas);
    }
}
