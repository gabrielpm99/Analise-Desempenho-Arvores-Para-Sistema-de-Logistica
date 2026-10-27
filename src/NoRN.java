package src;

public class NoRN extends No {
    public enum Cor { PRETO, VERMELHO }

    public Cor cor;
    public NoRN ponteiroPai;

    public NoRN(int valor) {
        super(valor);
        this.cor = Cor.VERMELHO;
        this.ponteiroPai = null;
    }
}