package src;

public class No {
    public int dados;
    public No ponteiroEsquerdo;
    public No ponteiroDireito;

    public No(int valor) {
        this.dados = valor;
        this.ponteiroEsquerdo = null;
        this.ponteiroDireito = null;
    }
}