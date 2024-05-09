create table if not exists "user"
(
    id            uuid,
    user_name     text,
    first_name    text,
    last_name     text,
    password_hash text,
    created_at    timestamptz,
    last_online   timestamptz,

    primary key (id)
);