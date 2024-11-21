create table topic
(
    id          uuid,
    name        text,
    description text,

    primary key (id)
);

alter table article
    add topic uuid;

alter table article
    add foreign key (topic) references topic (id);