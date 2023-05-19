CREATE TABLE location (
    id          uuid          not null,
    designation       varchar    not null,
    vehicle_id    uuid,
    constraint pk_location primary key (id)
);
