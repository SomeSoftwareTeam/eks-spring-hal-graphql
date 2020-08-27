# https://dev.mysql.com/doc/refman/8.0/en/stored-programs-logging.html
# SET GLOBAL log_bin_trust_function_creators = 1;

delimiter $$
create trigger create_property_trigger
    after insert
    on property
    for each row
begin
    insert into event(parent_id, attributes) values (new.id, '{}');
end
$$
delimiter ;
#
# create trigger create_fixture_trigger after insert on fixture
#     for each row insert into event(parent_id, attributes) values (new.id, '{"type": "CREATE"}');
#
# create trigger create_organization_trigger after insert on organization
#     for each row insert into event(parent_id, attributes) values (new.id, '{"type": "CREATE"}');
#
# create trigger create_record_trigger after insert on record
#     for each row insert into event(parent_id, attributes) values (new.id, '{"type": "CREATE"}');
#
# create trigger create_document_trigger after insert on document
#     for each row insert into event(parent_id, attributes) values (new.id, '{"type": "CREATE"}');