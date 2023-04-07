drop table if exists patient cascade;
drop table if exists observation cascade;

create table if not exists patient
(
    snils   bigint primary key,
    name    varchar(50) not null,
    surname varchar(50) not null,
    birth   varchar(50) not null
);

create table if not exists observation
(
    id               bigserial not null primary key,
    snils            bigint
        references patient (snils)
            on delete cascade,
    ecg              int       not null,
    pulse            int       not null,
    sat              int       not null,
    co2_insp         real      not null,
    co2_exp          real      not null,
    observation_time timestamp not null
);