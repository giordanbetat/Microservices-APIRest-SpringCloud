INSERT INTO persons (username, password,enabled, name, surname, email) VALUES ('giordan','$2a$10$Ufa2nuuN1pn4zaTAK3zJb.B8o7DhrQXdx.ZOtVzufe6CeXDVVgb/6',true,'Giordan','Betat','exemple@exemple.com');
INSERT INTO persons (username, password,enabled, name, surname, email) VALUES ('laiany','$2a$10$Ufa2nuuN1pn4zaTAK3zJb.B8o7DhrQXdx.ZOtVzufe6CeXDVVgb/6',true,'Laiany','Betat','exemple2@exemple.com');

INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO persons_roles (person_id, role_id) VALUES (1,1);
INSERT INTO persons_roles (person_id, role_id) VALUES (2,2);
INSERT INTO persons_roles (person_id, role_id) VALUES (2,1);