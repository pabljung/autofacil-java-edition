package com.autofacil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class EstatisticasUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static List<Aluguel> alugueis = Dados.getListaAlugueis();

    public static Map<String, Integer> contarAlugueisPorMes() {
        Map<String, Integer> mapa = new TreeMap<>();

        for (Aluguel aluguel : alugueis) {
            try {
                LocalDate data = LocalDate.parse(aluguel.getDataSaida(), formatter);
                String mesAno = String.format("%02d/%d", data.getMonthValue(), data.getYear());
                mapa.put(mesAno, mapa.getOrDefault(mesAno, 0) + 1);
            } catch (Exception e) {
                System.err.println("Erro ao processar data de saída: " + aluguel.getDataSaida());
            }
        }

        return mapa;
    }

    public static Map<String, Double> calcularFaturamentoPorMes() {
        Map<String, Double> mapa = new TreeMap<>();

        for (Aluguel aluguel : alugueis) {
            try {
                LocalDate data = LocalDate.parse(aluguel.getDataSaida(), formatter);
                String mesAno = String.format("%02d/%d", data.getMonthValue(), data.getYear());

                double valor = aluguel.getValorTotal();
                mapa.put(mesAno, mapa.getOrDefault(mesAno, 0.0) + valor);
            } catch (Exception e) {
                System.err.println("Erro ao processar valor de aluguel ou data: " + aluguel.getDataSaida());
            }
        }

        return mapa;
    }

    public static Map<String, Integer> contarAlugueisPorVeiculo() {
        Map<String, Integer> mapa = new HashMap<>();

        for (Aluguel aluguel : alugueis) {
            Veiculo v = aluguel.getVeiculo();
            String nomeVeiculo = v.getMarca() + " " + v.getModelo();

            mapa.put(nomeVeiculo, mapa.getOrDefault(nomeVeiculo, 0) + 1);
        }

        return mapa.entrySet()
                .stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }
}
