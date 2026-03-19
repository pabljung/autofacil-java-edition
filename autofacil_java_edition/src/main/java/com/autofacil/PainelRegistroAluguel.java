package com.autofacil;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class PainelRegistroAluguel extends JPanel {

    private JFormattedTextField txtCpfCliente;
    private JTextField txtPlacaVeiculo; // Alterado para JTextField
    private JFormattedTextField txtDataSaida;
    private JFormattedTextField txtDataPrevistaDevolucao;
    private JButton btnRegistrarAluguel;
    private JButton btnAluguelAleatorio;
    private PainelListagemAlugueis painelListagemAlugueis; // Para atualização automática

    private static final Color AZUL_CLARO = new Color(220, 235, 245);
    private static final Color VERDE_PASTEL = new Color(210, 245, 225);
    private static final Color CINZA_CLARO = new Color(245, 247, 250);
    private static final Color VERMELHO_BORDA = new Color(220, 60, 60);

    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PainelRegistroAluguel() {
        setLayout(new BorderLayout());
        setBackground(AZUL_CLARO);

        JLabel lblTitulo = new JLabel("Cadastro de Aluguel");
        lblTitulo.setFont(FonteUtils.carregarFonteRoboto(20f).deriveFont(Font.BOLD));
        lblTitulo.setForeground(new Color(60, 90, 140));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(AZUL_CLARO);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        txtCpfCliente = criarCampoFormatado("###.###.###-##", 16);

        // Substitui o campo de placa formatado por JTextField comum
        txtPlacaVeiculo = new JTextField(8);
        txtPlacaVeiculo.setFont(FonteUtils.carregarFonteRoboto(14f));
        txtPlacaVeiculo.setMaximumSize(new Dimension(200, 28));
        txtPlacaVeiculo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));

        txtDataSaida = criarCampoFormatado("##/##/####", 10);
        txtDataPrevistaDevolucao = criarCampoFormatado("##/##/####", 10);

        formPanel.add(criarLinhaCampo("CPF Cliente:", txtCpfCliente));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(criarLinhaCampo("Placa Veículo:", txtPlacaVeiculo));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(criarLinhaCampo("Data Saída (dd/MM/yyyy):", txtDataSaida));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(criarLinhaCampo("Data Prevista Devolução (dd/MM/yyyy):", txtDataPrevistaDevolucao));
        formPanel.add(Box.createVerticalStrut(20));

        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new BoxLayout(botoesPanel, BoxLayout.X_AXIS));
        botoesPanel.setBackground(AZUL_CLARO);

        btnRegistrarAluguel = criarBotaoEstilizado("Registrar Aluguel", VERDE_PASTEL);
        btnAluguelAleatorio = criarBotaoEstilizado("Registrar Aluguel Aleatório", CINZA_CLARO);

        botoesPanel.add(btnRegistrarAluguel);
        botoesPanel.add(Box.createHorizontalStrut(20));
        botoesPanel.add(btnAluguelAleatorio);

        formPanel.add(botoesPanel);

        add(lblTitulo, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        btnRegistrarAluguel.addActionListener(e -> registrarAluguel());
        btnAluguelAleatorio.addActionListener(e -> registrarAluguelAleatorio());

        aplicarFonteRecursivo(this, FonteUtils.carregarFonteRoboto(14f));
    }

    public void setPainelListagemAlugueis(PainelListagemAlugueis painel) {
        this.painelListagemAlugueis = painel;
    }

    private JPanel criarLinhaCampo(String label, JComponent campo) {
        JPanel linha = new JPanel();
        linha.setLayout(new BoxLayout(linha, BoxLayout.X_AXIS));
        linha.setBackground(AZUL_CLARO);
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(180, 28));
        lbl.setFont(FonteUtils.carregarFonteRoboto(14f));
        linha.add(lbl);
        linha.add(Box.createHorizontalStrut(10));
        linha.add(campo);
        return linha;
    }

    private JFormattedTextField criarCampoFormatado(String mascara, int colunas) {
        try {
            javax.swing.text.MaskFormatter formatter = new javax.swing.text.MaskFormatter(mascara);
            formatter.setPlaceholderCharacter('_');
            JFormattedTextField campo = new JFormattedTextField(formatter);
            campo.setColumns(colunas);
            campo.setFont(FonteUtils.carregarFonteRoboto(14f));
            campo.setMaximumSize(new Dimension(200, 28));
            campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                    BorderFactory.createEmptyBorder(2, 6, 2, 6)
            ));
            return campo;
        } catch (Exception e) {
            return new JFormattedTextField();
        }
    }

    private JButton criarBotaoEstilizado(String texto, Color corFundo) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setBackground(corFundo);
        btn.setForeground(new Color(40, 60, 80));
        btn.setFont(FonteUtils.carregarFonteRoboto(15f));
        btn.setBorder(new RoundedBorder(10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 36));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(corFundo.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(corFundo);
            }
        });
        return btn;
    }

    private void registrarAluguel() {
        resetarBordasCampos();

        String cpfCliente = txtCpfCliente.getText().replaceAll("[^\\d]", "");
        String placaVeiculo = txtPlacaVeiculo.getText().trim().toUpperCase(); // Ajuste para JTextField
        String placaBusca = placaVeiculo.replaceAll("[^A-Za-z0-9]", "");
        String dataSaida = txtDataSaida.getText();
        String dataPrevistaDevolucao = txtDataPrevistaDevolucao.getText();

        boolean valido = true;

        // Validação CPF (deve ter 11 dígitos numéricos)
        if (cpfCliente.length() != 11) {
            marcarCampoInvalido(txtCpfCliente);
            valido = false;
        }

        // Validação placa (antiga ou Mercosul, com ou sem hífen)
        if (!validarPlaca(placaVeiculo)) {
            marcarCampoInvalido(txtPlacaVeiculo);
            JOptionPane.showMessageDialog(this, "Placa inválida! Use o formato ABC-1234 (antigo) ou ABC1D23 (Mercosul).", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            valido = false;
        }

        // Validação datas
        if (!validarData(dataSaida)) {
            marcarCampoInvalido(txtDataSaida);
            valido = false;
        }
        if (!validarData(dataPrevistaDevolucao)) {
            marcarCampoInvalido(txtDataPrevistaDevolucao);
            valido = false;
        }

        if (!valido) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos corretamente.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verifica se cliente existe
        Type clienteListType = new TypeToken<List<Cliente>>() {}.getType();
        List<Cliente> clientes = GerenciadorArquivos.carregarDados("clientes.json", clienteListType);
        Cliente clienteEncontrado = clientes != null ? clientes.stream()
                .filter(c -> c.getCpf().replaceAll("[^\\d]", "").equals(cpfCliente))
                .findFirst()
                .orElse(null) : null;

        if (clienteEncontrado == null) {
            marcarCampoInvalido(txtCpfCliente);
            JOptionPane.showMessageDialog(this, "Cliente não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verifica se veículo disponível existe (aceita ambos formatos)
        Type veiculoListType = new TypeToken<List<Veiculo>>() {}.getType();
        List<Veiculo> veiculos = GerenciadorArquivos.carregarDados("veiculos.json", veiculoListType);
        Veiculo veiculoEncontrado = veiculos != null ? veiculos.stream()
                .filter(v -> normalizarPlaca(v.getPlaca()).equals(placaBusca) && v.getStatus().equals("Disponível"))
                .findFirst()
                .orElse(null) : null;

        if (veiculoEncontrado == null) {
            marcarCampoInvalido(txtPlacaVeiculo);
            JOptionPane.showMessageDialog(this, "Veículo não encontrado ou não disponível!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Registra aluguel
        Type aluguelListType = new TypeToken<List<Aluguel>>() {}.getType();
        List<Aluguel> alugueis = GerenciadorArquivos.carregarDados("/alugueis.json", aluguelListType);
        if (alugueis == null) alugueis = new ArrayList<>();
        int idAluguel = alugueis.size() + 1;

        Aluguel novoAluguel = new Aluguel(idAluguel, veiculoEncontrado, clienteEncontrado, dataSaida, dataPrevistaDevolucao);
        alugueis.add(novoAluguel);
        GerenciadorArquivos.salvarDados(alugueis, "alugueis.json");

        // Atualiza status do veículo para "Alugado"
        veiculoEncontrado.alugar();
        GerenciadorArquivos.salvarDados(veiculos, "veiculos.json");

        JOptionPane.showMessageDialog(this, "Aluguel registrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        if (painelListagemAlugueis != null) {
            painelListagemAlugueis.carregarAlugueis();
        }

        limparCampos();
    }

    private void registrarAluguelAleatorio() {
        try {
            Type clienteListType = new TypeToken<List<Cliente>>() {}.getType();
            List<Cliente> clientes = GerenciadorArquivos.carregarDados("/clientes.json", clienteListType);

            Type veiculoListType = new TypeToken<List<Veiculo>>() {}.getType();
            List<Veiculo> veiculos = GerenciadorArquivos.carregarDados("veiculos.json", veiculoListType);

            if (clientes == null || clientes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Necessário cadastrar pelo menos um cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (veiculos == null || veiculos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Necessário cadastrar pelo menos um veículo.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Veiculo> veiculosDisponiveis = new ArrayList<>();
            for (Veiculo v : veiculos) {
                if ("Disponível".equalsIgnoreCase(v.getStatus())) {
                    veiculosDisponiveis.add(v);
                }
            }

            if (veiculosDisponiveis.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum veículo disponível para aluguel.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Random random = new Random();
            Cliente cliente = clientes.get(random.nextInt(clientes.size()));
            Veiculo veiculo = veiculosDisponiveis.get(random.nextInt(veiculosDisponiveis.size()));

            LocalDate hoje = LocalDate.now();
            LocalDate dataPrevista = hoje.plusDays(random.nextInt(5) + 1);

            // Formatando corretamente os campos
            txtCpfCliente.setValue(cliente.getCpf());
            txtPlacaVeiculo.setText(veiculo.getPlaca()); // Ajuste para JTextField
            txtDataSaida.setValue(FORMATADOR_DATA.format(hoje));
            txtDataPrevistaDevolucao.setValue(FORMATADOR_DATA.format(dataPrevista));

            registrarAluguel();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao gerar aluguel aleatório: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void limparCampos() {
        txtCpfCliente.setValue(null);
        txtPlacaVeiculo.setText(""); // Ajuste para JTextField
        txtDataSaida.setValue(null);
        txtDataPrevistaDevolucao.setValue(null);
        resetarBordasCampos();
    }

    private void marcarCampoInvalido(JComponent campo) {
        campo.setBorder(BorderFactory.createLineBorder(VERMELHO_BORDA, 2));
    }

    private void resetarBordasCampos() {
        Border borderPadrao = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6));
        txtCpfCliente.setBorder(borderPadrao);
        txtPlacaVeiculo.setBorder(borderPadrao); // Ajuste para JTextField
        txtDataSaida.setBorder(borderPadrao);
        txtDataPrevistaDevolucao.setBorder(borderPadrao);
    }

    private boolean validarData(String data) {
        try {
            LocalDate.parse(data, FORMATADOR_DATA);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Adiciona função utilitária para validar placa (antiga e Mercosul)
    private boolean validarPlaca(String placa) {
        if (placa == null) return false;
        String placaLimpa = placa.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        // Antigo: ABC1234 ou ABC-1234
        String regexAntigo = "^[A-Z]{3}-?\\d{4}$";
        // Mercosul: ABC1D23
        String regexMercosul = "^[A-Z]{3}\\d[A-Z]\\d{2}$";
        return placaLimpa.matches("^[A-Z]{3}\\d{4}$") || // ABC1234
               placa.toUpperCase().matches(regexAntigo) ||
               placaLimpa.matches(regexMercosul);
    }

    // Função para normalizar placa (remove hífen e deixa maiúsculo)
    private String normalizarPlaca(String placa) {
        return placa.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    private void aplicarFonteRecursivo(Container container, Font font) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel || comp instanceof JButton ||
                    comp instanceof JTextField || comp instanceof JTable ||
                    comp instanceof JTextArea || comp instanceof JComboBox ||
                    comp instanceof JFormattedTextField) {
                comp.setFont(font);
            }
            if (comp instanceof Container) {
                aplicarFonteRecursivo((Container) comp, font);
            }
        }
    }

    static class RoundedBorder extends AbstractBorder {
        private final int radius;
        RoundedBorder(int radius) { this.radius = radius; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
        @Override
        public Insets getBorderInsets(Component c) { return new Insets(4, 12, 4, 12); }
        @Override
        public Insets getBorderInsets(Component c, Insets insets) { return getBorderInsets(c); }
    }
}
