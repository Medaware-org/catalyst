create table session
(
    id            uuid        not null,
    maintainer_id uuid        not null,
    session_token text        not null,
    created_at    timestamptz not null,
    accessed_at   timestamptz not null,
    invalidated   boolean     not null,

    primary key (id),
    foreign key (maintainer_id) references maintainer (id)
);