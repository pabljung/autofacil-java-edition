package com.autofacil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Year;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class PainelListagemVeiculos extends JPanel {

    private JTable tabelaVeiculos;
    private DefaultTableModel tableModel;
    private JButton btnFormatarDados;

    public PainelListagemVeiculos() {
        setLayout(new BorderLayout());
        Color azulSuave = new Color(220, 235, 245);
        Color azulDestaque = new Color(80, 140, 200);
        Color cinzaClaro = new Color(245, 247, 250);

        setBackground(cinzaClaro);

        JLabel titulo = new JLabel("Lista de Veículos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(azulDestaque);
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        String[] colunas = {"Placa", "Marca", "Modelo", "Preço Diária", "Status"};
        tableModel = new DefaultTableModel(colunas, 0);
        tabelaVeiculos = new JTable(tableModel);
        tabelaVeiculos.setFont(new Font("SansSerif", Font.PLAIN, 15));
        tabelaVeiculos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        tabelaVeiculos.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(tabelaVeiculos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        scrollPane.setPreferredSize(new Dimension(700, 400));


        // Botão Formatar Dados
        btnFormatarDados = new JButton("Formatar Dados");
        btnFormatarDados.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnFormatarDados.setBackground(new Color(120, 180, 120));
        btnFormatarDados.setForeground(Color.WHITE);
        btnFormatarDados.setFocusPainted(false);
        btnFormatarDados.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 160, 100), 1, true),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        btnFormatarDados.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFormatarDados.setOpaque(true);
        btnFormatarDados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFormatarDados.setBackground(new Color(100, 160, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFormatarDados.setBackground(new Color(120, 180, 120));
            }
        });
        btnFormatarDados.addActionListener(this::formatarDadosVeiculos);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(cinzaClaro);
        topPanel.add(titulo, BorderLayout.CENTER);
        topPanel.add(btnFormatarDados, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        carregarVeiculos();
    }

    public void carregarVeiculos() {
        tableModel.setRowCount(0); // Limpa a tabela
        Type veiculoListType = new TypeToken<List<Veiculo>>() {}.getType();
        List<Veiculo> veiculos = GerenciadorArquivos.carregarDadosArquivo("veiculos.json", veiculoListType);

        if (veiculos != null) {
            for (Veiculo v : veiculos) {
                Object[] rowData = {
                        v.getPlaca() != null ? v.getPlaca().trim() : "",
                        v.getMarca() != null ? v.getMarca().trim() : "",
                        v.getModelo() != null ? v.getModelo().trim() : "",
                        String.format("R$ %.2f", v.getPrecoDiaria()),
                        v.getStatus() != null ? v.getStatus().trim() : ""
                };
                tableModel.addRow(rowData);
            }
        }
    }


    private void formatarDadosVeiculos(ActionEvent e) {
        Type veiculoListType = new TypeToken<List<Veiculo>>() {}.getType();
        List<Veiculo> veiculos = GerenciadorArquivos.carregarDadosArquivo("veiculos.json", veiculoListType);

        if (veiculos == null || veiculos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum veículo para formatar.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int anoAtual = Year.now().getValue();
        Pattern placaPattern = Pattern.compile("([A-Z]{3})[- ]?(\\d{4})");
        boolean alterado = false;

        for (Veiculo v : veiculos) {
            // Placa: AAA-1234
            String placa = v.getPlaca();
            if (placa != null) {
                placa = placa.trim().toUpperCase().replaceAll("[^A-Z0-9]", "");
                Matcher m = placaPattern.matcher(placa);
                if (m.matches()) {
                    placa = m.group(1) + "-" + m.group(2);
                } else if (placa.length() == 7) {
                    placa = placa.substring(0, 3) + "-" + placa.substring(3);
                }
                if (!placa.equals(v.getPlaca())) {
                    v.setPlaca(placa);
                    alterado = true;
                }
            }

            // Ano: entre 1900 e anoAtual
            int ano = v.getAno();
            if (ano < 1900 || ano > anoAtual) {
                v.setAno(Math.max(1900, Math.min(ano, anoAtual)));
                alterado = true;
            }

            // Preço: duas casas decimais
            double preco = v.getPrecoDiaria();
            double precoFormatado = Math.round(preco * 100.0) / 100.0;
            if (preco != precoFormatado) {
                v.setPrecoDiaria(precoFormatado);
                alterado = true;
            }

            // Cor: capitalizar e trim
            String cor = v.getCor();
            if (cor != null && !cor.isEmpty()) {
                String corTrimmed = cor.trim();
                String corFormatada = corTrimmed.substring(0, 1).toUpperCase() + corTrimmed.substring(1).toLowerCase();
                if (!corFormatada.equals(cor)) {
                    v.setCor(corFormatada);
                    alterado = true;
                }
            }
        }

        if (alterado) {
            GerenciadorArquivos.salvarDados(veiculos, "veiculos.json");
            carregarVeiculos();
            JOptionPane.showMessageDialog(this, "Dados dos veículos formatados e salvos com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Todos os dados já estavam formatados.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
