package com.autofacil;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.google.gson.reflect.TypeToken;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXButton;

public class PainelCadastroVeiculo extends JXPanel {

    private JXTextField txtPlaca;
    private JXTextField txtMarca;
    private JXTextField txtModelo;
    private JFormattedTextField txtAno;
    private JXTextField txtCor;
    private JFormattedTextField txtPrecoDiaria;
    private JXButton btnSalvar;
    private JXButton btnVeiculoAleatorio;

    private final PainelListagemVeiculos painelListagemVeiculos;

    public PainelCadastroVeiculo(PainelListagemVeiculos painelListagemVeiculos) {
        this.painelListagemVeiculos = painelListagemVeiculos;

        Color azulSuave = new Color(220, 235, 245);
        Color azulDestaque = new Color(80, 140, 200);
        Color cinzaClaro = new Color(245, 247, 250);

        setBackground(cinzaClaro);
        setLayout(new BorderLayout());

        JLabel titulo = criarTitulo("Cadastro de Veículo", azulDestaque);

        JXPanel formPanel = criarPainelFormulario(cinzaClaro);

        JXPanel botoesPanel = criarPainelBotoes(cinzaClaro, azulDestaque);

        add(titulo, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(botoesPanel, BorderLayout.SOUTH);

        // Focar automaticamente no campo Placa ao abrir o painel
        SwingUtilities.invokeLater(() -> txtPlaca.requestFocusInWindow());
    }

    private JLabel criarTitulo(String texto, Color cor) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(cor);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        return titulo;
    }

    private JXPanel criarPainelFormulario(Color corFundo) {
        JXPanel formPanel = new JXPanel();
        formPanel.setBackground(corFundo);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        txtPlaca = new JXTextField();
        txtPlaca.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtPlaca.setPreferredSize(new Dimension(180, 30));
        txtPlaca.setPrompt("Digite a placa");
        txtPlaca.setToolTipText("Formato da placa: ABC-1234 ou ABC1D23");
        formPanel.add(criarLinha("Placa:", txtPlaca));

        txtMarca = new JXTextField();
        txtMarca.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtMarca.setPreferredSize(new Dimension(180, 30));
        txtMarca.setPrompt("Digite a marca");
        txtMarca.setToolTipText("Marca do veículo");
        formPanel.add(criarLinha("Marca:", txtMarca));

        txtModelo = new JXTextField();
        txtModelo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtModelo.setPreferredSize(new Dimension(180, 30));
        txtModelo.setPrompt("Digite o modelo");
        txtModelo.setToolTipText("Modelo do veículo");
        formPanel.add(criarLinha("Modelo:", txtModelo));

        txtAno = criarCampoFormatado("####");
        txtAno.setToolTipText("Ano do veículo (ex: 2020)");
        formPanel.add(criarLinha("Ano:", txtAno));

        txtCor = new JXTextField();
        txtCor.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtCor.setPreferredSize(new Dimension(180, 30));
        txtCor.setPrompt("Digite a cor");
        txtCor.setToolTipText("Cor do veículo");
        formPanel.add(criarLinha("Cor:", txtCor));

        txtPrecoDiaria = criarCampoPreco();
        txtPrecoDiaria.setToolTipText("Preço da diária (ex: 99,99)");
        formPanel.add(criarLinha("Preço da Diária:", txtPrecoDiaria));

        return formPanel;
    }

    private JXPanel criarPainelBotoes(Color corFundo, Color corDestaque) {
        JXPanel botoesPanel = new JXPanel();
        botoesPanel.setBackground(corFundo);
        botoesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnSalvar = criarBotao("Salvar Veículo", corDestaque);
        btnVeiculoAleatorio = criarBotao("Adicionar Veículo Aleatório", new Color(120, 180, 120));

        botoesPanel.add(btnSalvar);
        botoesPanel.add(btnVeiculoAleatorio);

        btnSalvar.addActionListener(e -> salvarVeiculo());
        btnVeiculoAleatorio.addActionListener(e -> adicionarVeiculoAleatorio());

        return botoesPanel;
    }

    private JXPanel criarLinha(String label, JComponent campo) {
        JXPanel linha = new JXPanel(new BorderLayout(10, 0));
        linha.setBackground(new Color(245, 247, 250));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lbl.setPreferredSize(new Dimension(120, 30));
        campo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        campo.setPreferredSize(new Dimension(180, 30));
        linha.add(lbl, BorderLayout.WEST);
        linha.add(campo, BorderLayout.CENTER);
        linha.setBorder(BorderFactory.createEmptyBorder(7, 0, 7, 0));
        return linha;
    }

    private JXButton criarBotao(String texto, Color corFundo) {
        JXButton btn = new JXButton(texto);
        btn.setFocusPainted(false);
        btn.setBackground(corFundo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(corFundo.darker(), 1, true),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(corFundo.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(corFundo); }
        });
        return btn;
    }

    private JFormattedTextField criarCampoFormatado(String mascara) {
        try {
            MaskFormatter formatter = new MaskFormatter(mascara);
            formatter.setPlaceholderCharacter('_');
            if ("UUU-####".equals(mascara)) {
                formatter.setValidCharacters("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
            }
            return new JFormattedTextField(formatter);
        } catch (ParseException e) {
            return new JFormattedTextField();
        }
    }

    private JFormattedTextField criarCampoPreco() {
        NumberFormat format = DecimalFormat.getNumberInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        JFormattedTextField campo = new JFormattedTextField(format);
        campo.setColumns(10);
        campo.setValue(0.00);
        return campo;
    }

    // Função utilitária para validar placa (antiga e Mercosul)
    private boolean validarPlaca(String placa) {
        if (placa == null) return false;
        String placaLimpa = placa.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        // Regex para formato antigo: ABC1234 ou ABC-1234
        String regexAntigo = "^[A-Z]{3}-?\\d{4}$";
        // Regex para formato Mercosul: ABC1D23
        String regexMercosul = "^[A-Z]{3}\\d[A-Z]\\d{2}$";
        // Aceita ABC1234, ABC-1234, ABC1D23
        return placa.toUpperCase().matches(regexAntigo) || placaLimpa.matches(regexMercosul);
    }

    private boolean validarCampos() {
        boolean valido = true;
        String placa = txtPlaca.getText().replace("_", "").trim();
        String marca = capitalizarTexto(txtMarca.getText());
        String modelo = capitalizarTexto(txtModelo.getText());
        String anoStr = txtAno.getText().replace("_", "").trim();
        String cor = capitalizarTexto(txtCor.getText());
        Object precoObj = txtPrecoDiaria.getValue();

        // Atualiza os campos visualmente já capitalizados
        txtMarca.setText(marca);
        txtModelo.setText(modelo);
        txtCor.setText(cor);

        // Reset bordas
        txtPlaca.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        txtMarca.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        txtModelo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        txtAno.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        txtCor.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        txtPrecoDiaria.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // Checagem de campos obrigatórios e borda vermelha
        if (placa.isEmpty()) {
            txtPlaca.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            valido = false;
        }
        if (marca.isEmpty()) {
            txtMarca.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            valido = false;
        }
        if (modelo.isEmpty()) {
            txtModelo.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            valido = false;
        }
        if (anoStr.isEmpty()) {
            txtAno.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            valido = false;
        }
        if (cor.isEmpty()) {
            txtCor.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            valido = false;
        }
        if (precoObj == null) {
            txtPrecoDiaria.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            valido = false;
        }

        if (!valido) {
            mostrarMensagemErro("Todos os campos (Placa, Marca, Modelo, Ano, Cor e Preço da Diária) devem ser preenchidos corretamente.");
        }

        int ano;
        try {
            ano = Integer.parseInt(anoStr);
            int anoAtual = java.time.Year.now().getValue();
            if (ano < 1900 || ano > anoAtual) {
                txtAno.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                mostrarMensagemErro("Ano deve ser um valor entre 1900 e " + anoAtual + ".");
                valido = false;
            }
        } catch (NumberFormatException e) {
            txtAno.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            mostrarMensagemErro("Ano inválido!");
            valido = false;
        }

        double precoDiaria;
        try {
            precoDiaria = ((Number) precoObj).doubleValue();
            if (precoDiaria <= 0) {
                txtPrecoDiaria.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                mostrarMensagemErro("Preço da diária deve ser maior que zero.");
                valido = false;
            } else {
                // Formata o campo para sempre mostrar 2 casas decimais
                txtPrecoDiaria.setValue(Double.valueOf(String.format("%.2f", precoDiaria).replace(",", ".")));
            }
        } catch (Exception e) {
            txtPrecoDiaria.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            mostrarMensagemErro("Preço inválido!");
            valido = false;
        }

        String placaLimpa = placa.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        if (!validarPlaca(placa)) {
            txtPlaca.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            JOptionPane.showMessageDialog(this, "Placa inválida! Use o formato ABC-1234 (antigo) ou ABC1D23 (Mercosul).", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            valido = false;
        } else if (!placa.isEmpty()) {
            txtPlaca.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        }

        return valido;
    }

    private void salvarVeiculo() {
        if (!validarCampos()) return;

        // Exibir diálogo de confirmação antes de salvar o veículo
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente salvar este veículo?",
            "Confirmação",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String placa = txtPlaca.getText().replace("_", "").trim();
        String marca = capitalizarTexto(txtMarca.getText());
        String modelo = capitalizarTexto(txtModelo.getText());
        String anoStr = txtAno.getText().replace("_", "").trim();
        String cor = capitalizarTexto(txtCor.getText());
        Object precoObj = txtPrecoDiaria.getValue();

        // Atualiza os campos visualmente já capitalizados
        txtMarca.setText(marca);
        txtModelo.setText(modelo);
        txtCor.setText(cor);

        int ano;
        try {
            ano = Integer.parseInt(anoStr);
        } catch (NumberFormatException e) {
            mostrarMensagemErro("Ano inválido!");
            return;
        }

        double precoDiaria;
        try {
            precoDiaria = ((Number) precoObj).doubleValue();
            precoDiaria = Double.parseDouble(String.format("%.2f", precoDiaria).replace(",", "."));
            txtPrecoDiaria.setValue(precoDiaria);
        } catch (Exception e) {
            mostrarMensagemErro("Preço inválido!");
            return;
        }

        placa = formatarPlaca(placa);
        // marca, modelo e cor já estão capitalizados

        Veiculo novoVeiculo = new Veiculo(placa, marca, modelo, ano, cor, precoDiaria);

        Type veiculoListType = new TypeToken<List<Veiculo>>() {}.getType();
        List<Veiculo> veiculos = GerenciadorArquivos.carregarDados("/veiculos.json", veiculoListType);
        if (veiculos == null) veiculos = new ArrayList<>();

        veiculos.add(novoVeiculo);
        GerenciadorArquivos.salvarDados(veiculos, "veiculos.json");

        // Atualizar a listagem de veículos automaticamente após salvar
        if (painelListagemVeiculos != null) painelListagemVeiculos.carregarVeiculos();

        mostrarMensagemSucesso("Veículo salvo com sucesso!");

        // Limpar os campos do formulário após salvar com sucesso
        limparCampos();

        // Focar novamente no campo Placa
        SwingUtilities.invokeLater(() -> txtPlaca.requestFocusInWindow());
    }

    private void adicionarVeiculoAleatorio() {
        String[] placas = {"ABC-1234", "DEF-5678", "GHI-9012", "JKL-3456", "MNO-7890", "PQR-2345", "STU-6789", "VWX-0123"};
        String[] marcas = {"Toyota", "Honda", "Volkswagen", "Ford", "Chevrolet", "Fiat", "Hyundai", "Renault"};
        String[] modelos = {"Corolla", "Civic", "Jetta", "Focus", "Onix", "Uno", "HB20", "Clio"};
        String[] cores = {"Branco", "Preto", "Prata", "Vermelho", "Azul", "Cinza", "Verde", "Amarelo"};

        Random rand = new Random();

        txtPlaca.setText(placas[rand.nextInt(placas.length)]);
        txtMarca.setText(marcas[rand.nextInt(marcas.length)]);
        txtModelo.setText(modelos[rand.nextInt(modelos.length)]);
        txtAno.setText(String.valueOf(rand.nextInt(24) + 2000));
        txtCor.setText(cores[rand.nextInt(cores.length)]);
        txtPrecoDiaria.setValue(50 + rand.nextInt(151));

        salvarVeiculo();
    }

    private String capitalizarTexto(String texto) {
        texto = texto.trim().toLowerCase().replaceAll("\\s+", " ");
        String[] partes = texto.split(" ");
        StringBuilder resultado = new StringBuilder();
        for (String parte : partes) {
            if (!parte.isEmpty()) {
                resultado.append(Character.toUpperCase(parte.charAt(0)))
                        .append(parte.substring(1))
                        .append(" ");
            }
        }
        return resultado.toString().trim();
    }

    private String formatarPlaca(String placa) {
        return placa.trim().toUpperCase().replaceAll("[_\\s]", "");
    }

    private void mostrarMensagemErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensagemSucesso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limparCampos() {
        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAno.setValue(null);
        txtCor.setText("");
        txtPrecoDiaria.setValue(0.00);
    }

}
