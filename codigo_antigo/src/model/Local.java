package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

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

        if (locais.isEmpty()) {
            int opcaoCadastro = JOptionPane.showConfirmDialog(null,
                    "Não há locais cadastrados. Deseja cadastrar um novo local?",
                    "Cadastro de Local", JOptionPane.YES_NO_OPTION);

            if (opcaoCadastro == JOptionPane.YES_OPTION) {
                String nomeLocal = JOptionPane.showInputDialog("Digite o nome do novo local:");
                if (nomeLocal != null && !nomeLocal.isEmpty()) {
                    int idLocal = cadastrar(nomeLocal);
                    return String.valueOf(idLocal);
                } else {
                    JOptionPane.showMessageDialog(null, "Nome do local não pode ser vazio.");
                    return null;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
                return null;
            }
        } else {
            String[] opcoes = { "Escolher Local Existente", "Cadastrar Novo Local" };
            int escolha = JOptionPane.showOptionDialog(null,
                    "Escolha uma opção:",
                    "Opções de Local",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]);

            if (escolha == 0) {
                Local escolhido = escolherLocal(locais);
                if (escolhido != null) {
                    return String.valueOf(escolhido.id);
                } else {
                    return null;
                }
            } else if (escolha == 1) {
                String nomeLocal = JOptionPane.showInputDialog("Digite o nome do novo local:");
                if (nomeLocal != null && !nomeLocal.isEmpty()) {
                    int idLocal = cadastrar(nomeLocal);
                    return String.valueOf(idLocal);
                } else {
                    JOptionPane.showMessageDialog(null, "Nome do local não pode ser vazio.");
                    return null;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
                return null;
            }
        }
    }

    private static Local escolherLocal(List<Local> locais) {
        if (locais.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há locais cadastrados.");
            return null;
        }

        String[] opcoes = locais.stream()
                .map(l -> l.nome)
                .toArray(String[]::new);

        String escolha = (String) JOptionPane.showInputDialog(null,
                "Escolha um local:",
                "Escolha de Local",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]);

        for (Local local : locais) {
            if (local.nome.equalsIgnoreCase(escolha)) {
                return local;
            }
        }

        return null;
    }
}
