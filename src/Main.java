import br.faesa.C3.algoritmos.entidades.LCInteiro;
import br.faesa.C3.algoritmos.ordenacao.HeapSort;
import br.faesa.C3.algoritmos.ordenacao.QuickSort;
import br.faesa.C3.algoritmos.pesquisa.AVL.ArvoreAVL;
import br.faesa.C3.algoritmos.pesquisa.AVL.NoAVL;

public class Main {

    public static void main(String[] args) {

        // Cria uma lista
        LCInteiro minhaLista = new LCInteiro();

        minhaLista.insereFinal(64);
        minhaLista.insereFinal(34);
        minhaLista.insereFinal(25);
        minhaLista.insereFinal(12);
        minhaLista.insereFinal(22);

        System.out.println("Lista sem ordenação: " + minhaLista.toString());

        // Testes algoritmos de ordenação
        // QuickSort
        QuickSort.sort(minhaLista);
        System.out.println("Ordenado com QuickSort: " + minhaLista.toString());

        // HeapSort
        HeapSort.sort(minhaLista);
        System.out.println("Ordenado com HeapSort: " + minhaLista.toString());



        System.out.println("\n=== TESTE ÁRVORE AVL ===");
        // Testes algoritmos de pesquisa
        // Árvore AVL
        ArvoreAVL arvoreAVL = new ArvoreAVL();

        // Inserir elementos na árvore AVL
        int[] elementosAVL = {50, 25, 75, 10, 30, 60, 80, 5, 15, 27, 35, 65, 85, 1, 2, 6};
        System.out.println("Inserindo elementos na Árvore AVL:");
        for (int elemento : elementosAVL) {
            System.out.print(elemento + " ");
            arvoreAVL.inserir(elemento);
        }
        System.out.println("\n");

        // Mostrar estrutura e traversals da árvore
        System.out.println("=== Visualização da Árvore ===");
        arvoreAVL.mostrarEstrutura();
        System.out.println();

        arvoreAVL.emOrdem();
        arvoreAVL.preOrdem();
        arvoreAVL.posOrdem();

        System.out.println("Altura da árvore: " + arvoreAVL.getAltura());
        System.out.println("Árvore vazia: " + arvoreAVL.estaVazia());
        System.out.println();

        // Testar pesquisa na árvore AVL
        System.out.println("=== Testes de Pesquisa ===");
        int[] elementosPesquisa = {30, 100, 5, 85, 45, 75};

        for (int elemento : elementosPesquisa) {
            NoAVL resultado = arvoreAVL.pesquisar(elemento);
            if (resultado != null) {
                System.out.println("Elemento " + elemento + " encontrado!");
            } else {
                System.out.println("Elemento " + elemento + " NÃO encontrado.");
            }
        }

        System.out.println("\nTeste da Árvore AVL concluído!");

    }

}
