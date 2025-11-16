import br.faesa.C3.algoritmos.entidades.*;

/**
 * Teste simples das novas funcionalidades
 */
public class TesteReservas {
    public static void main(String[] args) {
        System.out.println("=== TESTE DE CARREGAMENTO E ORDENAÇÃO ===\n");
        
        
        // 5. Teste de carregamento de arquivo
        System.out.println("\n5. Carregando arquivo de reservas:");
        try {
            LCItem reservas = LeArquivo.lerReservas("/br/faesa/C3/dados/Reserva1000alea.txt");
            System.out.println("Carregadas " + reservas.getQuant() + " reservas");
            
            System.out.println("\nPrimeiras 5 reservas (antes de ordenar):");
            for (int i = 0; i < Math.min(5, reservas.getQuant()); i++) {
                System.out.println("  " + reservas.getItem(i).toStringFormatado());
            }
            
            // Ordenar
            System.out.println("\nOrdenando...");
            long inicio = System.currentTimeMillis();
            reservas.heapsort();
            long tempo = System.currentTimeMillis() - inicio;
            
            System.out.println("Ordenado em " + tempo + " ms");
            System.out.println("\nPrimeiras 5 reservas (após ordenar):");
            for (int i = 0; i < Math.min(5, reservas.getQuant()); i++) {
                System.out.println("  " + reservas.getItem(i).toStringFormatado());
            }
            
            // Salvar
            System.out.println("\n6. Salvando arquivo ordenado:");
            EscreveArquivo.salvarReservas(reservas, "src/br/faesa/C3/dados/teste_ordenado.txt");
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== TESTE CONCLUÍDO ===");
    }
}
