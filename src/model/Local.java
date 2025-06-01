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

public class Local {

    public int id;
    public String nome;

    public Local() {}

    public Local(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Cadastra um novo local, evita duplicatas, retorna ID ou -1
    private static int cadastrar(String nome) {
        if (existeLocal(nome)) {
            JOptionPane.showMessageDialog(null, "Local já cadastrado: " + nome);
            return -1;
        }
        String sql = "INSERT INTO local (nome) VALUES (?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Local cadastrado com sucesso!");
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar local: " + e.getMessage());
        }
        return -1;
    }

    // Verifica se o local já existe (ignora caixa alta/baixa)
    private static boolean existeLocal(String nome) {
        String sql = "SELECT COUNT(*) FROM local WHERE LOWER(nome) = LOWER(?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar local: " + e.getMessage());
        }
        return false;
    }

    // Retorna a lista de todos os locais cadastrados, ordenados por nome
    public static List<Local> getLocais() {
        List<Local> lista = new ArrayList<>();
        String sql = "SELECT id, nome FROM local ORDER BY nome";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Local local = new Local();
                local.id = rs.getInt("id");
                local.nome = rs.getString("nome");
                lista.add(local);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao obter locais: " + e.getMessage());
        }
        return lista;
    }

    // Dialogo para escolher local ou cadastrar novo, retorna ID selecionado ou cadastrado
    public static String escolherLocalComCadastro() {
        List<Local> locais = getLocais();

        if (locais.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum local cadastrado.");
            return null;
        }

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Local l : locais) {
            listModel.addElement(l.nome);
        }

        JList<String> listaLocais = new JList<>(listModel);
        listaLocais.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaLocais.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        listaLocais.setVisibleRowCount(10);
        listaLocais.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        });

        JScrollPane scroll = new JScrollPane(listaLocais);
        scroll.setPreferredSize(new Dimension(380, 200));

        JButton btnConfirmar = UIUtils.criarBotao("Confirmar");
        JButton btnCancelar = UIUtils.criarBotao("Cancelar");
        JButton btnCadastrarNovo = UIUtils.criarBotao("Cadastrar Novo Local");

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(btnConfirmar);
        painelBotoes.add(btnCadastrarNovo);
        painelBotoes.add(btnCancelar);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.add(scroll, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        final JDialog dialog = new JDialog((Frame) null, "Escolha um local", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(painelPrincipal);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        final String[] resultado = {null};

        btnConfirmar.addActionListener(e -> {
            if (listaLocais.getSelectedValue() != null) {
                String selecionado = listaLocais.getSelectedValue();
                for (Local l : locais) {
                    if (l.nome.equalsIgnoreCase(selecionado)) {
                        resultado[0] = String.valueOf(l.id);
                        break;
                    }
                }
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Por favor, selecione um local.");
            }
        });

        btnCancelar.addActionListener(e -> {
            resultado[0] = null;
            dialog.dispose();
        });

        btnCadastrarNovo.addActionListener(e -> {
            Integer idNovo = cadastrarLocalDialog(dialog);
            if (idNovo != null) {
                resultado[0] = String.valueOf(idNovo);
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
        return resultado[0];
    }

    // Dialogo para cadastrar local, retorna ID cadastrado ou null
    public static Integer cadastrarLocalDialog(Window owner) {
        String nome = JOptionPane.showInputDialog(owner, "Digite o nome do novo local:");
        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }
        nome = nome.trim();

        int id = cadastrar(nome);
        if (id != -1) {
            JOptionPane.showMessageDialog(owner, "📍 Local cadastrado: " + nome);
            return id;
        }
        return null;
    }

    // Cadastro simples de local via menu, exibe mensagem de sucesso/erro
    public static void cadastrarLocal(JFrame frame) {
        cadastrarLocalDialog(frame);
    }
}
