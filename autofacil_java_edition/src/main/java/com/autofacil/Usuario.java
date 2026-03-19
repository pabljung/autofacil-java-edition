package com.autofacil;

public class Usuario {
    private String username;
    private String senha;
    private String perfil;
    private String email;
    private String caminhoFoto; // compatibilidade antiga
    private String fotoPerfil;  // novo campo para imagem de perfil

    public Usuario(String username, String senha, String perfil) {
        this.username = username;
        this.senha = senha;
        this.perfil = perfil != null ? perfil.toLowerCase() : "";
        this.email = "";
        this.caminhoFoto = "";
        this.fotoPerfil = "";
    }

    // Sobrecarga para incluir email, caminhoFoto e fotoPerfil (opcional)
    public Usuario(String username, String senha, String perfil, String email, String caminhoFoto, String fotoPerfil) {
        this.username = username;
        this.senha = senha;
        this.perfil = perfil != null ? perfil.toLowerCase() : "";
        this.email = email != null ? email : "";
        this.caminhoFoto = caminhoFoto != null ? caminhoFoto : "";
        this.fotoPerfil = fotoPerfil != null ? fotoPerfil : "";
    }

    public String getUsername() {
        return username;
    }

    public String getSenha() {
        return senha;
    }

    public String getPerfil() {
        return perfil;
    }

    public String getEmail() {
        return email;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
