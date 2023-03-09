CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO status VALUES (uuid_generate_v1(), 'Einsatzbereit');
INSERT INTO status VALUES (uuid_generate_v1(), 'Vor Ort');
INSERT INTO status VALUES (uuid_generate_v1(), 'Nicht vor Ort');
INSERT INTO status VALUES (uuid_generate_v1(), 'In Reparatur');
INSERT INTO status VALUES (uuid_generate_v1(), 'Kaputt');