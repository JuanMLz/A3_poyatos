package view;

import java.util.List;
import javax.swing.JOptionPane;
import model.Genero;
import model.Local;
import model.Show;

public class Main {
    // Metodo principal que inicia o programa
    public static void main(String[] args) throws Exception {

        // Loop principal que mantem o codigo executando até o usuario escolher sair
        int opcao = -1;
        do {
            opcao = Menu(); // Chama o metodo que exibe as opções clicáveis
            switch (opcao) {
                case 0:
                    mostrarTodosOsShows();
                    break;
                case 1:
                    pesquisarPorGenero();
                    break;
                case 2:
                    menuCadastro();
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, "Saindo do programa.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Digite uma das opções!");
                    break;
            }
        } while (opcao != 3); // 3 corresponde ao "Sair"
    }

    // Metodo que exibe o menu principal com opções clicáveis e retorna a opção do usuário
    public static int Menu() {
        String texto = "Showzão\nTodos seus shows em um só lugar!\n\n" +
                        "Escolha uma das opções abaixo:";

        // Definindo os botões clicáveis
        Object[] opcoes = {"Mostrar Shows", "Pesquisar Show", "Cadastrar", "Sair"};

        // Exibe o dialogo com botões clicáveis
        int opcao = JOptionPane.showOptionDialog(null, texto, "Menu Principal",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);

        // O valor retornado é o índice da opção selecionada (0, 1, 2 ou 3)
        return opcao;
    }

    // Metodo que exibe todos os shows cadastrados
    public static void mostrarTodosOsShows() {
        String listaShows = Show.montarStringShows(); // Obtém a lista formatada de shows
        if (listaShows.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há shows cadastrados.");
        } else {
            JOptionPane.showMessageDialog(null, listaShows);
        }
    }

    // Metodo que permite ao usuario pesquisar os shows por gênero
    public static void pesquisarPorGenero() {
        List<Genero> generos = Genero.getGeneros();
    
        // Criar um array de strings com o nome dos generos
        String[] opcoes = generos.stream()
                                .map(g -> g.nome)
                                .toArray(String[]::new);
    
        // Mostrar diálogo para escolha do gênero
        String escolha = (String) JOptionPane.showInputDialog(null,
            "Escolha um gênero:", // Mensagem exibida no dialogo
            "Pesquisar por Gênero", // Título da janela do dialogo
            JOptionPane.QUESTION_MESSAGE, // Interrogação
            null,
            opcoes,
            opcoes[0]);
    
        // Encontra o ID do gênero escolhido
        int idGeneroEscolhido = -1;
        for (Genero genero : generos) {
            if (genero.nome.equalsIgnoreCase(escolha)) {
                idGeneroEscolhido = genero.id;
                break;
            }
        }
    
        // Mostrar apenas os shows do gênero escolhido
        String listaShows = Show.montarStringShowsPorGenero(idGeneroEscolhido); // Chama o metodo que filtra os shows por genero e formata a string
        if (listaShows.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há shows cadastrados para este gênero.");
        } else {
            JOptionPane.showMessageDialog(null, listaShows);
        }
    }

    // Metodo que recebe a opção escolhida no MenuCadastro e chama o metodo correspondente
    public static void menuCadastro() {
        int opcao;
        do {
            opcao = MenuCadastro();
            switch (opcao) {
                case 0:
                    Show.cadastrarShow();
                    break;
                case 1:
                    cadastrarGenero();
                    break;
                case 2:
                    cadastrarLocal();
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, "Voltando para o menu principal.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Digite uma das opções!");
                    break;
            }
        } while (opcao != 3); // 3 corresponde a "Voltar"
    }

    // Metodo que exibe o MenuCadastro, que é a opção 3 do menu principal, e obtem a escolha do usuario
    public static int MenuCadastro() {
        String texto = "Showzão - Cadastro\n\n" +
                        "Escolha o que deseja cadastrar:";

        // Definindo os botões clicáveis
        Object[] opcoes = {"Cadastrar Show", "Cadastrar Gênero", "Cadastrar Local", "Voltar"};

        // Exibe o dialogo com botões clicáveis
        int opcao = JOptionPane.showOptionDialog(null, texto, "Cadastro",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);

        // O valor retornado é o índice da opção selecionada (0, 1, 2 ou 3)
        return opcao;
    }

    // Metodo para cadastrar um novo local
    public static void cadastrarLocal() {
        String nomeLocal = Local.verificarOuCadastrar(); // Chama o metodo que verifica se já tem o local salvo e cadastra um novo
        if (nomeLocal != null && !nomeLocal.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Local cadastrado: " + nomeLocal);
        }
    }

    // Metodo para cadastrar um novo genero
    public static void cadastrarGenero() {
        String nomeGenero = Genero.verificarOuCadastrar(); // Chama o metodo que verifica se já tem o genero salvo e cadastra um novo
        if (nomeGenero != null && !nomeGenero.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Gênero cadastrado: " + nomeGenero);
        }
    }
}
