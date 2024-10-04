create table element_metadata
(
    id         uuid not null,
    element    uuid not null,
    meta_key   text not null,
    meta_value text not null,

    primary key (id),
    foreign key (element) references sequential_element (id)
);