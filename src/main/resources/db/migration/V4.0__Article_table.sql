create table article
(
    id            uuid        not null,

    maintainer_id uuid        not null,

    title         text        not null,

    created_at    timestamptz not null,

    primary key (id),
    foreign key (maintainer_id) references maintainer (id)
);