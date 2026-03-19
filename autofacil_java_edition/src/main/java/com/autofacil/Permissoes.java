package com.autofacil;

import java.util.Arrays;

public class Permissoes {
    public static final String ADMIN = "admin";
    public static final String GERENTE = "gerente";
    public static final String USUARIO = "usuario";
    public static final String VISITANTE = "visitante";

    // Permissões específicas para cada perfil
    public static final String[] PERMISSOES_ADMIN = {"gerenciar_usuarios", "visualizar_relatorios", "configurar_sistema", "cadastrar_veiculo", "cadastrar_cliente", "registrar_aluguel", "registrar_devolucao"};
    public static final String[] PERMISSOES_GERENTE = {"visualizar_relatorios", "gerenciar_equipe", "cadastrar_veiculo", "cadastrar_cliente", "registrar_aluguel", "registrar_devolucao"};
    public static final String[] PERMISSOES_USUARIO = {"visualizar_dados_pessoais"};
    public static final String[] PERMISSOES_VISITANTE = {}; // Visitante sem permissões especiais

    // Método para verificar se um usuário tem uma permissão específica
    public static boolean temPermissao(String perfil, String permissao) {
        if (perfil == null) return false;
        switch (perfil.toLowerCase()) {
            case ADMIN:
                return true; // Admin tem todas as permissões
            case GERENTE:
                return Arrays.asList(PERMISSOES_GERENTE).contains(permissao);
            case USUARIO:
                return Arrays.asList(PERMISSOES_USUARIO).contains(permissao);
            case VISITANTE:
                return Arrays.asList(PERMISSOES_VISITANTE).contains(permissao);
            default:
                return false; // Perfil desconhecido não tem permissões
        }
    }

    // Métodos específicos usados na JanelaPrincipal

    public static boolean podeCadastrarVeiculo(Usuario usuarioLogado) {
        return temPermissao(usuarioLogado.getPerfil(), "cadastrar_veiculo");
    }

    public static boolean podeCadastrarCliente(Usuario usuarioLogado) {
        return temPermissao(usuarioLogado.getPerfil(), "cadastrar_cliente");
    }

    public static boolean podeRegistrarAluguel(Usuario usuarioLogado) {
        return temPermissao(usuarioLogado.getPerfil(), "registrar_aluguel");
    }

    public static boolean podeRegistrarDevolucao(Usuario usuarioLogado) {
        return temPermissao(usuarioLogado.getPerfil(), "registrar_devolucao");
    }
}
