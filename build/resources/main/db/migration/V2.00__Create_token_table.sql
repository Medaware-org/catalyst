create table if not exists auth_token
(
    id      uuid,
    user_id uuid,
    token   text,

    primary key (id),
    foreign key (user_id) references "user" (id)
);