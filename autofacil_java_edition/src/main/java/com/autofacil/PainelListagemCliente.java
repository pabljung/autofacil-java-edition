package com.autofacil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class PainelListagemCliente extends JPanel {

    private JTable tabelaClientes;
    private DefaultTableModel tableModel;

    public PainelListagemCliente() {
        setLayout(new BorderLayout());
        Color azulSuave = new Color(220, 235, 245);
        Color azulDestaque = new Color(80, 140, 200);
        Color cinzaClaro = new Color(245, 247, 250);

        setBackground(cinzaClaro);

        JLabel titulo = new JLabel("Lista de Clientes");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(azulDestaque);
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        String[] colunas = {"CPF", "Nome", "Telefone", "Email"};
        tableModel = new DefaultTableModel(colunas, 0);
        tabelaClientes = new JTable(tableModel);
        tabelaClientes.setFont(new Font("SansSerif", Font.PLAIN, 15));
        tabelaClientes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        tabelaClientes.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        add(titulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        carregarClientes();
    }

    public void carregarClientes() {
        tableModel.setRowCount(0); // Limpa a tabela
        Type clienteListType = new TypeToken<List<Cliente>>() {}.getType();

        // Usar carregarDadosArquivo para ler do sistema de arquivos local
        List<Cliente> clientes = GerenciadorArquivos.carregarDadosArquivo("clientes.json", clienteListType);

        if (clientes != null) {
            for (Cliente c : clientes) {
                Object[] rowData = {
                        formatarCPF(c.getCpf()),
                        c.getNome(),
                        c.getTelefone(),
                        c.getEmail()
                };
                tableModel.addRow(rowData);
            }
        }
    }
    private String formatarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;

        return cpf.substring(0, 3) + "." +
                cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-" +
                cpf.substring(9, 11);
    }
}
