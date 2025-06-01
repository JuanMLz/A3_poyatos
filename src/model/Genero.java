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

    // Construtor vazio
    public Genero() {}

    // Construtor com todos os argumentos
    public Genero(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    /**
     * Método para cadastrar um gênero e retornar o ID gerado.
     * 
     * @param nome Nome do gênero a ser cadastrado.
     * @return O ID do gênero cadastrado, ou -1 se houver erro.
     */
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

    /**
     * Método para obter a lista de gêneros cadastrados.
     * 
     * @return Uma lista de objetos Genero com todos os gêneros cadastrados.
     */
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

    /**
     * Verifica se há gêneros cadastrados. Se não houver, oferece a opção de cadastrar um novo gênero.
     * Se já houver gêneros cadastrados, permite ao usuário escolher entre um gênero existente ou cadastrar um novo.
     * 
     * @return O ID do gênero escolhido ou cadastrado, ou null se a operação for cancelada.
     */
    public static String verificarOuCadastrar() {
    List<Genero> generos = getGeneros();

    JDialog dialog = new JDialog((Frame) null, "Cadastrar ou Escolher Gênero", true);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dialog.setMinimumSize(new Dimension(450, 400));
    dialog.setLocationRelativeTo(null);

    JPanel painel = new JPanel(new BorderLayout(10, 10));
    painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    painel.setBackground(new Color(245, 245, 245));

    // Painel para o campo de texto com FlowLayout centralizado
    JPanel painelNovo = new JPanel(new FlowLayout(FlowLayout.CENTER));
    painelNovo.setBackground(painel.getBackground());

    JLabel labelNovo = new JLabel("Digite um novo gênero:");
    labelNovo.setFont(new Font("Segoe UI", Font.PLAIN, 20));

    JTextField campoNovo = new JTextField();
    campoNovo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
    campoNovo.setPreferredSize(new Dimension(350, 45));
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

    // Lista dos gêneros existentes
    JPanel painelLista = new JPanel(new BorderLayout(5, 5));
    painelLista.setBackground(painel.getBackground());

    JLabel labelLista = new JLabel("Ou escolha um gênero existente:");
    labelLista.setFont(new Font("Segoe UI", Font.PLAIN, 20));
    DefaultListModel<String> listModel = new DefaultListModel<>();
    for (Genero g : generos) {
        listModel.addElement(g.nome);
    }
    JList<String> listaGeneros = new JList<>(listModel);
    listaGeneros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // **Ajustar a fonte e alinhamento do texto da lista**
    listaGeneros.setFont(new Font("Segoe UI", Font.PLAIN, 20));
    listaGeneros.setCellRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setHorizontalAlignment(SwingConstants.CENTER); // Centraliza o texto
            return label;
        }
    });

    JScrollPane scroll = new JScrollPane(listaGeneros);
    scroll.setPreferredSize(new Dimension(380, 160));

    painelLista.add(labelLista, BorderLayout.NORTH);
    painelLista.add(scroll, BorderLayout.CENTER);

    // Painel central com os dois painéis empilhados (campo e lista)
    JPanel painelCentro = new JPanel();
    painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
    painelCentro.setBackground(painel.getBackground());
    painelCentro.add(painelNovo);
    painelCentro.add(Box.createRigidArea(new Dimension(0, 20)));
    painelCentro.add(painelLista);

    // Painel de botões centralizado
    JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
    painelBotoes.setBackground(painel.getBackground());

    JButton btnConfirmar = UIUtils.criarBotao("Confirmar");
    JButton btnCancelar = UIUtils.criarBotao("Cancelar");

    painelBotoes.add(btnConfirmar);
    painelBotoes.add(btnCancelar);

    painel.add(painelCentro, BorderLayout.CENTER);
    painel.add(painelBotoes, BorderLayout.SOUTH);

    dialog.getContentPane().add(painel);
    dialog.pack();
    dialog.setMinimumSize(dialog.getSize());

    final String[] resultado = {null};

    btnConfirmar.addActionListener(e -> {
        String novoGenero = campoNovo.getText().trim();

        if (!novoGenero.isEmpty()) {
            int id = cadastrar(novoGenero);
            if (id != -1) {
                resultado[0] = String.valueOf(id);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Erro ao cadastrar novo gênero.");
            }
            return;
        }

        String selecionado = listaGeneros.getSelectedValue();
        if (selecionado != null) {
            for (Genero g : generos) {
                if (g.nome.equalsIgnoreCase(selecionado)) {
                    resultado[0] = String.valueOf(g.id);
                    dialog.dispose();
                    return;
                }
            }
        }

        JOptionPane.showMessageDialog(dialog, "Por favor, digite um novo gênero ou selecione um existente.");
    });

    btnCancelar.addActionListener(e -> {
        resultado[0] = null;
        dialog.dispose();
    });

    dialog.setVisible(true);
    return resultado[0];
    }

    /**
     * Solicita o cadastro de um novo gênero de música.
     * Exibe uma mensagem com o nome do gênero cadastrado.
     */
    public static void cadastrarGenero(JFrame frame) {
        String nomeGenero = Genero.verificarOuCadastrar();
        if (nomeGenero != null && !nomeGenero.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "🎶 Gênero cadastrado: " + nomeGenero);
        }
    }

    public static String escolherGenero() {
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

    // Cria botões com UIUtils
    JButton btnConfirmar = UIUtils.criarBotao("Confirmar");
    JButton btnCancelar = UIUtils.criarBotao("Cancelar");

    // Painel para os botões
    JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
    painelBotoes.add(btnConfirmar);
    painelBotoes.add(btnCancelar);

    // Painel principal com scroll e botões
    JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
    painelPrincipal.add(scroll, BorderLayout.CENTER);
    painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

    JDialog dialog = new JDialog((Frame) null, "Escolha um gênero", true);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dialog.getContentPane().add(painelPrincipal);
    dialog.pack();
    dialog.setLocationRelativeTo(null);

    final String[] resultado = {null};

    btnConfirmar.addActionListener(e -> {
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

    btnCancelar.addActionListener(e -> {
        resultado[0] = null;
        dialog.dispose();
    });

    dialog.setVisible(true);

    return resultado[0];
}


}

