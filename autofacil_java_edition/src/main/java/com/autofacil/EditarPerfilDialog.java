package com.autofacil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class EditarPerfilDialog extends JDialog {
    private JTextField txtNovoNome;
    private JTextField txtNovoEmail;
    private JPasswordField txtSenhaAtual;
    private JPasswordField txtNovaSenha;
    private JPasswordField txtConfirmarSenha;
    private JButton btnSalvar;
    private Usuario usuarioLogado;

    public EditarPerfilDialog(JFrame parent, Usuario usuarioLogado) {
        super(parent, "Editar Perfil", true);
        this.usuarioLogado = usuarioLogado;

        setLayout(new BorderLayout(10, 10));
        JPanel painelCampos = new JPanel(new GridLayout(5, 2, 10, 10));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        painelCampos.add(new JLabel("Nome de usuário:"));
        txtNovoNome = new JTextField(usuarioLogado.getUsername());
        painelCampos.add(txtNovoNome);

        painelCampos.add(new JLabel("Email (opcional):"));
        txtNovoEmail = new JTextField(usuarioLogado.getEmail() != null ? usuarioLogado.getEmail() : "");
        painelCampos.add(txtNovoEmail);

        painelCampos.add(new JLabel("Senha atual (obrigatória):"));
        txtSenhaAtual = new JPasswordField();
        painelCampos.add(txtSenhaAtual);

        painelCampos.add(new JLabel("Nova senha (opcional):"));
        txtNovaSenha = new JPasswordField();
        painelCampos.add(txtNovaSenha);

        painelCampos.add(new JLabel("Confirmar nova senha:"));
        txtConfirmarSenha = new JPasswordField();
        painelCampos.add(txtConfirmarSenha);

        add(painelCampos, BorderLayout.CENTER);

        btnSalvar = new JButton("Salvar Alterações");
        JPanel painelBtn = new JPanel();
        painelBtn.add(btnSalvar);
        add(painelBtn, BorderLayout.SOUTH);

        btnSalvar.addActionListener(this::salvarAlteracoes);

        setSize(400, 320);
        setLocationRelativeTo(parent);
    }

    private void salvarAlteracoes(ActionEvent e) {
        String novoNome = txtNovoNome.getText().trim();
        String novoEmail = txtNovoEmail.getText().trim();
        String senhaAtual = new String(txtSenhaAtual.getPassword());
        String novaSenha = new String(txtNovaSenha.getPassword());
        String confirmarSenha = new String(txtConfirmarSenha.getPassword());

        // Validação básica
        if (novoNome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome de usuário não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (senhaAtual.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Você deve informar a senha atual para salvar as alterações.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!usuarioLogado.getSenha().equals(senhaAtual)) {
            JOptionPane.showMessageDialog(this, "Senha atual incorreta.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Se o usuário digitou nova senha, valida confirmação
        if (!novaSenha.isEmpty() || !confirmarSenha.isEmpty()) {
            if (!novaSenha.equals(confirmarSenha)) {
                JOptionPane.showMessageDialog(this, "Nova senha e confirmação não coincidem.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        java.lang.reflect.Type usuarioListType = new TypeToken<List<Usuario>>() {}.getType();
        List<Usuario> usuarios = GerenciadorArquivos.carregarDadosArquivo("data/usuarios.json", usuarioListType);

        if (usuarios == null) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Checa se novo nome está em uso por outro usuário
        boolean nomeDuplicado = usuarios.stream()
                .anyMatch(u -> u.getUsername().equals(novoNome) && !u.getUsername().equals(usuarioLogado.getUsername()));

        if (nomeDuplicado) {
            JOptionPane.showMessageDialog(this, "O nome de usuário já está em uso.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean atualizado = false;
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(usuarioLogado.getUsername())) {
                u.setUsername(novoNome);
                if (!novoEmail.isEmpty()) {
                    u.setEmail(novoEmail);
                }
                if (!novaSenha.isEmpty()) {
                    u.setSenha(novaSenha);
                }
                atualizado = true;
                break;
            }
        }

        if (atualizado) {
            usuarioLogado.setUsername(novoNome);
            if (!novoEmail.isEmpty()) usuarioLogado.setEmail(novoEmail);
            if (!novaSenha.isEmpty()) usuarioLogado.setSenha(novaSenha);

            GerenciadorArquivos.salvarDados(usuarios, "data/usuarios.json");
            JOptionPane.showMessageDialog(this, "Perfil atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
