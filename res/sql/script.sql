CREATE TABLE autor(
	autor_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(100) NOT NULL,
	sobrenome VARCHAR(100) NOT NULL,
	info TEXT
)engine=InnoDB;

CREATE TABLE assunto(
	assunto_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(100) NOT NULL,
	cores VARCHAR(30),
	cdu VARCHAR(20)
)engine=InnoDB;

CREATE TABLE editora(
	editora_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(100) NOT NULL
);

CREATE TABLE livro(
	livro_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	titulo VARCHAR(200) NOT NULL,
	resumo TEXT NOT NULL,
	isbn INTEGER,
	cutter VARCHAR(20),
	editora_id INTEGER,
	edicao VARCHAR(30),
	volume INTEGER,
	num_paginas INTEGER,
	assunto_id INTEGER,
	data_publicacao DATE,
	local_publicacao VARCHAR(100)
)engine=InnoDB;

ALTER TABLE livro ADD CONSTRAINT fk_editora_livro FOREIGN KEY(editora_id) REFERENCES editora(editora_id) ON UPDATE CASCADE;
ALTER TABLE livro ADD CONSTRAINT fk_assunto_livro FOREIGN KEY(assunto_id) REFERENCES assunto(assunto_id) ON UPDATE CASCADE;

CREATE TABLE autor_livro(
	autor_id INTEGER NOT NULL,
	livro_id INTEGER NOT NULL,
	PRIMARY KEY(autor_id, livro_id)
);

ALTER TABLE autor_livro ADD CONSTRAINT fk_autor_livro FOREIGN KEY(autor_id) REFERENCES autor(autor_id) ON UPDATE CASCADE;
ALTER TABLE autor_livro ADD CONSTRAINT fk_livro_autor FOREIGN KEY(livro_id) REFERENCES livro(livro_id) ON UPDATE CASCADE;

CREATE TABLE categoria(
	categoria_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(200)
)engine=InnoDB;

CREATE TABLE categoria_livro(
	categoria_id INTEGER NOT NULL,
	livro_id INTEGER NOT NULL,
	PRIMARY KEY(categoria_id, livro_id)
)engine=InnoDB;

ALTER TABLE categoria_livro ADD CONSTRAINT fk_categoria_livro FOREIGN KEY(categoria_id) REFERENCES categoria(categoria_id) ON UPDATE CASCADE;
ALTER TABLE categoria_livro ADD CONSTRAINT fk_livro_categoria FOREIGN KEY(livro_id) REFERENCES livro(livro_id) ON UPDATE CASCADE;

CREATE TABLE secao(
	secao_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(50) NOT NULL
);

CREATE TABLE origem(
	origem_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(50) NOT NULL
);

CREATE TABLE exemplar(
	num_registro INTEGER NOT NULL PRIMARY KEY,
	livro_id INTEGER NOT NULL,
	secao_id INTEGER,
	data_aquisicao DATE,
	origem_id INTEGER,
	fixo TINYINT NOT NULL DEFAULT 0,
	situacao INTEGER NOT NULL
);

ALTER TABLE exemplar ADD CONSTRAINT fk_livro_exemplar FOREIGN KEY(livro_id) REFERENCES livro(livro_id) ON UPDATE CASCADE;
ALTER TABLE exemplar ADD CONSTRAINT fk_secao_exemplar FOREIGN KEY(secao_id) REFERENCES secao(secao_id) ON UPDATE CASCADE;
ALTER TABLE exemplar ADD CONSTRAINT fk_origem_exemplar FOREIGN KEY(origem_id) REFERENCES origem(origem_id) ON UPDATE CASCADE;

CREATE TABLE baixa(
	baixa_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	num_registro INTEGER NOT NULL,
	data_hora TIMESTAMP NOT NULL,
	causa VARCHAR(50) NOT NULL
);

ALTER TABLE baixa ADD CONSTRAINT fk_exemplar_baixa FOREIGN KEY(num_registro) REFERENCES exemplar(num_registro) ON UPDATE CASCADE;

CREATE TABLE estado(
	sigla_estado VARCHAR(3) NOT NULL PRIMARY KEY,
	nome_estado VARCHAR(100) NOT NULL
);

CREATE TABLE cidade(
	cidade_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(200) NOT NULL,
	sigla_estado VARCHAR(3) NOT NULL
);

ALTER TABLE cidade ADD CONSTRAINT fk_estado_cidade FOREIGN KEY(sigla_estado) REFERENCES estado(sigla_estado) ON UPDATE CASCADE;

CREATE TABLE endereco(
	endereco_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
	logradouro VARCHAR(200) NOT NULL,
	numero INTEGER,
	cidade_id INTEGER,
	bairro VARCHAR(100),
	cep CHAR(8),
	complemento VARCHAR(30)
);

ALTER TABLE endereco ADD CONSTRAINT fk_cidade_endereco FOREIGN KEY(cidade_id) REFERENCES cidade(cidade_id) ON UPDATE CASCADE;

CREATE TABLE tipo_usuario(
    tipo_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(20),
    num_livros_emp INTEGER NOT NULL,
    num_livros_res INTEGER NOT NULL,
    dias_emprestimo INTEGER NOT NULL,
    num_renov INTEGER NOT NULL
);

CREATE TABLE usuario(
    usuario_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    tipo_id INTEGER NOT NULL,
    nome VARCHAR(200) NOT NULL,
    cpf CHAR(11),
    telefone CHAR(10),
    celular CHAR(10),
    email VARCHAR(200),
    endereco_id INTEGER,
    login VARCHAR(20),
    senha CHAR(35)
);

ALTER TABLE usuario ADD CONSTRAINT fk_tipo_usuario FOREIGN KEY(tipo_id) REFERENCES tipo_usuario(tipo_id) ON UPDATE CASCADE;
ALTER TABLE usuario ADD CONSTRAINT fk_endereco_usuario FOREIGN KEY(endereco_id) REFERENCES endereco(endereco_id) ON UPDATE CASCADE;

CREATE TABLE reserva(
	reserva_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    num_registro INTEGER NOT NULL,
    usuario_id INTEGER NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    data_hora DATE NOT NULL,
    data_hora_retirada DATETIME,
    cancelada TINYINT NOT NULL DEFAULT 0,
);

ALTER TABLE reserva ADD CONSTRAINT fk_exemplar_reserva FOREIGN KEY(num_registro) REFERENCES exemplar(num_registro) ON UPDATE CASCADE;
ALTER TABLE reserva ADD CONSTRAINT fk_usuario_reserva FOREIGN KEY(usuario_id) REFERENCES usuario(usuario_id) ON UPDATE CASCADE;

CREATE TABLE emprestimo(
    emprestimo_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    data_hora TIMESTAMP NOT NULL,
    usuario_id INTEGER NOT NULL,
    num_registro INTEGER NOT NULL,
    renovacoes INTEGER,
    data_hora_devolucao DATETIME,
    multa double
);

ALTER TABLE emprestimo ADD CONSTRAINT fk_usuario_emprestimo FOREIGN KEY(usuario_id) REFERENCES usuario(usuario_id) ON UPDATE CASCADE;
ALTER TABLE emprestimo ADD CONSTRAINT fk_exemplar_emprestimo FOREIGN KEY(num_registro) REFERENCES exemplar(num_registro) ON UPDATE CASCADE;