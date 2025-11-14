import br.faesa.C3.main.entidades.LCInteiro;
import br.faesa.C3.main.ordenacao.QuickSort;

public class Main {

    public static void main(String[] args) {
        LCInteiro minhaLista = new LCInteiro();

        minhaLista.insereFinal(64);
        minhaLista.insereFinal(34);
        minhaLista.insereFinal(25);
        minhaLista.insereFinal(12);
        minhaLista.insereFinal(22);

        System.out.println("Lista sem ordenação: " + minhaLista.toString());

        // Teste QuickSort
        QuickSort.sortList(minhaLista);
        System.out.println("Ordenado com QuickSort: " + minhaLista.toString());
    }

}
