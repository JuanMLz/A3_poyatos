# A3 DE GESTÃO E QUALIDADE DE SOFTWARE

## Professores

- Henrique Poyatos
- Magda Miyashiro

## Alunos

- Luís Felipe Moura Borges RA: 824154663
- Juan Moraes Lopes RA: 12523219000
- Gabriel Parra Boito RA: 824152876
- Cristiano Dario de Azevedo Filho RA: 823216267
- Lucas Henrique Rocha Lourenço RA: 323224861
- Vinícius Di Franco Heitor RA: 12524126336

---

## Descrição do Projeto

Este projeto tem como objetivo a **refatoração de um código de baixa qualidade**. O código original foi escrito de forma simples, com diversas oportunidades de melhoria em termos de estrutura, legibilidade, desempenho, design e manutenibilidade. Nossa tarefa nesta A3 é refatorar esse código para aplicarmos boas práticas de desenvolvimento de software.

### Código Original

O código enviado como base para o trabalho gerencia shows, gêneros musicais e locais, permitindo que o usuário visualize, pesquise e cadastre novos shows. O sistema funciona através de um menu interativo que apresenta as opções de visualizar, pesquisar ou cadastrar shows, gêneros e locais.

#### Funcionalidades principais do código original:

- Menu Principal: Exibe as opções para o usuário.

<details>
  <summary>Imagem do Menu Principal</summary>
  <img src="https://snipboard.io/6OgXui.jpg" alt="Imagem do Menu Principal" width="400px">
</details>

- Mostrar Shows: Exibe todos os shows cadastrados.

<details>
  <summary>Imagem de Mostrar Shows</summary>
  <img src="https://snipboard.io/RjTflr.jpg" alt="Imagem de Mostrar Shows" width="400px">
</details>

- Menu de Cadastro: Exibe o menu de cadastro de shows, gêneros e locais.

<details>
  <summary>Imagem do Menu de Cadastro</summary>
  <img src="https://snipboard.io/ko6Vjs.jpg" alt="Imagem do Menu de Cadastro" width="400px">
</details>

O código original apresentava falhas em termos de organização, clareza e design, tornando-o um bom candidato para refatoração.

---

## Objetivos da Refatoração

A refatoração deste código visa melhorar os seguintes aspectos:

1. **Estrutura do Código:** Tornar o código mais organizado, modular e de fácil entendimento.
2. **Legibilidade:** Garantir que o código seja mais fácil de ler.
3. **Desempenho:** Melhorar a performance do código, eliminando redundâncias e otimizando partes que possam ser mais eficientes.
4. **Manutenibilidade:** Facilitar futuras manutenções e expansões do código, aplicando boas práticas como a utilização de métodos e classes bem definidas.
5. **Melhora do Visual:** Melhorar a interface do usuário, tornando-a mais visualmente agradável.

---

## Melhorias Implementadas

- Novo Menu Principal: Exibe as opções para o usuário.

<details>
  <summary>Imagem do Novo Menu Principal</summary>
  <img src="https://snipboard.io/OtWgQh.jpg" alt="Imagem do Novo Menu Principal" width="400px">
</details>

- Novo Mostrar Shows: Exibe todos os shows cadastrados.

<details>
  <summary>Imagem do Novo Mostrar Shows</summary>
  <img src="https://snipboard.io/In1ldA.jpg" alt="Imagem do Novo Mostrar Shows" width="400px">
</details>

- Menu de Cadastro: Exibe o menu de cadastro de shows, gêneros e locais.

<details>
  <summary>Imagem do Novo Menu de Cadastro</summary>
  <img src="https://snipboard.io/y1Gk2D.jpg" alt="Imagem do Novo Menu de Cadastro" width="400px">
</details>

**Durante a refatoração, diversas melhorias foram implementadas para otimizar o código e aprimorar a experiência do usuário. As principais melhorias incluem:**

- Criação de uma branch separada para manter o código antigo isolado do código novo, facilitando o controle de versões e testes sem impactar a base original.
- Correção de erros de sintaxe em alguns scripts do código `Main` da interface gráfica, garantindo que a aplicação funcione corretamente sem travamentos ou erros inesperados.
- Organização do projeto em pastas específicas, com a criação da pasta `bin` para armazenar os executáveis do programa, promovendo uma melhor estrutura de diretórios e facilitando a execução.
- Implementação da pasta `lib`, contendo scripts responsáveis pela conexão com o banco de dados, o que modulariza e isola a lógica de persistência de dados do restante da aplicação.
- Criação e configuração do banco de dados mysql pelo console do aws, estabelecendo a conexão com o banco para realizar operações de armazenamento e consulta dos dados referentes aos shows, gêneros musicais e locais.
- Melhoria na modularização e legibilidade do código, com a utilização de funções e classes bem definidas, tornando o código mais fácil de entender e manter.
- Aprimoramento da interface do usuário, com menus interativos e visualmente mais organizados para facilitar a navegação e usabilidade do sistema.
- Implementação de comentarios em todo o codigo.
- **Criação de testes unitários utilizando JUnit** para validar as funcionalidades do código. Os testes foram realizados para as funcionalidades de **Gênero**, **Local** e **Show**, garantindo que o comportamento do sistema permanecesse correto após a refatoração.

---

## Testes Unitários

Foram criados testes unitários utilizando **JUnit** para validar as funcionalidades de **Gênero**, **Local** e **Show**. Abaixo estão os principais testes realizados:

### Gênero

1. **Testar recuperação de gêneros do banco de dados**: Verifica se o método `getGeneros()` retorna corretamente a lista de gêneros.
2. **Tratar exceções ao recuperar gêneros**: Testa o comportamento do sistema ao ocorrer um erro de banco de dados, garantindo que a lista seja vazia e uma mensagem de erro seja exibida.
3. **Cadastrar novo gênero**: Verifica se o método `cadastrarGeneroDialog()` permite o cadastro de um novo gênero e retorna o ID correto.
4. **Tentar cadastrar gênero já existente**: Verifica se o sistema impede o cadastro de um gênero que já está no banco de dados.

Esses testes ajudam a garantir a confiabilidade das funcionalidades relacionadas aos gêneros musicais no sistema.

### Local

1. **Testar recuperação de locais do banco de dados**: Verifica se o método `getLocais()` retorna corretamente a lista de locais.
2. **Tratar exceções ao recuperar locais**: Testa o comportamento do sistema ao ocorrer um erro de banco de dados, garantindo que a lista seja vazia e uma mensagem de erro seja exibida.
3. **Cadastrar novo local**: Verifica se o método `cadastrarLocalDialog()` permite o cadastro de um novo local e retorna o ID correto.
4. **Tentar cadastrar local já existente**: Verifica se o sistema impede o cadastro de um local que já está no banco de dados.
5. **Entrada cancelada no cadastro de local**: Verifica se o sistema lida corretamente quando o usuário cancela a operação de cadastro de um local.

Esses testes ajudam a garantir a confiabilidade das funcionalidades relacionadas aos locais no sistema.

### Show

1. **Testar recuperação de shows do banco de dados**: Verifica se o método `getShows()` retorna corretamente a lista de shows.
2. **Cadastrar novo show**: Verifica se o método `cadastrar()` insere um novo show no banco de dados corretamente.
3. **Remover show**: Verifica se o método `removerShow()` exclui corretamente um show do banco de dados.

Esses testes ajudam a garantir a confiabilidade das funcionalidades relacionadas aos shows no sistema.

---

## Conclusão

A refatoração realizada teve como objetivo melhorar a organização, legibilidade, desempenho e manutenibilidade do código, além de proporcionar uma interface mais amigável para o usuário. Os testes unitários garantiram que as funcionalidades do sistema permanecessem intactas após a refatoração, promovendo maior confiança no desenvolvimento contínuo do projeto.

---
