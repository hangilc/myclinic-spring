--
-- PostgreSQL database dump
--

-- Dumped from database version 11.0
-- Dumped by pg_dump version 11.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE IF EXISTS ONLY public.wqueue DROP CONSTRAINT IF EXISTS wqueue_visit_id_fkey;
ALTER TABLE IF EXISTS ONLY public.visit DROP CONSTRAINT IF EXISTS visit_shahokokuho_id_fkey;
ALTER TABLE IF EXISTS ONLY public.visit DROP CONSTRAINT IF EXISTS visit_roujin_id_fkey;
ALTER TABLE IF EXISTS ONLY public.visit DROP CONSTRAINT IF EXISTS visit_patient_id_fkey;
ALTER TABLE IF EXISTS ONLY public.visit DROP CONSTRAINT IF EXISTS visit_koukikourei_id_fkey;
ALTER TABLE IF EXISTS ONLY public.visit DROP CONSTRAINT IF EXISTS visit_kouhi_3_id_fkey;
ALTER TABLE IF EXISTS ONLY public.visit DROP CONSTRAINT IF EXISTS visit_kouhi_2_id_fkey;
ALTER TABLE IF EXISTS ONLY public.visit DROP CONSTRAINT IF EXISTS visit_kouhi_1_id_fkey;
ALTER TABLE IF EXISTS ONLY public.text DROP CONSTRAINT IF EXISTS text_visit_id_fkey;
ALTER TABLE IF EXISTS ONLY public.shouki DROP CONSTRAINT IF EXISTS shouki_visit_id_fkey;
ALTER TABLE IF EXISTS ONLY public.shinryou DROP CONSTRAINT IF EXISTS shinryou_visit_id_fkey;
ALTER TABLE IF EXISTS ONLY public.shinryou_attr DROP CONSTRAINT IF EXISTS shinryou_attr_shinryou_id_fkey;
ALTER TABLE IF EXISTS ONLY public.shahokokuho DROP CONSTRAINT IF EXISTS shahokokuho_patient_id_fkey;
ALTER TABLE IF EXISTS ONLY public.roujin DROP CONSTRAINT IF EXISTS roujin_patient_id_fkey;
ALTER TABLE IF EXISTS ONLY public.presc_example DROP CONSTRAINT IF EXISTS presc_example_iyakuhincode_fkey;
ALTER TABLE IF EXISTS ONLY public.pharma_queue DROP CONSTRAINT IF EXISTS pharma_queue_visit_id_fkey;
ALTER TABLE IF EXISTS ONLY public.payment DROP CONSTRAINT IF EXISTS payment_visit_id_fkey;
ALTER TABLE IF EXISTS ONLY public.koukikourei DROP CONSTRAINT IF EXISTS koukikourei_patient_id_fkey;
ALTER TABLE IF EXISTS ONLY public.kouhi DROP CONSTRAINT IF EXISTS kouhi_patient_id_fkey;
ALTER TABLE IF EXISTS ONLY public.intraclinic_tag_post DROP CONSTRAINT IF EXISTS intraclinic_tag_post_tag_id_fkey;
ALTER TABLE IF EXISTS ONLY public.intraclinic_tag_post DROP CONSTRAINT IF EXISTS intraclinic_tag_post_post_id_fkey;
ALTER TABLE IF EXISTS ONLY public.intraclinic_comment DROP CONSTRAINT IF EXISTS intraclinic_comment_post_id_fkey;
ALTER TABLE IF EXISTS ONLY public.gazou_label DROP CONSTRAINT IF EXISTS gazou_label_conduct_id_fkey;
ALTER TABLE IF EXISTS ONLY public.drug DROP CONSTRAINT IF EXISTS drug_visit_id_fkey;
ALTER TABLE IF EXISTS ONLY public.drug_attr DROP CONSTRAINT IF EXISTS drug_attr_drug_id_fkey;
ALTER TABLE IF EXISTS ONLY public.disease DROP CONSTRAINT IF EXISTS disease_patient_id_fkey;
ALTER TABLE IF EXISTS ONLY public.disease_adj DROP CONSTRAINT IF EXISTS disease_adj_shuushokugocode_fkey;
ALTER TABLE IF EXISTS ONLY public.disease_adj DROP CONSTRAINT IF EXISTS disease_adj_disease_id_fkey;
ALTER TABLE IF EXISTS ONLY public.conduct DROP CONSTRAINT IF EXISTS conduct_visit_id_fkey;
ALTER TABLE IF EXISTS ONLY public.conduct_shinryou DROP CONSTRAINT IF EXISTS conduct_shinryou_conduct_id_fkey;
ALTER TABLE IF EXISTS ONLY public.conduct_kizai DROP CONSTRAINT IF EXISTS conduct_kizai_conduct_id_fkey;
ALTER TABLE IF EXISTS ONLY public.conduct_drug DROP CONSTRAINT IF EXISTS conduct_drug_conduct_id_fkey;
ALTER TABLE IF EXISTS ONLY public.charge DROP CONSTRAINT IF EXISTS charge_visit_id_fkey;
DROP TRIGGER IF EXISTS check_shinryou_master ON public.shinryou_master;
DROP TRIGGER IF EXISTS check_shinryou ON public.shinryou;
DROP TRIGGER IF EXISTS check_kizai_master ON public.kizai_master;
DROP TRIGGER IF EXISTS check_iyakuhin_master ON public.iyakuhin_master;
DROP TRIGGER IF EXISTS check_drug ON public.drug;
DROP TRIGGER IF EXISTS check_disease ON public.disease;
DROP TRIGGER IF EXISTS check_conduct_shinryou ON public.conduct_shinryou;
DROP TRIGGER IF EXISTS check_conduct_kizai ON public.conduct_kizai;
DROP TRIGGER IF EXISTS check_conduct_drug ON public.conduct_drug;
DROP TRIGGER IF EXISTS check_byoumei_master ON public.byoumei_master;
DROP INDEX IF EXISTS public.visit_visited_at_idx;
DROP INDEX IF EXISTS public.visit_patient_id_idx;
DROP INDEX IF EXISTS public.text_visit_id_idx;
DROP INDEX IF EXISTS public.shinryou_visit_id_idx;
DROP INDEX IF EXISTS public.drug_visit_id_idx;
DROP INDEX IF EXISTS public.conduct_visit_id_idx;
ALTER TABLE IF EXISTS ONLY public.wqueue DROP CONSTRAINT IF EXISTS wqueue_pkey;
ALTER TABLE IF EXISTS ONLY public.visit DROP CONSTRAINT IF EXISTS visit_pkey;
ALTER TABLE IF EXISTS ONLY public.text DROP CONSTRAINT IF EXISTS text_pkey;
ALTER TABLE IF EXISTS ONLY public.shuushokugo_master DROP CONSTRAINT IF EXISTS shuushokugo_master_pkey;
ALTER TABLE IF EXISTS ONLY public.shouki DROP CONSTRAINT IF EXISTS shouki_pkey;
ALTER TABLE IF EXISTS ONLY public.shinryou DROP CONSTRAINT IF EXISTS shinryou_pkey;
ALTER TABLE IF EXISTS ONLY public.shinryou_master DROP CONSTRAINT IF EXISTS shinryou_master_pkey;
ALTER TABLE IF EXISTS ONLY public.shinryou_attr DROP CONSTRAINT IF EXISTS shinryou_attr_pkey;
ALTER TABLE IF EXISTS ONLY public.shahokokuho DROP CONSTRAINT IF EXISTS shahokokuho_pkey;
ALTER TABLE IF EXISTS ONLY public.roujin DROP CONSTRAINT IF EXISTS roujin_pkey;
ALTER TABLE IF EXISTS ONLY public.presc_example DROP CONSTRAINT IF EXISTS presc_example_pkey;
ALTER TABLE IF EXISTS ONLY public.practice_log DROP CONSTRAINT IF EXISTS practice_log_pkey;
ALTER TABLE IF EXISTS ONLY public.pharma_queue DROP CONSTRAINT IF EXISTS pharma_queue_pkey;
ALTER TABLE IF EXISTS ONLY public.pharma_drug DROP CONSTRAINT IF EXISTS pharma_drug_pkey;
ALTER TABLE IF EXISTS ONLY public.payment DROP CONSTRAINT IF EXISTS payment_pkey;
ALTER TABLE IF EXISTS ONLY public.patient DROP CONSTRAINT IF EXISTS patient_pkey;
ALTER TABLE IF EXISTS ONLY public.koukikourei DROP CONSTRAINT IF EXISTS koukikourei_pkey;
ALTER TABLE IF EXISTS ONLY public.kouhi DROP CONSTRAINT IF EXISTS kouhi_pkey;
ALTER TABLE IF EXISTS ONLY public.kizai_master DROP CONSTRAINT IF EXISTS kizai_master_pkey;
ALTER TABLE IF EXISTS ONLY public.iyakuhin_master DROP CONSTRAINT IF EXISTS iyakuhin_master_pkey;
ALTER TABLE IF EXISTS ONLY public.intraclinic_tag_post DROP CONSTRAINT IF EXISTS intraclinic_tag_post_pkey;
ALTER TABLE IF EXISTS ONLY public.intraclinic_tag DROP CONSTRAINT IF EXISTS intraclinic_tag_pkey;
ALTER TABLE IF EXISTS ONLY public.intraclinic_post DROP CONSTRAINT IF EXISTS intraclinic_post_pkey;
ALTER TABLE IF EXISTS ONLY public.intraclinic_comment DROP CONSTRAINT IF EXISTS intraclinic_comment_pkey;
ALTER TABLE IF EXISTS ONLY public.hotline DROP CONSTRAINT IF EXISTS hotline_pkey;
ALTER TABLE IF EXISTS ONLY public.gazou_label DROP CONSTRAINT IF EXISTS gazou_label_pkey;
ALTER TABLE IF EXISTS ONLY public.drug DROP CONSTRAINT IF EXISTS drug_pkey;
ALTER TABLE IF EXISTS ONLY public.drug_attr DROP CONSTRAINT IF EXISTS drug_attr_pkey;
ALTER TABLE IF EXISTS ONLY public.disease DROP CONSTRAINT IF EXISTS disease_pkey;
ALTER TABLE IF EXISTS ONLY public.disease_adj DROP CONSTRAINT IF EXISTS disease_adj_pkey;
ALTER TABLE IF EXISTS ONLY public.conduct_shinryou DROP CONSTRAINT IF EXISTS conduct_shinryou_pkey;
ALTER TABLE IF EXISTS ONLY public.conduct DROP CONSTRAINT IF EXISTS conduct_pkey;
ALTER TABLE IF EXISTS ONLY public.conduct_kizai DROP CONSTRAINT IF EXISTS conduct_kizai_pkey;
ALTER TABLE IF EXISTS ONLY public.conduct_drug DROP CONSTRAINT IF EXISTS conduct_drug_pkey;
ALTER TABLE IF EXISTS ONLY public.charge DROP CONSTRAINT IF EXISTS charge_pkey;
ALTER TABLE IF EXISTS ONLY public.byoumei_master DROP CONSTRAINT IF EXISTS byoumei_master_pkey;
DROP TABLE IF EXISTS public.wqueue;
DROP TABLE IF EXISTS public.visit;
DROP TABLE IF EXISTS public.text;
DROP TABLE IF EXISTS public.shuushokugo_master;
DROP TABLE IF EXISTS public.shouki;
DROP TABLE IF EXISTS public.shinryou_master;
DROP TABLE IF EXISTS public.shinryou_attr;
DROP TABLE IF EXISTS public.shinryou;
DROP TABLE IF EXISTS public.shahokokuho;
DROP TABLE IF EXISTS public.roujin;
DROP TABLE IF EXISTS public.presc_example;
DROP TABLE IF EXISTS public.practice_log;
DROP TABLE IF EXISTS public.pharma_queue;
DROP TABLE IF EXISTS public.pharma_drug;
DROP TABLE IF EXISTS public.payment;
DROP TABLE IF EXISTS public.patient;
DROP TABLE IF EXISTS public.koukikourei;
DROP TABLE IF EXISTS public.kouhi;
DROP TABLE IF EXISTS public.kizai_master;
DROP TABLE IF EXISTS public.iyakuhin_master;
DROP TABLE IF EXISTS public.intraclinic_tag_post;
DROP TABLE IF EXISTS public.intraclinic_tag;
DROP TABLE IF EXISTS public.intraclinic_post;
DROP TABLE IF EXISTS public.intraclinic_comment;
DROP TABLE IF EXISTS public.hotline;
DROP TABLE IF EXISTS public.gazou_label;
DROP TABLE IF EXISTS public.drug_attr;
DROP TABLE IF EXISTS public.drug;
DROP TABLE IF EXISTS public.disease_adj;
DROP TABLE IF EXISTS public.disease;
DROP TABLE IF EXISTS public.conduct_shinryou;
DROP TABLE IF EXISTS public.conduct_kizai;
DROP TABLE IF EXISTS public.conduct_drug;
DROP TABLE IF EXISTS public.conduct;
DROP TABLE IF EXISTS public.charge;
DROP TABLE IF EXISTS public.byoumei_master;
DROP FUNCTION IF EXISTS public.check_shinryou_master_fun();
DROP FUNCTION IF EXISTS public.check_shinryou_fun();
DROP FUNCTION IF EXISTS public.check_kizai_master_fun();
DROP FUNCTION IF EXISTS public.check_iyakuhin_master_fun();
DROP FUNCTION IF EXISTS public.check_drug_fun();
DROP FUNCTION IF EXISTS public.check_disease_fun();
DROP FUNCTION IF EXISTS public.check_conduct_shinryou_fun();
DROP FUNCTION IF EXISTS public.check_conduct_kizai_fun();
DROP FUNCTION IF EXISTS public.check_conduct_drug_fun();
DROP FUNCTION IF EXISTS public.check_byoumei_master_fun();
--
-- Name: check_byoumei_master_fun(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_byoumei_master_fun() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	declare
		count integer;
	begin
		select count(*) into count from byoumei_master where shoubyoumeicode = new.shoubyoumeicode 
			and valid_from <> new.valid_from
			and not (
				(valid_upto is not null and valid_upto < new.valid_from) or
				(new.valid_upto is not null and new.valid_upto < valid_from)
			);
		if count > 0 then
			raise exception 'conflicting byoumei_master row exists';
		end if;
		return new;
	end;
$$;


--
-- Name: check_conduct_drug_fun(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_conduct_drug_fun() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	declare
		count integer;
	begin
		select count(*) into count from iyakuhin_master m, conduct c, visit v 
			where m.iyakuhincode = new.iyakuhincode
			and new.conduct_id = c.conduct_id
			and v.visit_id = c.visit_id 
			and m.valid_from <= date(v.visited_at)
			and (m.valid_upto is null or m.valid_upto >= date(v.visited_at));
		if count < 1 then
			raise exception 'cannot find iyakuhin master for conductdrug';
		end if;
		if count > 1 then
			raise exception 'found multiple iyakuhin masters for conduct drug';
		end if;
		return new;
	end;
$$;


--
-- Name: check_conduct_kizai_fun(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_conduct_kizai_fun() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	declare
		count integer;
	begin
		select count(*) into count from kizai_master m, conduct c, visit v 
			where m.kizaicode = new.kizaicode
			and new.conduct_id = c.conduct_id
			and v.visit_id = c.visit_id 
			and m.valid_from <= date(v.visited_at)
			and (m.valid_upto is null or m.valid_upto >= date(v.visited_at));
		if count < 1 then
			raise exception 'cannot find kizai master for conduct kizai';
		end if;
		if count > 1 then
			raise exception 'found multiple kizai masters for conduct kizai';
		end if;
		return new;
	end;
$$;


--
-- Name: check_conduct_shinryou_fun(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_conduct_shinryou_fun() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	declare
		count integer;
	begin
		select count(*) into count from shinryou_master m, conduct c, visit v 
			where m.shinryoucode = new.shinryoucode
			and new.conduct_id = c.conduct_id
			and v.visit_id = c.visit_id 
			and m.valid_from <= date(v.visited_at)
			and (m.valid_upto is null or m.valid_upto >= date(v.visited_at));
		if count < 1 then
			raise exception 'cannot find shinryou master for conduct shinryou';
		end if;
		if count > 1 then
			raise exception 'found multiple shinryou masters for conduct shinryou';
		end if;
		return new;
	end;
$$;


--
-- Name: check_disease_fun(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_disease_fun() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	declare
		count integer;
	begin
		select count(*) into count from byoumei_master m where m.shoubyoumeicode = new.shoubyoumeicode
			and m.valid_from <= new.start_date
			and (m.valid_upto is null or m.valid_upto >= new.start_date);
		if count < 1 then
			raise exception 'cannot find shoubyoumei code for disease';
		end if;
		if count > 1 then
			raise exception 'found multiple shoubyoumei codes for disease';
		end if;
		return new;
	end;
$$;


--
-- Name: check_drug_fun(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_drug_fun() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	declare
		count integer;
	begin
		select count(*) into count from iyakuhin_master m, visit v where m.iyakuhincode = new.iyakuhincode
			and v.visit_id = new.visit_id 
			and m.valid_from <= date(v.visited_at)
			and (m.valid_upto is null or m.valid_upto >= date(v.visited_at));
		if count < 1 then
			raise exception 'cannot find iyakuhin master for drug';
		end if;
		if count > 1 then
			raise exception 'found multiple iyakuhin masters for drug';
		end if;
		return new;
	end;
$$;


--
-- Name: check_iyakuhin_master_fun(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_iyakuhin_master_fun() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	declare
		count integer;
	begin
		select count(*) into count from iyakuhin_master where iyakuhincode = new.iyakuhincode 
			and valid_from <> new.valid_from
			and not (
				(valid_upto is not null and valid_upto < new.valid_from) or
				(new.valid_upto is not null and new.valid_upto < valid_from)
			);
		if count > 0 then
			raise exception 'conflicting iyakuhin_master row exists';
		end if;
		return new;
	end;
$$;


--
-- Name: check_kizai_master_fun(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_kizai_master_fun() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	declare
		count integer;
	begin
		select count(*) into count from kizai_master where kizaicode = new.kizaicode 
			and valid_from <> new.valid_from
			and not (
				(valid_upto is not null and valid_upto < new.valid_from) or
				(new.valid_upto is not null and new.valid_upto < valid_from)
			);
		if count > 0 then
			raise exception 'conflicting kizai_master row exists';
		end if;
		return new;
	end;
$$;


--
-- Name: check_shinryou_fun(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_shinryou_fun() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	declare
		count integer;
	begin
		select count(*) into count from shinryou_master m, visit v where m.shinryoucode = new.shinryoucode
			and v.visit_id = new.visit_id 
			and m.valid_from <= date(v.visited_at)
			and (m.valid_upto is null or m.valid_upto >= date(v.visited_at));
		if count < 1 then
			raise exception 'cannot find shinryou master for shinryou';
		end if;
		if count > 1 then
			raise exception 'found multiple shinryou masters for shinryou';
		end if;
		return new;
	end;
$$;


--
-- Name: check_shinryou_master_fun(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.check_shinryou_master_fun() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
	declare
		count integer;
	begin
		select count(*) into count from shinryou_master where shinryoucode = new.shinryoucode 
			and valid_from <> new.valid_from
			and not (
				(valid_upto is not null and valid_upto < new.valid_from) or
				(new.valid_upto is not null and new.valid_upto < valid_from)
			);
		if count > 0 then
			raise exception 'conflicting shinryou_master row exists';
		end if;
		return new;
	end;
$$;


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: byoumei_master; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.byoumei_master (
    shoubyoumeicode integer NOT NULL,
    name character varying(60) NOT NULL,
    valid_from date NOT NULL,
    valid_upto date,
    CONSTRAINT byoumei_master_name_check CHECK ((char_length((name)::text) > 0)),
    CONSTRAINT byoumei_master_shoubyoumeicode_check CHECK ((shoubyoumeicode > 0))
);


--
-- Name: charge; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.charge (
    visit_id integer NOT NULL,
    charge smallint NOT NULL,
    CONSTRAINT charge_charge_check CHECK ((charge >= 0))
);


--
-- Name: conduct; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.conduct (
    conduct_id integer NOT NULL,
    visit_id integer NOT NULL,
    kind smallint NOT NULL,
    CONSTRAINT conduct_kind_check CHECK ((kind = ANY (ARRAY[0, 1, 2, 3])))
);


--
-- Name: conduct_conduct_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.conduct ALTER COLUMN conduct_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.conduct_conduct_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: conduct_drug; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.conduct_drug (
    conduct_drug_id integer NOT NULL,
    conduct_id integer NOT NULL,
    iyakuhincode integer NOT NULL,
    amount numeric(6,2) NOT NULL,
    CONSTRAINT conduct_drug_amount_check CHECK ((amount >= (0)::numeric)),
    CONSTRAINT conduct_drug_iyakuhincode_check CHECK ((iyakuhincode > 0))
);


--
-- Name: conduct_drug_conduct_drug_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.conduct_drug ALTER COLUMN conduct_drug_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.conduct_drug_conduct_drug_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: conduct_kizai; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.conduct_kizai (
    conduct_kizai_id integer NOT NULL,
    conduct_id integer NOT NULL,
    kizaicode integer NOT NULL,
    amount numeric(6,2) NOT NULL,
    CONSTRAINT conduct_kizai_amount_check CHECK ((amount > (0)::numeric)),
    CONSTRAINT conduct_kizai_kizaicode_check CHECK ((kizaicode > 0))
);


--
-- Name: conduct_kizai_conduct_kizai_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.conduct_kizai ALTER COLUMN conduct_kizai_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.conduct_kizai_conduct_kizai_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: conduct_shinryou; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.conduct_shinryou (
    conduct_shinryou_id integer NOT NULL,
    conduct_id integer NOT NULL,
    shinryoucode integer NOT NULL,
    CONSTRAINT conduct_shinryou_shinryoucode_check CHECK ((shinryoucode > 0))
);


--
-- Name: conduct_shinryou_conduct_shinryou_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.conduct_shinryou ALTER COLUMN conduct_shinryou_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.conduct_shinryou_conduct_shinryou_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: disease; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.disease (
    disease_id integer NOT NULL,
    patient_id integer NOT NULL,
    shoubyoumeicode integer NOT NULL,
    start_date date NOT NULL,
    end_date date,
    end_reason character(1) NOT NULL,
    CONSTRAINT disease_check CHECK (((end_date IS NULL) OR (start_date <= end_date))),
    CONSTRAINT disease_check1 CHECK ((((end_reason = 'N'::bpchar) AND (end_date IS NULL)) OR ((end_reason <> 'N'::bpchar) AND (end_date IS NOT NULL)))),
    CONSTRAINT disease_end_reason_check CHECK ((end_reason = ANY (ARRAY['N'::bpchar, 'C'::bpchar, 'S'::bpchar, 'D'::bpchar]))),
    CONSTRAINT disease_shoubyoumeicode_check CHECK ((shoubyoumeicode > 0))
);


--
-- Name: disease_adj; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.disease_adj (
    disease_adj_id integer NOT NULL,
    disease_id integer NOT NULL,
    shuushokugocode integer NOT NULL
);


--
-- Name: disease_adj_disease_adj_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.disease_adj ALTER COLUMN disease_adj_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.disease_adj_disease_adj_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: disease_disease_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.disease ALTER COLUMN disease_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.disease_disease_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: drug; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.drug (
    drug_id integer NOT NULL,
    visit_id integer NOT NULL,
    iyakuhincode integer NOT NULL,
    amount numeric(6,2) NOT NULL,
    usage character varying(255) NOT NULL,
    days smallint NOT NULL,
    category smallint NOT NULL,
    prescribed smallint NOT NULL,
    CONSTRAINT drug_amount_check CHECK ((amount >= (0)::numeric)),
    CONSTRAINT drug_category_check CHECK ((category = ANY (ARRAY[0, 1, 2, 3]))),
    CONSTRAINT drug_days_check CHECK ((days >= 0)),
    CONSTRAINT drug_iyakuhincode_check CHECK ((iyakuhincode > 0)),
    CONSTRAINT drug_prescribed_check CHECK ((prescribed = ANY (ARRAY[0, 1])))
);


--
-- Name: drug_attr; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.drug_attr (
    drug_id integer NOT NULL,
    tekiyou text NOT NULL
);


--
-- Name: drug_drug_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.drug ALTER COLUMN drug_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.drug_drug_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: gazou_label; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.gazou_label (
    conduct_id integer NOT NULL,
    label character varying(255)
);


--
-- Name: hotline; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hotline (
    hotline_id integer NOT NULL,
    message text NOT NULL,
    sender character varying(255) NOT NULL,
    recipient character varying(255) NOT NULL,
    posted_at timestamp without time zone NOT NULL,
    CONSTRAINT hotline_recipient_check CHECK ((char_length((recipient)::text) > 0)),
    CONSTRAINT hotline_sender_check CHECK ((char_length((sender)::text) > 0))
);


--
-- Name: hotline_hotline_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.hotline ALTER COLUMN hotline_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.hotline_hotline_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: intraclinic_comment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.intraclinic_comment (
    comment_id integer NOT NULL,
    name character varying(255) NOT NULL,
    content character varying(255) NOT NULL,
    post_id integer NOT NULL,
    created_at date NOT NULL
);


--
-- Name: intraclinic_comment_comment_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.intraclinic_comment ALTER COLUMN comment_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.intraclinic_comment_comment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: intraclinic_post; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.intraclinic_post (
    post_id integer NOT NULL,
    content text NOT NULL,
    created_at date NOT NULL
);


--
-- Name: intraclinic_post_post_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.intraclinic_post ALTER COLUMN post_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.intraclinic_post_post_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: intraclinic_tag; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.intraclinic_tag (
    tag_id integer NOT NULL,
    name character varying(255) NOT NULL
);


--
-- Name: intraclinic_tag_post; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.intraclinic_tag_post (
    tag_id integer NOT NULL,
    post_id integer NOT NULL
);


--
-- Name: intraclinic_tag_tag_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.intraclinic_tag ALTER COLUMN tag_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.intraclinic_tag_tag_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: iyakuhin_master; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iyakuhin_master (
    iyakuhincode integer NOT NULL,
    name character varying(32) NOT NULL,
    yomi character varying(20) NOT NULL,
    unit character varying(6) NOT NULL,
    yakka numeric(9,2) NOT NULL,
    madoku character(1) NOT NULL,
    kouhatsu character(1) NOT NULL,
    zaikei character(1) NOT NULL,
    valid_from date NOT NULL,
    valid_upto date,
    CONSTRAINT iyakuhin_master_check CHECK (((valid_upto IS NULL) OR (valid_from <= valid_upto))),
    CONSTRAINT iyakuhin_master_iyakuhincode_check CHECK ((iyakuhincode > 0)),
    CONSTRAINT iyakuhin_master_kouhatsu_check CHECK ((kouhatsu = ANY (ARRAY['0'::bpchar, '1'::bpchar]))),
    CONSTRAINT iyakuhin_master_madoku_check CHECK ((madoku = ANY (ARRAY['0'::bpchar, '1'::bpchar, '2'::bpchar, '3'::bpchar, '5'::bpchar]))),
    CONSTRAINT iyakuhin_master_name_check CHECK ((char_length((name)::text) > 0)),
    CONSTRAINT iyakuhin_master_yakka_check CHECK ((yakka >= (0)::numeric)),
    CONSTRAINT iyakuhin_master_yomi_check CHECK ((char_length((yomi)::text) > 0)),
    CONSTRAINT iyakuhin_master_zaikei_check CHECK ((zaikei = ANY (ARRAY['1'::bpchar, '3'::bpchar, '4'::bpchar, '6'::bpchar, '8'::bpchar])))
);


--
-- Name: kizai_master; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.kizai_master (
    kizaicode integer NOT NULL,
    name character varying(32) NOT NULL,
    yomi character varying(20) NOT NULL,
    unit character varying(6) NOT NULL,
    kingaku numeric(11,2) NOT NULL,
    valid_from date NOT NULL,
    valid_upto date,
    CONSTRAINT kizai_master_check CHECK (((valid_upto IS NULL) OR (valid_from <= valid_upto))),
    CONSTRAINT kizai_master_kizaicode_check CHECK ((kizaicode > 0)),
    CONSTRAINT kizai_master_name_check CHECK ((char_length((name)::text) > 0)),
    CONSTRAINT kizai_master_yomi_check CHECK ((char_length((yomi)::text) > 0))
);


--
-- Name: kouhi; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.kouhi (
    kouhi_id integer NOT NULL,
    patient_id integer NOT NULL,
    futansha integer NOT NULL,
    jukyuusha integer NOT NULL,
    valid_from date NOT NULL,
    valid_upto date,
    CONSTRAINT kouhi_check CHECK (((valid_upto IS NULL) OR (valid_from <= valid_upto))),
    CONSTRAINT kouhi_futansha_check CHECK ((futansha > 0)),
    CONSTRAINT kouhi_jukyuusha_check CHECK ((jukyuusha > 0))
);


--
-- Name: kouhi_kouhi_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.kouhi ALTER COLUMN kouhi_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.kouhi_kouhi_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: koukikourei; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.koukikourei (
    koukikourei_id integer NOT NULL,
    patient_id integer NOT NULL,
    hokensha_bangou integer NOT NULL,
    hihokensha_bangou integer NOT NULL,
    futan_wari smallint NOT NULL,
    valid_from date NOT NULL,
    valid_upto date,
    CONSTRAINT koukikourei_check CHECK (((valid_upto IS NULL) OR (valid_from <= valid_upto))),
    CONSTRAINT koukikourei_futan_wari_check CHECK ((futan_wari = ANY (ARRAY[0, 1, 2, 3]))),
    CONSTRAINT koukikourei_hihokensha_bangou_check CHECK ((hihokensha_bangou > 0)),
    CONSTRAINT koukikourei_hokensha_bangou_check CHECK ((hokensha_bangou > 0))
);


--
-- Name: koukikourei_koukikourei_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.koukikourei ALTER COLUMN koukikourei_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.koukikourei_koukikourei_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: patient; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.patient (
    patient_id integer NOT NULL,
    last_name character varying(20) NOT NULL,
    first_name character varying(20) NOT NULL,
    last_name_yomi character varying(20) NOT NULL,
    first_name_yomi character varying(20) NOT NULL,
    sex character(1) NOT NULL,
    birthday date NOT NULL,
    address character varying(80) NOT NULL,
    phone character varying(80) NOT NULL,
    CONSTRAINT patient_first_name_check CHECK ((char_length((first_name)::text) > 0)),
    CONSTRAINT patient_first_name_yomi_check CHECK ((char_length((first_name_yomi)::text) > 0)),
    CONSTRAINT patient_last_name_check CHECK ((char_length((last_name)::text) > 0)),
    CONSTRAINT patient_last_name_yomi_check CHECK ((char_length((last_name_yomi)::text) > 0)),
    CONSTRAINT patient_sex_check CHECK (((sex = 'M'::bpchar) OR (sex = 'F'::bpchar)))
);


--
-- Name: patient_patient_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.patient ALTER COLUMN patient_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.patient_patient_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: payment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.payment (
    visit_id integer NOT NULL,
    amount smallint NOT NULL,
    paytime timestamp without time zone NOT NULL,
    CONSTRAINT payment_amount_check CHECK ((amount >= 0))
);


--
-- Name: pharma_drug; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pharma_drug (
    iyakuhincode integer NOT NULL,
    description text NOT NULL,
    side_effect text NOT NULL
);


--
-- Name: pharma_queue; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pharma_queue (
    visit_id integer NOT NULL,
    pharma_state smallint NOT NULL,
    CONSTRAINT pharma_queue_pharma_state_check CHECK ((pharma_state = ANY (ARRAY[0, 1, 2])))
);


--
-- Name: practice_log; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.practice_log (
    practice_log_id integer NOT NULL,
    created_at timestamp without time zone NOT NULL,
    kind character varying(32) NOT NULL,
    body json NOT NULL,
    CONSTRAINT practice_log_kind_check CHECK ((char_length((kind)::text) > 0))
);


--
-- Name: practice_log_practice_log_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.practice_log ALTER COLUMN practice_log_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.practice_log_practice_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: presc_example; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.presc_example (
    presc_example_id integer NOT NULL,
    iyakuhincode integer NOT NULL,
    master_valid_from date NOT NULL,
    amount numeric(6,2) NOT NULL,
    usage character varying(255) NOT NULL,
    days smallint NOT NULL,
    category smallint NOT NULL,
    comment character varying(255) NOT NULL,
    CONSTRAINT presc_example_amount_check CHECK ((amount >= (0)::numeric)),
    CONSTRAINT presc_example_category_check CHECK ((category = ANY (ARRAY[0, 1, 2, 3]))),
    CONSTRAINT presc_example_days_check CHECK ((days >= 0))
);


--
-- Name: presc_example_presc_example_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.presc_example ALTER COLUMN presc_example_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.presc_example_presc_example_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: roujin; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.roujin (
    roujin_id integer NOT NULL,
    patient_id integer NOT NULL,
    shichouson integer NOT NULL,
    jukyuusha integer NOT NULL,
    futan_wari smallint NOT NULL,
    valid_from date NOT NULL,
    valid_upto date,
    CONSTRAINT roujin_check CHECK (((valid_upto IS NULL) OR (valid_from <= valid_upto))),
    CONSTRAINT roujin_futan_wari_check CHECK ((futan_wari = ANY (ARRAY[0, 1, 2, 3]))),
    CONSTRAINT roujin_jukyuusha_check CHECK ((jukyuusha > 0)),
    CONSTRAINT roujin_shichouson_check CHECK ((shichouson > 0))
);


--
-- Name: roujin_roujin_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.roujin ALTER COLUMN roujin_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.roujin_roujin_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: shahokokuho; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.shahokokuho (
    shahokokuho_id integer NOT NULL,
    patient_id integer NOT NULL,
    hokensha_bangou integer NOT NULL,
    hihokensha_kigou character varying(255) NOT NULL,
    hihokensha_bangou character varying(255) NOT NULL,
    honnin smallint NOT NULL,
    valid_from date NOT NULL,
    valid_upto date,
    kourei smallint NOT NULL,
    CONSTRAINT shahokokuho_check CHECK (((valid_upto IS NULL) OR (valid_from <= valid_upto))),
    CONSTRAINT shahokokuho_check1 CHECK (((char_length((hihokensha_kigou)::text) > 0) OR (char_length((hihokensha_bangou)::text) > 0))),
    CONSTRAINT shahokokuho_honnin_check CHECK (((honnin = 0) OR (honnin = 1))),
    CONSTRAINT shahokokuho_kourei_check CHECK ((kourei = ANY (ARRAY[0, 1, 2, 3])))
);


--
-- Name: shahokokuho_shahokokuho_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.shahokokuho ALTER COLUMN shahokokuho_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.shahokokuho_shahokokuho_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: shinryou; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.shinryou (
    shinryou_id integer NOT NULL,
    visit_id integer NOT NULL,
    shinryoucode integer NOT NULL,
    CONSTRAINT shinryou_shinryoucode_check CHECK ((shinryoucode > 0))
);


--
-- Name: shinryou_attr; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.shinryou_attr (
    shinryou_id integer NOT NULL,
    tekiyou text NOT NULL
);


--
-- Name: shinryou_master; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.shinryou_master (
    shinryoucode integer NOT NULL,
    name character varying(32) NOT NULL,
    tensuu numeric(9,2) NOT NULL,
    tensuu_shikibetsu character(1) NOT NULL,
    shuukeisaki character varying(3) NOT NULL,
    houkatsukensa character(2) NOT NULL,
    kensagroup character(2) NOT NULL,
    valid_from date NOT NULL,
    valid_upto date,
    CONSTRAINT shinryou_master_check CHECK (((valid_upto IS NULL) OR (valid_from <= valid_upto))),
    CONSTRAINT shinryou_master_houkatsukensa_check CHECK ((houkatsukensa = ANY (ARRAY['00'::bpchar, '01'::bpchar, '02'::bpchar, '03'::bpchar, '04'::bpchar, '05'::bpchar, '06'::bpchar, '07'::bpchar, '08'::bpchar, '09'::bpchar, '10'::bpchar, '11'::bpchar, '12'::bpchar]))),
    CONSTRAINT shinryou_master_kensagroup_check CHECK ((kensagroup = ANY (ARRAY['00'::bpchar, '01'::bpchar, '02'::bpchar, '03'::bpchar, '04'::bpchar, '05'::bpchar, '06'::bpchar, '07'::bpchar, '08'::bpchar, '11'::bpchar, '12'::bpchar, '13'::bpchar, '14'::bpchar, '15'::bpchar, '16'::bpchar, '31'::bpchar, '32'::bpchar, '33'::bpchar, '40'::bpchar, '41'::bpchar, '42'::bpchar]))),
    CONSTRAINT shinryou_master_name_check CHECK ((char_length((name)::text) > 0)),
    CONSTRAINT shinryou_master_shinryoucode_check CHECK ((shinryoucode > 0)),
    CONSTRAINT shinryou_master_shuukeisaki_check CHECK (((shuukeisaki)::text = ANY ((ARRAY['0'::character varying, '110'::character varying, '120'::character varying, '122'::character varying, '123'::character varying, '124'::character varying, '125'::character varying, '130'::character varying, '140'::character varying, '210'::character varying, '230'::character varying, '240'::character varying, '250'::character varying, '260'::character varying, '270'::character varying, '300'::character varying, '311'::character varying, '321'::character varying, '331'::character varying, '400'::character varying, '500'::character varying, '502'::character varying, '540'::character varying, '600'::character varying, '700'::character varying, '800'::character varying, '903'::character varying, '920'::character varying, '970'::character varying, '971'::character varying, '972'::character varying, '973'::character varying, '974'::character varying, '975'::character varying])::text[]))),
    CONSTRAINT shinryou_master_tensuu_shikibetsu_check CHECK ((tensuu_shikibetsu = ANY (ARRAY['1'::bpchar, '3'::bpchar, '4'::bpchar, '5'::bpchar, '6'::bpchar, '7'::bpchar, '8'::bpchar])))
);


--
-- Name: shinryou_shinryou_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.shinryou ALTER COLUMN shinryou_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.shinryou_shinryou_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: shouki; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.shouki (
    visit_id integer NOT NULL,
    shouki text NOT NULL
);


--
-- Name: shuushokugo_master; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.shuushokugo_master (
    shuushokugocode integer NOT NULL,
    name character varying(40) NOT NULL,
    CONSTRAINT shuushokugo_master_name_check CHECK ((char_length((name)::text) > 0))
);


--
-- Name: text; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.text (
    text_id integer NOT NULL,
    visit_id integer NOT NULL,
    content text NOT NULL
);


--
-- Name: text_text_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.text ALTER COLUMN text_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.text_text_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: visit; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.visit (
    visit_id integer NOT NULL,
    patient_id integer NOT NULL,
    visited_at timestamp without time zone NOT NULL,
    shahokokuho_id integer,
    roujin_id integer,
    koukikourei_id integer,
    kouhi_1_id integer,
    kouhi_2_id integer,
    kouhi_3_id integer
);


--
-- Name: visit_visit_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.visit ALTER COLUMN visit_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.visit_visit_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: wqueue; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wqueue (
    visit_id integer NOT NULL,
    wait_state smallint NOT NULL,
    CONSTRAINT wqueue_wait_state_check CHECK ((wait_state = ANY (ARRAY[0, 1, 2, 3, 4])))
);


--
-- Name: byoumei_master byoumei_master_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.byoumei_master
    ADD CONSTRAINT byoumei_master_pkey PRIMARY KEY (shoubyoumeicode, valid_from);


--
-- Name: charge charge_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.charge
    ADD CONSTRAINT charge_pkey PRIMARY KEY (visit_id);


--
-- Name: conduct_drug conduct_drug_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.conduct_drug
    ADD CONSTRAINT conduct_drug_pkey PRIMARY KEY (conduct_drug_id);


--
-- Name: conduct_kizai conduct_kizai_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.conduct_kizai
    ADD CONSTRAINT conduct_kizai_pkey PRIMARY KEY (conduct_kizai_id);


--
-- Name: conduct conduct_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.conduct
    ADD CONSTRAINT conduct_pkey PRIMARY KEY (conduct_id);


--
-- Name: conduct_shinryou conduct_shinryou_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.conduct_shinryou
    ADD CONSTRAINT conduct_shinryou_pkey PRIMARY KEY (conduct_shinryou_id);


--
-- Name: disease_adj disease_adj_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.disease_adj
    ADD CONSTRAINT disease_adj_pkey PRIMARY KEY (disease_adj_id);


--
-- Name: disease disease_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.disease
    ADD CONSTRAINT disease_pkey PRIMARY KEY (disease_id);


--
-- Name: drug_attr drug_attr_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.drug_attr
    ADD CONSTRAINT drug_attr_pkey PRIMARY KEY (drug_id);


--
-- Name: drug drug_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.drug
    ADD CONSTRAINT drug_pkey PRIMARY KEY (drug_id);


--
-- Name: gazou_label gazou_label_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.gazou_label
    ADD CONSTRAINT gazou_label_pkey PRIMARY KEY (conduct_id);


--
-- Name: hotline hotline_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hotline
    ADD CONSTRAINT hotline_pkey PRIMARY KEY (hotline_id);


--
-- Name: intraclinic_comment intraclinic_comment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.intraclinic_comment
    ADD CONSTRAINT intraclinic_comment_pkey PRIMARY KEY (comment_id);


--
-- Name: intraclinic_post intraclinic_post_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.intraclinic_post
    ADD CONSTRAINT intraclinic_post_pkey PRIMARY KEY (post_id);


--
-- Name: intraclinic_tag intraclinic_tag_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.intraclinic_tag
    ADD CONSTRAINT intraclinic_tag_pkey PRIMARY KEY (tag_id);


--
-- Name: intraclinic_tag_post intraclinic_tag_post_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.intraclinic_tag_post
    ADD CONSTRAINT intraclinic_tag_post_pkey PRIMARY KEY (tag_id, post_id);


--
-- Name: iyakuhin_master iyakuhin_master_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iyakuhin_master
    ADD CONSTRAINT iyakuhin_master_pkey PRIMARY KEY (iyakuhincode, valid_from);


--
-- Name: kizai_master kizai_master_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.kizai_master
    ADD CONSTRAINT kizai_master_pkey PRIMARY KEY (kizaicode, valid_from);


--
-- Name: kouhi kouhi_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.kouhi
    ADD CONSTRAINT kouhi_pkey PRIMARY KEY (kouhi_id);


--
-- Name: koukikourei koukikourei_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.koukikourei
    ADD CONSTRAINT koukikourei_pkey PRIMARY KEY (koukikourei_id);


--
-- Name: patient patient_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.patient
    ADD CONSTRAINT patient_pkey PRIMARY KEY (patient_id);


--
-- Name: payment payment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_pkey PRIMARY KEY (visit_id, paytime);


--
-- Name: pharma_drug pharma_drug_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pharma_drug
    ADD CONSTRAINT pharma_drug_pkey PRIMARY KEY (iyakuhincode);


--
-- Name: pharma_queue pharma_queue_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pharma_queue
    ADD CONSTRAINT pharma_queue_pkey PRIMARY KEY (visit_id);


--
-- Name: practice_log practice_log_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.practice_log
    ADD CONSTRAINT practice_log_pkey PRIMARY KEY (practice_log_id);


--
-- Name: presc_example presc_example_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.presc_example
    ADD CONSTRAINT presc_example_pkey PRIMARY KEY (presc_example_id);


--
-- Name: roujin roujin_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roujin
    ADD CONSTRAINT roujin_pkey PRIMARY KEY (roujin_id);


--
-- Name: shahokokuho shahokokuho_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.shahokokuho
    ADD CONSTRAINT shahokokuho_pkey PRIMARY KEY (shahokokuho_id);


--
-- Name: shinryou_attr shinryou_attr_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.shinryou_attr
    ADD CONSTRAINT shinryou_attr_pkey PRIMARY KEY (shinryou_id);


--
-- Name: shinryou_master shinryou_master_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.shinryou_master
    ADD CONSTRAINT shinryou_master_pkey PRIMARY KEY (shinryoucode, valid_from);


--
-- Name: shinryou shinryou_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.shinryou
    ADD CONSTRAINT shinryou_pkey PRIMARY KEY (shinryou_id);


--
-- Name: shouki shouki_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.shouki
    ADD CONSTRAINT shouki_pkey PRIMARY KEY (visit_id);


--
-- Name: shuushokugo_master shuushokugo_master_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.shuushokugo_master
    ADD CONSTRAINT shuushokugo_master_pkey PRIMARY KEY (shuushokugocode);


--
-- Name: text text_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.text
    ADD CONSTRAINT text_pkey PRIMARY KEY (text_id);


--
-- Name: visit visit_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.visit
    ADD CONSTRAINT visit_pkey PRIMARY KEY (visit_id);


--
-- Name: wqueue wqueue_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wqueue
    ADD CONSTRAINT wqueue_pkey PRIMARY KEY (visit_id);


--
-- Name: conduct_visit_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX conduct_visit_id_idx ON public.conduct USING btree (visit_id);


--
-- Name: drug_visit_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX drug_visit_id_idx ON public.drug USING btree (visit_id);


--
-- Name: shinryou_visit_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX shinryou_visit_id_idx ON public.shinryou USING btree (visit_id);


--
-- Name: text_visit_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX text_visit_id_idx ON public.text USING btree (visit_id);


--
-- Name: visit_patient_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX visit_patient_id_idx ON public.visit USING btree (patient_id);


--
-- Name: visit_visited_at_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX visit_visited_at_idx ON public.visit USING btree (visited_at);


--
-- Name: byoumei_master check_byoumei_master; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER check_byoumei_master BEFORE INSERT OR UPDATE ON public.byoumei_master FOR EACH ROW EXECUTE PROCEDURE public.check_byoumei_master_fun();


--
-- Name: conduct_drug check_conduct_drug; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER check_conduct_drug BEFORE INSERT OR UPDATE ON public.conduct_drug FOR EACH ROW EXECUTE PROCEDURE public.check_conduct_drug_fun();


--
-- Name: conduct_kizai check_conduct_kizai; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER check_conduct_kizai BEFORE INSERT OR UPDATE ON public.conduct_kizai FOR EACH ROW EXECUTE PROCEDURE public.check_conduct_kizai_fun();


--
-- Name: conduct_shinryou check_conduct_shinryou; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER check_conduct_shinryou BEFORE INSERT OR UPDATE ON public.conduct_shinryou FOR EACH ROW EXECUTE PROCEDURE public.check_conduct_shinryou_fun();


--
-- Name: disease check_disease; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER check_disease BEFORE INSERT OR UPDATE ON public.disease FOR EACH ROW EXECUTE PROCEDURE public.check_disease_fun();


--
-- Name: drug check_drug; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER check_drug BEFORE INSERT OR UPDATE ON public.drug FOR EACH ROW EXECUTE PROCEDURE public.check_drug_fun();


--
-- Name: iyakuhin_master check_iyakuhin_master; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER check_iyakuhin_master BEFORE INSERT OR UPDATE ON public.iyakuhin_master FOR EACH ROW EXECUTE PROCEDURE public.check_iyakuhin_master_fun();


--
-- Name: kizai_master check_kizai_master; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER check_kizai_master BEFORE INSERT OR UPDATE ON public.kizai_master FOR EACH ROW EXECUTE PROCEDURE public.check_kizai_master_fun();


--
-- Name: shinryou check_shinryou; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER check_shinryou BEFORE INSERT OR UPDATE ON public.shinryou FOR EACH ROW EXECUTE PROCEDURE public.check_shinryou_fun();


--
-- Name: shinryou_master check_shinryou_master; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER check_shinryou_master BEFORE INSERT OR UPDATE ON public.shinryou_master FOR EACH ROW EXECUTE PROCEDURE public.check_shinryou_master_fun();


--
-- Name: charge charge_visit_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.charge
    ADD CONSTRAINT charge_visit_id_fkey FOREIGN KEY (visit_id) REFERENCES public.visit(visit_id);


--
-- Name: conduct_drug conduct_drug_conduct_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.conduct_drug
    ADD CONSTRAINT conduct_drug_conduct_id_fkey FOREIGN KEY (conduct_id) REFERENCES public.conduct(conduct_id);


--
-- Name: conduct_kizai conduct_kizai_conduct_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.conduct_kizai
    ADD CONSTRAINT conduct_kizai_conduct_id_fkey FOREIGN KEY (conduct_id) REFERENCES public.conduct(conduct_id);


--
-- Name: conduct_shinryou conduct_shinryou_conduct_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.conduct_shinryou
    ADD CONSTRAINT conduct_shinryou_conduct_id_fkey FOREIGN KEY (conduct_id) REFERENCES public.conduct(conduct_id);


--
-- Name: conduct conduct_visit_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.conduct
    ADD CONSTRAINT conduct_visit_id_fkey FOREIGN KEY (visit_id) REFERENCES public.visit(visit_id);


--
-- Name: disease_adj disease_adj_disease_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.disease_adj
    ADD CONSTRAINT disease_adj_disease_id_fkey FOREIGN KEY (disease_id) REFERENCES public.disease(disease_id);


--
-- Name: disease_adj disease_adj_shuushokugocode_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.disease_adj
    ADD CONSTRAINT disease_adj_shuushokugocode_fkey FOREIGN KEY (shuushokugocode) REFERENCES public.shuushokugo_master(shuushokugocode);


--
-- Name: disease disease_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.disease
    ADD CONSTRAINT disease_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patient(patient_id);


--
-- Name: drug_attr drug_attr_drug_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.drug_attr
    ADD CONSTRAINT drug_attr_drug_id_fkey FOREIGN KEY (drug_id) REFERENCES public.drug(drug_id) ON DELETE CASCADE;


--
-- Name: drug drug_visit_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.drug
    ADD CONSTRAINT drug_visit_id_fkey FOREIGN KEY (visit_id) REFERENCES public.visit(visit_id);


--
-- Name: gazou_label gazou_label_conduct_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.gazou_label
    ADD CONSTRAINT gazou_label_conduct_id_fkey FOREIGN KEY (conduct_id) REFERENCES public.conduct(conduct_id);


--
-- Name: intraclinic_comment intraclinic_comment_post_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.intraclinic_comment
    ADD CONSTRAINT intraclinic_comment_post_id_fkey FOREIGN KEY (post_id) REFERENCES public.intraclinic_post(post_id);


--
-- Name: intraclinic_tag_post intraclinic_tag_post_post_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.intraclinic_tag_post
    ADD CONSTRAINT intraclinic_tag_post_post_id_fkey FOREIGN KEY (post_id) REFERENCES public.intraclinic_post(post_id);


--
-- Name: intraclinic_tag_post intraclinic_tag_post_tag_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.intraclinic_tag_post
    ADD CONSTRAINT intraclinic_tag_post_tag_id_fkey FOREIGN KEY (tag_id) REFERENCES public.intraclinic_tag(tag_id);


--
-- Name: kouhi kouhi_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.kouhi
    ADD CONSTRAINT kouhi_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patient(patient_id);


--
-- Name: koukikourei koukikourei_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.koukikourei
    ADD CONSTRAINT koukikourei_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patient(patient_id);


--
-- Name: payment payment_visit_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_visit_id_fkey FOREIGN KEY (visit_id) REFERENCES public.visit(visit_id);


--
-- Name: pharma_queue pharma_queue_visit_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pharma_queue
    ADD CONSTRAINT pharma_queue_visit_id_fkey FOREIGN KEY (visit_id) REFERENCES public.visit(visit_id);


--
-- Name: presc_example presc_example_iyakuhincode_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.presc_example
    ADD CONSTRAINT presc_example_iyakuhincode_fkey FOREIGN KEY (iyakuhincode, master_valid_from) REFERENCES public.iyakuhin_master(iyakuhincode, valid_from);


--
-- Name: roujin roujin_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roujin
    ADD CONSTRAINT roujin_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patient(patient_id);


--
-- Name: shahokokuho shahokokuho_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.shahokokuho
    ADD CONSTRAINT shahokokuho_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patient(patient_id);


--
-- Name: shinryou_attr shinryou_attr_shinryou_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.shinryou_attr
    ADD CONSTRAINT shinryou_attr_shinryou_id_fkey FOREIGN KEY (shinryou_id) REFERENCES public.shinryou(shinryou_id) ON DELETE CASCADE;


--
-- Name: shinryou shinryou_visit_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.shinryou
    ADD CONSTRAINT shinryou_visit_id_fkey FOREIGN KEY (visit_id) REFERENCES public.visit(visit_id);


--
-- Name: shouki shouki_visit_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.shouki
    ADD CONSTRAINT shouki_visit_id_fkey FOREIGN KEY (visit_id) REFERENCES public.visit(visit_id) ON DELETE CASCADE;


--
-- Name: text text_visit_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.text
    ADD CONSTRAINT text_visit_id_fkey FOREIGN KEY (visit_id) REFERENCES public.visit(visit_id);


--
-- Name: visit visit_kouhi_1_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.visit
    ADD CONSTRAINT visit_kouhi_1_id_fkey FOREIGN KEY (kouhi_1_id) REFERENCES public.kouhi(kouhi_id);


--
-- Name: visit visit_kouhi_2_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.visit
    ADD CONSTRAINT visit_kouhi_2_id_fkey FOREIGN KEY (kouhi_2_id) REFERENCES public.kouhi(kouhi_id);


--
-- Name: visit visit_kouhi_3_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.visit
    ADD CONSTRAINT visit_kouhi_3_id_fkey FOREIGN KEY (kouhi_3_id) REFERENCES public.kouhi(kouhi_id);


--
-- Name: visit visit_koukikourei_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.visit
    ADD CONSTRAINT visit_koukikourei_id_fkey FOREIGN KEY (koukikourei_id) REFERENCES public.koukikourei(koukikourei_id);


--
-- Name: visit visit_patient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.visit
    ADD CONSTRAINT visit_patient_id_fkey FOREIGN KEY (patient_id) REFERENCES public.patient(patient_id);


--
-- Name: visit visit_roujin_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.visit
    ADD CONSTRAINT visit_roujin_id_fkey FOREIGN KEY (roujin_id) REFERENCES public.roujin(roujin_id);


--
-- Name: visit visit_shahokokuho_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.visit
    ADD CONSTRAINT visit_shahokokuho_id_fkey FOREIGN KEY (shahokokuho_id) REFERENCES public.shahokokuho(shahokokuho_id);


--
-- Name: wqueue wqueue_visit_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wqueue
    ADD CONSTRAINT wqueue_visit_id_fkey FOREIGN KEY (visit_id) REFERENCES public.visit(visit_id);


--
-- PostgreSQL database dump complete
--

