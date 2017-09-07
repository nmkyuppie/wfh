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

-- SCHEMA: wfhschema

DROP SCHEMA wfhschema ;

CREATE SCHEMA wfhschema
    AUTHORIZATION postgres;

-- Table: wfhschema.time_tracker

DROP TABLE wfhschema.time_tracker;

CREATE TABLE wfhschema.time_tracker
(
    timetracker_starttime time(6) with time zone,
    timetracker_id integer NOT NULL,
    timetracker_emp_id integer NOT NULL,
    timetracker_endtime time(6) with time zone,
    CONSTRAINT timetracker_primarykey PRIMARY KEY (timetracker_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE wfhschema.time_tracker
    OWNER to postgres;

-- Table: wfhschema.effective_hours

DROP TABLE wfhschema.effective_hours;

CREATE TABLE wfhschema.effective_hours
(
    effective_id integer NOT NULL,
    effective_timetracker_id integer,
    effective_starttime time with time zone,
    effective_endtime time with time zone,
    CONSTRAINT effective_hours_pkey PRIMARY KEY (effective_id),
    CONSTRAINT effective_timetracker_foreignkey FOREIGN KEY (effective_timetracker_id)
        REFERENCES wfhschema.time_tracker (timetracker_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE wfhschema.effective_hours
    OWNER to postgres;

-- Table: wfhschema.idle_timetracker

DROP TABLE wfhschema.idle_timetracker;

CREATE TABLE wfhschema.idle_timetracker
(
    idle_id integer NOT NULL,
    idle_timetracker_id integer NOT NULL,
    idle_starttime time with time zone,
    idle_endtime time with time zone,
    CONSTRAINT idle_timetracker_pkey PRIMARY KEY (idle_id),
    CONSTRAINT idle_timetracker_foreignkey FOREIGN KEY (idle_timetracker_id)
        REFERENCES wfhschema.time_tracker (timetracker_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE wfhschema.idle_timetracker
    OWNER to postgres;

-- Table: wfhschema.break_timetracker

DROP TABLE wfhschema.break_timetracker;

CREATE TABLE wfhschema.break_timetracker
(
    break_id integer NOT NULL,
    break_timetracker_id integer,
    break_starttime time with time zone,
    break_endtime time with time zone,
    CONSTRAINT break_timetracker_pkey PRIMARY KEY (break_id),
    CONSTRAINT break_timetracker_foreignkey FOREIGN KEY (break_timetracker_id)
        REFERENCES wfhschema.time_tracker (timetracker_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE wfhschema.break_timetracker
    OWNER to postgres;

