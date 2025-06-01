package view;

import model.Genero;
import model.Local;
import model.Show;
import model.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Classe responsável pela interface gráfica do aplicativo Showzão.
 * Permite visualizar e cadastrar shows, gêneros e locais.
 */
public class Main {

    private static JFrame frame; // Janela principal da aplicação

    /**
     * Ponto de entrada da aplicação. Inicia a interface gráfica em thread apropriada.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::criarInterface);
    }

    /**
     * Configura e exibe a janela principal com botões para interações do usuário.
     */
    public static void criarInterface() {
        frame = new JFrame("🎵 Showzão - Todos seus shows em um só lugar!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(245, 245, 245));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título com imagem redimensionada
        ImageIcon originalIcon = new ImageIcon("src\\imagens\\iconeshowzao.png");
        Image imagemRedimensionada = originalIcon.getImage().getScaledInstance(250, 100, Image.SCALE_SMOOTH);
        JLabel titulo = new JLabel(new ImageIcon(imagemRedimensionada), SwingConstants.CENTER);

        // Subtítulo abaixo do título
        JLabel subtitulo = new JLabel("Todos seus shows em um só lugar!", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        subtitulo.setForeground(new Color(80, 80, 80));

        JPanel painelTopo = new JPanel(new GridLayout(2, 1));
        painelTopo.setBackground(new Color(245, 245, 245));
        painelTopo.add(titulo);
        painelTopo.add(subtitulo);

        // Painel com botões principais
        JPanel botoes = new JPanel(new GridLayout(5, 1, 12, 12));
        botoes.setBackground(new Color(245, 245, 245));

        JButton btnMostrar = UIUtils.criarBotao("📋 Mostrar Shows");
        JButton btnPesquisar = UIUtils.criarBotao("🔎 Pesquisar Shows por Gênero");
        JButton btnCadastrar = UIUtils.criarBotao("📝 Cadastrar");
        JButton btnSair = UIUtils.criarBotao("🚪 Sair");

        botoes.add(btnMostrar);
        botoes.add(btnPesquisar);
        botoes.add(btnCadastrar);
        botoes.add(btnSair);

        // Define ações dos botões
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
     * Exibe todos os shows cadastrados. Caso não haja, mostra aviso.
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
     * Permite pesquisar shows por gênero selecionado pelo usuário.
     * Exibe resultado filtrado ou mensagem caso não haja shows para o gênero.
     */
    public static void pesquisarPorGenero() {
        String idGeneroStr = Genero.escolherGeneroSomente();
        if (idGeneroStr == null) {
            return; // Cancelado ou nada selecionado
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
     * Exibe menu para cadastro de show, gênero ou local, ou para voltar.
     */
    public static void menuCadastro() {
        JDialog dialog = new JDialog(frame, "Cadastro", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new GridLayout(4, 1, 0, 10));
        painel.setBackground(new Color(245, 245, 245));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnCadastrarShow = UIUtils.criarBotao("Cadastrar Show");
        JButton btnCadastrarGenero = UIUtils.criarBotao("Cadastrar Gênero");
        JButton btnCadastrarLocal = UIUtils.criarBotao("Cadastrar Local");
        JButton btnVoltar = UIUtils.criarBotao("Voltar");

        painel.add(btnCadastrarShow);
        painel.add(btnCadastrarGenero);
        painel.add(btnCadastrarLocal);
        painel.add(btnVoltar);

        dialog.getContentPane().add(painel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);

        btnCadastrarShow.addActionListener(e -> {
            dialog.dispose();
            Show.cadastrarShow();
        });

        btnCadastrarGenero.addActionListener(e -> {
            dialog.dispose();
            Genero.cadastrarGeneroDialog(frame);
        });

        btnCadastrarLocal.addActionListener(e -> {
            dialog.dispose();
            Local.cadastrarLocal(frame);
        });

        btnVoltar.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }
}
