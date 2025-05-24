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

    private static int cadastrar(String nome) {
        String sql = "INSERT INTO local (nome) VALUES (?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            JOptionPane.showMessageDialog(null, "Local cadastrado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar local: " + e.getMessage());
        }
        return -1;
    }

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

    public static String verificarOuCadastrar() {
        List<Local> locais = getLocais();

        JDialog dialog = new JDialog((Frame) null, "Cadastrar ou Escolher Local", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setMinimumSize(new Dimension(450, 400));
        dialog.setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        painel.setBackground(new Color(245, 245, 245));

        // Campo para novo local
        JPanel painelNovo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelNovo.setBackground(painel.getBackground());

        JLabel labelNovo = new JLabel("Digite um novo local:");
        labelNovo.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        JTextField campoNovo = new JTextField();
        campoNovo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        campoNovo.setPreferredSize(new Dimension(350, 35));
        campoNovo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 2, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        JPanel painelCampoVertical = new JPanel();
        painelCampoVertical.setLayout(new BoxLayout(painelCampoVertical, BoxLayout.Y_AXIS));
        painelCampoVertical.setBackground(painel.getBackground());
        labelNovo.setAlignmentX(Component.CENTER_ALIGNMENT);
        campoNovo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelCampoVertical.add(labelNovo);
        painelCampoVertical.add(Box.createRigidArea(new Dimension(0, 8)));
        painelCampoVertical.add(campoNovo);

        painelNovo.add(painelCampoVertical);

        // Lista dos locais existentes
        JPanel painelLista = new JPanel(new BorderLayout(5, 5));
        painelLista.setBackground(painel.getBackground());

        JLabel labelLista = new JLabel("Ou escolha um local existente:");
        labelLista.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Local l : locais) {
            listModel.addElement(l.nome);
        }
        JList<String> listaLocais = new JList<>(listModel);
        listaLocais.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Fonte e alinhamento central da lista
        listaLocais.setFont(new Font("Segoe UI", Font.PLAIN, 20));
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
        scroll.setPreferredSize(new Dimension(380, 160));

        painelLista.add(labelLista, BorderLayout.NORTH);
        painelLista.add(scroll, BorderLayout.CENTER);

        // Painel central
        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setBackground(painel.getBackground());
        painelCentro.add(painelNovo);
        painelCentro.add(Box.createRigidArea(new Dimension(0, 20)));
        painelCentro.add(painelLista);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.setBackground(painel.getBackground());

        JButton btnConfirmar = criarBotao("Confirmar");
        JButton btnCancelar = criarBotao("Cancelar");

        painelBotoes.add(btnConfirmar);
        painelBotoes.add(btnCancelar);

        painel.add(painelCentro, BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        dialog.getContentPane().add(painel);
        dialog.pack();
        dialog.setMinimumSize(dialog.getSize());

        final String[] resultado = {null};

        btnConfirmar.addActionListener(e -> {
            String novoLocal = campoNovo.getText().trim();

            if (!novoLocal.isEmpty()) {
                int id = cadastrar(novoLocal);
                if (id != -1) {
                    resultado[0] = String.valueOf(id);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao cadastrar novo local.");
                }
                return;
            }

            String selecionado = listaLocais.getSelectedValue();
            if (selecionado != null) {
                for (Local l : locais) {
                    if (l.nome.equalsIgnoreCase(selecionado)) {
                        resultado[0] = String.valueOf(l.id);
                        dialog.dispose();
                        return;
                    }
                }
            }

            JOptionPane.showMessageDialog(dialog, "Por favor, digite um novo local ou selecione um existente.");
        });

        btnCancelar.addActionListener(e -> {
            resultado[0] = null;
            dialog.dispose();
        });

        dialog.setVisible(true);
        return resultado[0];
    }

    private static JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        btn.setBackground(new Color(220, 220, 220));
        btn.setForeground(Color.DARK_GRAY);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return btn;
    }
}
