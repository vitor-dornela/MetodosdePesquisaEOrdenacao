package br.faesa.C3.algoritmos.pesquisa.ABB;
import br.faesa.C3.entidades.LCInteiro;


public class ArvoreABB {

	// Atributos
	private NoABB raiz;
	private int quant;
	public ArvoreABB() {
		this.raiz = null;
		this.quant = 0;
	}

	public NoABB pesquisa (int num) {
		return pesquisa(num, raiz);
	}
	private NoABB pesquisa (int num, NoABB no) {
		if (no == null) {
			return null;
		}
		if (num == no.getNum()) {
			return no;
		}
		if (num > no.getNum()) {
			return pesquisa(num, no.getDir());
		}
		return pesquisa(num, no.getEsq());
	}

	public boolean insere (int num) {
		if (pesquisa(num) == null) {
			this.raiz = insere (num, this.raiz);
			return true;
		}
		return false;
	}

	private NoABB insere(int num, NoABB no) {
		if (no==null) {
			NoABB novo = new NoABB(num);
			this.quant++;
			return novo;
		}
		if (num < no.getNum()) {
			no.setEsq (insere (num, no.getEsq()));
		}else {
			no.setDir (insere (num, no.getDir()));
		}
		return no;
	}

	public NoABB remove (int num) {
		NoABB[] removido = new NoABB[1];
		this.raiz = remove (num, this.raiz, removido);
		return removido[0];
	}

	private NoABB remove (int num, NoABB no, NoABB[] removido) {
		if (no == null) {
			removido [0]=null;
			return no;
		}
		if (num < no.getNum()) {
			no.setEsq(remove(num, no.getEsq(), removido));
		} else if (num > no.getNum()) {
			no.setDir(remove(num, no.getDir(), removido));
		} else {
			removido [0] = no;
			this.quant--;
			if (no.getDir()== null) {
				return no.getEsq();
			} else if (no.getEsq() == null) {
				return no.getDir();
			} else {
				no.setEsq(maiorEsq(no, no.getEsq()));
			}
		}

		return no;
	}

	private NoABB maiorEsq(NoABB noRemovido, NoABB maior) {
		/*
		 * andar para a direita o maximo possivel, procurando o maior
		 * da esquerda.
		 */
		if (maior.getDir() != null) {
			maior.setDir(maiorEsq(noRemovido, maior.getDir()));
			return maior;
		}
		noRemovido.setNum(maior.getNum());
		return maior.getEsq();
	}
	public LCInteiro CamInOrdem() {
		LCInteiro vetor = new LCInteiro(this.quant);
		fazCamInOrdem(vetor, this.raiz);
		return (vetor);
	}

	private void fazCamInOrdem(LCInteiro vetor, NoABB no) {
		if (no != null) {
			fazCamInOrdem(vetor, no.getEsq());
			vetor.insereFinal(no.getNum());
			fazCamInOrdem(vetor, no.getDir());
		}
	}

	public LCInteiro CamPreOrdem() {
		LCInteiro vetor = new LCInteiro(this.quant);
		fazCamPreOrdem(vetor, this.raiz);
		return (vetor);
	}

	private void fazCamPreOrdem(LCInteiro vetor, NoABB no) {
		if (no != null) {
			vetor.insereFinal(no.getNum());
			fazCamPreOrdem(vetor, no.getEsq());
			fazCamPreOrdem(vetor, no.getDir());
		}
	}

	public LCInteiro CamPosOrdem() {
		LCInteiro vetor = new LCInteiro(this.quant);
		fazCamPosOrdem(vetor, this.raiz);
		return (vetor);
	}

	private void fazCamPosOrdem(LCInteiro vetor, NoABB no) {
		if (no != null) {
			fazCamPosOrdem(vetor, no.getEsq());
			fazCamPosOrdem(vetor, no.getDir());
			vetor.insereFinal(no.getNum());
		}
	}

	public ArvoreABB balancear() {
		LCInteiro vetor = CamInOrdem();
		ArvoreABB arvAux = new ArvoreABB();
		balancear(vetor, arvAux, 0, vetor.getQuant() - 1);
		return arvAux;
	}

	private void balancear(LCInteiro vetor, ArvoreABB arv, int esq, int dir) {
		int meio;

		if (esq <= dir) {
			meio = (esq + dir) / 2;
			arv.insere(vetor.get(meio));
			balancear(vetor, arv, esq, meio - 1);
			balancear(vetor, arv, meio + 1, dir);
		}
	}

	/** Soma de todos os nós da ABB */
	public int soma() {
		return soma(raiz);
	}

	private int soma(NoABB no) {
		if (no == null) return 0;
		return no.getNum()
				+ soma(no.getEsq())
				+ soma(no.getDir());
	}


	/**  Maior elemento da ABB */
	public Object maior() {
		if (raiz == null) {
			return null;
		}
		// inicializa o acumulador com o valor da raiz
		return maior(raiz, raiz.getNum());
	}

	private int maior(NoABB no, int maxAtual) {
		if (no == null) {
			return maxAtual;       // devolve o máximo encontrado até aqui
		}
		// atualiza o acumulador se este nó for maior
		if (no.getNum() > maxAtual) {
			maxAtual = no.getNum();
		}
		// varre sub-árvores
		maxAtual = maior(no.getEsq(), maxAtual);
		return maior(no.getDir(), maxAtual);
	}

	/** Retorna a altura da ABB (número de níveis) */
	public int altura() {
		return altura(raiz);
	}

	private int altura(NoABB no) {
		if (no == null) {
			return -1;                      // nenhuma no, altura 0
		}
		// calcula altura das sub-árvores
		int he = altura(no.getEsq());
		int hd = altura(no.getDir());
		// o nível atual + maior entre esquerda e direita
		return 1 + Math.max(he, hd);
	}

	/**
	 * Retorna um array de LCInteiro, onde cada posição i contém
	 * os valores dos nós no nível i (0 = raiz), até altura h.
	 */
	public LCInteiro[] nosPorNivel() {
		int h = altura();
		// capacidade = total de nós (usamos CamInOrdem())
		int total = CamInOrdem().getQuant();
		LCInteiro[] níveis = new LCInteiro[h+1];
		for (int i = 0; i <= h; i++) {
			níveis[i] = new LCInteiro(total);
		}
		preenchePorNivel(raiz, 0, níveis);
		return níveis;
	}

	private void preenchePorNivel(NoABB no, int lvl, LCInteiro[] níveis) {
		if (no == null) return;
		níveis[lvl].insereFinal(no.getNum());
		preenchePorNivel(no.getEsq(), lvl+1, níveis);
		preenchePorNivel(no.getDir(), lvl+1, níveis);
	}







}
