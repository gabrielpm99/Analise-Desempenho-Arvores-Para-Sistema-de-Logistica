package src;

public class RN extends Arvore {
    public long totalRotacoes = 0;
    public long totalComparacoes = 0;
    public long totalTrocasCores = 0;
    public int totalNos = 0;

    @Override
    public int obterTotalNos() {
        return this.totalNos;
    }

    public void resetarContadores() {
        totalRotacoes = 0;
        totalComparacoes = 0;
        totalTrocasCores = 0;
    }

    private NoRN cast(No noAtual) {
        return (NoRN) noAtual;
    }

    private boolean ehVermelho(NoRN noAtual) {
        if (noAtual == null) return false;
        return noAtual.cor == NoRN.Cor.VERMELHO;
    }

    private NoRN rotacionarEsquerda(NoRN noDesbalanceado) {
        totalRotacoes++;
        NoRN filhoDireito = cast(noDesbalanceado.ponteiroDireito);
        noDesbalanceado.ponteiroDireito = filhoDireito.ponteiroEsquerdo;
        if (filhoDireito.ponteiroEsquerdo != null) {
            cast(filhoDireito.ponteiroEsquerdo).ponteiroPai = noDesbalanceado;
        }
        filhoDireito.ponteiroPai = noDesbalanceado.ponteiroPai;
        filhoDireito.ponteiroEsquerdo = noDesbalanceado;
        noDesbalanceado.ponteiroPai = filhoDireito;

        filhoDireito.cor = noDesbalanceado.cor;
        noDesbalanceado.cor = NoRN.Cor.VERMELHO;
        totalTrocasCores += 2;

        return filhoDireito;
    }

    private NoRN rotacionarDireita(NoRN noDesbalanceado) {
        totalRotacoes++;
        NoRN filhoEsquerdo = cast(noDesbalanceado.ponteiroEsquerdo);
        noDesbalanceado.ponteiroEsquerdo = filhoEsquerdo.ponteiroDireito;
        if (filhoEsquerdo.ponteiroDireito != null) {
            cast(filhoEsquerdo.ponteiroDireito).ponteiroPai = noDesbalanceado;
        }
        filhoEsquerdo.ponteiroPai = noDesbalanceado.ponteiroPai;
        filhoEsquerdo.ponteiroDireito = noDesbalanceado;
        noDesbalanceado.ponteiroPai = filhoEsquerdo;

        filhoEsquerdo.cor = noDesbalanceado.cor;
        noDesbalanceado.cor = NoRN.Cor.VERMELHO;
        totalTrocasCores += 2;

        return filhoEsquerdo;
    }

    private void inverterCores(NoRN noAtual) {
        noAtual.cor = (noAtual.cor == NoRN.Cor.PRETO) ? NoRN.Cor.VERMELHO : NoRN.Cor.PRETO;
        if (noAtual.ponteiroEsquerdo != null) {
            NoRN filhoEsquerdo = cast(noAtual.ponteiroEsquerdo);
            filhoEsquerdo.cor = (filhoEsquerdo.cor == NoRN.Cor.PRETO) ? NoRN.Cor.VERMELHO : NoRN.Cor.PRETO;
        }
        if (noAtual.ponteiroDireito != null) {
            NoRN filhoDireito = cast(noAtual.ponteiroDireito);
            filhoDireito.cor = (filhoDireito.cor == NoRN.Cor.PRETO) ? NoRN.Cor.VERMELHO : NoRN.Cor.PRETO;
        }
        totalTrocasCores += 3;
    }

    private NoRN corrigirLLRB(NoRN noAtual) {
        if (ehVermelho(cast(noAtual.ponteiroDireito)) && !ehVermelho(cast(noAtual.ponteiroEsquerdo))) {
            noAtual = rotacionarEsquerda(noAtual);
        }
        if (ehVermelho(cast(noAtual.ponteiroEsquerdo)) && ehVermelho(cast(cast(noAtual.ponteiroEsquerdo).ponteiroEsquerdo))) {
            noAtual = rotacionarDireita(noAtual);
        }
        if (ehVermelho(cast(noAtual.ponteiroEsquerdo)) && ehVermelho(cast(noAtual.ponteiroDireito))) {
            inverterCores(noAtual);
        }
        return noAtual;
    }

    private NoRN inserirRecursivo(NoRN noAtual, int valor) {
        if (noAtual == null) {
            totalNos++;
            return new NoRN(valor);
        }

        totalComparacoes++;
        if (valor < noAtual.dados) {
            NoRN subArvoreEsquerda = inserirRecursivo(cast(noAtual.ponteiroEsquerdo), valor);
            noAtual.ponteiroEsquerdo = subArvoreEsquerda;
            subArvoreEsquerda.ponteiroPai = noAtual;
        } else if (valor > noAtual.dados) {
            totalComparacoes++;
            NoRN subArvoreDireita = inserirRecursivo(cast(noAtual.ponteiroDireito), valor);
            noAtual.ponteiroDireito = subArvoreDireita;
            subArvoreDireita.ponteiroPai = noAtual;
        } else {
            return noAtual;
        }

        return corrigirLLRB(noAtual);
    }

    @Override
    public void inserir(int valor) {
        raiz = inserirRecursivo(cast(raiz), valor);
        if (raiz != null) {
            cast(raiz).cor = NoRN.Cor.PRETO;
        }
    }

    private NoRN encontrarMenorValor(NoRN noAtual) {
        NoRN iterador = noAtual;
        while (iterador.ponteiroEsquerdo != null) {
            iterador = cast(iterador.ponteiroEsquerdo);
        }
        return iterador;
    }

    private NoRN moverVermelhoEsquerda(NoRN noAtual) {
        inverterCores(noAtual);
        if (noAtual.ponteiroDireito != null && ehVermelho(cast(cast(noAtual.ponteiroDireito).ponteiroEsquerdo))) {
            noAtual.ponteiroDireito = rotacionarDireita(cast(noAtual.ponteiroDireito));
            if (noAtual.ponteiroDireito != null) {
                cast(noAtual.ponteiroDireito).ponteiroPai = noAtual;
            }
            noAtual = rotacionarEsquerda(noAtual);
            inverterCores(noAtual);
        }
        return noAtual;
    }

    private NoRN moverVermelhoDireita(NoRN noAtual) {
        inverterCores(noAtual);
        if (noAtual.ponteiroEsquerdo != null && ehVermelho(cast(cast(noAtual.ponteiroEsquerdo).ponteiroEsquerdo))) {
            noAtual = rotacionarDireita(noAtual);
            inverterCores(noAtual);
        }
        return noAtual;
    }

    private NoRN removerMinimo(NoRN noAtual) {
        if (noAtual.ponteiroEsquerdo == null) {
            totalNos--;
            return null;
        }
        if (!ehVermelho(cast(noAtual.ponteiroEsquerdo)) && !ehVermelho(cast(cast(noAtual.ponteiroEsquerdo).ponteiroEsquerdo))) {
            noAtual = moverVermelhoEsquerda(noAtual);
        }
        NoRN subArvoreEsquerda = removerMinimo(cast(noAtual.ponteiroEsquerdo));
        noAtual.ponteiroEsquerdo = subArvoreEsquerda;
        if (subArvoreEsquerda != null) {
            subArvoreEsquerda.ponteiroPai = noAtual;
        }
        return corrigirLLRB(noAtual);
    }

    private NoRN removerRecursivo(NoRN noAtual, int valor) {
        if (noAtual == null) return null;

        totalComparacoes++;
        if (valor < noAtual.dados) {
            if (noAtual.ponteiroEsquerdo != null && !ehVermelho(cast(noAtual.ponteiroEsquerdo)) && !ehVermelho(cast(cast(noAtual.ponteiroEsquerdo).ponteiroEsquerdo))) {
                noAtual = moverVermelhoEsquerda(noAtual);
            }
            NoRN subArvoreEsquerda = removerRecursivo(cast(noAtual.ponteiroEsquerdo), valor);
            noAtual.ponteiroEsquerdo = subArvoreEsquerda;
            if (subArvoreEsquerda != null) {
                subArvoreEsquerda.ponteiroPai = noAtual;
            }
        } else {
            if (ehVermelho(cast(noAtual.ponteiroEsquerdo))) {
                noAtual = rotacionarDireita(noAtual);
            }
            if (valor == noAtual.dados && noAtual.ponteiroDireito == null) {
                totalNos--;
                return null;
            }
            if (noAtual.ponteiroDireito != null && !ehVermelho(cast(noAtual.ponteiroDireito)) && !ehVermelho(cast(cast(noAtual.ponteiroDireito).ponteiroEsquerdo))) {
                noAtual = moverVermelhoDireita(noAtual);
            }
            if (valor == noAtual.dados) {
                NoRN noMenorSucessor = encontrarMenorValor(cast(noAtual.ponteiroDireito));
                noAtual.dados = noMenorSucessor.dados;
                NoRN subArvoreDireita = removerMinimo(cast(noAtual.ponteiroDireito));
                noAtual.ponteiroDireito = subArvoreDireita;
                if (subArvoreDireita != null) {
                    subArvoreDireita.ponteiroPai = noAtual;
                }
            } else {
                NoRN subArvoreDireita = removerRecursivo(cast(noAtual.ponteiroDireito), valor);
                noAtual.ponteiroDireito = subArvoreDireita;
                if (subArvoreDireita != null) {
                    subArvoreDireita.ponteiroPai = noAtual;
                }
            }
        }
        return corrigirLLRB(noAtual);
    }

    @Override
    public void remover(int valor) {
        if (raiz == null) return;

        if (!ehVermelho(cast(raiz.ponteiroEsquerdo)) && !ehVermelho(cast(raiz.ponteiroDireito))) {
            cast(raiz).cor = NoRN.Cor.VERMELHO;
        }

        raiz = removerRecursivo(cast(raiz), valor);
        if (raiz != null) {
            cast(raiz).cor = NoRN.Cor.PRETO;
        }
    }

    private int obterAlturaRecursivo(No noAtual) {
        if (noAtual == null) return 0;
        return 1 + Math.max(obterAlturaRecursivo(noAtual.ponteiroEsquerdo), obterAlturaRecursivo(noAtual.ponteiroDireito));
    }

    @Override
    public int obterAlturaTotal() {
        return obterAlturaRecursivo(raiz);
    }

    public int obterFatorBalanceamentoRaiz() {
        if (raiz == null) return 0;
        return obterAlturaRecursivo(raiz.ponteiroEsquerdo) - obterAlturaRecursivo(raiz.ponteiroDireito);
    }
}