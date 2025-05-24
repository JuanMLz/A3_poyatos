package view;

import model.Genero;
import model.Local;
import model.Show;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Classe responsável pela criação e gerenciamento da interface gráfica do aplicativo Showzão.
 * O aplicativo permite que o usuário visualize e cadastre shows, gêneros e locais.
 */
public class Main {

    private static JFrame frame;

    /**
     * Método principal que inicializa a interface gráfica do aplicativo.
     * A interface é criada em uma thread separada para evitar problemas com a interface do usuário.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::criarInterface);
    }

    /**
     * Cria a interface gráfica do aplicativo, incluindo a janela principal,
     * os painéis de informações e os botões de interação.
     */
    public static void criarInterface() {
        frame = new JFrame("🎵 Showzão - Todos seus shows em um só lugar!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 370);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Painel principal com layout BorderLayout
        JPanel painel = new JPanel();
        painel.setLayout(new BorderLayout());
        painel.setBackground(new Color(245, 245, 245));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título e subtítulo
        JLabel titulo = new JLabel("🎵 Showzão", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        titulo.setForeground(new Color(44, 62, 80));

        JLabel subtitulo = new JLabel("Todos seus shows em um só lugar!", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(80, 80, 80));

        // Painel topo com título e subtítulo
        JPanel painelTopo = new JPanel(new GridLayout(2, 1));
        painelTopo.setBackground(new Color(245, 245, 245));
        painelTopo.add(titulo);
        painelTopo.add(subtitulo);

        // Painel de botões
        JPanel botoes = new JPanel();
        botoes.setLayout(new GridLayout(5, 1, 12, 12));
        botoes.setBackground(new Color(245, 245, 245));

        // Botões de ação
        JButton btnMostrar = criarBotao("📋 Mostrar Shows");
        JButton btnPesquisar = criarBotao("🔎 Pesquisar Shows por Gênero");
        JButton btnCadastrar = criarBotao("📝 Cadastrar");
        JButton btnSair = criarBotao("🚪 Sair");

        botoes.add(btnMostrar);
        botoes.add(btnPesquisar);
        botoes.add(btnCadastrar);
        botoes.add(btnSair);

        // Ações dos botões
        btnMostrar.addActionListener(e -> mostrarTodosOsShows());
        btnPesquisar.addActionListener(e -> pesquisarPorGenero());
        btnCadastrar.addActionListener(e -> menuCadastro());
        btnSair.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Saindo do programa.");
            System.exit(0);
        });

        painel.add(painelTopo, BorderLayout.NORTH);
        painel.add(botoes, BorderLayout.CENTER);

        frame.setContentPane(painel);
        frame.setVisible(true);
    }

    /**
     * Cria um botão estilizado com o texto fornecido.
     * 
     * @param texto O texto que será exibido no botão
     * @return O botão estilizado
     */
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

    /**
     * Exibe todos os shows cadastrados no sistema.
     * Se não houver shows cadastrados, uma mensagem de alerta é exibida.
     */
    public static void mostrarTodosOsShows() {
    List<Show> shows = Show.getShows();
    List<Genero> generos = Genero.getGeneros();
    List<Local> locais = Local.getLocais();

    if (shows.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Não há shows cadastrados.");
        return;
    }

    JPanel painelShows = new JPanel();
    painelShows.setLayout(new BoxLayout(painelShows, BoxLayout.Y_AXIS));
    painelShows.setBackground(new Color(245, 245, 245));
    painelShows.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    for (Show show : shows) {
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

        JLabel lblLink = new JLabel("<html><a href=\'" + show.link + "\'>Link do show</a></html>");
        lblLink.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        lblLink.setForeground(new Color(10, 102, 194));
        lblLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(show.link));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Não foi possível abrir o link.");
                }
            }
        });

        cardShow.add(lblNome);
        cardShow.add(Box.createVerticalStrut(5));
        cardShow.add(lblLocal);
        cardShow.add(lblData);
        cardShow.add(lblGenero);
        cardShow.add(Box.createVerticalStrut(5));
        cardShow.add(lblLink);

        painelShows.add(cardShow);
        painelShows.add(Box.createVerticalStrut(15));
    }

    JScrollPane scrollPane = new JScrollPane(painelShows);
    scrollPane.getVerticalScrollBar().setUnitIncrement(12);

    JLabel tituloShowzao = new JLabel("🎵 Showzão", SwingConstants.CENTER);
    tituloShowzao.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
    tituloShowzao.setForeground(new Color(44, 62, 80));

    JPanel painelTopoShowzao = new JPanel(new GridLayout(1, 1));
    painelTopoShowzao.setBackground(new Color(245, 245, 245));
    painelTopoShowzao.add(tituloShowzao);
    painelTopoShowzao.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    JPanel painelDialogo = new JPanel(new BorderLayout(0, 10));
    painelDialogo.setBackground(new Color(245, 245, 245));
    painelDialogo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    painelDialogo.add(painelTopoShowzao, BorderLayout.NORTH);
    painelDialogo.add(scrollPane, BorderLayout.CENTER);

    scrollPane.setPreferredSize(new Dimension(380, 250));

    JButton btnOk = criarBotao("OK");
    Object[] options = {btnOk};

    final JOptionPane optionPane = new JOptionPane(
            painelDialogo,
            JOptionPane.PLAIN_MESSAGE,
            JOptionPane.DEFAULT_OPTION,
            null,
            options,
            options[0]);

    final JDialog dialog = new JDialog(frame, "Todos os shows", true);
    dialog.setContentPane(optionPane);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    btnOk.addActionListener(e -> dialog.dispose());

    dialog.pack();
    dialog.setLocationRelativeTo(frame);
    dialog.setVisible(true);
}




    /**
     * Permite ao usuário pesquisar shows por gênero.
     * Se não houver shows para o gênero selecionado, uma mensagem de alerta é exibida.
     */
    public static void pesquisarPorGenero() {
        List<Genero> generos = Genero.getGeneros();
        if (generos.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nenhum gênero cadastrado.");
            return;
        }

        String[] nomesGeneros = generos.stream().map(g -> g.nome).toArray(String[]::new);
        JComboBox<String> combo = new JComboBox<>(nomesGeneros);
        combo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(245, 245, 245));
        panel.add(new JLabel("Escolha um gênero:"), BorderLayout.NORTH);
        panel.add(combo, BorderLayout.CENTER);

        int resultado = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Pesquisar por Gênero",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (resultado == JOptionPane.OK_OPTION) {
            String escolhido = (String) combo.getSelectedItem();
            int idGenero = generos.stream()
                                  .filter(g -> g.nome.equalsIgnoreCase(escolhido))
                                  .findFirst()
                                  .map(g -> g.id)
                                  .orElse(-1);

            String listaShows = Show.montarStringShowsPorGenero(idGenero);
            if (listaShows.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Não há shows cadastrados para este gênero.");
            } else {
                JTextArea areaTexto = new JTextArea(listaShows);
                areaTexto.setEditable(false);
                areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane scrollPane = new JScrollPane(areaTexto);
                scrollPane.setPreferredSize(new Dimension(350, 200));
                JOptionPane.showMessageDialog(frame, scrollPane, "Shows por Gênero", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Exibe um menu de cadastro para o usuário escolher entre cadastrar show, gênero, local ou voltar.
     */
    public static void menuCadastro() {
    // Cria um diálogo modal com o JFrame como dono
    JDialog dialog = new JDialog(frame, "Cadastro", true);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    JPanel painel = new JPanel(new GridLayout(4, 1, 0, 10));
    painel.setBackground(new Color(245, 245, 245));
    painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JButton btnCadastrarShow = criarBotao("Cadastrar Show");
    JButton btnCadastrarGenero = criarBotao("Cadastrar Gênero");
    JButton btnCadastrarLocal = criarBotao("Cadastrar Local");
    JButton btnVoltar = criarBotao("Voltar");

    painel.add(btnCadastrarShow);
    painel.add(btnCadastrarGenero);
    painel.add(btnCadastrarLocal);
    painel.add(btnVoltar);

    dialog.getContentPane().add(painel);
    dialog.pack();
    dialog.setLocationRelativeTo(frame);

    // Ações dos botões
    btnCadastrarShow.addActionListener(e -> {
        dialog.dispose(); // fecha o diálogo antes
        Show.cadastrarShow();
    });

    btnCadastrarGenero.addActionListener(e -> {
        dialog.dispose();
        cadastrarGenero();
    });

    btnCadastrarLocal.addActionListener(e -> {
        dialog.dispose();
        cadastrarLocal();
    });

    btnVoltar.addActionListener(e -> dialog.dispose());

    dialog.setVisible(true);
}



    /**
     * Solicita o cadastro de um novo gênero de música.
     * Exibe uma mensagem com o nome do gênero cadastrado.
     */
    public static void cadastrarGenero() {
        String nomeGenero = Genero.verificarOuCadastrar();
        if (nomeGenero != null && !nomeGenero.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "🎶 Gênero cadastrado: " + nomeGenero);
        }
    }

    /**
     * Solicita o cadastro de um novo local de show.
     * Exibe uma mensagem com o nome do local cadastrado.
     */
    public static void cadastrarLocal() {
        String nomeLocal = Local.verificarOuCadastrar();
        if (nomeLocal != null && !nomeLocal.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "📍 Local cadastrado: " + nomeLocal);
        }
    }
}