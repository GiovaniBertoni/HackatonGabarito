# Projeto Corretor de Gabaritos - Hackathon 5º Período

Este projeto é uma solução completa (Web + Mobile) para a correção automática de provas objetivas, desenvolvido para o Hackathon do 5º Período de Sistemas para Internet da UniALFA.

O sistema permite que administradores façam a gestão de turmas, disciplinas e alunos; que professores criem provas com pesos dinâmicos e corrijam-nas através de um aplicativo móvel; e que alunos consultem as suas notas.

---

## 🚀 Tecnologias Utilizadas

**Backend:**
* Java 21
* Spring Boot 3
* Spring Data JPA
* Spring Security
* MySQL
* Thymeleaf + Bootstrap 5

**Frontend (Mobile):**
* Flutter 3
* Dart
* `http` para consumo de API
* `shared_preferences` para gestão de sessão

---

## ✨ Funcionalidades Implementadas

* **Painel Web Administrativo:** CRUD completo para Alunos, Turmas e Disciplinas, com gestão de senhas e acesso a uma consola de gestão da base de dados.
* **Painel Web do Professor:** Criação de provas com número de questões e pesos dinâmicos, e visualização de resultados com estatísticas (média, maior/menor nota) e ordenação por colunas.
* **Painel Web do Aluno:** Login e visualização do seu histórico de notas detalhado.
* **Aplicação Mobile para Professores:**
    * Login seguro e automático (sessão persistente).
    * Seleção de aluno e prova a partir de dados reais do backend.
    * Preenchimento de gabarito com número dinâmico de questões.
    * Envio das respostas para correção automática e instantânea no sistema.

---

## 🔧 Como Executar o Projeto

### Pré-requisitos
* Java 21+
* Maven 3+
* MySQL (ou XAMPP/WAMP) a correr na porta 3306.
* Flutter 3+
* Uma IDE para Java (ex: IntelliJ)
* Uma IDE para Flutter (ex: VS Code, Android Studio)

### Backend (Java)
1.  Crie uma base de dados vazia no seu MySQL chamada `hackathon_db`.
2.  Clone o repositório: `git clone https://github.com/GiovaniBertoni/HackatonGabarito.git`
3.  Abra o projeto na sua IDE.
4.  No ficheiro `src/main/resources/application.properties`, configure o seu utilizador e senha do MySQL.
5.  Execute a classe principal `HackathonApplication.java`. O backend irá popular a base de dados com dados de teste e estará a correr em `http://localhost:8080`.

### Aplicação Mobile (Flutter)
1.  Clone o repositório: `git clone https://github.com/GiovaniBertoni/HackatonGabaritoFlutter.git`
2.  Abra o projeto na sua IDE.
3.  Execute `flutter pub get` no terminal para descarregar as dependências.
4.  No ficheiro `lib/services/api_service.dart`, altere a variável `_baseUrl` para o endereço IP da máquina que está a correr o backend (ex: `http://192.168.1.39:8080`).
5.  Execute a aplicação num emulador ou dispositivo físico com `flutter run` ou premindo F5.

---

## 👥 Equipa

* Breno ra: 14000
* Leticia ra: 13736
* Giovani ra: 14272
* Ryan ra: 13604
