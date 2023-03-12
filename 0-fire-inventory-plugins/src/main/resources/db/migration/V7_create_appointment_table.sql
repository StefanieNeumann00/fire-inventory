CREATE TABLE appointment (
   id          uuid          not null,
   designation       varchar    not null,
   description       varchar;
   vehicle_id    uuid,
   equipment_id    uuid,
   due_date        Date     not null;
   time_interval   int;
   end_date        Date;
   constraint pk_appointment primary key (id),
   constraint fk_appointmentvehicle foreign key (vehicle_id) references Vehicle(id),
   constraint fk_appointmentequipment foreign key (equipment_id) references Equipment(id)
);