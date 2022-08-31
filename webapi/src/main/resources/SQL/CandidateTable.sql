CREATE TABLE public.candidate
(
    id bigint  NOT NULL GENERATED ALWAYS AS IDENTITY (START 1 INCREMENT 1 ),
    name character varying(45) NOT NULL,
    CONSTRAINT candidate_pkey PRIMARY KEY (id)
)