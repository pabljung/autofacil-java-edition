# Relatório do Projeto AutoFácil Java Edition

## Introdução

Este relatório documenta o desenvolvimento do sistema **AutoFácil Java Edition**, uma aplicação desktop para gerenciamento de uma locadora de veículos. O projeto foi desenvolvido como parte de um trabalho prático para disciplinas iniciais do curso de Engenharia da Computação, com o objetivo de aplicar os conceitos de Programação Orientada a Objetos, interfaces gráficas com Java Swing e persistência de dados usando arquivos JSON.

## Objetivos do Projeto

* Desenvolver um sistema funcional que simula a operação de uma locadora de veículos.
* Praticar a organização de código em camadas (domínio, interface, persistência).
* Utilizar componentes gráficos e aplicar lógica de negócio.
* Persistir dados em arquivos JSON com leitura e escrita automáticas.

## Tecnologias Utilizadas

* **Java**: linguagem principal do projeto.
* **Java Swing**: biblioteca gráfica utilizada para a interface do usuário.
* **Gson** (Google): biblioteca para serialização/desserialização de objetos Java para JSON.
* **FlatLaf**: biblioteca para aplicar temas modernos à interface gráfica.
* **IDE**: IntelliJ IDEA e Visual Studio Code foram utilizados em diferentes momentos do projeto.

## Estrutura do Projeto

```
autofacil_java_edition/
├── src/com/autofacil/
│   ├── dominio/
│   │   ├── Veiculo.java
│   │   ├── Cliente.java
│   │   └── Aluguel.java
│   ├── gui/
│   │   ├── JanelaPrincipal.java
│   │   ├── PainelCadastroVeiculo.java
│   │   ├── PainelListagemVeiculos.java
│   │   ├── PainelCadastroCliente.java
│   │   ├── PainelListagemCliente.java
│   │   ├── PainelRegistroAluguel.java
│   │   ├── PainelDevolucaoAluguel.java
│   │   └── PainelListagemAlugueis.java
│   ├── persistencia/
│   │   ├── Dados.java
│   │   └── GerenciadorArquivos.java
│   ├── util/
│   │   └── FonteUtils.java
│   └── Dados.java
├── data/
│   ├── veiculos.json
│   ├── clientes.json
│   └── alugueis.json
└── README.md
```

## Classes Implementadas

### Classes de Domínio

* **Veiculo.java**
  Representa um veículo com placa, marca, modelo, ano, cor, preço da diária e status (disponível ou alugado).

* **Cliente.java**
  Contém CPF, nome, telefone e email do cliente.

* **Aluguel.java**
  Representa um aluguel com ID, cliente, veículo, datas de saída/devolução e valor total.

### Classes de Interface (GUI)

* **JanelaPrincipal.java**: gerencia as abas e menus do sistema.
* **Painéis de Veículo/Cliente/Aluguel**: controlam cadastros, listagens e formulários.

### Persistência

* **GerenciadorArquivos.java**: salva e carrega dados em JSON.
* **Dados.java**: carrega listas iniciais do classpath para testes.

## Funcionalidades

* **Cadastro de veículos e clientes**
* **Registro de aluguéis com cálculo automático do valor total**
* **Registro de devolução com atualização de status**
* **Listagens em tabelas com fonte personalizada**
* **Foto de perfil de usuário e troca manual**
* **Tema claro/escuro com FlatLaf**
* **Validação de dados na interface**
* **Sistema de login e perfis com permissões diferentes**

## Pontos Fortes

1. **Separacão clara entre camadas** (domínio, GUI e persistência).
2. **Interface amigável com menus e ícones personalizados.**
3. **Validações preventivas** para evitar dados inconsistentes.
4. **Atualização em tempo real da interface após alterações.**
5. **Uso de padrões modernos de GUI com FlatLaf.**

## Desafios Encontrados

* Garantir que os dados fossem atualizados corretamente entre diferentes painéis.
* Sincronizar a interface com os dados JSON (leitura/gravação).
* Corrigir inconsistências ao atualizar objetos aninhados como veículos dentro de aluguéis.
* Entender a hierarquia correta dos componentes Swing (tabelas, abas, painéis).
* Lidar com conflitos de arquivos entre os diretórios `data` do classpath e sistema de arquivos local.

## Prompts Utilizados com a IA

Durante o desenvolvimento, foram utilizados diversos prompts com o ChatGPT para solucionar problemas, melhorar funcionalidades e gerar ideias. Abaixo alguns exemplos:

* "Quero adicionar filtros avançados à listagem de aluguéis no Java Swing usando JComboBox e JTextField."
* "Como faço para trocar o ícone de usuário por uma imagem personalizada escolhida pelo JFileChooser?"
* "Corrija esse código onde tento formatar o CPF na JTable."
* "Meu JSON está desatualizado, pode verificar se o erro está na leitura dos dados?"
* "Adicione exemplos realistas no meu arquivo alugueis.json."
* "Como aplicar uma fonte personalizada Roboto em todos os componentes Swing?"
* "Quero criar uma tela de início com dados estatísticos para administradores."
* "Me ajude a criar uma estrutura de permissões para perfis no sistema."
* "Estou com problemas ao sincronizar a foto do perfil após edição. Pode revisar o método?"

Esses prompts auxiliaram na implementação de soluções modernas, guiando a escrita de código mais limpo e eficaz.

## Melhorias Futuras

* **Sistema de busca e ordenação por múltiplos critérios.**
* **Dashboard com gráficos de faturamento e utilização.**
* **Validações reforçadas (ex: CPF válido, data lógica).**
* **Relatórios exportáveis (PDF/Excel).**
* **Backup automático e versão online (com banco de dados).**

## Conclusão

O AutoFácil Java Edition é um projeto completo, funcional e com uma base sólida. Ele demonstra de forma prática a importância da organização modular, da interface amigável e do uso de bibliotecas externas para enriquecer a experiência do usuário. Além disso, o uso da inteligência artificial auxiliou diretamente na resolução de desafios técnicos e no aprimoramento de funcionalidades, otimizando o tempo de desenvolvimento e a qualidade do produto final.
