-- =========================================
-- SCHEMA
-- =========================================
CREATE SCHEMA IF NOT EXISTS folha_ponto;
SET search_path TO folha_ponto;

-- folha_ponto.organizacao definition

-- Drop table

-- DROP TABLE folha_ponto.organizacao;

CREATE TABLE folha_ponto.organizacao (
	id uuid DEFAULT gen_random_uuid() NOT NULL,
	nome varchar(255) NOT NULL,
	status varchar(32) DEFAULT 'ATIVO'::character varying NOT NULL,
	criado_por varchar(64) NOT NULL,
	criado_em timestamp NOT NULL,
	atualizado_por varchar(64) NULL,
	atualizado_em timestamp NULL,
	CONSTRAINT organizacao_pkey PRIMARY KEY (id)
);
CREATE INDEX idx_org_criado_por ON folha_ponto.organizacao USING btree (criado_por);


-- folha_ponto.unidade definition

-- Drop table

-- DROP TABLE folha_ponto.unidade;

CREATE TABLE folha_ponto.unidade (
	id uuid DEFAULT gen_random_uuid() NOT NULL,
	organizacao_id uuid NOT NULL,
	nome varchar(255) NOT NULL,
	tipo_geometria varchar(16) NOT NULL,
	centro_geo_lat float8 NULL,
	centro_geo_lng float8 NULL,
	raio_geo_metros int4 NULL,
	poligono_geo jsonb NULL,
	criado_por varchar(64) NOT NULL,
	criado_em timestamp NOT NULL,
	atualizado_por varchar(64) NULL,
	atualizado_em timestamp NULL,
	CONSTRAINT unidade_pkey PRIMARY KEY (id),
	CONSTRAINT unidade_organizacao_id_fkey FOREIGN KEY (organizacao_id) REFERENCES folha_ponto.organizacao(id)
);
CREATE INDEX idx_unidade_org ON folha_ponto.unidade USING btree (organizacao_id);


-- folha_ponto.vinculo_usuario definition

-- Drop table

-- DROP TABLE folha_ponto.vinculo_usuario;

CREATE TABLE folha_ponto.vinculo_usuario (
	id uuid DEFAULT gen_random_uuid() NOT NULL,
	organizacao_id uuid NOT NULL,
	usuario_id varchar(64) NOT NULL,
	papel varchar(32) NOT NULL,
	data_inicio timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	data_fim timestamp NULL,
	status varchar(32) DEFAULT 'ATIVO'::character varying NOT NULL,
	criado_por varchar(64) NOT NULL,
	criado_em timestamp NOT NULL,
	atualizado_por varchar(64) NULL,
	atualizado_em timestamp NULL,
	CONSTRAINT vinculo_usuario_pkey PRIMARY KEY (id),
	CONSTRAINT vinculo_usuario_organizacao_id_fkey FOREIGN KEY (organizacao_id) REFERENCES folha_ponto.organizacao(id)
);
CREATE UNIQUE INDEX uq_vinculo_usuario_org ON folha_ponto.vinculo_usuario USING btree (usuario_id, organizacao_id);


-- folha_ponto.alocacao_unidade definition

-- Drop table

-- DROP TABLE folha_ponto.alocacao_unidade;

CREATE TABLE folha_ponto.alocacao_unidade (
	id uuid NOT NULL,
	vinculo_usuario_id uuid NOT NULL,
	unidade_id uuid NOT NULL,
	papel_operacional varchar(32) NOT NULL,
	status varchar(32) DEFAULT 'ATIVO'::character varying NOT NULL,
	data_inicio timestamp NOT NULL,
	data_fim timestamp NULL,
	criado_por varchar(64) NOT NULL,
	criado_em timestamp NOT NULL,
	atualizado_por varchar(64) NULL,
	atualizado_em timestamp NULL,
	CONSTRAINT alocacao_unidade_pkey PRIMARY KEY (id),
	CONSTRAINT chk_alocacao_papel CHECK (((papel_operacional)::text = ANY ((ARRAY['PARTICIPANTE'::character varying, 'MANAGER'::character varying, 'GESTOR_TITULAR'::character varying, 'GESTOR_SUBSTITUTO'::character varying, 'ASSISTENTE'::character varying, 'OWNER'::character varying])::text[]))),
	CONSTRAINT chk_alocacao_status CHECK (((status)::text = ANY ((ARRAY['ATIVO'::character varying, 'SUSPENSO'::character varying, 'ENCERRADO'::character varying])::text[]))),
	CONSTRAINT fk_alocacao_unidade FOREIGN KEY (unidade_id) REFERENCES folha_ponto.unidade(id) ON DELETE CASCADE,
	CONSTRAINT fk_alocacao_vinculo FOREIGN KEY (vinculo_usuario_id) REFERENCES folha_ponto.vinculo_usuario(id) ON DELETE CASCADE
);
CREATE INDEX idx_alocacao_vinculo ON folha_ponto.alocacao_unidade USING btree (vinculo_usuario_id, status);


-- folha_ponto.convite definition

-- Drop table

-- DROP TABLE folha_ponto.convite;

CREATE TABLE folha_ponto.convite (
	id uuid DEFAULT gen_random_uuid() NOT NULL,
	organizacao_id uuid NOT NULL,
	unidade_id uuid NULL,
	papel_esperado varchar(32) DEFAULT 'PARTICIPANTE'::character varying NOT NULL,
	codigo varchar(32) NOT NULL,
	data_expiracao timestamp NOT NULL,
	usado bool DEFAULT false NOT NULL,
	criado_por varchar(64) NOT NULL,
	criado_em timestamp NOT NULL,
	atualizado_por varchar(64) NULL,
	atualizado_em timestamp NULL,
	CONSTRAINT chk_convite_papel CHECK (((papel_esperado)::text = ANY ((ARRAY['PARTICIPANTE'::character varying, 'MANAGER'::character varying, 'GESTOR_TITULAR'::character varying, 'GESTOR_SUBSTITUTO'::character varying, 'ASSISTENTE'::character varying, 'OWNER'::character varying])::text[]))),
	CONSTRAINT convite_pkey PRIMARY KEY (id),
	CONSTRAINT convite_organizacao_id_fkey FOREIGN KEY (organizacao_id) REFERENCES folha_ponto.organizacao(id),
	CONSTRAINT fk_convite_unidade FOREIGN KEY (unidade_id) REFERENCES folha_ponto.unidade(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX uq_convite_codigo ON folha_ponto.convite USING btree (codigo);


-- folha_ponto.registro_presenca definition

-- Drop table

-- DROP TABLE folha_ponto.registro_presenca;

CREATE TABLE folha_ponto.registro_presenca (
	id uuid DEFAULT gen_random_uuid() NOT NULL,
	organizacao_id uuid NOT NULL,
	unidade_id uuid NOT NULL,
	usuario_id varchar(64) NOT NULL,
	latitude float8 NULL,
	longitude float8 NULL,
	precisao_gps float8 NULL,
	referencia_biometrica uuid NULL,
	pontuacao_biometrica float8 NULL,
	biometria_valida bool NULL,
	dispositivo_id varchar(128) NOT NULL,
	modo_registro varchar(16) NOT NULL,
	usuario_intermediario_id varchar(64) NULL,
	pontuacao_risco float8 NULL,
	indicadores_risco jsonb NULL,
	status_tecnico varchar(32) DEFAULT 'RECEBIDO'::character varying NOT NULL,
	status_administrativo varchar(32) DEFAULT 'PENDENTE'::character varying NOT NULL,
	capturado_em timestamp NOT NULL,
	recebido_no_servidor_em timestamp NOT NULL,
	criado_por varchar(64) NOT NULL,
	criado_em timestamp NOT NULL,
	CONSTRAINT registro_presenca_pkey PRIMARY KEY (id),
	CONSTRAINT registro_presenca_organizacao_id_fkey FOREIGN KEY (organizacao_id) REFERENCES folha_ponto.organizacao(id),
	CONSTRAINT registro_presenca_unidade_id_fkey FOREIGN KEY (unidade_id) REFERENCES folha_ponto.unidade(id)
);
CREATE INDEX idx_presenca_org_tempo ON folha_ponto.registro_presenca USING btree (organizacao_id, recebido_no_servidor_em);
CREATE INDEX idx_presenca_org_usuario ON folha_ponto.registro_presenca USING btree (organizacao_id, usuario_id);
CREATE INDEX idx_presenca_unidade_tempo ON folha_ponto.registro_presenca USING btree (unidade_id, recebido_no_servidor_em);


-- folha_ponto.alocacao_unidade_aud definition

-- Drop table

-- DROP TABLE folha_ponto.alocacao_unidade_aud;

CREATE TABLE folha_ponto.alocacao_unidade_aud (
	id uuid NOT NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	vinculo_usuario_id uuid NULL,
	unidade_id uuid NULL,
	papel_operacional varchar(32) NULL,
	status varchar(32) NULL,
	data_inicio timestamp NULL,
	data_fim timestamp NULL,
	criado_por varchar(64) NULL,
	criado_em timestamp NULL,
	atualizado_por varchar(64) NULL,
	atualizado_em timestamp NULL,
	rev_end int8 NULL,
	revend_tstmp timestamp NULL,
	CONSTRAINT alocacao_unidade_aud_pkey PRIMARY KEY (id, rev)
);


-- folha_ponto.vinculo_usuario_aud definition

-- Drop table

-- DROP TABLE folha_ponto.vinculo_usuario_aud;

CREATE TABLE folha_ponto.vinculo_usuario_aud (
	id uuid NOT NULL,
	rev int8 NOT NULL,
	revtype int2 NULL,
	organizacao_id uuid NULL,
	usuario_id varchar(64) NULL,
	papel varchar(32) NULL,
	status varchar(32) NULL,
	data_inicio timestamp NULL,
	data_fim timestamp NULL,
	criado_por varchar(64) NULL,
	criado_em timestamp NULL,
	atualizado_por varchar(64) NULL,
	atualizado_em timestamp NULL,
	rev_end int8 NULL,
	revend_tstmp timestamp NULL,
	CONSTRAINT vinculo_usuario_aud_pkey PRIMARY KEY (id, rev)
);


-- folha_ponto.alocacao_unidade_aud foreign keys

ALTER TABLE folha_ponto.alocacao_unidade_aud ADD CONSTRAINT fk_alocacao_unidade_aud_rev FOREIGN KEY (rev) REFERENCES auditoria.revinfo(id);

-- folha_ponto.vinculo_usuario_aud foreign keys

ALTER TABLE folha_ponto.vinculo_usuario_aud ADD CONSTRAINT fk_vinculo_usuario_aud_rev FOREIGN KEY (rev) REFERENCES auditoria.revinfo(id);
