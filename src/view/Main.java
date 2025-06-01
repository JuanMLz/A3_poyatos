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
            JPanel cardShow = UIUtils.criarCardShow(show, generos, locais, frame, () -> {
                dialog[0].dispose();
                mostrarTodosOsShows();
            });

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
    String idGeneroStr = Genero.escolherGeneroSomente();
    if (idGeneroStr == null) {
        return; // operação cancelada ou nada selecionado
    }
    int idGenero = Integer.parseInt(idGeneroStr);

    List<Show> shows = Show.getShows();
    List<Genero> generos = Genero.getGeneros();
    List<Local> locais = Local.getLocais();

    List<Show> showsFiltrados = shows.stream()
                                    .filter(s -> s.codGenero == idGenero)
                                    .toList();

    if (showsFiltrados.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Não há shows cadastrados para este gênero.");
        return;
    }

    JPanel painelShows = new JPanel();
    painelShows.setLayout(new BoxLayout(painelShows, BoxLayout.Y_AXIS));
    painelShows.setBackground(new Color(245, 245, 245));
    painelShows.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    final JDialog[] dialog = new JDialog[1];

    for (Show show : showsFiltrados) {
        JPanel cardShow = UIUtils.criarCardShow(show, generos, locais, frame, () -> {
            dialog[0].dispose();
            pesquisarPorGenero();
        });

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

    dialog[0] = optionPane.createDialog(frame, "Shows por Gênero");

    okButton.addActionListener(e -> dialog[0].dispose());

    dialog[0].setVisible(true);
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
            Genero.cadastrarGeneroDialog(frame); // Chama metodo para cadastrar genero
        });

        btnCadastrarLocal.addActionListener(e -> {
            dialog.dispose();
            Local.cadastrarLocal(frame); // Chama metodo para cadastrar local
        });

        btnVoltar.addActionListener(e -> dialog.dispose()); // Fecha o menu

        dialog.setVisible(true); //Exibe o menu
        }
    }