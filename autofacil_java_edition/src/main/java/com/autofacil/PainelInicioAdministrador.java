package com.autofacil;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import org.jdesktop.swingx.border.DropShadowBorder;
public class PainelInicioAdministrador extends JPanel {

    private JLabel lblTotalVeiculos;
    private JLabel lblVeiculosDisponiveis;
    private JLabel lblVeiculosAlugados;
    private JLabel lblTotalClientes;
    private JLabel lblTotalAlugueis;
    private JLabel lblAlugueisAtivos;

    public PainelInicioAdministrador() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 248, 252)); // Fundo mais suave

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(22, 22, 22, 22);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        lblTotalVeiculos = criarValorLabel();
        lblVeiculosDisponiveis = criarValorLabel();
        lblVeiculosAlugados = criarValorLabel();
        lblTotalClientes = criarValorLabel();
        lblTotalAlugueis = criarValorLabel();
        lblAlugueisAtivos = criarValorLabel();

        int cardWidth = 250;
        int cardHeight = 120;

        // Cards com cor, ícone e texto
        JPanel card1 = criarCard("Total de Veículos", lblTotalVeiculos, new Color(68, 138, 255), cardWidth, cardHeight, "carro.png");
        JPanel card2 = criarCard("Veículos Disponíveis", lblVeiculosDisponiveis, new Color(0, 191, 165), cardWidth, cardHeight, "disponivel.png");
        JPanel card3 = criarCard("Veículos Alugados", lblVeiculosAlugados, new Color(255, 143, 0), cardWidth, cardHeight, "alugado.png");
        JPanel card4 = criarCard("Total de Clientes", lblTotalClientes, new Color(142, 36, 170), cardWidth, cardHeight, "cliente.png");
        JPanel card5 = criarCard("Total de Aluguéis", lblTotalAlugueis, new Color(255, 202, 40), cardWidth, cardHeight, "aluguel.png");
        JPanel card6 = criarCard("Aluguéis Ativos", lblAlugueisAtivos, new Color(41, 182, 246), cardWidth, cardHeight, "ativo.png");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(card1, gbc);
        gbc.gridx = 1;
        add(card2, gbc);
        gbc.gridx = 2;
        add(card3, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(card4, gbc);
        gbc.gridx = 1;
        add(card5, gbc);
        gbc.gridx = 2;
        add(card6, gbc);

        atualizarDados();
    }

    private JPanel criarCard(String titulo, JLabel valor, Color cor, int w, int h, String iconeNome) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(w, h));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(cor.darker(), 2, true),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));
        card.setLayout(new BorderLayout(10, 10)); // Espaçamento entre componentes

        // Ícone pequeno no canto superior esquerdo
        JLabel lblIcone = new JLabel();
        ImageIcon icone = carregarIcone(iconeNome, 30, 30);
        if (icone != null) {
            lblIcone.setIcon(icone);
        }

        // Painel topo com ícone e título
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FonteUtils.carregarFonteRoboto(16f).deriveFont(Font.BOLD));
        lblTitulo.setForeground(cor.darker());
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        topo.add(lblIcone, BorderLayout.WEST);
        topo.add(lblTitulo, BorderLayout.CENTER);

        // Valor em destaque centralizado e grande
        valor.setForeground(cor.darker());
        valor.setHorizontalAlignment(SwingConstants.CENTER);
        valor.setFont(FonteUtils.carregarFonteRoboto(36f).deriveFont(Font.BOLD));

        card.add(topo, BorderLayout.NORTH);
        card.add(valor, BorderLayout.CENTER);

        // Sombra sutil para o card (apenas se quiser)
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new DropShadowBorder(cor.darker(), 5, 0.3f, 8, false, true, true, false),
                card.getBorder()
        ));

        return card;
    }

    private JLabel criarValorLabel() {
        JLabel lbl = new JLabel("0");
        lbl.setFont(FonteUtils.carregarFonteRoboto(32f).deriveFont(Font.BOLD));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private ImageIcon carregarIcone(String nomeArquivo, int largura, int altura) {
        try {
            java.net.URL url = getClass().getClassLoader().getResource("icons/" + nomeArquivo);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone: " + nomeArquivo);
        }
        return null;
    }

    private void atualizarDados() {
        List<Veiculo> veiculos = GerenciadorArquivos.carregarDadosArquivo("veiculos.json", new TypeToken<List<Veiculo>>() {
        }.getType());
        List<Cliente> clientes = GerenciadorArquivos.carregarDadosArquivo("clientes.json", new TypeToken<List<Cliente>>() {
        }.getType());
        List<Aluguel> alugueis = GerenciadorArquivos.carregarDadosArquivo("alugueis.json", new TypeToken<List<Aluguel>>() {
        }.getType());

        int totalVeiculos = veiculos != null ? veiculos.size() : 0;
        int totalClientes = clientes != null ? clientes.size() : 0;
        int totalAlugueis = alugueis != null ? alugueis.size() : 0;

        int veiculosAlugados = 0;
        int veiculosDisponiveis = 0;
        if (veiculos != null) {
            for (Veiculo v : veiculos) {
                if ("Disponível".equalsIgnoreCase(v.getStatus())) veiculosDisponiveis++;
                else veiculosAlugados++;
            }
        }

        int alugueisAtivos = 0;
        if (alugueis != null) {
            for (Aluguel a : alugueis) {
                // Se existir getStatusAluguel(), use essa verificação:
                if (a.getStatusAluguel() != null && a.getStatusAluguel().equalsIgnoreCase("Ativo")) {
                    alugueisAtivos++;
                } else {
                    // Se não tiver getStatusAluguel(), verifica dataDevolucaoEfetiva
                    String dataDevEfetiva = a.getDataDevolucaoEfetiva();
                    if (dataDevEfetiva == null || dataDevEfetiva.trim().isEmpty()) {
                        alugueisAtivos++;
                    }
                }
            }
        }

        lblTotalVeiculos.setText(String.valueOf(totalVeiculos));
        lblVeiculosDisponiveis.setText(String.valueOf(veiculosDisponiveis));
        lblVeiculosAlugados.setText(String.valueOf(veiculosAlugados));
        lblTotalClientes.setText(String.valueOf(totalClientes));
        lblTotalAlugueis.setText(String.valueOf(totalAlugueis));
        lblAlugueisAtivos.setText(String.valueOf(alugueisAtivos));
    }
}


