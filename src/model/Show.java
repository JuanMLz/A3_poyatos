package model;

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

    // Atributos que armazenam as informações de um show
    public int id;         // Identificador único no banco
    public int codGenero;  // Código do gênero musical associado
    public int codLocal;   // Código do local onde o show acontecerá
    public String nome;    // Nome do show
    public String data;    // Data do show no formato DD/MM
    public String link;    // Link para venda de ingressos ou informações

    // Construtor padrão (vazio)
    public Show() {}

    // Construtor completo para facilitar criação de objetos com todos os dados
    public Show(int id, String nome, String data, int codGenero, int codLocal, String link) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.codGenero = codGenero;
        this.codLocal = codLocal;
        this.link = link;
    }

    /**
     * Retorna uma lista com todos os shows cadastrados no banco de dados.
     * A consulta retorna os dados ordenados alfabeticamente pelo nome do show.
     * 
     * @return Lista de objetos Show com todos os shows encontrados.
     */
    public static List<Show> getShows() {
        List<Show> lista = new ArrayList<>();
        String sql = "SELECT id, nome, data, codGenero, codLocal, link FROM shows ORDER BY nome";

        // Tenta abrir conexão, preparar a consulta e executar
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Para cada registro retornado, cria um objeto Show e adiciona na lista
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
            // Caso ocorra erro, exibe mensagem para o usuário
            JOptionPane.showMessageDialog(null, "Erro ao obter shows: " + e.getMessage());
        }
        return lista; // Retorna a lista, mesmo que vazia se não houver registros
    }

    /**
     * Exibe uma janela para o usuário cadastrar um novo show.
     * O formulário solicita nome e data do show.
     * O usuário escolhe ou cadastra um gênero e um local.
     * Também pode informar um link (opcional).
     * Caso o usuário cancele em qualquer etapa, o cadastro é abortado.
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

        // Exibe janela com OK e Cancelar para o usuário preencher os dados
        int resultado = JOptionPane.showConfirmDialog(
            null,
            painel,
            "Cadastro de Show",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        // Se o usuário confirmar o cadastro, processa os dados
        if (resultado == JOptionPane.OK_OPTION) {
            String nome = campoNome.getText().trim();
            String data = campoData.getText().trim();

            // Valida que nome e data foram preenchidos
            if (nome.isEmpty() || data.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, preencha nome e data do show.");
                return; // Interrompe o processo se dados inválidos
            }

            // Escolha do gênero, com opção para cadastrar novo gênero na hora
            String codGeneroStr = Genero.escolherGeneroComCadastro();
            if (codGeneroStr == null) {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
                return; // Interrompe se o usuário cancelar
            }
            int codGenero = Integer.parseInt(codGeneroStr);

            // Escolha do local, com opção para cadastrar novo local na hora
            String codLocalStr = Local.escolherLocalComCadastro();
            if (codLocalStr == null) {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
                return; // Interrompe se o usuário cancelar
            }
            int codLocal = Integer.parseInt(codLocalStr);

            // Solicita link para o show (opcional)
            String link = JOptionPane.showInputDialog("Digite o link do show:");

            // Cria objeto Show com os dados coletados
            Show show = new Show(0, nome, data, codGenero, codLocal, link);

            // Persiste no banco de dados
            cadastrar(show);
        }
    }

    /**
     * Insere os dados do show no banco de dados.
     * Exibe mensagem de sucesso ou erro para o usuário.
     * 
     * @param show Objeto contendo os dados do show a ser inserido.
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
     * Remove o show do banco de dados usando o id do objeto fornecido.
     * Exibe mensagem de sucesso ou erro para o usuário.
     * 
     * @param show Objeto Show que será removido (usa seu id).
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
