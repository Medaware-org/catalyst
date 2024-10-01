create table maintainer
(
    id            uuid        not null,

    first_name    text        not null,
    last_name     text        not null,

    username      text        not null,

--  This is to de-couple the username, as well as the first and last names
--  from what is displayed as the author's name on the articles
    display_name  text        not null,

    password_hash text        not null,

    created_at    timestamptz not null,

    primary key (id)
);