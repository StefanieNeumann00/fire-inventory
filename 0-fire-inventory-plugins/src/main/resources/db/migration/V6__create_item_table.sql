CREATE TABLE item (
    id          uuid          not null,
    vehicle_id    uuid,
    equipment_id    uuid,
    constraint pk_item primary key (id),
    constraint fk_itemvehicle foreign key (vehicle_id) references Vehicle(id),
    constraint fk_itemequipment foreign key (equipment_id) references Equipment(id)
);