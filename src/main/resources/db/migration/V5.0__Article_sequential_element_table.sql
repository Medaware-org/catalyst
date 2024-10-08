create table sequential_element
(
    id                uuid not null,

    handle            text not null,

    preceding_element uuid,

    article           uuid not null,

    primary key (id),
    foreign key (article) references article (id)
);

alter table sequential_element
    add foreign key (preceding_element) references sequential_element (id);