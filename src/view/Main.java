package view;

import model.Genero;
import model.Local;
import model.Show;
import model.UIUtils;


import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Classe responsável pela criação e gerenciamento da interface gráfica do aplicativo Showzão.
 * O aplicativo permite que o usuário visualize e cadastre shows, gêneros e locais.
 */
public class Main {

    private static JFrame frame; // Janela principal do aplicativo

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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Finaliza app ao fechar a janela
        frame.setSize(600, 500); // Tamanho da janela
        frame.setLocationRelativeTo(null); // Centraliza na tela
        frame.setResizable(false); // Impede redimensionamento

        // Painel principal com layout BorderLayout
        JPanel painel = new JPanel();
        painel.setLayout(new BorderLayout());
        painel.setBackground(new Color(245, 245, 245)); // Cor clara de fundo
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Espacamento interno

        // Título e subtítulo
       ImageIcon originalIcon = new ImageIcon("src\\imagens\\iconeshowzao.png");
        Image imagemRedimensionada = originalIcon.getImage().getScaledInstance(250, 100, Image.SCALE_SMOOTH); // ajuste o tamanho aqui
        ImageIcon iconRedimensionado = new ImageIcon(imagemRedimensionada);
        // Label do titulo com imagem redimensionada
        JLabel titulo = new JLabel(iconRedimensionado, SwingConstants.CENTER);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        // Label do subtitulo
        JLabel subtitulo = new JLabel("Todos seus shows em um só lugar!", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
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
        JButton btnMostrar = UIUtils.criarBotao("📋 Mostrar Shows");
        JButton btnPesquisar = UIUtils.criarBotao("🔎 Pesquisar Shows por Gênero");
        JButton btnCadastrar = UIUtils.criarBotao("📝 Cadastrar");
        JButton btnSair = UIUtils.criarBotao("🚪 Sair");
        // Adiciona os botoes ao painel
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
            System.exit(0); // Encerra o progama
        });
        // Adiciona os paineis a janela principal
        painel.add(painelTopo, BorderLayout.NORTH);
        painel.add(botoes, BorderLayout.CENTER);
        // Define o painel criado com conteudo da janela e exibe a janela
        frame.setContentPane(painel);
        frame.setVisible(true);
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

    final JDialog[] dialog = new JDialog[1];

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

        JLabel lblLink = new JLabel("<html><a href=\"" + show.link + "\">Link do show</a></html>");
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

        // Aqui usamos UIUtils para criar o botão Excluir
        JButton btnExcluir = UIUtils.criarBotaoExcluir(show, frame, () -> {
            dialog[0].dispose();
            mostrarTodosOsShows();
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

        painelShows.add(cardShow);
        painelShows.add(Box.createVerticalStrut(15));
    }

    JScrollPane scrollPane = new JScrollPane(painelShows);
    scrollPane.setPreferredSize(new Dimension(380, 300));
    scrollPane.getVerticalScrollBar().setUnitIncrement(12);

    JButton okButton = UIUtils.criarBotao("OK");

    JOptionPane optionPane = new JOptionPane(
        scrollPane,
        JOptionPane.PLAIN_MESSAGE,
        JOptionPane.DEFAULT_OPTION,
        null,
        new Object[]{okButton},
        okButton
    );

    dialog[0] = optionPane.createDialog(frame, "Shows Cadastrados");

    okButton.addActionListener(e -> dialog[0].dispose());

    dialog[0].setVisible(true);
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
        
        // Converte lista de generos para array de nome para JComboBox
        String[] nomesGeneros = generos.stream().map(g -> g.nome).toArray(String[]::new);
        JComboBox<String> combo = new JComboBox<>(nomesGeneros);
        combo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        // Painel com label e combo para selecao
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(245, 245, 245));
        panel.add(new JLabel("Escolha um gênero:"), BorderLayout.NORTH);
        panel.add(combo, BorderLayout.CENTER);
        // Exibe dialogo para escolha de genero
        int resultado = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Pesquisar por Gênero",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        // Se o usuario confirmar
        if (resultado == JOptionPane.OK_OPTION) {
            String escolhido = (String) combo.getSelectedItem();
            // Obtem o id do genero selecionado
            int idGenero = generos.stream()
                                  .filter(g -> g.nome.equalsIgnoreCase(escolhido))
                                  .findFirst()
                                  .map(g -> g.id)
                                  .orElse(-1);
            // Monta a lista de shows para esse genero
            String listaShows = Show.montarStringShowsPorGenero(idGenero);
            if (listaShows.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Não há shows cadastrados para este gênero.");
            } else {
            // Exibe os shows encontrados em uma area
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
    // Painel com GridLayout para 4 botoes
    JPanel painel = new JPanel(new GridLayout(4, 1, 0, 10));
    painel.setBackground(new Color(245, 245, 245));
    painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    // Cria os botoes para opcao de cadastro
    JButton btnCadastrarShow = UIUtils.criarBotao("Cadastrar Show");
    JButton btnCadastrarGenero = UIUtils.criarBotao("Cadastrar Gênero");
    JButton btnCadastrarLocal = UIUtils.criarBotao("Cadastrar Local");
    JButton btnVoltar = UIUtils.criarBotao("Voltar");
    // Adiciona os botoes ao painel
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
        Show.cadastrarShow(); // chama o metodo cadastrar show
    });

    btnCadastrarGenero.addActionListener(e -> {
        dialog.dispose();
        Genero.cadastrarGenero(frame); // Chama metodo para cadastrar genero
    });

    btnCadastrarLocal.addActionListener(e -> {
        dialog.dispose();
        Local.cadastrarLocal(frame); // Chama metodo para cadastrar local
    });

    btnVoltar.addActionListener(e -> dialog.dispose()); // Fecha o menu

    dialog.setVisible(true); //Exibe o menu
    }
}