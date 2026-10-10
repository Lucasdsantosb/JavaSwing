import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TelaAtacadao extends JFrame {

    private static final long serialVersionUID = 1L;

    // Modelo do Produto
    static class Produto {
        String codigo;
        String nome;
        double precoVarejo;
        double precoAtacado;
        int qtdMinimaAtacado;

        public Produto(String codigo, String nome, double precoVarejo, double precoAtacado, int qtdMinimaAtacado) {
            this.codigo = codigo;
            this.nome = nome;
            this.precoVarejo = precoVarejo;
            this.precoAtacado = precoAtacado;
            this.qtdMinimaAtacado = qtdMinimaAtacado;
        }
    }

    // Modelo do Item do Carrinho
    static class ItemCarrinho {
        Produto produto;
        int quantidade;

        public ItemCarrinho(Produto produto, int quantidade) {
            this.produto = produto;
            this.quantidade = quantidade;
        }

        public boolean isAtacado() {
            return quantidade >= produto.qtdMinimaAtacado;
        }

        public double getPrecoUnitario() {
            return isAtacado() ? produto.precoAtacado : produto.precoVarejo;
        }

        public double getTotal() {
            return getPrecoUnitario() * quantidade;
        }
    }

    // Componentes da Interface
    private JTextField txtBusca;
    private JTextField txtQtd;
    private JTable tabelaCarrinho;
    private DefaultTableModel modelTabela;
    private JLabel lblTotalGeral;
    private JLabel lblTotalItens;
    private JLabel lblEconomia;

    // Dados e Estado
    private final List<Produto> catalogoProdutos = new ArrayList<>();
    private final List<ItemCarrinho> carrinho = new ArrayList<>();
    // Compatível com Java 19+ (Locale.of em vez do construtor obsoleto)
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    public TelaAtacadao() {
        carregarProdutosMock();
        configurarJanela();
        inicializarComponentes();
    }

    private void carregarProdutosMock() {
        catalogoProdutos.add(new Produto("7891001", "Arroz Tipo 1 5kg", 24.90, 21.50, 6));
        catalogoProdutos.add(new Produto("7891002", "Feijão Carioca 1kg", 8.50, 6.90, 10));
        catalogoProdutos.add(new Produto("7891003", "Óleo de Soja 900ml", 7.20, 5.99, 12));
        catalogoProdutos.add(new Produto("7891004", "Leite Integral 1L", 4.80, 3.99, 24));
        catalogoProdutos.add(new Produto("7891005", "Açúcar Refinado 1kg", 4.20, 3.50, 10));
        catalogoProdutos.add(new Produto("7891006", "Café Torrado 500g", 16.90, 14.50, 5));
    }

    private void configurarJanela() {
        setTitle("SISTEMA ATACADÃO - MÓDULO VENDAS / PDV");
        setSize(1100, 700); // Passando larg e alt diretamente
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        // 1. Cabeçalho
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // 2. Painel Central (Split de Pesquisa + Carrinho)
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setResizeWeight(0.55);

        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        mainSplit.setLeftComponent(leftPanel);
        mainSplit.setRightComponent(rightPanel);

        add(mainSplit, BorderLayout.CENTER);

        // 3. Rodapé
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(21, 101, 192));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel title = new JLabel("ATACADÃO DO POVO - CAIXA 04");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JLabel operator = new JLabel("Operador: João Silva | Status: ABERTO");
        operator.setFont(new Font("SansSerif", Font.PLAIN, 14));
        operator.setForeground(Color.WHITE);

        panel.add(title, BorderLayout.WEST);
        panel.add(operator, BorderLayout.EAST);
        return panel;
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(" Lançamento de Itens "));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCodigo = new JLabel("Código de Barras / Nome:");
        lblCodigo.setFont(new Font("SansSerif", Font.BOLD, 12));
        txtBusca = new JTextField();
        txtBusca.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel lblQtd = new JLabel("Quantidade:");
        lblQtd.setFont(new Font("SansSerif", Font.BOLD, 12));
        txtQtd = new JTextField("1");
        txtQtd.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton btnAdicionar = new JButton("Adicionar (Enter)");
        btnAdicionar.setBackground(new Color(46, 125, 50));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("SansSerif", Font.BOLD, 12));

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.7;
        formPanel.add(lblCodigo, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(lblQtd, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(txtBusca, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(txtQtd, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        formPanel.add(btnAdicionar, gbc);

        String[] colsCat = {"Cód", "Descrição", "P. Varejo", "P. Atacado", "Qtd Min. Atacado"};
        DefaultTableModel catModel = new DefaultTableModel(colsCat, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Produto p : catalogoProdutos) {
            catModel.addRow(new Object[]{
                p.codigo, p.nome,
                currencyFormatter.format(p.precoVarejo),
                currencyFormatter.format(p.precoAtacado),
                p.qtdMinimaAtacado + " un"
            });
        }

        JTable tabCatalogo = new JTable(catModel);
        JScrollPane scrollCat = new JScrollPane(tabCatalogo);
        scrollCat.setBorder(BorderFactory.createTitledBorder(" Tabela de Preços (Varejo x Atacado) "));

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollCat, BorderLayout.CENTER);

        btnAdicionar.addActionListener(e -> adicionarItem());
        txtBusca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    adicionarItem();
                }
            }
        });

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(" Cupom Fiscal / Carrinho "));

        String[] colunas = {"Item", "Produto", "Qtd", "Tipo", "Unitário", "Total"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabelaCarrinho = new JTable(modelTabela);
        tabelaCarrinho.setRowHeight(24);
        tabelaCarrinho.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(tabelaCarrinho);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel resumoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        resumoPanel.setBackground(new Color(238, 238, 238));
        resumoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTotalItens = new JLabel("Total de Itens: 0");
        lblEconomia = new JLabel("Economia Atacado: R$ 0,00");
        lblEconomia.setForeground(new Color(46, 125, 50));

        JLabel lblTotalLabel = new JLabel("TOTAL GERAL:");
        lblTotalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        lblTotalGeral = new JLabel("R$ 0,00");
        lblTotalGeral.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTotalGeral.setForeground(new Color(198, 40, 40));

        resumoPanel.add(lblTotalItens);
        resumoPanel.add(lblEconomia);
        resumoPanel.add(lblTotalLabel);
        resumoPanel.add(lblTotalGeral);

        panel.add(resumoPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panel.setBackground(new Color(220, 224, 230));

        JButton btnRemover = new JButton("Remover Item Selecionado");
        JButton btnCancelar = new JButton("Cancelar Venda (F4)");
        JButton btnFinalizar = new JButton("Finalizar Venda (F10)");

        btnFinalizar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnFinalizar.setBackground(new Color(46, 125, 50));
        btnFinalizar.setForeground(Color.WHITE);

        panel.add(btnRemover);
        panel.add(btnCancelar);
        panel.add(btnFinalizar);

        btnRemover.addActionListener(e -> removerItemSelecionado());
        btnCancelar.addActionListener(e -> limparVenda());
        btnFinalizar.addActionListener(e -> finalizarVenda());

        return panel;
    }

    private void adicionarItem() {
        String busca = txtBusca.getText().trim();
        int quantidade;

        try {
            quantidade = Integer.parseInt(txtQtd.getText().trim());
            if (quantidade <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Produto produtoEncontrado = null;
        for (Produto p : catalogoProdutos) {
            if (p.codigo.equalsIgnoreCase(busca) || p.nome.toLowerCase().contains(busca.toLowerCase())) {
                produtoEncontrado = p;
                break;
            }
        }

        if (produtoEncontrado == null) {
            JOptionPane.showMessageDialog(this, "Produto não encontrado!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        carrinho.add(new ItemCarrinho(produtoEncontrado, quantidade));
        atualizarTabela();

        txtBusca.setText("");
        txtQtd.setText("1");
        txtBusca.requestFocus();
    }

    private void removerItemSelecionado() {
        int selectedRow = tabelaCarrinho.getSelectedRow();
        if (selectedRow >= 0) {
            carrinho.remove(selectedRow);
            atualizarTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item para remover.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limparVenda() {
        if (!carrinho.isEmpty()) {
            int op = JOptionPane.showConfirmDialog(this, "Deseja realmente cancelar a venda atual?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                carrinho.clear();
                atualizarTabela();
            }
        }
    }

    private void finalizarVenda() {
        if (carrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O carrinho está vazio!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = carrinho.stream().mapToDouble(ItemCarrinho::getTotal).sum();
        JOptionPane.showMessageDialog(this, 
            "Venda Finalizada com Sucesso!\nTotal: " + currencyFormatter.format(total), 
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        carrinho.clear();
        atualizarTabela();
    }

    private void atualizarTabela() {
        modelTabela.setRowCount(0);

        double totalGeral = 0.0;
        double totalSemDesconto = 0.0;
        int totalVolumeItens = 0;

        for (int i = 0; i < carrinho.size(); i++) {
            ItemCarrinho item = carrinho.get(i);
            double totalItem = item.getTotal();

            totalGeral += totalItem;
            totalSemDesconto += item.produto.precoVarejo * item.quantidade;
            totalVolumeItens += item.quantidade;

            modelTabela.addRow(new Object[]{
                i + 1,
                item.produto.nome,
                item.quantidade,
                item.isAtacado() ? "ATACADO" : "VAREJO",
                currencyFormatter.format(item.getPrecoUnitario()),
                currencyFormatter.format(totalItem)
            });
        }

        double economia = totalSemDesconto - totalGeral;

        lblTotalGeral.setText(currencyFormatter.format(totalGeral));
        lblTotalItens.setText("Total de Volume: " + totalVolumeItens + " un");
        lblEconomia.setText("Economia Atacado: " + currencyFormatter.format(economia));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new TelaAtacadao().setVisible(true));
    }
}