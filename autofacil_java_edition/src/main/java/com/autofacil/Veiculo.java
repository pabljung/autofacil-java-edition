package com.autofacil;

public class Veiculo {
    private String placa;
    private String marca;
    private String modelo;
    private int ano;
    private String cor;
    private double precoDiaria;
    private String status;

    public Veiculo(String placa, String marca, String modelo, int ano, String cor, double precoDiaria) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.cor = cor;
        this.precoDiaria = precoDiaria;
        this.status = "Disponível";
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public double getPrecoDiaria() {
        return precoDiaria;
    }

    public void setPrecoDiaria(double precoDiaria) {
        this.precoDiaria = precoDiaria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void alugar() {
        this.status = "Alugado";
    }

    public void devolver() {
        this.status = "Disponível";
    }

    public void setDisponivel(boolean b) {
        if (b) {
            this.status = "Disponível";
        } else {
            this.status = "Indisponível";

        }
    }

    public void setKilometragem(long kilometragemAtual) {

    }
}


