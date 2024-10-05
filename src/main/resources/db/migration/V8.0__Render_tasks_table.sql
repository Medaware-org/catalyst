create table render_task
(
    id          uuid        not null,

    article_id  uuid        not null,

    antg_res    text        not null,
    html_res    text        not null,

    invalidated boolean     not null default false,

    created_at  timestamptz not null,

    primary key (id),
    foreign key (article_id) references article (id)
);