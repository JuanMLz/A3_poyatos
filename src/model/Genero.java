package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import dao.Conexao;

public class Genero {

    public int id;
    public String nome;

    // Construtor vazio
    public Genero() {}

    // Construtor com todos os argumentos
    public Genero(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Método para cadastrar um gênero e retornar o ID gerado
    private static int cadastrar(String nome) {
        String sql = "INSERT INTO genero (nome) VALUES (?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.executeUpdate();

            // Obtém o ID gerado automaticamente
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Retorna o ID do gênero cadastrado
            }
            JOptionPane.showMessageDialog(null, "Gênero cadastrado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar gênero: " + e.getMessage());
        }
        return -1; // Retorna -1 se ocorrer algum erro
    }

    // Método para obter a lista de gêneros
    public static List<Genero> getGeneros() {
        List<Genero> lista = new ArrayList<>();
        String sql = "SELECT id, nome FROM genero ORDER BY nome";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Genero genero = new Genero();
                genero.id = rs.getInt("id");
                genero.nome = rs.getString("nome");
                lista.add(genero);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao obter gêneros: " + e.getMessage());
        }
        return lista;
    }

    // Verifica se há gêneros cadastrados. Se não houver, oferece a opção de cadastrar um novo gênero.
    // Se já houver gêneros cadastrados, permite ao usuário escolher entre um gênero existente ou cadastrar um novo.
    // Retorna o ID do gênero escolhido ou cadastrado, ou null se a operação for cancelada.
    public static String verificarOuCadastrar() {
        List<Genero> generos = getGeneros();

        if (generos.isEmpty()) {
            int opcaoCadastro = JOptionPane.showConfirmDialog(null,
                    "Não há gêneros cadastrados. Deseja cadastrar um novo gênero?",
                    "Cadastro de Gênero", JOptionPane.YES_NO_OPTION);

            if (opcaoCadastro == JOptionPane.YES_OPTION) {
                String nomeGenero = JOptionPane.showInputDialog("Digite o nome do novo gênero:");
                if (nomeGenero != null && !nomeGenero.isEmpty()) {
                    int idGenero = cadastrar(nomeGenero); // Cadastra o novo gênero e retorna o ID
                    return String.valueOf(idGenero); // Retorna o ID do gênero cadastrado como String
                } else {
                    JOptionPane.showMessageDialog(null, "Nome do gênero não pode ser vazio.");
                    return null; // Retorna null se o nome do gênero for vazio
                }
            } else {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
                return null; // Retorna null se o usuário não deseja cadastrar um novo gênero
            }
        } else {
            // Oferece ao usuário a escolha entre cadastrar um novo gênero ou escolher um existente
            String[] opcoes = { "Escolher Gênero Existente", "Cadastrar Novo Gênero" };
            int escolha = JOptionPane.showOptionDialog(null,
                    "Escolha uma opção:",
                    "Opções de Gênero",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]);

            if (escolha == 0) { // Escolher Gênero Existente
                Genero escolhido = escolherGenero(generos);
                if (escolhido != null) {
                    return String.valueOf(escolhido.id); // Retorna o ID do gênero escolhido como String
                } else {
                    return null; // Retorna null se o usuário cancelar a escolha
                }
            } else if (escolha == 1) { // Cadastrar Novo Gênero
                String nomeGenero = JOptionPane.showInputDialog("Digite o nome do novo gênero:");
                if (nomeGenero != null && !nomeGenero.isEmpty()) {
                    int idGenero = cadastrar(nomeGenero); // Cadastra o novo gênero e retorna o ID
                    return String.valueOf(idGenero); // Retorna o ID do gênero cadastrado como String
                } else {
                    JOptionPane.showMessageDialog(null, "Nome do gênero não pode ser vazio.");
                    return null; // Retorna null se o nome do gênero for vazio
                }
            } else {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
                return null; // Retorna null se o usuário cancelar a operação
            }
        }
    }

    // Permite ao usuário escolher um gênero existente a partir da lista de gêneros cadastrados,
    // obtida através do método verificarOuCadastrar.
    private static Genero escolherGenero(List<Genero> generos) {
        // Verifica se há gêneros cadastrados
        if (generos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há gêneros cadastrados.");
            return null; // Retorna null se não houver gêneros cadastrados
        }
    
        // Converte a lista de Gêneros em um array com os nomes dos gêneros
        String[] opcoes = generos.stream()
                                .map(g -> g.nome)
                                .toArray(String[]::new);
    
        // Mostra as opções de escolha de gênero
        String escolha = (String) JOptionPane.showInputDialog(null,
                "Escolha um gênero:", // Mensagem exibida no diálogo
                "Escolha de Gênero", // Título da janela de diálogo
                JOptionPane.QUESTION_MESSAGE, // Ícone de interrogação
                null,
                opcoes, // Opções obtidas como array de nomes dos gêneros
                opcoes[0]); // Valor padrão, primeiro elemento do array
    
        // Percorre a lista de gêneros para encontrar o gênero escolhido pelo usuário
        for (Genero genero : generos) {
            if (genero.nome.equalsIgnoreCase(escolha)) { // Método equalsIgnoreCase permite ignorar maiúsculas/minúsculas
                return genero; // Retorna o objeto Genero escolhido
            }
        }
    
        return null; // Retorna null se o usuário cancelar a escolha
    }

}
