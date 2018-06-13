create table practice_log (
    practice_log_id INT NOT NULL AUTO_INCREMENT,
    practice_date date,
    kind varchar(32),
    body text,
    primary key (practice_log_id),
    index (practice_date)
);