package src;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class Arvore {
    protected No raiz;

    public Arvore() {
        this.raiz = null;
    }

    public boolean buscar(int valor) {
        return buscarRecursivo(raiz, valor);
    }

    protected boolean buscarRecursivo(No no, int valor) {
        if (no == null) return false;
        if (no.dados == valor) return true;
        if (valor < no.dados) return buscarRecursivo(no.ponteiroEsquerdo, valor);
        else return buscarRecursivo(no.ponteiroDireito, valor);
    }

    public void imprimirOrdem(PrintWriter out) {
        imprimirOrdemRecursivo(raiz, out);
        out.flush();
    }

    protected void imprimirOrdemRecursivo(No no, PrintWriter out) {
        if (no != null) {
            imprimirOrdemRecursivo(no.ponteiroEsquerdo, out);
            out.print(no.dados + " ");
            imprimirOrdemRecursivo(no.ponteiroDireito, out);
        }
    }

    // --- LÓGICA DE IMPRESSÃO VISUAL ---

    private class CaixaVisual {
        List<String> linhas = new ArrayList<>();
        int centro;
        int largura;
    }

    public void imprimirArvore(PrintWriter out) {
        if (raiz == null) {
            out.println("  Arvore Vazia");
            return;
        }
        CaixaVisual caixa = construirVisual(raiz);
        for (String linha : caixa.linhas) {
            out.println("  " + linha);
        }
        out.flush();
    }

    private CaixaVisual construirVisual(No no) {
        if (no == null) return null;

        String texto = "(" + no.dados;
        if (no instanceof NoRN) {
            texto += (((NoRN) no).cor == NoRN.Cor.VERMELHO) ? "(V)" : "(P)";
        }
        texto += ")";
        int len = texto.length();

        CaixaVisual esq = construirVisual(no.ponteiroEsquerdo);
        CaixaVisual dir = construirVisual(no.ponteiroDireito);

        CaixaVisual res = new CaixaVisual();

        if (esq == null && dir == null) {
            res.linhas.add(texto);
            res.centro = len / 2;
            res.largura = len;
            return res;
        }

        int minGap = 2; 

        if (esq != null && dir != null) {
            int espacoMinimo = minGap;
            int distanciaEntreCentros = (esq.largura - esq.centro) + espacoMinimo + dir.centro;
            if (distanciaEntreCentros < len + 2) {
                espacoMinimo += (len + 2 - distanciaEntreCentros);
            }

            res.largura = esq.largura + espacoMinimo + dir.largura;
            int esqC = esq.centro;
            int dirC = esq.largura + espacoMinimo + dir.centro;
            res.centro = (esqC + dirC) / 2;

            int posTexto = res.centro - len / 2;

            StringBuilder linhaTexto = new StringBuilder(repetir(" ", res.largura));
            inserir(linhaTexto, texto, posTexto);

            linhaTexto.setCharAt(esqC, '┌');
            for (int i = esqC + 1; i < posTexto; i++) linhaTexto.setCharAt(i, '─');
            for (int i = posTexto + len; i < dirC; i++) linhaTexto.setCharAt(i, '─');
            linhaTexto.setCharAt(dirC, '┐');

            res.linhas.add(linhaTexto.toString());

            StringBuilder linhaHastes = new StringBuilder(repetir(" ", res.largura));
            linhaHastes.setCharAt(esqC, '│');
            linhaHastes.setCharAt(dirC, '│');
            res.linhas.add(linhaHastes.toString());

            int maxH = Math.max(esq.linhas.size(), dir.linhas.size());
            for (int i = 0; i < maxH; i++) {
                String l = (i < esq.linhas.size()) ? esq.linhas.get(i) : repetir(" ", esq.largura);
                String r = (i < dir.linhas.size()) ? dir.linhas.get(i) : repetir(" ", dir.largura);
                res.linhas.add(padRight(l, esq.largura) + repetir(" ", espacoMinimo) + padRight(r, dir.largura));
            }
            return res;

        } else if (esq != null) {
            int posTexto = esq.centro + minGap + 2;
            res.centro = posTexto + len / 2;
            res.largura = Math.max(esq.largura, posTexto + len);

            StringBuilder linhaTexto = new StringBuilder(repetir(" ", res.largura));
            inserir(linhaTexto, texto, posTexto);

            int esqC = esq.centro;
            linhaTexto.setCharAt(esqC, '┌');
            for (int i = esqC + 1; i < posTexto; i++) linhaTexto.setCharAt(i, '─');

            res.linhas.add(linhaTexto.toString());

            StringBuilder linhaHastes = new StringBuilder(repetir(" ", res.largura));
            linhaHastes.setCharAt(esqC, '│');
            res.linhas.add(linhaHastes.toString());

            for (int i = 0; i < esq.linhas.size(); i++) {
                res.linhas.add(padRight(esq.linhas.get(i), res.largura));
            }
            return res;

        } else { // dir != null
            int posTexto = 0;
            res.centro = len / 2;
            int shift = len + minGap + 2;
            res.largura = dir.largura + shift;

            StringBuilder linhaTexto = new StringBuilder(repetir(" ", res.largura));
            inserir(linhaTexto, texto, posTexto);

            int dirC = dir.centro + shift;
            for (int i = posTexto + len; i < dirC; i++) linhaTexto.setCharAt(i, '─');
            linhaTexto.setCharAt(dirC, '┐');

            res.linhas.add(linhaTexto.toString());

            StringBuilder linhaHastes = new StringBuilder(repetir(" ", res.largura));
            linhaHastes.setCharAt(dirC, '│');
            res.linhas.add(linhaHastes.toString());

            for (int i = 0; i < dir.linhas.size(); i++) {
                res.linhas.add(repetir(" ", shift) + padRight(dir.linhas.get(i), dir.largura));
            }
            return res;
        }
    }

    private void inserir(StringBuilder sb, String texto, int pos) {
        for (int i = 0; i < texto.length(); i++) {
            sb.setCharAt(pos + i, texto.charAt(i));
        }
    }

    private String repetir(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    private String padRight(String s, int n) {
        if (s.length() >= n) return s;
        return s + repetir(" ", n - s.length());
    }

    public abstract int obterTotalNos();
    public abstract int obterAlturaTotal();
    public abstract void inserir(int valor);
    public abstract void remover(int valor);
}