CREATE TABLE IF NOT EXISTS public.t_user
(
    id       bigint                                              NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    username character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT t_user_pkey PRIMARY KEY (id),
    CONSTRAINT uk_username UNIQUE (username)
)
    TABLESPACE pg_default;

ALTER TABLE public.t_user
    OWNER to postgres;