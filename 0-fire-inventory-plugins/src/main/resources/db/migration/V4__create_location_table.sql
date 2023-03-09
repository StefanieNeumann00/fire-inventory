CREATE TABLE location (
    id          uuid          not null,
    vehicle_id    uuid,
    place_id    uuid,
    constraint pk_location primary key (id),
    constraint fk_locationvehicle foreign key (vehicle_id) references Vehicle(id),
    constraint fk_locationplace foreign key (place_id) references Place(id)
);