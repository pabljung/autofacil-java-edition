package com.autofacil;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class FonteUtils {

    public static Font carregarFonteRoboto(float tamanho) {
        try (InputStream is = FonteUtils.class.getClassLoader().getResourceAsStream("fonts/Roboto-Regular.ttf")) {
            if (is == null) {
                System.err.println("Arquivo de fonte não encontrado no classpath em fonts/Roboto-Regular.ttf");
                return new Font("SansSerif", Font.PLAIN, (int) tamanho);
            }
            Font fonte = Font.createFont(Font.TRUETYPE_FONT, is);
            return fonte.deriveFont(Font.PLAIN, tamanho);
        } catch (FontFormatException | IOException e) {
            System.err.println("Erro ao carregar fonte Roboto: " + e.getMessage());
            return new Font("SansSerif", Font.PLAIN, (int) tamanho);
        }
    }
}
