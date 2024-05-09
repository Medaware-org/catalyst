create table if not exists article
(
    id         uuid,

    created_by uuid,
    created_at timestamptz,

    title      text,
    lead       text,
    content    text,

    primary key (id),
    foreign key (created_by) references "user" (id)
);