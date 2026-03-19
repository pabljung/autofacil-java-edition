package com.autofacil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXButton;

public class PainelCadastroCliente extends JXPanel {
    private JXTextField txtCpf;
    private JXTextField txtNome;
    private JXTextField txtTelefone;
    private JXTextField txtEmail;
    private JXButton btnSalvar;
    private JXButton btnGerarAleatorio;

    private final PainelListagemCliente painelListagemCliente;

    public PainelCadastroCliente(PainelListagemCliente painelListagemCliente) {
        this.painelListagemCliente = painelListagemCliente;
        setLayout(new GridBagLayout());
        setBackground(new Color(220, 235, 245));
        setPreferredSize(new Dimension(500, 500)); // Ajuste de largura/altura do painel

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Cadastro de Cliente");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblCpf = new JLabel("CPF:");
        lblCpf.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(lblCpf, gbc);
        gbc.gridx = 1;
        txtCpf = new JXTextField();
        txtCpf.setPrompt("Digite o CPF");
        txtCpf.setPreferredSize(new Dimension(250, 30));
        txtCpf.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(txtCpf, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(lblNome, gbc);
        gbc.gridx = 1;
        txtNome = new JXTextField();
        txtNome.setPrompt("Digite o nome");
        txtNome.setPreferredSize(new Dimension(250, 30));
        txtNome.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(lblTelefone, gbc);
        gbc.gridx = 1;
        txtTelefone = new JXTextField();
        txtTelefone.setPrompt("Digite o telefone");
        txtTelefone.setPreferredSize(new Dimension(250, 30));
        txtTelefone.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(txtTelefone, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(lblEmail, gbc);
        gbc.gridx = 1;
        txtEmail = new JXTextField();
        txtEmail.setPrompt("Digite o email");
        txtEmail.setPreferredSize(new Dimension(250, 30));
        txtEmail.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        btnSalvar = new JXButton("Salvar");
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(120, 180, 120));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Efeito hover
        btnSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
            Color original = btnSalvar.getBackground();
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalvar.setBackground(new Color(90, 150, 90));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalvar.setBackground(original);
            }
        });
        add(btnSalvar, gbc);


        btnSalvar.addActionListener(this::salvarCliente);

        // Botão Gerar Cliente Aleatório
        btnGerarAleatorio = new JXButton("Gerar Cliente Aleatório");
        btnGerarAleatorio.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGerarAleatorio.setBackground(new Color(100, 140, 180));
        btnGerarAleatorio.setForeground(Color.WHITE);
        btnGerarAleatorio.setFocusPainted(false);
        btnGerarAleatorio.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6; // linha abaixo do salvar
        gbc.gridwidth = 2;
        add(btnGerarAleatorio, gbc);

        btnGerarAleatorio.addActionListener(e -> gerarClienteAleatorio());
    }

    private void gerarClienteAleatorio() {
        String cpf = txtCpf.getText().trim();
        String nome = txtNome.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = nome.toLowerCase().replace(" ", ".") + "@exemplo.com";

        Cliente clienteAleatorio = new Cliente(cpf, nome, telefone, email);

        Type clienteListType = new TypeToken<List<Cliente>>() {}.getType();
        List<Cliente> clientes = GerenciadorArquivos.carregarDadosArquivo("clientes.json", clienteListType);
        if (clientes == null) clientes = new ArrayList<>();
        clientes.add(clienteAleatorio);

        GerenciadorArquivos.salvarDados(clientes, "clientes.json");

        if (painelListagemCliente != null) {
            painelListagemCliente.carregarClientes();
        }

        JOptionPane.showMessageDialog(this, "Cliente aleatório gerado e salvo:\n" +
                "Nome: " + nome + "\nCPF: " + cpf, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void salvarCliente(ActionEvent e) {
        String cpf = txtCpf.getText().trim();
        String nome = txtNome.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();

        // Verificação de campos obrigatórios
        if (cpf.isEmpty() || nome.isEmpty() || telefone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validação de CPF: apenas números e 11 dígitos
        if (!cpf.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "O CPF deve conter exatamente 11 dígitos numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validação de e-mail: deve conter "@" e "."
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "O e-mail informado é inválido. Deve conter '@' e '.'.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente novoCliente = new Cliente(cpf, nome, telefone, email);

        Type clienteListType = new TypeToken<List<Cliente>>() {}.getType();
        List<Cliente> clientes = GerenciadorArquivos.carregarDadosArquivo("clientes.json", clienteListType);
        if (clientes == null) clientes = new ArrayList<>();
        clientes.add(novoCliente);

        GerenciadorArquivos.salvarDados(clientes, "clientes.json");

        if (painelListagemCliente != null) {
            painelListagemCliente.carregarClientes();
        }

        JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private void limparCampos() {
        txtCpf.setText("");
        txtNome.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
    }
}
