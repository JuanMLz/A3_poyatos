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
        JButton btnPesquisar = criarBotao("🔎 Pesquisar por Gênero");
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
     * Exibe uma janela de entrada de texto personalizada para o usuário.
     * 
     * @param titulo O título da janela de entrada
     * @param mensagem A mensagem a ser exibida na janela
     * @return O texto inserido pelo usuário, ou null se cancelado
     */
    private static String inputDialogModern(String titulo, String mensagem) {
        JTextField textField = new JTextField(20);
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(245, 245, 245));
        panel.add(new JLabel(mensagem), BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);

        int resultado = JOptionPane.showConfirmDialog(
            frame,
            panel,
            titulo,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (resultado == JOptionPane.OK_OPTION) {
            return textField.getText().trim();
        }
        return null;
    }

    /**
     * Exibe todos os shows cadastrados no sistema.
     * Se não houver shows cadastrados, uma mensagem de alerta é exibida.
     */
    public static void mostrarTodosOsShows() {
        String listaShows = Show.montarStringShows();
        if (listaShows.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Não há shows cadastrados.");
        } else {
            JTextArea areaTexto = new JTextArea(listaShows);
            areaTexto.setEditable(false);
            areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(areaTexto);
            scrollPane.setPreferredSize(new Dimension(350, 200));
            JOptionPane.showMessageDialog(frame, scrollPane, "Shows Cadastrados", JOptionPane.INFORMATION_MESSAGE);
        }
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
        String[] opcoes = {"Cadastrar Show", "Cadastrar Gênero", "Cadastrar Local", "Voltar"};
        int escolha = JOptionPane.showOptionDialog(frame,
                "Escolha uma opção de cadastro:",
                "Cadastro",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes[0]);

        switch (escolha) {
            case 0 -> Show.cadastrarShow();
            case 1 -> cadastrarGenero();
            case 2 -> cadastrarLocal();
            default -> {} // Voltar
        }
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