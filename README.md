# 🚗 AutoFácil Java Edition

Sistema desktop de **locadora de veículos** desenvolvido em **Java**, com interface gráfica utilizando **Swing** e persistência de dados em **JSON (Gson)**.

## 📌 Sobre o Projeto

O **AutoFácil Java Edition** é uma aplicação desenvolvida com o objetivo de simular a gestão completa de uma locadora de veículos, permitindo o controle de clientes, carros e aluguéis de forma prática e intuitiva.

O sistema foi desenvolvido com foco em aprendizado, aplicando conceitos de:
- Programação Orientada a Objetos (POO)
- Interface gráfica com Java Swing
- Manipulação de arquivos JSON
- Organização em camadas

---

## 🛠️ Tecnologias Utilizadas

- **Java (JDK 8+)**
- **Swing (GUI)**
- **Gson (Google)** → para persistência de dados em JSON
- **VS Code / IntelliJ (opcional)**

---

## ⚙️ Funcionalidades

### 👤 Gestão de Clientes
- Cadastro de clientes
- Edição de informações
- Listagem de clientes
- Busca e filtros

### 🚘 Gestão de Veículos
- Cadastro de veículos
- Controle de disponibilidade
- Listagem de carros

### 📄 Gestão de Aluguéis
- Registro de aluguel
- Cálculo de valor total
- Histórico de aluguéis
- Filtros avançados (por data, cliente, valor, etc.)

### 📊 Dashboard (Admin)
- Total de veículos
- Veículos alugados
- Total de clientes
- Total de aluguéis

### 🔍 Extras
- Sistema de login
- Upload de foto de perfil
- Interface atualizada em tempo real
- Filtros avançados e ordenação

---

## 📁 Estrutura do Projeto

autofacil_java_edition/
│
├── data/ # Arquivos JSON (persistência)
│
├── src/main/java/com/autofacil/
│ ├── Main.java
│ ├── JanelaPrincipal.java
│
│ ├── Cliente.java
│ ├── Aluguel.java
│ ├── Dados.java
│
│ ├── GerenciadorArquivos.java
│ ├── EstatisticasUtil.java
│ ├── DateParserUtil.java
│
│ ├── PainelCadastroCliente.java
│ ├── PainelCadastroVeiculo.java
│ ├── PainelDevolucaoAluguel.java
│ ├── PainelEstatisticas.java
│
│ └── EditarPerfilDialog.java

---

## 💾 Persistência de Dados

Os dados são armazenados em arquivos **JSON**, incluindo:

- Clientes
- Veículos
- Aluguéis

Vantagens:
- Simples de usar
- Fácil de visualizar e editar
- Não requer banco de dados externo

---

## 🎯 Objetivos do Projeto

- Consolidar conhecimentos em Java
- Desenvolver aplicações desktop completas
- Trabalhar com manipulação de dados
- Simular um sistema real de gestão

---

## 🚀 Melhorias Futuras

- Refatoração para padrão MVC
- Integração com banco de dados (MySQL/PostgreSQL)
- API com Spring Boot
- Versão web do sistema
- Relatórios em PDF

---

## 👨‍💻 Autor

**Pablo**  
Estudante de Engenharia da Computação 🚀

---

## 📄 Licença

Projeto desenvolvido para fins acadêmicos.
