CREATE TABLE IF NOT EXISTS public.t_privilege
(
    id   bigint                                              NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT t_privilege_pkey PRIMARY KEY (id),
    CONSTRAINT uk_privilegeName UNIQUE (name)
)
    TABLESPACE pg_default;

ALTER TABLE public.t_privilege
    OWNER to postgres;