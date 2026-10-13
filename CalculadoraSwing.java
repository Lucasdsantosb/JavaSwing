import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CalculadoraSwing {

    static double primeiroNumero = 0;
    static String operador = "";
    static boolean novoNumero = true;

    public static void main(String[] args) {

        JFrame janela = new JFrame("Calculadora");

        JTextField display = new JTextField("0");
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(display.getFont().deriveFont(28f));

        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(4, 4, 5, 5));

        JButton botao7 = new JButton("7");
        JButton botao8 = new JButton("8");
        JButton botao9 = new JButton("9");
        JButton divisao = new JButton("/");

        JButton botao4 = new JButton("4");
        JButton botao5 = new JButton("5");
        JButton botao6 = new JButton("6");
        JButton multiplicacao = new JButton("*");

        JButton botao1 = new JButton("1");
        JButton botao2 = new JButton("2");
        JButton botao3 = new JButton("3");
        JButton subtracao = new JButton("-");

        JButton botao0 = new JButton("0");
        JButton limpar = new JButton("C");
        JButton igual = new JButton("=");
        JButton adicao = new JButton("+");

        JButton[] numeros = {
            botao0, botao1, botao2, botao3, botao4,
            botao5, botao6, botao7, botao8, botao9
        };

        for (JButton botao : numeros) {
            botao.addActionListener(e -> {
                if (novoNumero) {
                    display.setText(botao.getText());
                    novoNumero = false;
                } else {
                    String textoAtual = display.getText();

                    if (textoAtual.equals("0")) {
                        display.setText(botao.getText());
                    } else {
                        display.setText(textoAtual + botao.getText());
                    }
                }
            });
        }

        adicao.addActionListener(e -> {
            guardarOperacao(display, "+");
        });

        subtracao.addActionListener(e -> {
            guardarOperacao(display, "-");
        });

        multiplicacao.addActionListener(e -> {
            guardarOperacao(display, "*");
        });

        divisao.addActionListener(e -> {
            guardarOperacao(display, "/");
        });

        igual.addActionListener(e -> {
            if (operador.isEmpty() || display.getText().isEmpty()) {
                return;
            }

            double segundoNumero =
                Double.parseDouble(display.getText());

            double resultado;

            switch (operador) {
                case "+":
                    resultado = primeiroNumero + segundoNumero;
                    break;

                case "-":
                    resultado = primeiroNumero - segundoNumero;
                    break;

                case "*":
                    resultado = primeiroNumero * segundoNumero;
                    break;

                case "/":
                    if (segundoNumero == 0) {
                        display.setText("Erro: divisão por zero");
                        operador = "";
                        novoNumero = true;
                        return;
                    }

                    resultado = primeiroNumero / segundoNumero;
                    break;

                default:
                    return;
            }

            if (Double.isFinite(resultado)) {
                if (resultado == Math.floor(resultado)) {
                    display.setText(String.valueOf((long) resultado));
                } else {
                    display.setText(String.valueOf(resultado));
                }
            } else {
                display.setText("Resultado inválido");
            }

            operador = "";
            novoNumero = true;
        });

        limpar.addActionListener(e -> {
            display.setText("0");
            primeiroNumero = 0;
            operador = "";
            novoNumero = true;
        });

        painel.add(botao7);
        painel.add(botao8);
        painel.add(botao9);
        painel.add(divisao);

        painel.add(botao4);
        painel.add(botao5);
        painel.add(botao6);
        painel.add(multiplicacao);

        painel.add(botao1);
        painel.add(botao2);
        painel.add(botao3);
        painel.add(subtracao);

        painel.add(botao0);
        painel.add(limpar);
        painel.add(igual);
        painel.add(adicao);

        janela.setLayout(new BorderLayout(5, 5));
        janela.add(display, BorderLayout.NORTH);
        janela.add(painel, BorderLayout.CENTER);

        janela.setSize(350, 450);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }

    static void guardarOperacao(JTextField display, String novoOperador) {
        if (display.getText().isEmpty()
                || display.getText().equals("Erro: divisão por zero")
                || display.getText().equals("Resultado inválido")) {
            return;
        }

        primeiroNumero = Double.parseDouble(display.getText());
        operador = novoOperador;
        novoNumero = true;
    }
}