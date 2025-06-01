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

public class Genero {

    public int id;
    public String nome;

    public Genero() {}

    public Genero(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    private static int cadastrar(String nome) {
        // Antes de cadastrar, verifica se já existe para evitar duplicatas
        if (existeGenero(nome)) {
            JOptionPane.showMessageDialog(null, "Gênero já cadastrado: " + nome);
            return -1;
        }

        String sql = "INSERT INTO genero (nome) VALUES (?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Gênero cadastrado com sucesso!");
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar gênero: " + e.getMessage());
        }
        return -1;
    }

    private static boolean existeGenero(String nome) {
        String sql = "SELECT COUNT(*) FROM genero WHERE LOWER(nome) = LOWER(?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar gênero: " + e.getMessage());
        }
        return false;
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

    // Método privado para criar o diálogo comum, com opção de botão para cadastro
    private static String escolherGeneroDialog(boolean permitirCadastro) {
        List<Genero> generos = getGeneros();
        if (generos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum gênero cadastrado.");
            return null;
        }

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Genero g : generos) {
            listModel.addElement(g.nome);
        }

        JList<String> listaGeneros = new JList<>(listModel);
        listaGeneros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaGeneros.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        listaGeneros.setVisibleRowCount(10);
        listaGeneros.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                        int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        });

        JScrollPane scroll = new JScrollPane(listaGeneros);
        scroll.setPreferredSize(new Dimension(380, 200));

        // Declara essas variáveis antes para estarem disponíveis nas lambdas
        final String[] resultado = {null};
        final JDialog dialog = new JDialog((Frame) null, "Escolha um gênero", true);

        // Agora crie os botões, usando as variáveis já declaradas
        JButton btnConfirmar = UIUtils.criarBotaoComAcao("Confirmar", () -> {
            if (listaGeneros.getSelectedValue() != null) {
                String selecionado = listaGeneros.getSelectedValue();
                for (Genero g : generos) {
                    if (g.nome.equalsIgnoreCase(selecionado)) {
                        resultado[0] = String.valueOf(g.id);
                        break;
                    }
                }
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Por favor, selecione um gênero.");
            }
        });

        JButton btnCancelar = UIUtils.criarBotaoComAcao("Cancelar", () -> {
            resultado[0] = null;
            dialog.dispose();
        });

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));

        if (permitirCadastro) {
            JButton btnCadastrarNovo = UIUtils.criarBotaoComAcao("Cadastrar Novo Gênero", () -> {
                Integer novoId = cadastrarGeneroDialog(null);
                if (novoId != null) {
                    resultado[0] = String.valueOf(novoId);
                    dialog.dispose();  // fecha diálogo retornando o novo ID
                }
            });
            painelBotoes.add(btnCadastrarNovo);
        }

        painelBotoes.add(btnConfirmar);
        painelBotoes.add(btnCancelar);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.add(scroll, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(painelPrincipal);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        dialog.setVisible(true);
        return resultado[0];
    }


    // Método público para escolher gênero só com seleção, sem cadastro
    public static String escolherGeneroSomente() {
        return escolherGeneroDialog(false);
    }

    // Método público para escolher gênero com opção de cadastrar novo
    public static String escolherGeneroComCadastro() {
        return escolherGeneroDialog(true);
    }

    // Método público para cadastrar novo gênero via input simples, retornando o ID do gênero cadastrado
    public static Integer cadastrarGeneroDialog(Frame framePai) {
        String nome = JOptionPane.showInputDialog(framePai, "Digite o nome do novo gênero:");
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }
        nome = nome.trim();

        int id = cadastrar(nome);
        if (id != -1) {
            JOptionPane.showMessageDialog(framePai, "🎶 Gênero cadastrado: " + nome);
            return id;
        }
        return null;
    }
}
