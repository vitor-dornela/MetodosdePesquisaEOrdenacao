package br.faesa.C3;

import br.faesa.C3.algoritmos.pesquisa.Hashing.HashingEncadeado;
import br.faesa.C3.entidades.Item;
import br.faesa.C3.entidades.LCItem;

/**
 * Teste simples para verificar a implementação do Hashing Encadeado.
 */
public class TesteHashing {
    
    public static void main(String[] args) {
        System.out.println("=== TESTE HASHING ENCADEADO ===\n");
        
        // Cria alguns itens de teste
        LCItem reservas = new LCItem();
        reservas.insereFinal(new Item("R000001", "JOAO SILVA", "V123", "01/01/2024", "10A"));
        reservas.insereFinal(new Item("R000002", "MARIA SANTOS", "V456", "02/01/2024", "15B"));
        reservas.insereFinal(new Item("R000003", "JOAO SILVA", "V789", "03/01/2024", "20C"));
        reservas.insereFinal(new Item("R000004", "PEDRO COSTA", "V111", "04/01/2024", "25D"));
        reservas.insereFinal(new Item("R000005", "MARIA SANTOS", "V222", "05/01/2024", "30E"));
        
        System.out.println("Reservas criadas: " + reservas.getQuant());
        
        // Cria tabela hash
        HashingEncadeado hash = new HashingEncadeado(7); // Número primo
        hash.carregarDeLCItem(reservas);
        
        System.out.println("\nTabela hash carregada!");
        hash.imprimirEstatisticas();
        
        // Testa pesquisas
        System.out.println("\n=== TESTES DE PESQUISA ===");
        
        String[] nomesTeste = {"JOAO SILVA", "MARIA SANTOS", "PEDRO COSTA", "ANA OLIVEIRA"};
        
        for (String nome : nomesTeste) {
            System.out.println("\nPesquisando: " + nome);
            LCItem resultados = hash.pesquisar(nome);
            
            if (resultados.eVazia()) {
                System.out.println("  Nenhuma reserva encontrada.");
            } else {
                System.out.println("  Encontradas " + resultados.getQuant() + " reserva(s):");
                for (int i = 0; i < resultados.getQuant(); i++) {
                    Item item = resultados.getItem(i);
                    System.out.println("    - " + item.getChave() + " | " + 
                                     item.getNome() + " | " + 
                                     item.getCodigoVoo() + " | " + 
                                     item.getData() + " | " + 
                                     item.getAssento());
                }
            }
        }
        
        System.out.println("\n=== TESTE CONCLUÍDO ===");
    }
}
