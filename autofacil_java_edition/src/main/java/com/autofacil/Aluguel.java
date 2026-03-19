package com.autofacil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class Aluguel {
    private int idAluguel;
    private Veiculo veiculo;
    private Cliente cliente;
    private String dataSaida;
    private String dataPrevistaDevolucao;
    private String dataDevolucaoEfetiva;
    private double valorTotal;
    private String statusAluguel;

    public Aluguel(int idAluguel, Veiculo veiculo, Cliente cliente, String dataSaida, String dataPrevistaDevolucao) {
        this.idAluguel = idAluguel;
        this.veiculo = veiculo;
        this.cliente = cliente;
        this.dataSaida = dataSaida;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.statusAluguel = "Ativo";
    }

    public int getIdAluguel() {
        return idAluguel;
    }

    public void setIdAluguel(int idAluguel) {
        this.idAluguel = idAluguel;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(String dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(String dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public String getDataDevolucaoEfetiva() {
        return dataDevolucaoEfetiva;
    }

    public void setDataDevolucaoEfetiva(String dataDevolucaoEfetiva) {
        this.dataDevolucaoEfetiva = dataDevolucaoEfetiva;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatusAluguel() {
        return statusAluguel;
    }

    public void setStatusAluguel(String statusAluguel) {
        this.statusAluguel = statusAluguel;
    }

    public void finalizarAluguel(String dataDevolucaoEfetivaStr) {
        try {
            // Faz o parse da data de devolução com formatos flexíveis
            LocalDate dataDevolucaoEfetiva = DateParserUtil.parseDataFlexivel(dataDevolucaoEfetivaStr);

            // Também parse da data de saída, assumindo padrão dd/MM/yyyy (ajuste se precisar)
            LocalDate dataSaidaLocal = DateParserUtil.parseDataFlexivel(this.dataSaida);

            // Calcula diferença de dias entre saída e devolução
            long dias = ChronoUnit.DAYS.between(dataSaidaLocal, dataDevolucaoEfetiva);
            if (dias <= 0) {
                dias = 1; // mínimo 1 dia para cobrança
            }

            this.dataDevolucaoEfetiva = dataDevolucaoEfetiva.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            this.valorTotal = dias * this.veiculo.getPrecoDiaria();
            this.statusAluguel = "Concluído";

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data inválida: " + dataDevolucaoEfetivaStr);
        }
    }

    public CharSequence getDataDevolucao() {
        return dataDevolucaoEfetiva != null ? dataDevolucaoEfetiva : "Pendente";
    }

    public String getCpfCliente() {
        return cliente != null ? cliente.getCpf() : "Cliente não definido";


    }
}



