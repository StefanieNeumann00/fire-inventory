CREATE TABLE equipment (
    id          uuid          not null,
    designation       varchar    not null,
    status_id    uuid    not null,
    location_id    uuid    not null,
    constraint pk_equipment primary key (id),
    constraint fk_equipmentstatus foreign key (status_id) references Status(id),
    constraint fk_equipmentlocation foreign key (location_id) references Location(id)
);