import src.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    private static boolean lerEValidarEntradaEmLote(Scanner scanner, int[] qtdOut, List<Integer> valores) {
        System.out.print("Quantidade de elementos: ");
        if (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.println("\n  [Aviso] Quantidade esperada invalida! Digite um numero positivo maior que zero.\n");
            return false;
        }

        int qtd = scanner.nextInt();
        scanner.nextLine();

        if (qtd <= 0) {
            System.out.println("\n  [Aviso] Quantidade esperada invalida! Digite um numero positivo maior que zero.\n");
            return false;
        }

        qtdOut[0] = qtd;

        System.out.print("Digite os " + qtd + " valores separados por espaco: ");
        String linha = scanner.nextLine().trim();

        if (linha.isEmpty()) {
            System.out.println("\n  [Aviso] Nenhum valor foi digitado. Operacao cancelada.\n");
            return false;
        }

        String[] tokens = linha.split("\\s+");
        for (String token : tokens) {
            try {
                valores.add(Integer.parseInt(token));
            } catch (NumberFormatException e) {
                System.out.println("\n  [Aviso] Entrada invalida! Certifique-se de digitar apenas numeros inteiros.\n");
                return false;
            }
        }

        if (valores.size() < qtd) {
            System.out.println("\n  [Aviso] Voce forneceu MENOS elementos (" + valores.size() + ") do que o esperado (" + qtd + ")!\n");
            return false;
        }

        if (valores.size() > qtd) {
            System.out.println("\n  [Aviso] Voce forneceu MAIS elementos (" + valores.size() + ") do que o esperado (" + qtd + ")!\n");
            return false;
        }

        return true;
    }

    private static void imprimirEstatisticas(String operacao, AVL avl, RN rn, double tempoMsAVL, double tempoMsRN) {
        System.out.println("\n\n  [" + operacao + "]");
        System.out.printf("  %-8s%-10s%-10s%-12s%-14s%-14s%-12s\n",
                "Arvore", "Altura", "FB Raiz", "Rotacoes", "Comparacoes", "Trocas Cor", "Tempo (ms)");
        System.out.println("  --------------------------------------------------------------------");

        String tempoAVLStr = (tempoMsAVL >= 0) ? String.format("%.3f", tempoMsAVL) : "-";
        System.out.printf("  %-8s%-10d%-10d%-12d%-14d%-14s%-12s\n",
                "AVL", avl.obterAlturaTotal(), avl.obterFatorBalanceamentoRaiz(),
                avl.totalRotacoes, avl.totalComparacoes, "N/A", tempoAVLStr);

        String tempoRNStr = (tempoMsRN >= 0) ? String.format("%.3f", tempoMsRN) : "-";
        System.out.printf("  %-8s%-10d%-10d%-12d%-14d%-14d%-12s\n\n",
                "RN", rn.obterAlturaTotal(), rn.obterFatorBalanceamentoRaiz(),
                rn.totalRotacoes, rn.totalComparacoes, rn.totalTrocasCores, tempoRNStr);

        avl.resetarContadores();
        rn.resetarContadores();
    }

    private static void executarTesteRandomico(AVL avl, RN rn, int qtdInsercoes, int qtdBuscas, int qtdRemocoes) {
        Random rand = new Random();
        List<Integer> pacotes = new ArrayList<>(qtdInsercoes);

        for (int i = 0; i < qtdInsercoes; i++) {
            pacotes.add(rand.nextInt(qtdInsercoes * 10));
        }

        long inicioAVL_Ins = System.nanoTime();
        for (int pacote : pacotes) {
            avl.inserir(pacote);
        }
        long fimAVL_Ins = System.nanoTime();
        double tempoAVL_Ins = (fimAVL_Ins - inicioAVL_Ins) / 1_000_000.0;

        long inicioRN_Ins = System.nanoTime();
        for (int pacote : pacotes) {
            rn.inserir(pacote);
        }
        long fimRN_Ins = System.nanoTime();
        double tempoRN_Ins = (fimRN_Ins - inicioRN_Ins) / 1_000_000.0;

        imprimirEstatisticas("Insercao de " + qtdInsercoes + " elementos randomicos", avl, rn, tempoAVL_Ins, tempoRN_Ins);

        if (qtdBuscas > 0) {
            long inicioAVL_Bus = System.nanoTime();
            for (int i = 0; i < qtdBuscas; i++) {
                avl.buscar(pacotes.get(i % qtdInsercoes));
            }
            long fimAVL_Bus = System.nanoTime();
            double tempoAVL_Bus = (fimAVL_Bus - inicioAVL_Bus) / 1_000_000.0;

            long inicioRN_Bus = System.nanoTime();
            for (int i = 0; i < qtdBuscas; i++) {
                rn.buscar(pacotes.get(i % qtdInsercoes));
            }
            long fimRN_Bus = System.nanoTime();
            double tempoRN_Bus = (fimRN_Bus - inicioRN_Bus) / 1_000_000.0;

            imprimirEstatisticas("Busca de " + qtdBuscas + " elementos randomicos", avl, rn, tempoAVL_Bus, tempoRN_Bus);
        }

        int limiteRemocao = Math.min(qtdRemocoes, qtdInsercoes);

        if (limiteRemocao > 0) {
            long inicioAVL_Rem = System.nanoTime();
            for (int i = 0; i < limiteRemocao; i++) {
                avl.remover(pacotes.get(i));
            }
            long fimAVL_Rem = System.nanoTime();
            double tempoAVL_Rem = (fimAVL_Rem - inicioAVL_Rem) / 1_000_000.0;

            long inicioRN_Rem = System.nanoTime();
            for (int i = 0; i < limiteRemocao; i++) {
                rn.remover(pacotes.get(i));
            }
            long fimRN_Rem = System.nanoTime();
            double tempoRN_Rem = (fimRN_Rem - inicioRN_Rem) / 1_000_000.0;

            imprimirEstatisticas("Remocao de " + limiteRemocao + " elementos randomicos", avl, rn, tempoAVL_Rem, tempoRN_Rem);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AVL arvoreAVL = new AVL();
        RN arvoreRN = new RN();

        int opcao = -1;

        while (opcao != 0) {
            System.out.println("1. Inserir elemento(s)");
            System.out.println("2. Remover elemento(s)");
            System.out.println("3. Buscar elemento");
            System.out.println("4. Percurso em ordem");
            System.out.println("5. Imprimir estrutura da arvore");
            System.out.println("6. Obter altura total");
            System.out.println("7. Obter fator de balanceamento da raiz");
            System.out.println("8. Executar Teste com numeros randomicos");
            System.out.println("9. Resetar arvores");
            System.out.println("0. Sair");
            System.out.print("Opcao: ");

            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                System.out.println("\n  [Aviso] Opcao invalida. Digite apenas numeros referentes ao menu.\n");
                continue;
            }

            opcao = scanner.nextInt();

            if (opcao == 1) {
                int[] qtd = new int[1];
                List<Integer> valores = new ArrayList<>();

                if (!lerEValidarEntradaEmLote(scanner, qtd, valores)) continue;

                for (int val : valores) {
                    arvoreAVL.inserir(val);
                    arvoreRN.inserir(val);
                }
                imprimirEstatisticas("Insercao de " + qtd[0] + " elemento(s)", arvoreAVL, arvoreRN, -1, -1);
            } 
            else if (opcao == 2) {
                if (arvoreAVL.obterAlturaTotal() == 0) {
                    System.out.println("\n  [Aviso] As arvores estao vazias! Nao ha elementos para remover.\n");
                    continue;
                }

                int[] qtd = new int[1];
                List<Integer> valores = new ArrayList<>();

                if (!lerEValidarEntradaEmLote(scanner, qtd, valores)) continue;

                for (int val : valores) {
                    arvoreAVL.remover(val);
                    arvoreRN.remover(val);
                }
                imprimirEstatisticas("Remocao de " + qtd[0] + " elemento(s)", arvoreAVL, arvoreRN, -1, -1);
            } 
            else if (opcao == 3) {
                if (arvoreAVL.obterAlturaTotal() == 0) {
                    System.out.println("\n  [Aviso] As arvores estao vazias! Nao ha elementos para buscar.\n");
                    continue;
                }

                System.out.print("Valor a buscar: ");
                if (!scanner.hasNextInt()) {
                    scanner.nextLine();
                    System.out.println("\n  [Aviso] Valor invalido. Digite um numero inteiro para a busca.\n");
                    continue;
                }

                int valor = scanner.nextInt();

                long inicioAVL = System.nanoTime();
                boolean resAVL = arvoreAVL.buscar(valor);
                long fimAVL = System.nanoTime();
                double tempoAVL = (fimAVL - inicioAVL) / 1_000_000.0;

                long inicioRN = System.nanoTime();
                boolean resRN = arvoreRN.buscar(valor);
                long fimRN = System.nanoTime();
                double tempoRN = (fimRN - inicioRN) / 1_000_000.0;

                System.out.print("\n\n  Busca (" + valor + ") -> AVL: ");
                if (resAVL) {
                    System.out.printf("Encontrado (%.4f ms)", tempoAVL);
                } else {
                    System.out.print("Nao Encontrado");
                }

                System.out.print(" | RN: ");
                if (resRN) {
                    System.out.printf("Encontrado (%.4f ms)", tempoRN);
                } else {
                    System.out.print("Nao Encontrado");
                }
                System.out.println("\n");

                arvoreAVL.resetarContadores();
                arvoreRN.resetarContadores();
            } 
            else if (opcao == 4) {
                if (arvoreAVL.obterAlturaTotal() == 0) {
                    System.out.println("\n  [Aviso] As arvores estao vazias.\n");
                    continue;
                }
                
                int maxNos = Math.max(arvoreAVL.obterTotalNos(), arvoreRN.obterTotalNos());
                if (maxNos > 10) {
                    System.out.print("\n  [Aviso] As arvores possuem mais de 10 nos (" + maxNos + "). Deseja salvar o percurso em um arquivo .txt? (S/N): ");
                    String resp = scanner.next();
                    if (resp.equalsIgnoreCase("S")) {
                        try (PrintWriter out = new PrintWriter(new FileWriter("percurso.txt"))) {
                            out.print("Percurso em ordem (AVL): ");
                            arvoreAVL.imprimirOrdem(out);
                            out.print("\nPercurso em ordem (RN):  ");
                            arvoreRN.imprimirOrdem(out);
                            System.out.println("  [Sucesso] Percurso salvo no arquivo 'percurso.txt'.\n");
                        } catch (IOException e) {
                            System.out.println("  [Erro] Falha ao salvar arquivo.\n");
                        }
                    } else {
                        System.out.println("  [Aviso] Operacao de impressao cancelada.\n");
                    }
                } else {
                    PrintWriter consoleOut = new PrintWriter(System.out, true);
                    System.out.print("\n  Percurso em ordem (AVL): ");
                    arvoreAVL.imprimirOrdem(consoleOut);
                    System.out.print("\n  Percurso em ordem (RN):  ");
                    arvoreRN.imprimirOrdem(consoleOut);
                    System.out.println("\n");
                }
            } 
            else if (opcao == 5) {
                if (arvoreAVL.obterAlturaTotal() == 0) {
                    System.out.println("\n  [Aviso] As arvores estao vazias.\n");
                    continue;
                }
                
                int maxNos = Math.max(arvoreAVL.obterTotalNos(), arvoreRN.obterTotalNos());
                if (maxNos > 10) {
                    System.out.print("\n  [Aviso] As arvores possuem mais de 10 nos (" + maxNos + "). Deseja salvar a estrutura visual em um arquivo .txt? (S/N): ");
                    String resp = scanner.next();
                    if (resp.equalsIgnoreCase("S")) {
                        try (PrintWriter out = new PrintWriter(new FileWriter("estrutura_arvore.txt"))) {
                            out.println("ARVORE AVL:\n");
                            arvoreAVL.imprimirArvore(out);
                            out.println("\n\nARVORE RUBRO-NEGRA:\n");
                            arvoreRN.imprimirArvore(out);
                            System.out.println("  [Sucesso] Estrutura salva no arquivo 'estrutura_arvore.txt'.\n");
                        } catch (IOException e) {
                            System.out.println("  [Erro] Falha ao salvar arquivo.\n");
                        }
                    } else {
                        System.out.println("  [Aviso] Operacao de impressao cancelada.\n");
                    }
                } else {
                    PrintWriter consoleOut = new PrintWriter(System.out, true);
                    consoleOut.println("\n\n  ARVORE AVL:");
                    arvoreAVL.imprimirArvore(consoleOut);
                    consoleOut.println("\n  ARVORE RUBRO-NEGRA:");
                    arvoreRN.imprimirArvore(consoleOut);
                    consoleOut.println();
                }
            } 
            else if (opcao == 6) {
                System.out.println("\n  Altura Total -> AVL: " + arvoreAVL.obterAlturaTotal()
                        + " | RN: " + arvoreRN.obterAlturaTotal() + "\n");
            } 
            else if (opcao == 7) {
                System.out.println("\n  Fator de Balanceamento da Raiz -> AVL: " + arvoreAVL.obterFatorBalanceamentoRaiz()
                        + " | RN: " + arvoreRN.obterFatorBalanceamentoRaiz() + "\n");
            } 
            else if (opcao == 8) {
                System.out.print("Quantidade a inserir: ");
                if (!scanner.hasNextInt()) {
                    scanner.nextLine();
                    System.out.println("\n  [Aviso] Quantidade de insercoes invalida.\n");
                    continue;
                }
                int qtdIns = scanner.nextInt();

                if (qtdIns <= 0) {
                    System.out.println("\n  [Aviso] Quantidade de insercoes invalida.\n");
                    continue;
                }

                System.out.print("Quantidade a buscar: ");
                if (!scanner.hasNextInt()) {
                    scanner.nextLine();
                    System.out.println("\n  [Aviso] Quantidade de buscas invalida.\n");
                    continue;
                }
                int qtdBus = scanner.nextInt();

                if (qtdBus < 0) {
                    System.out.println("\n  [Aviso] Quantidade de buscas invalida.\n");
                    continue;
                }

                System.out.print("Quantidade a remover: ");
                if (!scanner.hasNextInt()) {
                    scanner.nextLine();
                    System.out.println("\n  [Aviso] Quantidade de remocoes invalida.\n");
                    continue;
                }
                int qtdRem = scanner.nextInt();

                if (qtdRem < 0) {
                    System.out.println("\n  [Aviso] Quantidade de remocoes invalida.\n");
                    continue;
                }

                arvoreAVL = new AVL();
                arvoreRN = new RN();
                executarTesteRandomico(arvoreAVL, arvoreRN, qtdIns, qtdBus, qtdRem);
            } 
            else if (opcao == 9) {
                arvoreAVL = new AVL();
                arvoreRN = new RN();
                System.out.println("\n  [Sucesso] Arvores reinicializadas com sucesso.\n");
            }
        }

        scanner.close();
    }
}