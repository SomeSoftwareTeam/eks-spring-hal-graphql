create trigger create_property_trigger after insert on property
    for each row insert into event(parent_id, attributes) values (new.id, '{}');
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