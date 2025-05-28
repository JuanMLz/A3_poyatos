package model;

// Imports necessários para operações com Banco de Dados e manipulação de coleções
import java.sql.Connection; // Faz a conexão com o Banco de Dados
import java.sql.PreparedStatement; // Executa consultas SQL 
import java.sql.ResultSet; // Armazena os resultados das consultas SQL
import java.sql.SQLException; // Faz o tratamento das exceções relacionadas ao Banco de Dados
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;

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
    
public static void removerShow(Show show) {
    String sql = "DELETE FROM shows WHERE id = ?";
    try (Connection conn = Conexao.getConexao();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, show.id);
        int linhasAfetadas = ps.executeUpdate();

        if (linhasAfetadas > 0) {
            JOptionPane.showMessageDialog(null, "Show removido com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Show não encontrado ou já foi removido.");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Erro ao remover show: " + e.getMessage());
    }
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
    // Cria painel com layout vertical para os campos
    JPanel painel = new JPanel();
    painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
    painel.setBackground(new Color(245, 245, 245));
    painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Label e campo para nome do show
    JLabel labelNome = new JLabel("Nome do show:");
    labelNome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    JTextField campoNome = new JTextField();
    campoNome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    campoNome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
    campoNome.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Espaço entre os campos
    painel.add(labelNome);
    painel.add(Box.createRigidArea(new Dimension(0, 5)));
    painel.add(campoNome);
    painel.add(Box.createRigidArea(new Dimension(0, 15)));

    // Label e campo para data do show
    JLabel labelData = new JLabel("Data do show (DD/MM):");
    labelData.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    JTextField campoData = new JTextField();
    campoData.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    campoData.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
    campoData.setAlignmentX(Component.LEFT_ALIGNMENT);

    painel.add(labelData);
    painel.add(Box.createRigidArea(new Dimension(0, 5)));
    painel.add(campoData);

    // Exibe painel com OK/CANCEL
    int resultado = JOptionPane.showConfirmDialog(
        null,
        painel,
        "Cadastro de Show",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE
    );

    if (resultado == JOptionPane.OK_OPTION) {
        String nome = campoNome.getText().trim();
        String data = campoData.getText().trim();

        if (nome.isEmpty() || data.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha nome e data do show.");
            return;
        }

        // Obtém o código do gênero
        String codGeneroStr = Genero.verificarOuCadastrar();
        if (codGeneroStr == null) {
            JOptionPane.showMessageDialog(null, "Operação cancelada.");
            return;
        }
        int codGenero = Integer.parseInt(codGeneroStr);

        // Obtém o código do local
        String codLocalStr = Local.verificarOuCadastrar();
        if (codLocalStr == null) {
            JOptionPane.showMessageDialog(null, "Operação cancelada.");
            return;
        }
        int codLocal = Integer.parseInt(codLocalStr);

        // Link do show
        String link = JOptionPane.showInputDialog("Digite o link do show:");

        // Cria objeto Show
        Show show = new Show(0, nome, data, codGenero, codLocal, link);

        // Cadastra no banco
        cadastrar(show);
    }
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