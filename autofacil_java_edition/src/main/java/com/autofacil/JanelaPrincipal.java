package com.autofacil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import com.google.gson.reflect.TypeToken;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class JanelaPrincipal extends JFrame {

    private PainelCadastroVeiculo painelCadastroVeiculo;
    private PainelListagemVeiculos painelListagemVeiculos;
    private PainelCadastroCliente painelCadastroCliente;
    private PainelListagemCliente painelListagemCliente;
    private PainelRegistroAluguel painelRegistroAluguel;
    private PainelDevolucaoAluguel painelDevolucaoAluguel;
    private PainelListagemAlugueis painelListagemAlugueis;

    private Usuario usuarioLogado;

    private JLabel lblUsuario;
    private ImageIcon userIcon;
    private ImageIcon userFotoIcon;

    public JanelaPrincipal(Usuario usuarioLogado) {
        if (usuarioLogado != null) {
            Usuario recarregado = recarregarUsuario(usuarioLogado.getUsername());
            this.usuarioLogado = (recarregado != null) ? recarregado : usuarioLogado;
        } else {
            this.usuarioLogado = new Usuario("visitante", "", "Visitante");
        }

        setTitle("AutoFácil Java Edition - Locadora de Veículos");
        setSize(1100, 750);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem sairItem = new JMenuItem("Sair");
        sairItem.addActionListener(e -> System.exit(0));
        menuArquivo.add(sairItem);

        JMenuItem editarPerfilItem = new JMenuItem("Editar Perfil");
        editarPerfilItem.addActionListener(e -> abrirEditarPerfil());
        menuArquivo.add(editarPerfilItem);

        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> realizarLogout());
        menuArquivo.add(logoutItem);

        menuBar.add(menuArquivo);

        JMenu menuConfiguracoes = new JMenu("Configurações");
        JCheckBoxMenuItem temaEscuroItem = new JCheckBoxMenuItem("Tema Escuro");
        temaEscuroItem.setSelected(false);

        temaEscuroItem.addActionListener(e -> {
            boolean darkMode = temaEscuroItem.isSelected();
            alternarTemaFlatLaf(darkMode);
        });

        menuConfiguracoes.add(temaEscuroItem);
        menuBar.add(menuConfiguracoes);

        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem sobreItem = new JMenuItem("Sobre");
        sobreItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "AutoFácil Java Edition\nLocadora de Veículos\nDesenvolvido por você!", "Sobre", JOptionPane.INFORMATION_MESSAGE));
        menuAjuda.add(sobreItem);
        menuBar.add(menuAjuda);

        atualizarLabelUsuario();
        if (usuarioLogado == null) return;
        lblUsuario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblUsuario.setToolTipText("Clique para alterar a foto de perfil");
        lblUsuario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                escolherFotoPerfil();
            }
        });

        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(lblUsuario);

        setJMenuBar(menuBar);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 15));

        ImageIcon iconeCarro = carregarIcone("carro.png", 20, 20);
        ImageIcon iconeCliente = carregarIcone("cliente.png", 20, 20);
        ImageIcon iconeEstatisticas = carregarIcone("estatisticas.png", 20, 20);
        ImageIcon iconeAluguel = carregarIcone("car.rent.png", 20, 20);
        ImageIcon iconeHome = carregarIcone("home.png", 20, 20);
        if (iconeCarro == null || iconeCliente == null || iconeEstatisticas == null || iconeAluguel == null) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar ícones. Verifique os arquivos no diretório 'icons'.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
// Painel de Início / Dashboard do Administrador
        PainelInicioAdministrador painelInicioAdmin = new PainelInicioAdministrador();
        JPanel inicioPanel = new JPanel(new BorderLayout());
        inicioPanel.add(painelInicioAdmin, BorderLayout.CENTER);
        tabbedPane.addTab("Início", iconeHome, inicioPanel);
        // Painel de Estatísticas

        JPanel estatisticasPanel = new JPanel(new BorderLayout());
        PainelEstatisticas painelEstatisticas = new PainelEstatisticas();
        estatisticasPanel.add(painelEstatisticas, BorderLayout.CENTER);
        tabbedPane.addTab("Estatísticas", iconeEstatisticas, estatisticasPanel);

        JPanel veiculosPanel = new JPanel(new BorderLayout());
        JTabbedPane veiculosTabbedPane = new JTabbedPane();
        veiculosTabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 14));
        painelListagemVeiculos = new PainelListagemVeiculos();
        painelCadastroVeiculo = new PainelCadastroVeiculo(painelListagemVeiculos);

        if (Permissoes.podeCadastrarVeiculo(usuarioLogado)) {
            veiculosTabbedPane.addTab("Cadastrar Veículo", painelCadastroVeiculo);
        }
        veiculosTabbedPane.addTab("Listar Veículos", painelListagemVeiculos);
        veiculosPanel.add(veiculosTabbedPane, BorderLayout.CENTER);
        tabbedPane.addTab("Veículos", iconeCarro, veiculosPanel);

        JPanel clientesPanel = new JPanel(new BorderLayout());
        JTabbedPane clientesTabbedPane = new JTabbedPane();
        clientesTabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 14));
        painelListagemCliente = new PainelListagemCliente();
        painelCadastroCliente = new PainelCadastroCliente(painelListagemCliente);

        if (Permissoes.podeCadastrarCliente(usuarioLogado)) {
            clientesTabbedPane.addTab("Cadastrar Cliente", painelCadastroCliente);
        }
        clientesTabbedPane.addTab("Listar Clientes", painelListagemCliente);
        clientesPanel.add(clientesTabbedPane, BorderLayout.CENTER);
        tabbedPane.addTab("Clientes", iconeCliente, clientesPanel);

        JPanel alugueisPanel = new JPanel(new BorderLayout());
        JTabbedPane alugueisTabbedPane = new JTabbedPane();
        alugueisTabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 14));

        painelDevolucaoAluguel = new PainelDevolucaoAluguel();
        painelListagemAlugueis = new PainelListagemAlugueis();
        painelRegistroAluguel = new PainelRegistroAluguel();

        painelDevolucaoAluguel.setPainelListagemAlugueis(painelListagemAlugueis);
        painelRegistroAluguel.setPainelListagemAlugueis(painelListagemAlugueis);


        if (Permissoes.podeRegistrarAluguel(usuarioLogado)) {
            alugueisTabbedPane.addTab("Registrar Aluguel", painelRegistroAluguel);
        }
        if (Permissoes.podeRegistrarDevolucao(usuarioLogado)) {
            alugueisTabbedPane.addTab("Registrar Devolução", painelDevolucaoAluguel);
        }
        alugueisTabbedPane.addTab("Listar Aluguéis", painelListagemAlugueis);
        alugueisPanel.add(alugueisTabbedPane, BorderLayout.CENTER);
        tabbedPane.addTab("Aluguéis", iconeAluguel, alugueisPanel);

        add(tabbedPane);

        JPanel[] paineis = {
                painelCadastroVeiculo,
                painelListagemVeiculos,
                painelCadastroCliente,
                painelListagemCliente,
                painelRegistroAluguel,
                painelDevolucaoAluguel,
                painelListagemAlugueis
        };
        aplicarFonteRobotoEmTodosPainéis(paineis);

        SwingUtilities.updateComponentTreeUI(this);
    }

    private ImageIcon carregarIcone(String nomeArquivo, int largura, int altura) {
        try {
            URL url = getClass().getClassLoader().getResource("icons/" + nomeArquivo);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            System.err.println("Não foi possível carregar o ícone: " + nomeArquivo);
        }
        return null;
    }

    private ImageIcon carregarFotoPerfilUsuario(Usuario usuario) {
        String caminho = usuario.getFotoPerfil();
        if (caminho != null && !caminho.isEmpty()) {
            File file = new File(caminho);
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(caminho);
                Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        }
        File padrao = new File("data/perfis/" + usuario.getUsername() + ".png");
        if (padrao.exists()) {
            ImageIcon icon = new ImageIcon(padrao.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }

    private void escolherFotoPerfil() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Escolher Foto de Perfil");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imagens (.jpg, .png)", "jpg", "jpeg", "png"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();
            String extensao = arquivo.getName().toLowerCase().endsWith(".png") ? ".png" : ".jpg";
            File destino = new File("data/perfis/" + usuarioLogado.getUsername() + extensao);

            // Cria a pasta se não existir
            destino.getParentFile().mkdirs();

            try {
                Files.copy(arquivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                usuarioLogado.setFotoPerfil(destino.getAbsolutePath());

                // Atualiza o JSON usando o método da classe GerenciadorArquivos
                GerenciadorArquivos.atualizarFotoUsuarioNoJson(usuarioLogado.getUsername(), destino.getAbsolutePath());

                // Recarrega o usuário atualizado para garantir dados sincronizados
                this.usuarioLogado = recarregarUsuario(usuarioLogado.getUsername());

                // Atualiza o ícone e label na interface
                atualizarLabelUsuario();

                JOptionPane.showMessageDialog(this, "Foto de perfil atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar a foto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para recarregar o usuário do JSON pelo username
    private Usuario recarregarUsuario(String username) {
        try {
            java.lang.reflect.Type usuarioListType = new TypeToken<List<Usuario>>() {}.getType();
            List<Usuario> usuarios = GerenciadorArquivos.carregarDadosArquivo("data/usuarios.json", usuarioListType);
            if (usuarios != null) {
                for (Usuario u : usuarios) {
                    if (u.getUsername().equals(username)) {
                        return u;
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("Erro ao recarregar usuário: " + ex.getMessage());
        }
        return usuarioLogado; // retorna o atual se não encontrar
    }

    // Atualiza o texto e o ícone do label do usuário
    private void atualizarLabelUsuario() {
        if (lblUsuario == null) {
            lblUsuario = new JLabel();
            lblUsuario.setFont(new Font("SansSerif", Font.BOLD, 13));
            lblUsuario.setForeground(new Color(60, 60, 60));
            lblUsuario.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
            lblUsuario.setIconTextGap(8);
        }
        String nomeUsuario = usuarioLogado.getUsername();
        String perfilUsuario = usuarioLogado.getPerfil();
        userIcon = carregarIcone("user.png", 32, 32);
        userFotoIcon = carregarFotoPerfilUsuario(usuarioLogado);
        lblUsuario.setText("Usuário: " + nomeUsuario + "  |  Perfil: " +
                (perfilUsuario.isEmpty() ? "" : perfilUsuario.substring(0, 1).toUpperCase() + perfilUsuario.substring(1)));
        lblUsuario.setIcon(userFotoIcon != null ? userFotoIcon : userIcon);
    }

    public static void aplicarFonteRobotoEmTodosPainéis(JPanel[] paineis) {
        Font roboto = FonteUtils.carregarFonteRoboto(14f);
        for (JPanel painel : paineis) {
            if (painel != null) {
                aplicarFonteRecursivo(painel, roboto);
            }
        }
    }

    private static void aplicarFonteRecursivo(Container container, Font font) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel || comp instanceof JButton ||
                    comp instanceof JTextField || comp instanceof JTable ||
                    comp instanceof JTextArea || comp instanceof JComboBox ||
                    comp instanceof JFormattedTextField) {
                comp.setFont(font);
            }
            if (comp instanceof Container) {
                aplicarFonteRecursivo((Container) comp, font);
            }
        }
    }

    public static void alternarTemaFlatLaf(boolean darkMode) {
        try {
            if (darkMode) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alternar tema: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarLogout() {
        this.dispose();
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }

    private void abrirEditarPerfil() {
        EditarPerfilDialog dialog = new EditarPerfilDialog(this, usuarioLogado);
        dialog.setVisible(true);
    }
}
