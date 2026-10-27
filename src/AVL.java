package src;

public class AVL extends Arvore {
    public long totalRotacoes = 0;
    public long totalComparacoes = 0;
    public int totalNos = 0;

    @Override
    public int obterTotalNos() {
        return this.totalNos;
    }

    public void resetarContadores() {
        totalRotacoes = 0;
        totalComparacoes = 0;
    }

    private NoAVL cast(No no) {
        return (NoAVL) no;
    }

    private int obterAltura(No noAtual) {
        if (noAtual == null) return 0;
        return cast(noAtual).altura;
    }

    private int obterFatorBalanceamento(No noAtual) {
        if (noAtual == null) return 0;
        return obterAltura(noAtual.ponteiroEsquerdo) - obterAltura(noAtual.ponteiroDireito);
    }

    private No rotacionarDireita(No noDesbalanceado) {
        totalRotacoes++;
        No filhoEsquerdo = noDesbalanceado.ponteiroEsquerdo;
        No subArvoreDireita = filhoEsquerdo.ponteiroDireito;

        filhoEsquerdo.ponteiroDireito = noDesbalanceado;
        noDesbalanceado.ponteiroEsquerdo = subArvoreDireita;

        cast(noDesbalanceado).altura = Math.max(obterAltura(noDesbalanceado.ponteiroEsquerdo), obterAltura(noDesbalanceado.ponteiroDireito)) + 1;
        cast(filhoEsquerdo).altura = Math.max(obterAltura(filhoEsquerdo.ponteiroEsquerdo), obterAltura(filhoEsquerdo.ponteiroDireito)) + 1;

        return filhoEsquerdo;
    }

    private No rotacionarEsquerda(No noDesbalanceado) {
        totalRotacoes++;
        No filhoDireito = noDesbalanceado.ponteiroDireito;
        No subArvoreEsquerda = filhoDireito.ponteiroEsquerdo;

        filhoDireito.ponteiroEsquerdo = noDesbalanceado;
        noDesbalanceado.ponteiroDireito = subArvoreEsquerda;

        cast(noDesbalanceado).altura = Math.max(obterAltura(noDesbalanceado.ponteiroEsquerdo), obterAltura(noDesbalanceado.ponteiroDireito)) + 1;
        cast(filhoDireito).altura = Math.max(obterAltura(filhoDireito.ponteiroEsquerdo), obterAltura(filhoDireito.ponteiroDireito)) + 1;

        return filhoDireito;
    }

    @Override
    public void inserir(int valor) {
        raiz = inserirRecursivo(raiz, valor);
    }

    private No inserirRecursivo(No noAtual, int valor) {
        if (noAtual == null) {
            totalNos++;
            return new NoAVL(valor);
        }

        totalComparacoes++;
        if (valor < noAtual.dados) {
            noAtual.ponteiroEsquerdo = inserirRecursivo(noAtual.ponteiroEsquerdo, valor);
        } else {
            totalComparacoes++;
            if (valor > noAtual.dados) {
                noAtual.ponteiroDireito = inserirRecursivo(noAtual.ponteiroDireito, valor);
            } else {
                return noAtual;
            }
        }

        cast(noAtual).altura = 1 + Math.max(obterAltura(noAtual.ponteiroEsquerdo), obterAltura(noAtual.ponteiroDireito));

        int balanceamento = obterFatorBalanceamento(noAtual);

        totalComparacoes++;
        if (balanceamento > 1 && valor < noAtual.ponteiroEsquerdo.dados) {
            return rotacionarDireita(noAtual);
        }

        totalComparacoes++;
        if (balanceamento < -1 && valor > noAtual.ponteiroDireito.dados) {
            return rotacionarEsquerda(noAtual);
        }

        totalComparacoes++;
        if (balanceamento > 1 && valor > noAtual.ponteiroEsquerdo.dados) {
            noAtual.ponteiroEsquerdo = rotacionarEsquerda(noAtual.ponteiroEsquerdo);
            return rotacionarDireita(noAtual);
        }

        totalComparacoes++;
        if (balanceamento < -1 && valor < noAtual.ponteiroDireito.dados) {
            noAtual.ponteiroDireito = rotacionarDireita(noAtual.ponteiroDireito);
            return rotacionarEsquerda(noAtual);
        }

        return noAtual;
    }

    private No encontrarMenorValor(No noAtual) {
        No iterador = noAtual;
        while (iterador.ponteiroEsquerdo != null) {
            iterador = iterador.ponteiroEsquerdo;
        }
        return iterador;
    }

    @Override
    public void remover(int valor) {
        raiz = removerRecursivo(raiz, valor);
    }

    private No removerRecursivo(No noAtual, int valor) {
        if (noAtual == null) {
            return null;
        }

        totalComparacoes++;
        if (valor < noAtual.dados) {
            noAtual.ponteiroEsquerdo = removerRecursivo(noAtual.ponteiroEsquerdo, valor);
        } else {
            totalComparacoes++;
            if (valor > noAtual.dados) {
                noAtual.ponteiroDireito = removerRecursivo(noAtual.ponteiroDireito, valor);
            } else {
                if (noAtual.ponteiroEsquerdo == null || noAtual.ponteiroDireito == null) {
                    No noSubstituto = noAtual.ponteiroEsquerdo != null ? noAtual.ponteiroEsquerdo : noAtual.ponteiroDireito;
                    if (noSubstituto == null) {
                        noAtual = null;
                    } else {
                        noAtual = noSubstituto;
                    }
                    totalNos--;
                } else {
                    No noSubstituto = encontrarMenorValor(noAtual.ponteiroDireito);
                    noAtual.dados = noSubstituto.dados;
                    noAtual.ponteiroDireito = removerRecursivo(noAtual.ponteiroDireito, noSubstituto.dados);
                }
            }
        }

        if (noAtual == null) {
            return null;
        }

        cast(noAtual).altura = 1 + Math.max(obterAltura(noAtual.ponteiroEsquerdo), obterAltura(noAtual.ponteiroDireito));

        int balanceamento = obterFatorBalanceamento(noAtual);

        if (balanceamento > 1 && obterFatorBalanceamento(noAtual.ponteiroEsquerdo) >= 0) {
            return rotacionarDireita(noAtual);
        }

        if (balanceamento > 1 && obterFatorBalanceamento(noAtual.ponteiroEsquerdo) < 0) {
            noAtual.ponteiroEsquerdo = rotacionarEsquerda(noAtual.ponteiroEsquerdo);
            return rotacionarDireita(noAtual);
        }

        if (balanceamento < -1 && obterFatorBalanceamento(noAtual.ponteiroDireito) <= 0) {
            return rotacionarEsquerda(noAtual);
        }

        if (balanceamento < -1 && obterFatorBalanceamento(noAtual.ponteiroDireito) > 0) {
            noAtual.ponteiroDireito = rotacionarDireita(noAtual.ponteiroDireito);
            return rotacionarEsquerda(noAtual);
        }

        return noAtual;
    }

    @Override
    public int obterAlturaTotal() {
        return obterAltura(raiz);
    }

    public int obterFatorBalanceamentoRaiz() {
        return obterFatorBalanceamento(raiz);
    }
}