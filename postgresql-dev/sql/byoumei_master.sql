CREATE TABLE byoumei_master (
	shoubyoumeicode INTEGER NOT NULL CHECK (shoubyoumeicode > 0),
	name VARCHAR(60) NOT NULL CHECK (char_length(name) > 0),
	valid_from DATE NOT NULL,
	valid_upto DATE,
	PRIMARY KEY (shoubyoumeicode, valid_from)
);

CREATE OR REPLACE FUNCTION check_byoumei_master_fun() returns trigger AS $$
	DECLARE
		count INTEGER;
	BEGIN
		SELECT count(*) INTO count FROM public.byoumei_master WHERE shoubyoumeicode = new.shoubyoumeicode 
			AND valid_from <> new.valid_from
			AND not (
				(valid_upto IS NOT NULL AND valid_upto < new.valid_from) OR
				(new.valid_upto IS NOT NULL AND new.valid_upto < valid_from)
			);
		IF count > 0 THEN
			RAISE EXCEPTION 'conflicting byoumei_master row exists';
		END IF;
		RETURN new;
	END;
$$ LANGUAGE PLPGSQL;

create trigger check_byoumei_master before insert or update on byoumei_master
	for each row execute procedure check_byoumei_master_fun();