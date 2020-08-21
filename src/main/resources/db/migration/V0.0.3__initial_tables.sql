# https://dev.mysql.com/doc/refman/8.0/en/create-table.html
create table organization
(
    attributes json not null,
    created_at timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id binary(16) primary key,
    name varchar(255) not null,
    owner_id varchar(255) not null,
    updated_at timestamp not null default current_timestamp
);

create table organization_member
(
    organization_id binary(16) not null,
    created_at timestamp not null default current_timestamp,
    id binary(16) primary key,
    member_id varchar(255) not null,
    FOREIGN KEY (organization_id) REFERENCES organization(id)
);

create table club
(
    attributes json not null,
    created_at timestamp not null default current_timestamp,
    description varchar(255) not null,
    deleted boolean not null default false,
    id binary(16) primary key,
    name varchar(255) not null,
    owner_id varchar(255) not null,
    updated_at timestamp not null default current_timestamp
);

create table club_member
(
    club_id binary(16) not null,
    created_at timestamp not null default current_timestamp,
    id binary(16) primary key,
    member_id varchar(255) not null,
    FOREIGN KEY (club_id) REFERENCES club(id)
);

create table property
(
    address varchar(255) not null,
    address_formatted boolean default false,
    attributes json not null,
    club_id binary(16),
    created_at timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id binary(16) primary key,
    location point srid 3857,
    name varchar(255) not null,
    owner_id varchar(255) not null,
    updated_at timestamp not null default current_timestamp,
    FOREIGN KEY (club_id) REFERENCES club(id)
);

create table verification
(
    created_at timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id binary(16) primary key,
    name varchar(255) not null,
    attributes json not null,
    owner_id varchar(255) not null,
    property_id binary(16),
    updated_at timestamp not null default current_timestamp,
    FOREIGN KEY (property_id) REFERENCES property(id)
);

create table fixture
(
    attributes json not null,
    created_at timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id binary(16) primary key,
    name varchar(255) not null,
    owner_id varchar(255) not null,
    property_id binary(16),
    updated_at timestamp not null default current_timestamp,
    FOREIGN KEY (property_id) REFERENCES property(id)
);

create table record
(
    attributes json not null,
    created_at timestamp not null default current_timestamp,
    deleted boolean not null default false,
    id binary(16) primary key,
    name varchar(255) not null,
    owner_id varchar(255) not null,
    parent_id binary(16),
    updated_at timestamp not null default current_timestamp
);

create table document
(
    created_at timestamp not null default current_timestamp,
    deleted boolean not null default false,
    description varchar(255) not null,
    id binary(16) primary key,
    attributes json not null,
    name varchar(255) not null,
    owner_id varchar(255) not null,
    property_id binary(16),
    updated_at timestamp not null default current_timestamp,
    url varchar(255) not null,
    FOREIGN KEY (property_id) REFERENCES property(id)
);