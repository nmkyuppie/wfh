/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  rajaser
 * Created: Sep 7, 2017
 */

-- Database: wfh

DROP DATABASE wfh;

CREATE DATABASE wfh
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Table: public.timetracker

-- DROP TABLE public.timetracker;

CREATE TABLE public.timetracker
(
    id integer NOT NULL DEFAULT nextval('timetracker_id_seq1'::regclass),
    empid integer,
    starttime timestamp without time zone,
    endtime timestamp without time zone,
    CONSTRAINT timetracker_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.timetracker
    OWNER to postgres;

-- Table: public.break

-- DROP TABLE public.break;

CREATE TABLE public.break
(
    id integer NOT NULL DEFAULT nextval('break_id_seq'::regclass),
    timetrackerid integer,
    starttime timestamp without time zone,
    endtime timestamp without time zone,
    CONSTRAINT break_pkey PRIMARY KEY (id),
    CONSTRAINT break_timetrackerid_fkey FOREIGN KEY (timetrackerid)
        REFERENCES public.timetracker (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.break
    OWNER to postgres;

-- Table: public.effective

-- DROP TABLE public.effective;

CREATE TABLE public.effective
(
    id integer NOT NULL DEFAULT nextval('effective_id_seq'::regclass),
    timetrackerid integer,
    starttime timestamp without time zone,
    endtime timestamp without time zone,
    CONSTRAINT effective_pkey PRIMARY KEY (id),
    CONSTRAINT effective_timetrackerid_fkey FOREIGN KEY (timetrackerid)
        REFERENCES public.timetracker (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.effective
    OWNER to postgres;

-- Table: public.idle

-- DROP TABLE public.idle;

CREATE TABLE public.idle
(
    id integer NOT NULL DEFAULT nextval('idle_id_seq'::regclass),
    timetrackerid integer,
    starttime timestamp without time zone,
    endtime timestamp without time zone,
    CONSTRAINT idle_pkey PRIMARY KEY (id),
    CONSTRAINT idle_timetrackerid_fkey FOREIGN KEY (timetrackerid)
        REFERENCES public.timetracker (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.idle
    OWNER to postgres;

-- Table: public.settings

-- DROP TABLE public.settings;

CREATE TABLE public.settings
(
    key character varying COLLATE pg_catalog."default",
    value character varying COLLATE pg_catalog."default"
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.settings
    OWNER to postgres;