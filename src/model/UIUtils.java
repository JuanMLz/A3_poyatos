package model;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class UIUtils {

    public static JPanel criarCardShow(Show show, List<Genero> generos, List<Local> locais, JFrame framePai, Runnable atualizarCallback) {
    JPanel cardShow = new JPanel();
    cardShow.setLayout(new BoxLayout(cardShow, BoxLayout.Y_AXIS));
    cardShow.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));
    cardShow.setBackground(Color.WHITE);
    cardShow.setAlignmentX(Component.LEFT_ALIGNMENT);
    cardShow.setMaximumSize(new Dimension(360, Integer.MAX_VALUE));

    String nomeGenero = generos.stream()
            .filter(g -> g.id == show.codGenero)
            .map(g -> g.nome)
            .findFirst()
            .orElse("Desconhecido");

    String nomeLocal = locais.stream()
            .filter(l -> l.id == show.codLocal)
            .map(l -> l.nome)
            .findFirst()
            .orElse("Desconhecido");

    JLabel lblNome = new JLabel(show.nome);
    lblNome.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
    lblNome.setForeground(new Color(44, 62, 80));

    JLabel lblLocal = new JLabel("Local: " + nomeLocal);
    lblLocal.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
    lblLocal.setForeground(new Color(80, 80, 80));

    JLabel lblData = new JLabel("Data: " + show.data);
    lblData.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
    lblData.setForeground(new Color(80, 80, 80));

    JLabel lblGenero = new JLabel("<html>Gênero: " + nomeGenero + "</html>");
    lblGenero.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 13));
    lblGenero.setForeground(new Color(120, 120, 120));

    JLabel lblLink = new JLabel("<html><a href=\"" + show.link + "\">Link do show</a></html>");
    lblLink.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
    lblLink.setForeground(new Color(10, 102, 194));
    lblLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
    lblLink.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(show.link));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(framePai, "Não foi possível abrir o link.");
            }
        }
    });

    JButton btnExcluir = UIUtils.criarBotaoExcluir(show, framePai, () -> {
        atualizarCallback.run();
    });

    cardShow.add(lblNome);
    cardShow.add(Box.createVerticalStrut(5));
    cardShow.add(lblLocal);
    cardShow.add(lblData);
    cardShow.add(lblGenero);
    cardShow.add(Box.createVerticalStrut(5));
    cardShow.add(lblLink);
    cardShow.add(Box.createVerticalStrut(10));
    cardShow.add(btnExcluir);

    return cardShow;
    }


    public static JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        btn.setBackground(new Color(220, 220, 220));
        btn.setForeground(Color.DARK_GRAY);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return btn;
    }

    public static JButton criarBotaoExcluir(Show show, JFrame frame, Runnable callbackExclusao) {
        JButton btnExcluir = criarBotao("Excluir");
        btnExcluir.setBackground(new Color(255, 102, 102));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setBorder(new LineBorder(new Color(200, 0, 0), 1, true));
        btnExcluir.addActionListener(e -> {
            int confirmacao = JOptionPane.showConfirmDialog(
                frame,
                "Tem certeza que deseja excluir o show \"" + show.nome + "\"?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
            );
            if (confirmacao == JOptionPane.YES_OPTION) {
                Show.removerShow(show);
                callbackExclusao.run();
            }
        });
        return btnExcluir;
    }
}
