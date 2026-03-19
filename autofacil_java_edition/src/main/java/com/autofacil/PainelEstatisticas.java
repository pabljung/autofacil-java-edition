package com.autofacil;

import javax.swing.*;
import java.awt.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.util.Map;

public class PainelEstatisticas extends JPanel {

    public PainelEstatisticas() {
        setLayout(new BorderLayout());

        JTabbedPane abas = new JTabbedPane();

        abas.add("Aluguéis por Mês", criarGraficoAlugueisPorMes());
        abas.add("Faturamento por Mês", criarGraficoFaturamentoPorMes());
        abas.add("Veículos Mais Alugados", criarGraficoVeiculosMaisAlugados());

        add(abas, BorderLayout.CENTER);
    }

    private JPanel criarGraficoAlugueisPorMes() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> alugueisPorMes = EstatisticasUtil.contarAlugueisPorMes();

        for (Map.Entry<String, Integer> entry : alugueisPorMes.entrySet()) {
            dataset.addValue(entry.getValue(), "Aluguéis", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Aluguéis por Mês",
                "Mês",
                "Quantidade",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        // Gradiente azul
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        GradientPaint gradient = new GradientPaint(
                0f, 0f, new Color(0, 120, 215),
                0f, 0f, new Color(0, 180, 255));
        renderer.setSeriesPaint(0, gradient);

        return new ChartPanel(chart);
    }

    private JPanel criarGraficoFaturamentoPorMes() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> faturamentoPorMes = EstatisticasUtil.calcularFaturamentoPorMes();

        for (Map.Entry<String, Double> entry : faturamentoPorMes.entrySet()) {
            dataset.addValue(entry.getValue(), "Faturamento", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Faturamento por Mês",
                "Mês",
                "Valor (R$)",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        // Gradiente verde
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        GradientPaint gradient = new GradientPaint(
                0f, 0f, new Color(34, 139, 34),
                0f, 0f, new Color(144, 238, 144));
        renderer.setSeriesPaint(0, gradient);

        return new ChartPanel(chart);
    }

    private JPanel criarGraficoVeiculosMaisAlugados() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> veiculosPopulares = EstatisticasUtil.contarAlugueisPorVeiculo();

        for (Map.Entry<String, Integer> entry : veiculosPopulares.entrySet()) {
            dataset.addValue(entry.getValue(), "Aluguéis", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Veículos Mais Alugados",
                "Veículo",
                "Quantidade",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        // Gradiente vermelho
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        GradientPaint gradient = new GradientPaint(
                0f, 0f, new Color(178, 34, 34),
                0f, 0f, new Color(240, 128, 128));
        renderer.setSeriesPaint(0, gradient);

        return new ChartPanel(chart);
    }
}
