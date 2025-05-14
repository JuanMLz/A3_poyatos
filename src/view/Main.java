package view;

import java.util.List;

import javax.swing.JOptionPane;

import model.Genero;
import model.Local;
import model.Show;

public class Main {
    //Metodo principal que inicia o programa
    public static void main(String[] args) throws Exception {
    
        //Loop principal que mantem o codigo executando até o usuario escolher sair
        int opcao = -1;
        do {
            opcao = Menu();//Chama o metodo que exibe as opções
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

    //Metodo que exibe o menu principal e retorna a opção do usuario
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
            opcao = Integer.valueOf(opcaoDigitada);//Converte a opção para inteiro
        }
        return opcao;
    }

    //Metodo que exibe todos os shows cadastradas
    public static void mostrarTodosOsShows() {
        String listaShows = Show.montarStringShows(); // Obtém a lista formatada de shows
        if (listaShows.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há shows cadastrados.");
        } else {
            JOptionPane.showMessageDialog(null, listaShows);
        }
    }

    //Metodo que permite ao usuario perquisar os shows por gênero
    public static void pesquisarPorGenero() {
        List<Genero> generos = Genero.getGeneros();
    
        // Criar um array de strings com o nome dos generos
        String[] opcoes = generos.stream()
                                .map(g -> g.nome)
                                .toArray(String[]::new);
    
        // Mostrar diálogo para escolha do gênero
        String escolha = (String) JOptionPane.showInputDialog(null,
            "Escolha um gênero:",//Mensagem exibida no dialogo
            "Pesquisar por Gênero",//Titulo da janela do dialogo
            JOptionPane.QUESTION_MESSAGE,//Interrogação
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
        String listaShows = Show.montarStringShowsPorGenero(idGeneroEscolhido);//Chama o metodo que filtra os shows por genero e formata a string
        if (listaShows.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há shows cadastrados para este gênero.");
        } else {
            JOptionPane.showMessageDialog(null, listaShows);
        }
    }

    //Metodo que recebe a opção escolhida no MenuCadastro e chama o metodo correspondente
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

    //Metodo que exibe o MenuCadastro, que é a opção 3 do menu principal, e obtem a escolha do usuario
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

    //Metodo para cadastrar um novo local
    public static void cadastrarLocal() {
        String nomeLocal = Local.verificarOuCadastrar();//Chama o metodo que verifica se ja tem o local salvo e cadastra um novo
        if (nomeLocal != null && !nomeLocal.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Local cadastrado: " + nomeLocal);
        }
    }

    //Metodo para cadastrar um novo genero
    public static void cadastrarGenero() {
        String nomeGenero = Genero.verificarOuCadastrar();//Chama o metodo que verifica se ja tem o genero salvo e cadastra um novo
        if (nomeGenero != null && !nomeGenero.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Genero cadastrado: " + nomeGenero);
        }
    }
}
