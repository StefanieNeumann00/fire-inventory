CREATE TABLE item (
    id          uuid          not null,
    designation       varchar    not null,
    status    varchar    not null,
    location_id    uuid    not null,
    constraint pk_item primary key (id),
    constraint fk_itemlocation foreign key (location_id) references Location(id)
);