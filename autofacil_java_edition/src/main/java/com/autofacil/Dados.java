package com.autofacil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

public class Dados {


    public static List<Aluguel> getListaAlugueis() {
        try {
            Type aluguelListType = new TypeToken<List<Aluguel>>(){}.getType();
            return GerenciadorArquivos.carregarDadosArquivo("data/alugueis.json", aluguelListType);
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados do arquivo data/alugueis.json:");
            e.printStackTrace();
            return List.of();
        }
    }


    public static List<Veiculo> getListaVeiculos() {
        try (Reader reader = new InputStreamReader(
                Dados.class.getResourceAsStream("/data/veiculos.json"))) {
            return new Gson().fromJson(reader, new TypeToken<List<Veiculo>>(){}.getType());
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados do arquivo data/veiculos.json:");
            e.printStackTrace();
            return List.of();
        }
    }

    public static List<Cliente> getListaClientes() {
        try (Reader reader = new InputStreamReader(
                Dados.class.getResourceAsStream("/data/clientes.json"))) {
            return new Gson().fromJson(reader, new TypeToken<List<Cliente>>(){}.getType());
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados do arquivo data/clientes.json:");
            e.printStackTrace();
            return List.of();
        }
    }
}
