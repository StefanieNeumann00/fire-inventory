CREATE TABLE vehicle (
    id          uuid          not null,
    designation       varchar    not null,
    status_id    uuid    not null,
    place_id    uuid    not null,
    constraint pk_vehicle primary key (id),
    constraint fk_vehiclestatus foreign key (status_id) references Status(id),
    constraint fk_vehicleplace foreign key (place_id) references Place(id)
);