CREATE TABLE IF NOT EXISTS public.t_users_privileges
(
    user_id      bigint NOT NULL,
    privilege_id bigint NOT NULL,
    CONSTRAINT t_users_privileges_pkey PRIMARY KEY (user_id, privilege_id),
    CONSTRAINT fk5a5ltpwhvxl8xsry8fe5r0hcu FOREIGN KEY (user_id)
        REFERENCES public.t_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk92acwhja175cu4pud6fyg4i88 FOREIGN KEY (privilege_id)
        REFERENCES public.t_privilege (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    TABLESPACE pg_default;

ALTER TABLE public.t_users_privileges
    OWNER to postgres;