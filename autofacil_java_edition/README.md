# AutoFácil Java Edition - Sistema de Locadora de Veículos

## Descrição
Sistema desktop para gerenciamento de locadora de veículos desenvolvido em Java com interface gráfica Swing, temas modernos FlatLaf e persistência de dados em arquivos JSON.

## Tecnologias Utilizadas
- **Java 17+**: Linguagem principal
- **Swing**: Interface gráfica tradicional
- **FlatLaf**: Look and Feel moderno e personalizável (claro e escuro)
- **Gson**: Serialização e desserialização JSON
- **SwingX**: Componentes adicionais para UI (ex: DropShadowBorder)
- **Maven**: Gerenciamento de dependências e build

## Estrutura do Projeto
```
autofacil_java_edition/
├── src/main/java/com/autofacil/
│ ├── Veiculo.java # Classe de domínio para veículos
│ ├── Cliente.java # Classe de domínio para clientes
│ ├── Aluguel.java # Classe de domínio para aluguéis
│ ├── GerenciadorArquivos.java # Classe para persistência de dados
│ ├── JanelaPrincipal.java # Janela principal da aplicação (com abas)
│ ├── PainelInicioAdministrador.java # Painel inicial para administrador (dashboard)
│ ├── PainelCadastroVeiculo.java # Painel para cadastro de veículos
│ ├── PainelListagemVeiculos.java # Painel para listagem de veículos
│ ├── PainelCadastroCliente.java # Painel para cadastro de clientes
│ ├── PainelListagemCliente.java # Painel para listagem de clientes
│ ├── PainelRegistroAluguel.java # Painel para registro de aluguéis
│ ├── PainelDevolucaoAluguel.java # Painel para devolução de veículos
│ └── PainelListagemAlugueis.java # Painel para listagem de aluguéis
├── data/
│ ├── veiculos.json # Dados de exemplo de veículos
│ ├── clientes.json # Dados de exemplo de clientes
│ └── alugueis.json # Dados de exemplo de aluguéis
| └── historico_login.json # Histórico de logins (opcional)
| └── usuarios.json # Usuários (opcional)
├── lib/
├── RELATORIO.md # Relatório detalhado do projeto
└── README.md # Este arquivo

## Estrutura do Projeto

## Funcionalidades

### Dashboard Inicial (PainelInicioAdministrador)
- Visualização resumida dos dados da locadora:
  - Total de veículos cadastrados
  - Veículos disponíveis
  - Veículos alugados
  - Total de clientes
  - Total de aluguéis registrados
  - Aluguéis ativos (em andamento)

### Gerenciamento de Veículos
- Cadastro completo de veículos (placa, marca, modelo, ano, cor, preço diária)
- Listagem e controle de status (Disponível / Alugado)

### Gerenciamento de Clientes
- Cadastro e listagem de clientes com dados essenciais (CPF, nome, telefone, email)

### Gerenciamento de Aluguéis
- Registro de aluguéis com validação automática
- Registro de devoluções com cálculo automático do valor devido
- Listagem do histórico completo de aluguéis

### Interface e Experiência
- Temas claros e escuros via FlatLaf
- Feedback visual e mensagens de erro/sucesso
- Ícones personalizados e foto de perfil do usuário
- Bordas com sombra nos cards usando SwingX DropShadowBorder para visual moderno

## Como Executar

### Pré-requisitos
- JDK 17 ou superior (recomendado JDK 17 ou 24 para preview features)
- Maven instalado

### Compilação e execução via Maven
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.autofacil.JanelaPrincipal"
```
## Dados de Exemplo
O projeto inclui arquivos JSON com dados de exemplo:
- **veiculos.json**: 3 veículos pré-cadastrados
- **clientes.json**: 3 clientes pré-cadastrados
- **alugueis.json**: Arquivo vazio para novos aluguéis

## Arquitetura

### Classes de Domínio
- **Veiculo**: Representa um veículo da locadora
- **Cliente**: Representa um cliente da locadora
- **Aluguel**: Representa um contrato de aluguel

### Persistência
- **GerenciadorArquivos**: Gerencia a serialização/desserialização de dados em JSON

### Interface Gráfica
- **JanelaPrincipal**: Janela principal com abas organizadas
- **Painéis**: Cada funcionalidade possui seu próprio painel especializado

## Características Técnicas
- **Encapsulamento**: Atributos privados com getters/setters
- **Validações**: Verificação de dados antes de operações críticas
- **Feedback**: Mensagens de sucesso/erro para o usuário
- **Modularidade**: Código organizado em classes especializadas

## Autor
Desenvolvido como projeto educacional para demonstrar conceitos de:
- Programação Orientada a Objetos em Java
- Desenvolvimento de interfaces gráficas com Swing
- Persistência de dados em formato JSON

## Licença
Este projeto é destinado para fins educacionais.
