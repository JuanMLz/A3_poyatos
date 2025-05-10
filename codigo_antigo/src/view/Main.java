package view;

import java.util.List;
import javax.swing.JOptionPane;
import model.Genero;
import model.Local;
import model.Show;

public class Main {
    public static void main(String[] args) throws Exception {
        int opcao = -1;
        do {
            opcao = Menu();
            switch (opcao) {
                case 1:
                    mostrarTodosOsShows();
                    break;
                case 2:
                    pesquisarPorGenero();
                    break;
                case 3:
                    menuCadastro(); 
                    break;
                case 0:
                    JOptionPane.showMessageDialog(null, "Saindo do programa.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Digite uma das opções!");
                    break;
            }
        } while (opcao != 0);
    }

    public static int Menu() {
        String texto = "Showzão\nTodos seus shows em um só lugar!\n\n" +
                        "1 - Mostrar Shows\n" +
                        "2 - Pesquisar Show\n" +
                        "3 - Cadastrar\n" +
                        "0 - Sair\n" +
                        "\nDigite uma opção!";
        int opcao = -1;
        String opcaoDigitada = JOptionPane.showInputDialog(texto);
        if (opcaoDigitada != null && !opcaoDigitada.isEmpty()) {
            opcao = Integer.valueOf(opcaoDigitada);
        }
        return opcao;
    }

    public static void mostrarTodosOsShows() {
        String listaShows = Show.montarStringShows();
        if (listaShows.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há shows cadastrados.");
        } else {
            JOptionPane.showMessageDialog(null, listaShows);
        }
    }

    public static void pesquisarPorGenero() {
        List<Genero> generos = Genero.getGeneros();
        String[] opcoes = generos.stream()
                                .map(g -> g.nome)
                                .toArray(String[]::new);
        String escolha = (String) JOptionPane.showInputDialog(null,
            "Escolha um gênero:",
            "Pesquisar por Gênero",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcoes,
            opcoes[0]);
        int idGeneroEscolhido = -1;
        for (Genero genero : generos) {
            if (genero.nome.equalsIgnoreCase(escolha)) {
                idGeneroEscolhido = genero.id;
                break;
            }
        }
        String listaShows = Show.montarStringShowsPorGenero(idGeneroEscolhido);
        if (listaShows.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há shows cadastrados para este gênero.");
        } else {
            JOptionPane.showMessageDialog(null, listaShows);
        }
    }

    public static void menuCadastro() {
        int opcao;
        do {
            opcao = MenuCadastro();
            switch (opcao) {
                case 1:
                    Show.cadastrarShow();
                    break;
                case 2:
                    cadastrarGenero();
                    break;
                case 3:
                    cadastrarLocal();
                    break;
                case 0:
                    JOptionPane.showMessageDialog(null, "Voltando para o menu principal.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Digite uma das opções!");
                    break;
            }
        } while (opcao != 0);
    }

    public static int MenuCadastro() {
        String texto = "Showzão - Cadastro\n\n" +
                        "1 - Cadastrar Show\n" +
                        "2 - Cadastrar Gênero\n" +
                        "3 - Cadastrar Local\n" +
                        "0 - Voltar\n" +
                        "\nDigite uma opção!";
        int opcao = -1;
        String opcaoDigitada = JOptionPane.showInputDialog(texto);
        if (opcaoDigitada != null && !opcaoDigitada.isEmpty()) {
            opcao = Integer.valueOf(opcaoDigitada);
        }
        return opcao;
    }

    public static void cadastrarLocal() {
        String nomeLocal = Local.verificarOuCadastrar();
        if (nomeLocal != null && !nomeLocal.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Local cadastrado: " + nomeLocal);
        }
    }

    public static void cadastrarGenero() {
        String nomeGenero = Genero.verificarOuCadastrar();
        if (nomeGenero != null && !nomeGenero.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Genero cadastrado: " + nomeGenero);
        }
    }
}
