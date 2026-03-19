package com.autofacil;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        // Aplica o tema FlatLaf claro
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Erro ao aplicar FlatLaf: " + e.getMessage());
        }

        // Abre a tela de login na Thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }
}
