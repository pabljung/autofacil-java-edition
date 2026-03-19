package com.autofacil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TelaLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnVisitante;  // Botão para visitante

    public TelaLogin() {
        setTitle("Login - AutoFácil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 220);  // Aumentei altura para caber o novo botão
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridLayout(4, 2, 10, 10)); // 4 linhas agora
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painel.add(new JLabel("Usuário:"));
        txtUsuario = new JTextField();
        painel.add(txtUsuario);

        painel.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        painel.add(txtSenha);

        btnEntrar = new JButton("Entrar");
        painel.add(new JLabel()); // espaço vazio
        painel.add(btnEntrar);

        btnVisitante = new JButton("Entrar como Visitante");
        painel.add(new JLabel()); // espaço vazio para alinhar
        painel.add(btnVisitante);

        add(painel);

        btnEntrar.addActionListener(this::verificarLogin);
        btnVisitante.addActionListener(this::entrarComoVisitante);
    }

    private void verificarLogin(ActionEvent e) {
        String usuario = txtUsuario.getText();
        String senha = new String(txtSenha.getPassword());

        java.lang.reflect.Type usuarioListType = new TypeToken<List<Usuario>>() {}.getType();
        List<Usuario> usuarios = GerenciadorArquivos.carregarDados("usuarios.json", usuarioListType);

        if (usuarios == null || usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum usuário encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Usuario u : usuarios) {
            if (u.getUsername().equals(usuario) && u.getSenha().equals(senha)) {
                JOptionPane.showMessageDialog(this, "Bem-vindo, " + u.getPerfil() + "!");
                registrarHistoricoLogin(u.getUsername(), u.getPerfil());
                this.dispose();

                Usuario usuarioLogado = u;
                new JanelaPrincipal(usuarioLogado).setVisible(true);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void entrarComoVisitante(ActionEvent e) {
        Usuario visitante = new Usuario("visitante", "", "Visitante"); // Usuário padrão visitante
        JOptionPane.showMessageDialog(this, "Entrando como visitante!");
        registrarHistoricoLogin(visitante.getUsername(), visitante.getPerfil());
        this.dispose();
        new JanelaPrincipal(visitante).setVisible(true);
    }

    // --- Registro de histórico de login ---
    private void registrarHistoricoLogin(String usuario, String perfil) {
        try {
            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            HistoricoLogin registro = new HistoricoLogin(usuario, perfil, dataHora);

            java.lang.reflect.Type histListType = new TypeToken<List<HistoricoLogin>>() {}.getType();

            // Trocar para carregar do sistema de arquivos real
            List<HistoricoLogin> historico = GerenciadorArquivos.carregarDadosArquivo("/historico_login.json", histListType);
            if (historico == null) historico = new ArrayList<>();

            historico.add(registro);

            GerenciadorArquivos.salvarDados(historico, "historico_login.json");

            System.out.println("Histórico atualizado: " + historico.size() + " registros.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao registrar histórico de login: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Classe interna para registro do histórico
    private static class HistoricoLogin {
        private String usuario;
        private String perfil;
        private String dataHora;

        public HistoricoLogin(String usuario, String perfil, String dataHora) {
            this.usuario = usuario;
            this.perfil = perfil;
            this.dataHora = dataHora;
        }
    }
}
