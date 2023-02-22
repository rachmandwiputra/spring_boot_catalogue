CREATE TABLE ms_user(
    id serial PRIMARY KEY NOT NULL,
    fullname varchar(50) NOT NULL,
    role_id varchar(5) NOT NULL,
    call_number varchar(15) NULL,
    created_date timestamp NOT NULL,
    creator_id int4 NOT NULL,
    updated_date timestamp NULL,
    updater_id int4 NULL,
    deleted_date timestamp NULL,
    deleter_id int4 NULL,
    rec_status varchar(1) NOT NULL
)
WITH(
    OIDS = FALSE
);