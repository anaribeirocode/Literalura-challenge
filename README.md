# LiterAlura - Catálogo de Livros 📚

## Descrição do Projeto
O LiterAlura é uma aplicação back-end desenvolvida em Java com o framework **Spring Boot**. O objetivo do projeto é construir um catálogo de livros dinâmico que consome dados da [API Gutendex](https://gutendex.com/) (baseada no acervo do Project Gutenberg) e persiste as informações de livros e autores em um banco de dados relacional **PostgreSQL**.

Este projeto faz parte do desafio da trilha de Java do **Programa ONE (Oracle Next Education)** em parceria com a Alura.

## ⚙️ Funcionalidades Implementadas
O sistema possui um menu interativo via console que permite:
- **Buscar livros pelo título:** Consome a API Gutendex, formata o JSON recebido e salva o livro e seu respectivo autor no banco de dados.
- **Listar livros registrados:** Retorna todos os livros que já foram salvos no banco local.
- **Listar autores registrados:** Retorna todos os autores salvos no banco local.
- **Listar autores vivos em um determinado ano:** Utiliza *Derived Queries* do Spring Data JPA para filtrar autores pela data de nascimento e falecimento.
- **Listar livros em um determinado idioma:** Filtra o banco de dados para exibir livros salvos em idiomas específicos (ex: `en`, `pt`).

## 🛠️ Tecnologias Utilizadas
- **Java 21**
- **Spring Boot**
- **Spring Data JPA** (Mapeamento Objeto-Relacional e manipulação do banco de dados)
- **PostgreSQL** (Banco de dados relacional)
- **Jackson (ObjectMapper)** (Desserialização dos dados JSON da API)
- **Maven** (Gerenciador de dependências)
