alter table article
    add column root_element uuid;

alter table article
    add foreign key (root_element) references sequential_element (id);