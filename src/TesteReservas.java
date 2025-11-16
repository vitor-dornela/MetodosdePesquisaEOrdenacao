import br.faesa.C3.algoritmos.entidades.*;

/**
 * Teste simples das novas funcionalidades
 */
public class TesteReservas {
    public static void main(String[] args) {
        System.out.println("=== TESTE DE CARREGAMENTO E ORDENAÇÃO ===\n");
        
        // 1. Teste básico de criação de Item
        System.out.println("1. Criando items manualmente:");
        Item item1 = new Item("R000005", "MARIA SILVA", "V100", "01/01/2024", "10A");
        Item item2 = new Item("R000001", "JOÃO SANTOS", "V200", "02/01/2024", "20B");
        Item item3 = new Item("R000003", "JOÃO SANTOS", "V150", "03/01/2024", "15C");
        
        System.out.println(item1.toStringFormatado());
        System.out.println(item2.toStringFormatado());
        System.out.println(item3.toStringFormatado());
        
        // 2. Teste de comparação
        System.out.println("\n2. Teste de comparação:");
        System.out.println("item2.compareTo(item1) = " + item2.compareTo(item1) + " (JOÃO < MARIA)");
        System.out.println("item2.compareTo(item3) = " + item2.compareTo(item3) + " (R000001 < R000003)");
        
        // 3. Teste de LCItem com ordenação
        System.out.println("\n3. Criando LCItem e inserindo items:");
        LCItem lista = new LCItem();
        lista.insereFinal(item1);
        lista.insereFinal(item2);
        lista.insereFinal(item3);
        
        System.out.println("Antes da ordenação:");
        for (int i = 0; i < lista.getQuant(); i++) {
            System.out.println("  " + lista.getItem(i).toStringFormatado());
        }
        
        // 4. Ordenar usando HeapSort
        System.out.println("\n4. Ordenando com HeapSort:");
        lista.heapsort();
        
        System.out.println("Após ordenação:");
        for (int i = 0; i < lista.getQuant(); i++) {
            System.out.println("  " + lista.getItem(i).toStringFormatado());
        }
        
        // 5. Teste de carregamento de arquivo
        System.out.println("\n5. Carregando arquivo de reservas:");
        try {
            LCItem reservas = ReservaReader.lerReservas("/br/faesa/C3/dados/Reserva1000alea.txt");
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
            ArquivoEscritor.salvarReservas(reservas, "src/br/faesa/C3/dados/teste_ordenado.txt");
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== TESTE CONCLUÍDO ===");
    }
}
