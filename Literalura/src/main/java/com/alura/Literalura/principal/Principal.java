package com.alura.Literalura.principal;

import com.alura.Literalura.model.*;
import com.alura.Literalura.repository.AutorRepository;
import com.alura.Literalura.repository.LivroRepository;
import com.alura.Literalura.service.ConsumoAPI;
import com.alura.Literalura.service.ConverteDados;

import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books/?search=";

    private LivroRepository livroRepository;
    private AutorRepository autorRepository;

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    \n*************************************************
                    ESCOLHA UMA OPÇÃO:
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    0 - Sair
                    *************************************************
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1 -> buscarLivroWeb();
                case 2 -> listarLivrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosPorAno();
                case 5 -> listarLivrosPorIdioma();
                case 0 -> System.out.println("Encerrando a aplicação...");
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void buscarLivroWeb() {
        System.out.println("Digite o nome do livro para busca:");
        var nomeLivro = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeLivro.replace(" ", "%20"));
        DadosResultados dados = conversor.obterDados(json, DadosResultados.class);

        if (dados.resultados().isEmpty()) {
            System.out.println("Livro não encontrado na API.");
            return;
        }

        DadosLivro dadosLivro = dados.resultados().get(0);
        Livro livro = new Livro(dadosLivro);

        if (!dadosLivro.autores().isEmpty()) {
            DadosAutor dadosAutor = dadosLivro.autores().get(0);
            Autor autor = autorRepository.findByNome(dadosAutor.nome());

            if (autor == null) {
                autor = new Autor(dadosAutor);
                autorRepository.save(autor);
            }
            livro.setAutor(autor);
        }

        try {
            livroRepository.save(livro);
            System.out.println("Livro salvo com sucesso: " + livro.getTitulo());
        } catch (Exception e) {
            System.out.println("Este livro já está cadastrado no banco de dados.");
        }
    }

    private void listarLivrosRegistrados() {
        List<Livro> livros = livroRepository.findAll();
        livros.forEach(l -> System.out.println(l.toString() + " | Autor: " + (l.getAutor() != null ? l.getAutor().getNome() : "Desconhecido")));
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivosPorAno() {
        System.out.println("Digite o ano para pesquisar autores vivos:");
        var ano = leitura.nextInt();
        List<Autor> autoresVivos = autorRepository.buscarAutoresVivosNoAno(ano);

        if (autoresVivos.isEmpty()) {
            System.out.println("Nenhum autor encontrado vivo neste ano.");
        } else {
            autoresVivos.forEach(System.out::println);
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.println("Digite o idioma para busca (ex: pt, en, es, fr):");
        var idioma = leitura.nextLine();
        List<Livro> livrosIdioma = livroRepository.findByIdioma(idioma);

        if (livrosIdioma.isEmpty()) {
            System.out.println("Nenhum livro encontrado com o idioma inserido.");
        } else {
            System.out.println("Temos " + livrosIdioma.size() + " livro(s) no idioma '" + idioma + "':");
            livrosIdioma.forEach(System.out::println);
        }
    }
}
