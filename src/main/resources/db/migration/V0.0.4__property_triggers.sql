delimiter $$
create trigger create_property_trigger
    after insert
    on property
    for each row
begin
    insert into event(parent_id, attributes)
    values (new.id,
            concat('{',
                   '"type": "CREATE",',
                   '"description": "Created property ', new.name, '"'
                   '}'));
end
$$
delimiter ;