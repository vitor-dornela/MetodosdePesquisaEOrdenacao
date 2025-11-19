package br.faesa.C3;

import java.io.IOException;

import br.faesa.C3.algoritmos.pesquisa.ABB.ArvoreABBItem;
import br.faesa.C3.algoritmos.pesquisa.AVL.ArvoreAVLItem;
import br.faesa.C3.entidades.Item;
import br.faesa.C3.entidades.LCItem;
import br.faesa.C3.helper.EscreveArquivo;
import br.faesa.C3.helper.LeArquivo;

/**
 * Programa para realizar pesquisas de reservas usando ABB e AVL.
 * Utiliza as estruturas de dados LCItem para armazenar datasets e resultados.
 * 
 * Para cada um dos 12 datasets de reservas:
 * 1. Carrega as reservas do arquivo
 * 2. Constrói ABB (balanceada) e AVL (auto-balanceada)
 * 3. Pesquisa os 400 nomes do arquivo nome.txt
 * 4. Repete 5 vezes para calcular tempo médio
 * 5. Salva os resultados em data/searched/
 * 6. Compara performance entre ABB e AVL
 */
public class PesquisaReservas {

    private static final int NUM_EXECUCOES = 5;
    private static final String CAMINHO_NOMES = "data/raw/nome.txt";
    private static final String CAMINHO_ESTATISTICAS = "data/estatisticas_pesquisa.csv";

    public static void main(String[] args) {
        // Datasets a processar (usando LCItem para consistência)
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

        // Carrega os nomes a pesquisar (uma única vez) usando LCItem
        LCItem nomesPesquisa = LeArquivo.lerNomesComoLCItem(CAMINHO_NOMES);
        System.out.println("Total de nomes para pesquisa: " + nomesPesquisa.getQuant());

        boolean primeiraLinha = true;

        // Processa cada dataset
        for (int i = 0; i < datasets.getQuant(); i++) {
            String nomeDataset = datasets.getItem(i).getNome();
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
     * Processa um dataset: constrói ABB e AVL, pesquisa nomes e compara resultados.
     */
    private static void processarDataset(String nomeDataset, LCItem nomesPesquisa, 
            boolean primeiraLinha) throws IOException {
        
        String caminhoEntrada = "data/raw/" + nomeDataset + ".txt";
        String caminhoSaidaABB = "data/searched/ABB" + nomeDataset + ".txt";
        String caminhoSaidaAVL = "data/searched/AVL" + nomeDataset + ".txt";

        // Carrega as reservas (fora do tempo)
        LCItem reservas = LeArquivo.lerReservas(caminhoEntrada);
        System.out.println("  Reservas carregadas: " + reservas.getQuant());

        // ========== ABB ==========
        LCItem[] resultadosPesquisaABB = null;
        long tempoTotalABB = 0;

        for (int exec = 0; exec < NUM_EXECUCOES; exec++) {
            long inicio = System.currentTimeMillis();

            // Constrói ABB balanceada usando lista de reservas
            ArvoreABBItem abb = new ArvoreABBItem();
            abb.construirBalanceada(reservas);

            // Realiza as pesquisas usando LCItem de nomes
            resultadosPesquisaABB = new LCItem[nomesPesquisa.getQuant()];
            for (int i = 0; i < nomesPesquisa.getQuant(); i++) {
                String nome = nomesPesquisa.getItem(i).getNome();
                resultadosPesquisaABB[i] = abb.pesquisa(nome);
            }

            long fim = System.currentTimeMillis();
            tempoTotalABB += (fim - inicio);
        }

        double mediaABB = tempoTotalABB / (double) NUM_EXECUCOES;
        System.out.printf("  ABB: %.2f ms\n", mediaABB);

        // Salva resultados ABB usando LCItem diretamente
        EscreveArquivo.salvarResultadosPesquisa(caminhoSaidaABB, nomesPesquisa, resultadosPesquisaABB);

        // ========== AVL ==========
        LCItem[] resultadosPesquisaAVL = null;
        long tempoTotalAVL = 0;

        for (int exec = 0; exec < NUM_EXECUCOES; exec++) {
            long inicio = System.currentTimeMillis();

            // Constrói AVL (auto-balanceada) inserindo cada item da lista
            ArvoreAVLItem avl = new ArvoreAVLItem();
            for (int i = 0; i < reservas.getQuant(); i++) {
                avl.insere(reservas.getItem(i));
            }

            // Realiza as pesquisas usando LCItem de nomes
            resultadosPesquisaAVL = new LCItem[nomesPesquisa.getQuant()];
            for (int i = 0; i < nomesPesquisa.getQuant(); i++) {
                String nome = nomesPesquisa.getItem(i).getNome();
                resultadosPesquisaAVL[i] = avl.pesquisa(nome);
            }

            long fim = System.currentTimeMillis();
            tempoTotalAVL += (fim - inicio);
        }

        double mediaAVL = tempoTotalAVL / (double) NUM_EXECUCOES;
        System.out.printf("  AVL: %.2f ms\n", mediaAVL);

        // Salva resultados AVL usando LCItem diretamente
        EscreveArquivo.salvarResultadosPesquisa(caminhoSaidaAVL, nomesPesquisa, resultadosPesquisaAVL);

        // ========== Estatísticas ==========
        // Salva ambas as estruturas no mesmo arquivo
        EscreveArquivo.salvarEstatisticas(
            CAMINHO_ESTATISTICAS,
            nomeDataset,
            "ABB",
            mediaABB,
            reservas.getQuant(),
            primeiraLinha
        );
        
        EscreveArquivo.salvarEstatisticas(
            CAMINHO_ESTATISTICAS,
            nomeDataset,
            "AVL",
            mediaAVL,
            reservas.getQuant(),
            false // Nunca primeira linha para AVL
        );

        // Conta quantos nomes foram encontrados (usando resultados ABB)
        int encontrados = 0;
        int totalReservasEncontradas = 0;
        for (LCItem resultado : resultadosPesquisaABB) {
            if (resultado != null && !resultado.eVazia()) {
                encontrados++;
                totalReservasEncontradas += resultado.getQuant();
            }
        }
        System.out.printf("  Nomes encontrados: %d de %d (%.1f%%)\n", 
            encontrados, nomesPesquisa.getQuant(), 
            (encontrados * 100.0) / nomesPesquisa.getQuant());
        System.out.println("  Total de reservas: " + totalReservasEncontradas);
    }
}
