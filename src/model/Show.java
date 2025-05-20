package model;

// Imports necessários para operações com Banco de Dados e manipulação de coleções
import java.sql.Connection; // Faz a conexão com o Banco de Dados
import java.sql.PreparedStatement; // Executa consultas SQL 
import java.sql.ResultSet; // Armazena os resultados das consultas SQL
import java.sql.SQLException; // Faz o tratamento das exceções relacionadas ao Banco de Dados
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import dao.Conexao; // Importa a classe que faz a conexão com o Banco de Dados

public class Show {

    // Variáveis da classe que representam os atributos dos shows
    public int id; // Identificador único do show
    public int codGenero; // Código do gênero associado ao show
    public int codLocal; // Código do local associado ao show
    public String nome; // Nome do show
    public String data; // Data do show
    public String link; // Link associado à venda do ingresso do show

    // Construtor vazio
    public Show() {
    }

    // Construtor com todos os argumentos
    public Show(int id, String nome, String data, int codGenero, int codLocal, String link) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.codGenero = codGenero;
        this.codLocal = codLocal;
        this.link = link;
    }

    /**
     * Método estático para obter todos os shows do banco de dados.
     * 
     * @return lista de shows obtidos do banco de dados.
     */
    public static List<Show> getShows() {
        List<Show> lista = new ArrayList<>(); // Lista para armazenar os shows que estão cadastrados no banco de dados
        String sql = "SELECT id, nome, data, codGenero, codLocal, link FROM shows ORDER BY nome";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Laço de repetição que extrai e itera as informações dos shows
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String data = rs.getString("data");
                int codGenero = rs.getInt("codGenero");
                int codLocal = rs.getInt("codLocal");
                String link = rs.getString("link");

                // Usando esse construtor, é criado um novo objeto com os dados extraídos
                Show show = new Show(id, nome, data, codGenero, codLocal, link);
                lista.add(show); // E depois é adicionado na lista onde estão todos os shows
            }
        } catch (SQLException e) {
            // Trata a exceção e informa por uma mensagem caso haja erro na operação
            JOptionPane.showMessageDialog(null, "Erro ao obter shows: " + e.getMessage());
        }
        return lista; // Retorna a lista de shows recuperados do banco de dados
    }

    /**
     * Método estático para cadastrar um novo show.
     * Este método coleta os dados necessários do usuário e chama o método para
     * inseri-los no banco de dados.
     */
    public static void cadastrarShow() {
        String nome = JOptionPane.showInputDialog("Digite o nome do show:");
        String data = JOptionPane.showInputDialog("Digite a data do show (DD/MM):");

        // Obtém o código do gênero
        String codGeneroStr = Genero.verificarOuCadastrar();
        if (codGeneroStr == null) {
            JOptionPane.showMessageDialog(null, "Operação cancelada.");
            return; // Retorna se o usuário cancelar a operação
        }
        int codGenero = Integer.parseInt(codGeneroStr);

        // Obtém o código do local
        String codLocalStr = Local.verificarOuCadastrar();
        if (codLocalStr == null) {
            JOptionPane.showMessageDialog(null, "Operação cancelada.");
            return; // Retorna se o usuário cancelar a operação
        }
        int codLocal = Integer.parseInt(codLocalStr);

        String link = JOptionPane.showInputDialog("Digite o link do show:");

        // Cria um novo objeto Show com os dados fornecidos
        Show show = new Show(0, nome, data, codGenero, codLocal, link);

        // Chama o método estático para cadastrar o show no banco de dados
        cadastrar(show);
    }

    /**
     * Método estático para inserir um show no banco de dados.
     * 
     * @param show objeto que contém os dados do show a ser cadastrado
     */
    public static void cadastrar(Show show) {
        String sql = "INSERT INTO shows (nome, data, codGenero, codLocal, link) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, show.nome);
            ps.setString(2, show.data);
            ps.setInt(3, show.codGenero);
            ps.setInt(4, show.codLocal);
            ps.setString(5, show.link);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Show cadastrado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar show: " + e.getMessage());
        }
    }

    /**
     * Método para montar e formatar uma String com um show e seus detalhes.
     * 
     * @return uma string com os shows formatados.
     */
    public static String montarStringShows() {
        List<Show> shows = getShows(); // Obtém a lista de shows diretamente
        List<Genero> generos = Genero.getGeneros();
        List<Local> locais = Local.getLocais();

        StringBuilder sb = new StringBuilder(); // Usado para formatar as informações
        for (Show show : shows) {
            // Obter o nome do gênero ID
            String nomeGenero = generos.stream()
                    .filter(g -> g.id == show.codGenero) // Filtra os generos pelo ID do show
                    .map(g -> g.nome) // Mapeia para o nome do gênero
                    .findFirst() // Obtém o primeiro gênero que o filtro encontra
                    .orElse("Desconhecido"); // Caso não encontre, retorna essa mensagem

            // Obter o nome do local pelo ID
            String nomeLocal = locais.stream()
                    .filter(l -> l.id == show.codLocal)
                    .map(l -> l.nome)
                    .findFirst()
                    .orElse("Desconhecido");

            // Aqui é onde a formatação é feita
            // Linha 1: Nome do show e local
            sb.append(show.nome).append(" - ").append(nomeLocal).append("\n");

            // Linha 2: Data do show e gênero
            sb.append(show.data).append(" - ").append(nomeGenero).append("\n");

            // Linha 3: Link completo
            sb.append(show.link).append("\n");

            // Linha em branco para separar os shows
            sb.append("\n");
        }
        return sb.toString(); // Retorna a string com todos os shows formatados
    }

    /**
     * Método para montar e formatar uma String com os shows filtrados por gênero.
     * 
     * @param idGenero código do gênero que será filtrado
     * @return uma string com os shows filtrados pelo gênero e formatados.
     */
    public static String montarStringShowsPorGenero(int idGenero) {
        List<Show> shows = getShows(); // Obtém a lista de shows do banco de dados
        List<Genero> generos = Genero.getGeneros();
        List<Local> locais = Local.getLocais();

        StringBuilder sb = new StringBuilder();
        for (Show show : shows) {
            if (show.codGenero == idGenero) { // Filtra os shows e retorna somente o gênero selecionado
                // Obter o nome do gênero pelo ID
                String nomeGenero = generos.stream()
                        .filter(g -> g.id == show.codGenero)
                        .map(g -> g.nome)
                        .findFirst()
                        .orElse("Desconhecido");
                
                // Obtem o nome do local pelo ID
                String nomeLocal = locais.stream()
                        .filter(l -> l.id == show.codLocal)
                        .map(l -> l.nome)
                        .findFirst()
                        .orElse("Desconhecido");

                // Formatação da String
                // Linha 1: Nome do show e local
                sb.append(show.nome).append(" - ").append(nomeLocal).append("\n");

                // Linha 2: Data do show e gênero
                sb.append(show.data).append(" - ").append(nomeGenero).append("\n");

                // Linha 3: Link completo
                sb.append(show.link).append("\n");

                // Linha em branco para separar os shows
                sb.append("\n");
            }
        }
        return sb.toString(); // Retorna a string com o show formatado e filtrado pelo gênero
    }
}