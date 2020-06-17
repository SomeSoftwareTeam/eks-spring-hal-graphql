create table property
(
    created timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    location point srid 0,
    name varchar(255) not null,
    owner varchar(255) not null,
    attributes json not null,
    updated timestamp not null default current_timestamp
);

create table verification
(
    created timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) not null,
    attributes json not null,
    property_id bigint unsigned,
    updated timestamp not null default current_timestamp,
    FOREIGN KEY (property_id) REFERENCES property(id)
);

create table fixture
(
    created timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) not null,
    owner varchar(255) not null,
    attributes json not null,
    property_id bigint unsigned,
    updated timestamp not null default current_timestamp,
    FOREIGN KEY (property_id) REFERENCES property(id)
);