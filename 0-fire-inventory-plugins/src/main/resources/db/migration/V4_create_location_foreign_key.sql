ALTER TABLE location
ADD FOREIGN KEY (vehicle_id) REFERENCES Item(id);