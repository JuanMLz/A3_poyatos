package model;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UIUtils {

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
