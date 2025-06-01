package model;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class UIUtils {

    /**
     * Cria um painel (card) visual para exibir informações de um show.
     * O card exibe nome, local, data, gênero, link clicável e botão para excluir o show.
     * 
     * @param show Show que será exibido no card.
     * @param generos Lista de gêneros para identificar o nome do gênero do show.
     * @param locais Lista de locais para identificar o nome do local do show.
     * @param framePai JFrame pai para mensagens de erro e ações.
     * @param atualizarCallback Callback para executar após exclusão, para atualizar a interface.
     * @return JPanel configurado como card do show.
     */
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

        // Busca o nome do gênero pelo id, ou "Desconhecido" caso não encontre
        String nomeGenero = generos.stream()
                .filter(g -> g.id == show.codGenero)
                .map(g -> g.nome)
                .findFirst()
                .orElse("Desconhecido");

        // Busca o nome do local pelo id, ou "Desconhecido" caso não encontre
        String nomeLocal = locais.stream()
                .filter(l -> l.id == show.codLocal)
                .map(l -> l.nome)
                .findFirst()
                .orElse("Desconhecido");

        // Labels para as informações do show
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

        // Label com link clicável que tenta abrir no navegador padrão
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

        // Botão vermelho para excluir o show, com confirmação e callback para atualizar a lista
        JButton btnExcluir = UIUtils.criarBotaoExcluir(show, framePai, atualizarCallback);

        // Adiciona todos os componentes ao painel card
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

    /**
     * Cria um botão padrão com estilo uniforme para a interface.
     * 
     * @param texto Texto que aparecerá no botão.
     * @return JButton configurado com estilo personalizado.
     */
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

    /**
     * Cria um botão para excluir um show com estilo vermelho,
     * pede confirmação antes de excluir e executa callback após exclusão.
     * 
     * @param show Show que será excluído.
     * @param frame Janela pai para mostrar a confirmação.
     * @param callbackExclusao Função a ser executada após exclusão bem sucedida.
     * @return JButton configurado para exclusão do show.
     */
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
