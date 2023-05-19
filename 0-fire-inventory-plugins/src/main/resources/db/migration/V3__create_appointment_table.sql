CREATE TABLE appointment (
    id          uuid          not null,
    designation       varchar    not null,
    description       varchar,
    item_id    uuid,
    due_date        Date     not null,
    constraint pk_appointment primary key (id),
    constraint fk_appointmentitem foreign key (item_id) references item(id)
);