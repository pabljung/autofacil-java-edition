package com.autofacil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivos {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String DATA_DIR = resolverDataDir();

    private static String resolverDataDir() {
        String userDir = System.getProperty("user.dir");

        String[] tentativas = {
                userDir + "/autofacil_java_edition/data/",   // ← esse vai funcionar
                userDir + "/data/",
                userDir + "/../autofacil_java_edition/data/",
        };

        for (String caminho : tentativas) {
            File dir = new File(caminho);
            if (dir.exists() && dir.isDirectory()) {
                System.out.println("[GerenciadorArquivos] data/ encontrada em: " + dir.getAbsolutePath());
                return dir.getAbsolutePath() + "/";
            }
        }

        System.err.println("[GerenciadorArquivos] AVISO: data/ não encontrada! user.dir = " + userDir);
        return userDir + "/autofacil_java_edition/data/";
    }

    public static <T> void salvarDados(List<T> dados, String nomeArquivo) {
        File file = new File(DATA_DIR + nomeArquivo);
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (FileWriter writer = new FileWriter(file)) {
                GSON.toJson(dados, writer);
            }
            System.out.println("Dados salvos em: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erro ao salvar " + file.getAbsolutePath() + ": " + e.getMessage());
        }
    }

    public static <T> List<T> carregarDados(String nomeArquivo, Type tipo) {
        File file = new File(DATA_DIR + nomeArquivo);
        System.out.println("Carregando: " + file.getAbsolutePath());
        try (FileReader reader = new FileReader(file)) {
            List<T> dados = GSON.fromJson(reader, tipo);
            return dados != null ? dados : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar " + file.getAbsolutePath() + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Mantido por compatibilidade com chamadas existentes
    public static <T> List<T> carregarDadosArquivo(String caminhoCompleto, Type tipo) {
        return carregarDados(new File(caminhoCompleto).getName(), tipo);
    }

    public static <T> List<T> carregarDadosRelativo(String nomeArquivo, Type tipo) {
        return carregarDados(nomeArquivo, tipo);
    }

    public static void atualizarFotoUsuarioNoJson(String username, String novoCaminhoFoto) {
        try {
            Type usuarioListType = new TypeToken<List<Usuario>>() {}.getType();
            List<Usuario> usuarios = carregarDados("usuarios.json", usuarioListType);

            if (usuarios == null) {
                usuarios = new ArrayList<>();
            }

            boolean encontrado = false;
            for (Usuario u : usuarios) {
                if (u.getUsername().equals(username)) {
                    u.setFotoPerfil(novoCaminhoFoto);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                System.out.println("Usuário não encontrado: " + username);
            }

            salvarDados(usuarios, "usuarios.json");
            System.out.println("Foto atualizada para o usuário: " + username);
        } catch (Exception ex) {
            System.err.println("Erro ao atualizar foto: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}