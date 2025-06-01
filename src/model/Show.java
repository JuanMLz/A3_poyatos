package model;

// Imports necessários para operações com Banco de Dados e manipulação de coleções
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;

import dao.Conexao;

public class Show {

    // Variáveis da classe que representam os atributos dos shows
    public int id; // Identificador único do show
    public int codGenero; // Código do gênero associado ao show
    public int codLocal; // Código do local associado ao show
    public String nome; // Nome do show
    public String data; // Data do show
    public String link; // Link associado à venda do ingresso do show

    // Construtor vazio
    public Show() {}

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
        List<Show> lista = new ArrayList<>();
        String sql = "SELECT id, nome, data, codGenero, codLocal, link FROM shows ORDER BY nome";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Show show = new Show(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("data"),
                    rs.getInt("codGenero"),
                    rs.getInt("codLocal"),
                    rs.getString("link")
                );
                lista.add(show);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao obter shows: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método estático para cadastrar um novo show.
     * Coleta os dados do usuário e usa os métodos atualizados para escolher gênero e local,
     * ambos com opção para cadastrar novos itens.
     */
    public static void cadastrarShow() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(new Color(245, 245, 245));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelNome = new JLabel("Nome do show:");
        labelNome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField campoNome = new JTextField();
        campoNome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoNome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        campoNome.setAlignmentX(Component.LEFT_ALIGNMENT);

        painel.add(labelNome);
        painel.add(Box.createRigidArea(new Dimension(0, 5)));
        painel.add(campoNome);
        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel labelData = new JLabel("Data do show (DD/MM):");
        labelData.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField campoData = new JTextField();
        campoData.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoData.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        campoData.setAlignmentX(Component.LEFT_ALIGNMENT);

        painel.add(labelData);
        painel.add(Box.createRigidArea(new Dimension(0, 5)));
        painel.add(campoData);

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

            // Escolher gênero com opção de cadastrar novo
            String codGeneroStr = Genero.escolherGeneroComCadastro();
            if (codGeneroStr == null) {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
                return;
            }
            int codGenero = Integer.parseInt(codGeneroStr);

            // Escolher local com opção de cadastrar novo (método novo similar ao do Genero)
            String codLocalStr = Local.escolherLocalComCadastro();
            if (codLocalStr == null) {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
                return;
            }
            int codLocal = Integer.parseInt(codLocalStr);

            String link = JOptionPane.showInputDialog("Digite o link do show:");

            Show show = new Show(0, nome, data, codGenero, codLocal, link);

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
     * Método estático para remover um show do banco usando o id do show
     */
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
}
