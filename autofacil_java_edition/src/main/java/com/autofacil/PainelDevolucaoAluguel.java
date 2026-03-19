package com.autofacil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTable;

public class PainelDevolucaoAluguel extends JXPanel {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JXTable tabelaAlugueisAtivos;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JXButton btnAtualizarLista;

    private PainelListagemAlugueis painelListagemAlugueis;

    public PainelDevolucaoAluguel() {
        Color azulSuave = new Color(220, 235, 245);
        Color azulDestaque = new Color(80, 140, 200);
        Color cinzaClaro = new Color(245, 247, 250);

        setBackground(cinzaClaro);
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = criarTitulo("Devolução de Aluguéis", azulDestaque);
        add(titulo, BorderLayout.NORTH);

        JXPanel tabelaPanel = new JXPanel(new BorderLayout());
        tabelaPanel.setBackground(cinzaClaro);
        tabelaPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(azulDestaque.darker(), 1),
                "Aluguéis Ativos (Clique para Devolver)",
                0, 0, new Font("SansSerif", Font.BOLD, 14), azulDestaque.darker()
        ));

        String[] colunas = {"ID Aluguel", "Placa", "Marca", "Modelo", "Data Retirada", "Data Prev. Devolução", "CPF Cliente"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaAlugueisAtivos = new JXTable(tableModel);
        tabelaAlugueisAtivos.setFillsViewportHeight(true);
        tabelaAlugueisAtivos.setRowHeight(25);
        tabelaAlugueisAtivos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabelaAlugueisAtivos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabelaAlugueisAtivos.getTableHeader().setBackground(azulDestaque.brighter());
        tabelaAlugueisAtivos.getTableHeader().setForeground(Color.WHITE);
        tabelaAlugueisAtivos.setGridColor(azulSuave);
        tabelaAlugueisAtivos.setSelectionBackground(azulDestaque.brighter());
        tabelaAlugueisAtivos.setSelectionForeground(Color.WHITE);
        tabelaAlugueisAtivos.setBackground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabelaAlugueisAtivos.getColumnCount(); i++) {
            tabelaAlugueisAtivos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scrollPane = new JScrollPane(tabelaAlugueisAtivos);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(azulDestaque, 1));
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);
        add(tabelaPanel, BorderLayout.CENTER);

        JXPanel botoesPanel = new JXPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        botoesPanel.setBackground(cinzaClaro);

        btnAtualizarLista = criarBotao("Atualizar Lista", new Color(100, 180, 100));
        btnAtualizarLista.addActionListener(e -> carregarAlugueisAtivos());
        botoesPanel.add(btnAtualizarLista);

        add(botoesPanel, BorderLayout.SOUTH);

        tabelaAlugueisAtivos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelaAlugueisAtivos.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    int idAluguel = (int) tabelaAlugueisAtivos.getValueAt(row, 0);
                    iniciarDevolucao(idAluguel);
                }
            }
        });

        carregarAlugueisAtivos();
    }

    private JLabel criarTitulo(String texto, Color cor) {
        JLabel titulo = new JLabel(texto, SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(cor);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        return titulo;
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
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(corFundo.darker());
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(corFundo);
            }
        });
        return btn;
    }

    public void setPainelListagemAlugueis(PainelListagemAlugueis painel) {
        this.painelListagemAlugueis = painel;
    }

    private void carregarAlugueisAtivos() {
        tableModel.setRowCount(0);

        Type aluguelListType = new TypeToken<List<Aluguel>>() {}.getType();
        List<Aluguel> alugueis = GerenciadorArquivos.carregarDadosRelativo("alugueis.json", aluguelListType);

        if (alugueis != null && !alugueis.isEmpty()) {
            List<Aluguel> alugueisAtivos = alugueis.stream()
                    .filter(a -> a.getStatusAluguel().equalsIgnoreCase("Ativo"))
                    .collect(Collectors.toList());

            for (Aluguel aluguel : alugueisAtivos) {
                String placa = aluguel.getVeiculo() != null ? aluguel.getVeiculo().getPlaca() : "N/A";
                String marca = aluguel.getVeiculo() != null ? aluguel.getVeiculo().getMarca() : "N/A";
                String modelo = aluguel.getVeiculo() != null ? aluguel.getVeiculo().getModelo() : "N/A";

                tableModel.addRow(new Object[]{
                        aluguel.getIdAluguel(),
                        placa,
                        marca,
                        modelo,
                        aluguel.getDataSaida(),
                        aluguel.getDataPrevistaDevolucao(),
                        aluguel.getCpfCliente()
                });
            }
        }
    }

    private void iniciarDevolucao(int idAluguel) {
        Type aluguelListType = new TypeToken<List<Aluguel>>() {}.getType();
        List<Aluguel> alugueis = GerenciadorArquivos.carregarDados("alugueis.json", aluguelListType);

        if (alugueis == null) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados de aluguéis.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<Aluguel> aluguelOpt = alugueis.stream()
                .filter(a -> a.getIdAluguel() == idAluguel)
                .findFirst();

        if (aluguelOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aluguel selecionado não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Aluguel aluguelAtivo = aluguelOpt.get();

        JTextField dataDevolucaoField = new JFormattedTextField(createMaskFormatter("##/##/####"));
        dataDevolucaoField.setText(LocalDate.now().format(FORMATTER));
        dataDevolucaoField.setPreferredSize(new Dimension(150, 25));
        dataDevolucaoField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        int resultData = JOptionPane.showConfirmDialog(this,
                new Object[]{
                        "Data de Retirada: " + aluguelAtivo.getDataSaida(),
                        "Data Prevista de Devolução: " + aluguelAtivo.getDataPrevistaDevolucao(),
                        "Placa: " + aluguelAtivo.getVeiculo().getPlaca(),
                        "CPF do Cliente: " + aluguelAtivo.getCpfCliente(),
                        " ",
                        "Informe a Data de Devolução Efetiva (DD/MM/AAAA):", dataDevolucaoField
                },
                "Confirmação de Devolução", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (resultData != JOptionPane.OK_OPTION) return;

        String dataDevolucaoStr = dataDevolucaoField.getText().trim();
        LocalDate dataDevolucaoEfetiva;
        try {
            dataDevolucaoEfetiva = LocalDate.parse(dataDevolucaoStr, FORMATTER);
            if (dataDevolucaoEfetiva.isBefore(LocalDate.parse(aluguelAtivo.getDataSaida(), FORMATTER))) {
                JOptionPane.showMessageDialog(this, "A data de devolução não pode ser anterior à data de retirada.", "Erro de Data", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data de devolução inválida. Use o formato DD/MM/AAAA.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField cpfField = new JFormattedTextField(createMaskFormatter("###.###.###-##"));
        cpfField.setText(aluguelAtivo.getCpfCliente());
        cpfField.setPreferredSize(new Dimension(150, 25));
        cpfField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        int resultCpf = JOptionPane.showConfirmDialog(this,
                new Object[]{"Confirme o CPF do Cliente:", cpfField},
                "Confirmação de Cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (resultCpf != JOptionPane.OK_OPTION) return;

        String cpfConfirmado = cpfField.getText().trim().replaceAll("[^0-9]", "");
        if (!cpfConfirmado.equals(aluguelAtivo.getCpfCliente().replaceAll("[^0-9]", ""))) {
            JOptionPane.showMessageDialog(this, "CPF informado não corresponde ao cliente do aluguel.", "Erro de Confirmação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long diasAlugados = ChronoUnit.DAYS.between(LocalDate.parse(aluguelAtivo.getDataSaida(), FORMATTER), dataDevolucaoEfetiva);
        if (diasAlugados == 0) diasAlugados = 1;

        Type veiculoListType = new TypeToken<List<Veiculo>>() {}.getType();
        List<Veiculo> veiculos = GerenciadorArquivos.carregarDados("veiculos.json", veiculoListType);
        double precoDiaria = 0.0;
        if (veiculos != null) {
            Optional<Veiculo> veiculoOpt = veiculos.stream()
                    .filter(v -> normalizarPlaca(v.getPlaca()).equals(normalizarPlaca(aluguelAtivo.getVeiculo().getPlaca())))
                    .findFirst();
            if (veiculoOpt.isPresent()) {
                precoDiaria = veiculoOpt.get().getPrecoDiaria();
            }
        }
        double valorCalculado = diasAlugados * precoDiaria;

        int confirmFinal = JOptionPane.showConfirmDialog(this,
                String.format("Confirmar devolução para:\n" +
                                "Veículo: %s (%s)\n" +
                                "Cliente: %s\n" +
                                "Data de Devolução: %s\n" +
                                "Valor Total a Pagar: R$ %.2f\n\n" +
                                "Deseja realmente finalizar este aluguel?",
                        aluguelAtivo.getVeiculo().getModelo(), aluguelAtivo.getVeiculo().getPlaca(),
                        aluguelAtivo.getCpfCliente(), dataDevolucaoStr, valorCalculado),
                "Confirmação Final", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmFinal != JOptionPane.YES_OPTION) return;

        aluguelAtivo.finalizarAluguel(dataDevolucaoStr);
        aluguelAtivo.setValorTotal(valorCalculado);

        if (veiculos != null) {
            veiculos.stream()
                    .filter(v -> normalizarPlaca(v.getPlaca()).equals(normalizarPlaca(aluguelAtivo.getVeiculo().getPlaca())))
                    .findFirst()
                    .ifPresent(v -> v.setDisponivel(true));
        }

        GerenciadorArquivos.salvarDados(alugueis, "alugueis.json");
        GerenciadorArquivos.salvarDados(veiculos, "veiculos.json");

        if (painelListagemAlugueis != null) {
            painelListagemAlugueis.carregarAlugueis();
        }

        mostrarMensagemSucesso("Devolução registrada com sucesso para o veículo " + aluguelAtivo.getVeiculo().getPlaca() + "!");
        carregarAlugueisAtivos();
    }

    private JFormattedTextField.AbstractFormatter createMaskFormatter(String mask) {
        try {
            return new javax.swing.text.MaskFormatter(mask);
        } catch (ParseException e) {
            return null;
        }
    }

    private String normalizarPlaca(String placa) {
        if (placa == null) return "";
        return placa.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    private void mostrarMensagemSucesso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
}
