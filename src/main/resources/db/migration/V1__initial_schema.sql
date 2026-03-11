CREATE SEQUENCE IF NOT EXISTS pessoa_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS endereco_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS localizacao_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS local_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS forum_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS pergunta_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS resposta_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS usuario_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS denuncia_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS imagem_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS pessoa (
    id BIGINT PRIMARY KEY DEFAULT nextval('pessoa_seq'),
    nome VARCHAR(255),
    imagem VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS endereco (
    id BIGINT PRIMARY KEY DEFAULT nextval('endereco_seq'),
    logradouro VARCHAR(200) NOT NULL,
    numero INTEGER,
    bairro VARCHAR(200),
    cidade VARCHAR(120) NOT NULL,
    uf VARCHAR(2) NOT NULL,
    cep VARCHAR(12),
    complemento VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS localizacao (
    id BIGINT PRIMARY KEY DEFAULT nextval('localizacao_seq'),
    latitude VARCHAR(255),
    longitude VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT PRIMARY KEY DEFAULT nextval('usuario_seq'),
    pessoa_id BIGINT,
    email VARCHAR(255) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    CONSTRAINT ux_usuario_email UNIQUE (email),
    CONSTRAINT fk_usuario_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
);

CREATE TABLE IF NOT EXISTS forum (
    id BIGINT PRIMARY KEY DEFAULT nextval('forum_seq'),
    usuario_id BIGINT,
    CONSTRAINT fk_forum_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS pergunta (
    id BIGINT PRIMARY KEY DEFAULT nextval('pergunta_seq'),
    pergunta VARCHAR(255),
    forum_id BIGINT,
    CONSTRAINT fk_pergunta_forum FOREIGN KEY (forum_id) REFERENCES forum(id)
);

CREATE TABLE IF NOT EXISTS resposta (
    id BIGINT PRIMARY KEY DEFAULT nextval('resposta_seq'),
    resposta VARCHAR(255),
    pergunta_id BIGINT,
    CONSTRAINT fk_resposta_pergunta FOREIGN KEY (pergunta_id) REFERENCES pergunta(id)
);

CREATE TABLE IF NOT EXISTS local (
    id BIGINT PRIMARY KEY DEFAULT nextval('local_seq'),
    nome VARCHAR(255),
    localizacao_id BIGINT,
    endereco_id BIGINT,
    CONSTRAINT fk_local_localizacao FOREIGN KEY (localizacao_id) REFERENCES localizacao(id),
    CONSTRAINT fk_local_endereco FOREIGN KEY (endereco_id) REFERENCES endereco(id)
);

CREATE TABLE IF NOT EXISTS denuncia (
    id BIGINT PRIMARY KEY DEFAULT nextval('denuncia_seq'),
    nome_local VARCHAR(200) NOT NULL,
    endereco_id BIGINT NOT NULL,
    problema TEXT NOT NULL,
    sugestao TEXT,
    criado_em TIMESTAMP,
    usuario_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_denuncia_endereco FOREIGN KEY (endereco_id) REFERENCES endereco(id),
    CONSTRAINT fk_denuncia_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS imagem (
    id BIGINT PRIMARY KEY DEFAULT nextval('imagem_seq'),
    denuncia_id BIGINT NOT NULL,
    dados OID NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    filename VARCHAR(255),
    tamanho_bytes BIGINT,
    ordem INTEGER,
    criado_em TIMESTAMP,
    CONSTRAINT fk_imagem_denuncia FOREIGN KEY (denuncia_id) REFERENCES denuncia(id) ON DELETE CASCADE
);
