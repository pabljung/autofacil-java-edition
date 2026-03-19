package com.autofacil;

import javax.swing.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal(Usuario usuarioLogado) {
        setTitle("AutoFácil - Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Bem-vindo, " + usuarioLogado.getUsername() + " (" + usuarioLogado.getPerfil() + ")");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        add(label);
    }
}
