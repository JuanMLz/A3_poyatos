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

    public Genero() {}

    public Genero(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    private static int cadastrar(String nome) {
        String sql = "INSERT INTO genero (nome) VALUES (?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            JOptionPane.showMessageDialog(null, "Gênero cadastrado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar gênero: " + e.getMessage());
        }
        return -1;
    }

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

    public static String verificarOuCadastrar() {
        List<Genero> generos = getGeneros();

        if (generos.isEmpty()) {
            int opcaoCadastro = JOptionPane.showConfirmDialog(null,
                    "Não há gêneros cadastrados. Deseja cadastrar um novo gênero?",
                    "Cadastro de Gênero", JOptionPane.YES_NO_OPTION);

            if (opcaoCadastro == JOptionPane.YES_OPTION) {
                String nomeGenero = JOptionPane.showInputDialog("Digite o nome do novo gênero:");
                if (nomeGenero != null && !nomeGenero.isEmpty()) {
                    int idGenero = cadastrar(nomeGenero);
                    return String.valueOf(idGenero);
                } else {
                    JOptionPane.showMessageDialog(null, "Nome do gênero não pode ser vazio.");
                    return null;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
                return null;
            }
        } else {
            String[] opcoes = { "Escolher Gênero Existente", "Cadastrar Novo Gênero" };
            int escolha = JOptionPane.showOptionDialog(null,
                    "Escolha uma opção:",
                    "Opções de Gênero",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]);

            if (escolha == 0) {
                Genero escolhido = escolherGenero(generos);
                if (escolhido != null) {
                    return String.valueOf(escolhido.id);
                } else {
                    return null;
                }
            } else if (escolha == 1) {
                String nomeGenero = JOptionPane.showInputDialog("Digite o nome do novo gênero:");
                if (nomeGenero != null && !nomeGenero.isEmpty()) {
                    int idGenero = cadastrar(nomeGenero);
                    return String.valueOf(idGenero);
                } else {
                    JOptionPane.showMessageDialog(null, "Nome do gênero não pode ser vazio.");
                    return null;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
                return null;
            }
        }
    }

    private static Genero escolherGenero(List<Genero> generos) {
        if (generos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há gêneros cadastrados.");
            return null;
        }

        String[] opcoes = generos.stream()
                                .map(g -> g.nome)
                                .toArray(String[]::new);

        String escolha = (String) JOptionPane.showInputDialog(null,
                "Escolha um gênero:",
                "Escolha de Gênero",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]);

        for (Genero genero : generos) {
            if (genero.nome.equalsIgnoreCase(escolha)) {
                return genero;
            }
        }

        return null;
    }
}
