-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 23/06/2025 às 19:10
-- Versão do servidor: 10.4.32-MariaDB
-- Versão do PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `hackathon_db`
--
CREATE DATABASE IF NOT EXISTS `hackathon_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `hackathon_db`;

-- --------------------------------------------------------

--
-- Estrutura para tabela `aluno`
--

DROP TABLE IF EXISTS `aluno`;
CREATE TABLE `aluno` (
  `id` bigint(20) NOT NULL,
  `usuario_id` bigint(20) DEFAULT NULL,
  `ra` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `aluno`
--

INSERT INTO `aluno` (`id`, `usuario_id`, `ra`) VALUES
(1, 3, '0001'),
(2, 4, '0002');

-- --------------------------------------------------------

--
-- Estrutura para tabela `disciplina`
--

DROP TABLE IF EXISTS `disciplina`;
CREATE TABLE `disciplina` (
  `id` bigint(20) NOT NULL,
  `nome` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `disciplina`
--

INSERT INTO `disciplina` (`id`, `nome`) VALUES
(1, 'Frameworks de Desenvolvimento Web');

-- --------------------------------------------------------

--
-- Estrutura para tabela `gabarito`
--

DROP TABLE IF EXISTS `gabarito`;
CREATE TABLE `gabarito` (
  `id` bigint(20) NOT NULL,
  `prova_id` bigint(20) NOT NULL,
  `respostas` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `gabarito`
--

INSERT INTO `gabarito` (`id`, `prova_id`, `respostas`) VALUES
(1, 1, '{\"1\":{\"resposta\":\"A\",\"valor\":2.0},\"5\":{\"resposta\":\"E\",\"valor\":2.0},\"4\":{\"resposta\":\"B\",\"valor\":2.0},\"3\":{\"resposta\":\"D\",\"valor\":2.0},\"2\":{\"resposta\":\"C\",\"valor\":2.0}}');

-- --------------------------------------------------------

--
-- Estrutura para tabela `prova`
--

DROP TABLE IF EXISTS `prova`;
CREATE TABLE `prova` (
  `data_aplicacao` date DEFAULT NULL,
  `disciplina_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `professor_id` bigint(20) NOT NULL,
  `turma_id` bigint(20) NOT NULL,
  `titulo` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `prova`
--

INSERT INTO `prova` (`data_aplicacao`, `disciplina_id`, `id`, `professor_id`, `turma_id`, `titulo`) VALUES
('2025-06-23', 1, 1, 2, 1, 'Avaliação 1 - Spring Boot');

-- --------------------------------------------------------

--
-- Estrutura para tabela `resposta_aluno`
--

DROP TABLE IF EXISTS `resposta_aluno`;
CREATE TABLE `resposta_aluno` (
  `nota` double DEFAULT NULL,
  `aluno_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `prova_id` bigint(20) NOT NULL,
  `respostas` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `turma`
--

DROP TABLE IF EXISTS `turma`;
CREATE TABLE `turma` (
  `ano` int(11) NOT NULL,
  `id` bigint(20) NOT NULL,
  `ano_letivo` varchar(255) DEFAULT NULL,
  `nome` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `turma`
--

INSERT INTO `turma` (`ano`, `id`, `ano_letivo`, `nome`) VALUES
(2025, 1, NULL, 'Sistemas para Internet - 5º Período');

-- --------------------------------------------------------

--
-- Estrutura para tabela `turma_alunos`
--

DROP TABLE IF EXISTS `turma_alunos`;
CREATE TABLE `turma_alunos` (
  `aluno_id` bigint(20) NOT NULL,
  `turma_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `turma_alunos`
--

INSERT INTO `turma_alunos` (`aluno_id`, `turma_id`) VALUES
(1, 1),
(2, 1);

-- --------------------------------------------------------

--
-- Estrutura para tabela `usuario`
--

DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `perfil` enum('ADMIN','PROFESSOR','ALUNO') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `usuario`
--

INSERT INTO `usuario` (`id`, `email`, `nome`, `senha`, `perfil`) VALUES
(1, 'admin@unialfa.com', 'Admin Geral', '$2a$10$ti1vlfD5kRbZkWZf0tKFveR1V3Q5xo9J6vyNBov2kF87u8A7D86z2', 'ADMIN'),
(2, 'professor@unialfa.com', 'Prof. Severino', '$2a$10$a2Uqzhh1bKfQwjSHGZuVd.G8fWS3AGMjxIIF73Xn7/cMvUqzLjmYC', 'PROFESSOR'),
(3, 'aluno.godofredo@unialfa.com', 'Godofredo Arantes', '$2a$10$Q0QVLQOj6x.q.C/.YyFd8.XDHwEdsSMuvZwG1aI74q/3I2MMpIPYi', 'ALUNO'),
(4, 'giovanibertoni99@gmail.com', 'Giovani de Melo Bertoni', '$2a$10$M/usYBFySpb/jf6/lpCZkevFSpm18CxzsgHmb/bM/ENkOay5O9LyS', 'ALUNO');

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `aluno`
--
ALTER TABLE `aluno`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_p3aj1urfrd6mo5m2ycjlae8g7` (`usuario_id`),
  ADD UNIQUE KEY `UK_h6vnjfpl82o82vn1tx4g75yq3` (`ra`);

--
-- Índices de tabela `disciplina`
--
ALTER TABLE `disciplina`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_5e7mrxyfs14yqtibi2frwwxcc` (`nome`);

--
-- Índices de tabela `gabarito`
--
ALTER TABLE `gabarito`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_a3wr3v416ot2aufu8x3tp099u` (`prova_id`);

--
-- Índices de tabela `prova`
--
ALTER TABLE `prova`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKk6aadu7rglwjyy25w9d61jyll` (`disciplina_id`),
  ADD KEY `FKna0qpnab1wtytcdvayh82fh67` (`professor_id`),
  ADD KEY `FKnc0asx3bnw4mgqri5pwks5a4u` (`turma_id`);

--
-- Índices de tabela `resposta_aluno`
--
ALTER TABLE `resposta_aluno`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKrrl5mqox9nvryrraau18kfkgs` (`aluno_id`),
  ADD KEY `FK4svty34gthtyrg25u4qv2lp3v` (`prova_id`);

--
-- Índices de tabela `turma`
--
ALTER TABLE `turma`
  ADD PRIMARY KEY (`id`);

--
-- Índices de tabela `turma_alunos`
--
ALTER TABLE `turma_alunos`
  ADD PRIMARY KEY (`aluno_id`,`turma_id`),
  ADD KEY `FKn95nbs8utamw04eggpbwou0px` (`turma_id`);

--
-- Índices de tabela `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_5171l57faosmj8myawaucatdw` (`email`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `aluno`
--
ALTER TABLE `aluno`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de tabela `disciplina`
--
ALTER TABLE `disciplina`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de tabela `gabarito`
--
ALTER TABLE `gabarito`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de tabela `prova`
--
ALTER TABLE `prova`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de tabela `resposta_aluno`
--
ALTER TABLE `resposta_aluno`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `turma`
--
ALTER TABLE `turma`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de tabela `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `aluno`
--
ALTER TABLE `aluno`
  ADD CONSTRAINT `FKsgpw6tb2kerkceshx1b10rhkg` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`);

--
-- Restrições para tabelas `gabarito`
--
ALTER TABLE `gabarito`
  ADD CONSTRAINT `FKfa6gchm3j7mo5raa24g2hb737` FOREIGN KEY (`prova_id`) REFERENCES `prova` (`id`);

--
-- Restrições para tabelas `prova`
--
ALTER TABLE `prova`
  ADD CONSTRAINT `FKk6aadu7rglwjyy25w9d61jyll` FOREIGN KEY (`disciplina_id`) REFERENCES `disciplina` (`id`),
  ADD CONSTRAINT `FKna0qpnab1wtytcdvayh82fh67` FOREIGN KEY (`professor_id`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `FKnc0asx3bnw4mgqri5pwks5a4u` FOREIGN KEY (`turma_id`) REFERENCES `turma` (`id`);

--
-- Restrições para tabelas `resposta_aluno`
--
ALTER TABLE `resposta_aluno`
  ADD CONSTRAINT `FK4svty34gthtyrg25u4qv2lp3v` FOREIGN KEY (`prova_id`) REFERENCES `prova` (`id`),
  ADD CONSTRAINT `FKrrl5mqox9nvryrraau18kfkgs` FOREIGN KEY (`aluno_id`) REFERENCES `aluno` (`id`);

--
-- Restrições para tabelas `turma_alunos`
--
ALTER TABLE `turma_alunos`
  ADD CONSTRAINT `FK4lta9eiqe04d0kiq6pcv1egih` FOREIGN KEY (`aluno_id`) REFERENCES `aluno` (`id`),
  ADD CONSTRAINT `FKn95nbs8utamw04eggpbwou0px` FOREIGN KEY (`turma_id`) REFERENCES `turma` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
