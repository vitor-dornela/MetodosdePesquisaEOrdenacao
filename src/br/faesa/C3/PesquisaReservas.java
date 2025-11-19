package br.faesa.C3;

import java.io.IOException;

import br.faesa.C3.algoritmos.pesquisa.ABB.ArvoreABBItem;
import br.faesa.C3.entidades.LCItem;
import br.faesa.C3.helper.EscreveArquivo;
import br.faesa.C3.helper.LeArquivo;

/**
 * Programa para realizar pesquisas de reservas usando Árvore Binária de Busca (ABB).
 * 
 * Para cada um dos 12 datasets de reservas:
 * 1. Carrega as reservas do arquivo
 * 2. Constrói uma ABB indexada por nome
 * 3. Balanceia a árvore para otimizar as buscas
 * 4. Pesquisa os 400 nomes do arquivo nome.txt
 * 5. Repete 5 vezes para calcular tempo médio
 * 6. Salva os resultados em data/searched/ABB*.txt
 */
public class PesquisaReservas {

    private static final int NUM_EXECUCOES = 5;
    private static final String CAMINHO_NOMES = "data/raw/nome.txt";
    private static final String CAMINHO_ESTATISTICAS = "data/estatisticas_pesquisa.csv";

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
     * Processa um dataset: constrói ABB, balanceia, pesquisa nomes e salva resultados.
     */
    private static void processarDataset(String nomeDataset, String[] nomesPesquisa, 
            boolean primeiraLinha) throws IOException {
        
        String caminhoEntrada = "data/raw/" + nomeDataset + ".txt";
        String caminhoSaida = "data/searched/ABB" + nomeDataset + ".txt";

        // Carrega as reservas
        // Carrega o arquivo uma vez (fora do tempo)
        LCItem reservas = LeArquivo.lerReservas(caminhoEntrada);
        System.out.println("  Reservas carregadas: " + reservas.getQuant());

        // Variáveis para armazenar resultados da última execução
        LCItem[] resultadosPesquisa = null;
        long tempoTotal = 0;

        // Executa NUM_EXECUCOES vezes para calcular média
        for (int exec = 0; exec < NUM_EXECUCOES; exec++) {
            long inicio = System.currentTimeMillis();

            // 4.1) Carrega em ABB e balanceia
            // Nota: construirBalanceada() faz load+balance de forma otimizada
            // para evitar StackOverflowError em dados ordenados (50k elementos)
            ArvoreABBItem abb = new ArvoreABBItem();
            abb.construirBalanceada(reservas);

            // 4.2) Realiza as pesquisas nos 400 nomes
            resultadosPesquisa = new LCItem[nomesPesquisa.length];
            for (int i = 0; i < nomesPesquisa.length; i++) {
                resultadosPesquisa[i] = abb.pesquisa(nomesPesquisa[i]);
            }

            long fim = System.currentTimeMillis();
            tempoTotal += (fim - inicio);
        }
        // 4.3) Após rodar 5 vezes, termina de contar o tempo

        // Calcula média
        double media = tempoTotal / (double) NUM_EXECUCOES;
        System.out.printf("  ABB: %.2f ms (média de %d execuções)\n", media, NUM_EXECUCOES);

        // Salva os resultados da última execução
        EscreveArquivo.salvarResultadosPesquisa(caminhoSaida, nomesPesquisa, resultadosPesquisa);

        // Salva estatísticas
        EscreveArquivo.salvarEstatisticas(
            CAMINHO_ESTATISTICAS,
            nomeDataset,
            "ABB",
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
