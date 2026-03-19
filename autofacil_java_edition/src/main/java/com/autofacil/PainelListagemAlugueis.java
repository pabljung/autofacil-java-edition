package com.autofacil;

import com.google.gson.reflect.TypeToken;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTextField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PainelListagemAlugueis extends JXPanel {

    private final JXTable tabelaAlugueis;
    private final DefaultTableModel tableModel;

    private final JXTextField filtroPlaca;
    private final JXTextField filtroCpf;
    private final JFormattedTextField filtroDataInicio;
    private final JFormattedTextField filtroDataFim;
    private final JComboBox<String> filtroStatus;
    private final JXTextField txtValorMin;
    private final JXTextField txtValorMax;
    private final JComboBox<String> comboOrdenarPor;
    private final JComboBox<String> comboDirecao;
    private final JXButton btnAplicarFiltros;
    private final JXButton btnLimparFiltros;

    public PainelListagemAlugueis() {
        setLayout(new BorderLayout());
        setBackground(new Color(220, 235, 245));

        // --- PAINEL SUPERIOR PARA TÍTULO E FILTROS ---
        JXPanel topPanel = new JXPanel(new BorderLayout());
        topPanel.setBackground(new Color(220, 235, 245));

        JLabel lblTitulo = new JLabel("Lista de Aluguéis");
        lblTitulo.setFont(new Font("Roboto", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(60, 90, 140));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(lblTitulo, BorderLayout.NORTH);

        // --- PAINEL DE FILTROS ---
        JXPanel filtroPanel = new JXPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filtroPanel.setBackground(new Color(245, 247, 250));
        filtroPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        filtroPlaca = new JXTextField();
        filtroPlaca.setColumns(8);
        filtroCpf = new JXTextField();
        filtroCpf.setColumns(10);
        txtValorMin = new JXTextField();
        txtValorMin.setColumns(6);
        txtValorMax = new JXTextField();
        txtValorMax.setColumns(6);

        try {
            filtroDataInicio = new JFormattedTextField(new MaskFormatter("##/##/####"));
            filtroDataFim = new JFormattedTextField(new MaskFormatter("##/##/####"));
        } catch (ParseException e) {
            throw new RuntimeException("Erro ao criar máscara de data", e);
        }
        filtroDataInicio.setColumns(8);
        filtroDataFim.setColumns(8);

        filtroStatus = new JComboBox<>(new String[]{"Todos", "Ativo", "Concluído", "Atrasado"});
        comboOrdenarPor = new JComboBox<>(new String[]{"Sem ordenação", "Data Saída", "Valor Total", "Data Devolução Efetiva"});
        comboDirecao = new JComboBox<>(new String[]{"Crescente", "Decrescente"});

        btnAplicarFiltros = new JXButton("Aplicar Filtros");
        btnLimparFiltros = new JXButton("Limpar Filtros");

        filtroPanel.add(new JLabel("Placa:"));
        filtroPanel.add(filtroPlaca);
        filtroPanel.add(new JLabel("CPF:"));
        filtroPanel.add(filtroCpf);
        filtroPanel.add(new JLabel("Data Início:"));
        filtroPanel.add(filtroDataInicio);
        filtroPanel.add(new JLabel("Data Fim:"));
        filtroPanel.add(filtroDataFim);
        filtroPanel.add(new JLabel("Status:"));
        filtroPanel.add(filtroStatus);
        filtroPanel.add(new JLabel("Valor Mín:"));
        filtroPanel.add(txtValorMin);
        filtroPanel.add(new JLabel("Valor Máx:"));
        filtroPanel.add(txtValorMax);
        filtroPanel.add(new JLabel("Ordenar por:"));
        filtroPanel.add(comboOrdenarPor);
        filtroPanel.add(comboDirecao);
        filtroPanel.add(btnAplicarFiltros);
        filtroPanel.add(btnLimparFiltros);

        topPanel.add(filtroPanel, BorderLayout.CENTER);

        // --- TABELA DE DADOS ---
        String[] colunas = {"ID Aluguel", "Placa Veículo", "CPF Cliente", "Data Saída",
                "Data Prevista Devolução", "Data Devolução Efetiva", "Valor Total", "Status"};

        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaAlugueis = new JXTable(tableModel);
        tabelaAlugueis.setFont(new Font("Roboto", Font.PLAIN, 14));
        tabelaAlugueis.setRowHeight(28);
        tabelaAlugueis.setSelectionBackground(new Color(210, 245, 225));
        tabelaAlugueis.setGridColor(new Color(200, 220, 230));

        tabelaAlugueis.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        // Ajuste a altura preferida do scrollpane para garantir que mais linhas sejam visíveis
        tabelaAlugueis.setPreferredScrollableViewportSize(new Dimension(950, 800)); // Aumentado de 350 para 500

        JTableHeader header = tabelaAlugueis.getTableHeader();
        header.setFont(new Font("Roboto", Font.BOLD, 15));
        header.setBackground(new Color(200, 220, 240));
        header.setForeground(new Color(40, 60, 80));

        JScrollPane scrollPane = new JScrollPane(tabelaAlugueis);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        scrollPane.getViewport().setBackground(new Color(245, 247, 250));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Garante a barra de rolagem

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnAplicarFiltros.addActionListener(e -> aplicarFiltros());
        btnLimparFiltros.addActionListener(e -> limparFiltros());

        // Chamada inicial para carregar todos os aluguéis sem filtro
        carregarAlugueisSemFiltroInicial();
    }

    private void carregarAlugueisFiltrados(String placa, String cpf, String dataInicioStr, String dataFimStr,
                                           String status, double valorMin, double valorMax, String ordenarPor, boolean desc) {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);

            Type aluguelListType = new TypeToken<List<Aluguel>>() {}.getType();
            List<Aluguel> alugueis = GerenciadorArquivos.carregarDadosArquivo("alugueis.json", aluguelListType);

            // Adicionado print de debug para verificar os aluguéis carregados
            System.out.println("Aluguéis carregados (para filtro): " + (alugueis == null ? 0 : alugueis.size()));

            if (alugueis != null && !alugueis.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                List<Aluguel> filtrados = alugueis.stream()
                        .filter(a -> placa == null || a.getVeiculo().getPlaca().equalsIgnoreCase(placa))
                        .filter(a -> cpf == null || (a.getCliente() != null && a.getCliente().getCpf().replaceAll("[^\\d]", "").equals(cpf)))
                        .filter(a -> status == null || status.equalsIgnoreCase("Todos") || a.getStatusAluguel().equalsIgnoreCase(status))
                        .filter(a -> {
                            if (dataInicioStr == null) return true;
                            try {
                                LocalDate dataInicio = LocalDate.parse(dataInicioStr, formatter);
                                return !LocalDate.parse(a.getDataSaida(), formatter).isBefore(dataInicio);
                            } catch (Exception e) {
                                return true; // Se a data estiver inválida, não filtra
                            }
                        })
                        .filter(a -> {
                            if (dataFimStr == null) return true;
                            try {
                                LocalDate dataFim = LocalDate.parse(dataFimStr, formatter);
                                return !LocalDate.parse(a.getDataSaida(), formatter).isAfter(dataFim);
                            } catch (Exception e) {
                                return true; // Se a data estiver inválida, não filtra
                            }
                        })
                        .filter(a -> a.getValorTotal() >= valorMin && a.getValorTotal() <= valorMax)
                        .collect(Collectors.toList());

                // Ordenação
                if (ordenarPor != null && !ordenarPor.equals("Sem ordenação")) {
                    Comparator<Aluguel> comparator = null;

                    if (ordenarPor.equals("Data Saída")) {
                        comparator = Comparator.comparing(a -> LocalDate.parse(a.getDataSaida(), formatter));
                    } else if (ordenarPor.equals("Valor Total")) {
                        comparator = Comparator.comparingDouble(Aluguel::getValorTotal);
                    } else if (ordenarPor.equals("Data Devolução Efetiva")) {
                        comparator = Comparator.comparing(a -> {
                            String dataDev = a.getDataDevolucaoEfetiva();
                            if (dataDev == null || dataDev.trim().isEmpty()) return LocalDate.MIN;
                            try {
                                return LocalDate.parse(dataDev, formatter);
                            } catch (Exception e) {
                                return LocalDate.MIN;
                            }
                        });
                    }

                    if (comparator != null) {
                        if (desc) {
                            comparator = comparator.reversed();
                        }
                        filtrados.sort(comparator);
                    }
                }

                for (Aluguel a : filtrados) {
                    double valorTotal = a.getValorTotal();
                    // Removido o cálculo do valor total aqui, pois ele deve ser definitivo no objeto Aluguel
                    // if (valorTotal <= 0) { valorTotal = calcularValorTotal(a); }

                    String dataDevolucaoEfetiva = a.getDataDevolucaoEfetiva() != null ? a.getDataDevolucaoEfetiva() : "";

                    Object[] rowData = {
                            a.getIdAluguel(),
                            a.getVeiculo() != null ? a.getVeiculo().getPlaca() : "",
                            a.getCliente() != null ? a.getCliente().getCpf() : "",
                            a.getDataSaida(),
                            a.getDataPrevistaDevolucao(),
                            dataDevolucaoEfetiva,
                            String.format("R$ %.2f", valorTotal), // Usa o valorTotal já definido no objeto
                            a.getStatusAluguel()
                    };
                    tableModel.addRow(rowData);
                }
                // Adicionado print de debug para verificar quantos aluguéis filtrados estão sendo adicionados à tabela
                System.out.println("Aluguéis filtrados adicionados à tabela: " + filtrados.size());
            } else {
                System.out.println("Nenhum aluguel encontrado para filtro ou arquivo vazio.");
            }
            tableModel.fireTableDataChanged();
            tabelaAlugueis.revalidate();
            tabelaAlugueis.repaint();
        });
    }

    // Mantido o método calcularValorTotal caso ainda seja usado em outras partes da aplicação
    private double calcularValorTotal(Aluguel aluguel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate dataSaida = LocalDate.parse(aluguel.getDataSaida(), formatter);
            LocalDate dataPrevista = LocalDate.parse(aluguel.getDataPrevistaDevolucao(), formatter);

            long dias = java.time.temporal.ChronoUnit.DAYS.between(dataSaida, dataPrevista);
            if (dias <= 0) dias = 1;

            double precoDiaria = aluguel.getVeiculo().getPrecoDiaria();

            return precoDiaria * dias;
        } catch (Exception e) {
            System.err.println("Erro ao calcular valor total para aluguel: " + aluguel.getIdAluguel() + " - " + e.getMessage());
            return 0.0;
        }
    }

    public void carregarAlugueis() {
        // Agora, carregarAlugueis chama carregarAlugueisFiltrados com parâmetros nulos para não aplicar filtros
        carregarAlugueisFiltrados(null, null, null, null, "Todos", 0.0, Double.MAX_VALUE, "Sem ordenação", false);
    }

    // Novo método para a chamada inicial, garantindo que não há filtro no início
    private void carregarAlugueisSemFiltroInicial() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);

            Type aluguelListType = new TypeToken<List<Aluguel>>() {}.getType();
            List<Aluguel> alugueis = GerenciadorArquivos.carregarDadosArquivo("alugueis.json", aluguelListType);

            // Adicionado print de debug para verificar os aluguéis carregados
            System.out.println("Aluguéis carregados (inicial): " + (alugueis == null ? 0 : alugueis.size()));

            if (alugueis != null && !alugueis.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                for (Aluguel a : alugueis) {
                    String placa = a.getVeiculo() != null ? a.getVeiculo().getPlaca() : "";
                    String cpfCliente = a.getCliente() != null ? a.getCliente().getCpf() : "";
                    String dataDevolucaoEfetiva = a.getDataDevolucaoEfetiva() != null ? a.getDataDevolucaoEfetiva() : "";

                    Object[] rowData = {
                            a.getIdAluguel(),
                            placa,
                            cpfCliente,
                            a.getDataSaida(),
                            a.getDataPrevistaDevolucao(),
                            dataDevolucaoEfetiva,
                            String.format("R$ %.2f", a.getValorTotal()),
                            a.getStatusAluguel()
                    };
                    tableModel.addRow(rowData);
                }
                // Adicionado print de debug para verificar quantos aluguéis estão sendo adicionados à tabela
                System.out.println("Aluguéis adicionados à tabela (inicial): " + alugueis.size());
            } else {
                System.out.println("Nenhum aluguel encontrado no arquivo JSON ou arquivo vazio.");
            }

            tableModel.fireTableDataChanged();
            tabelaAlugueis.revalidate();
            tabelaAlugueis.repaint();
        });
    }

    private void aplicarFiltros() {
        String placa = filtroPlaca.getText().trim().toUpperCase();
        String cpf = filtroCpf.getText().replaceAll("[^\\d]", "").trim();

        placa = placa.isEmpty() ? null : placa;
        cpf = cpf.isEmpty() ? null : cpf;

        String dataInicioStr = filtroDataInicio.getText().trim();
        String dataFimStr = filtroDataFim.getText().trim();

        if (dataInicioStr.equals("__/__/____")) dataInicioStr = "";
        if (dataFimStr.equals("__/__/____")) dataFimStr = "";

        if (!dataInicioStr.isEmpty() && !dataInicioStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Data Início inválida. Use o formato dd/MM/aaaa.", "Erro de Filtro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!dataFimStr.isEmpty() && !dataFimStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Data Fim inválida. Use o formato dd/MM/aaaa.", "Erro de Filtro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        dataInicioStr = dataInicioStr.isEmpty() ? null : dataInicioStr;
        dataFimStr = dataFimStr.isEmpty() ? null : dataFimStr;

        String status = filtroStatus.getSelectedItem().toString();

        double valorMin = 0.0;
        double valorMax = Double.MAX_VALUE;
        try {
            if (!txtValorMin.getText().trim().isEmpty()) {
                valorMin = Double.parseDouble(txtValorMin.getText().replace(",", "."));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor mínimo inválido.", "Erro de Filtro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            if (!txtValorMax.getText().trim().isEmpty()) {
                valorMax = Double.parseDouble(txtValorMax.getText().replace(",", "."));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor máximo inválido.", "Erro de Filtro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ordenarPor = (String) comboOrdenarPor.getSelectedItem();
        boolean desc = comboDirecao.getSelectedItem().equals("Decrescente");

        carregarAlugueisFiltrados(
                placa,
                cpf,
                dataInicioStr,
                dataFimStr,
                status,
                valorMin,
                valorMax,
                ordenarPor,
                desc
        );
    }

    private void limparFiltros() {
        filtroPlaca.setText("");
        filtroCpf.setText("");
        filtroDataInicio.setValue(null);
        filtroDataFim.setValue(null);
        filtroStatus.setSelectedIndex(0);
        txtValorMin.setText("");
        txtValorMax.setText("");
        comboOrdenarPor.setSelectedIndex(0);
        comboDirecao.setSelectedIndex(0);
        carregarAlugueisSemFiltroInicial(); // Volta a carregar sem filtros após limpar
    }
}