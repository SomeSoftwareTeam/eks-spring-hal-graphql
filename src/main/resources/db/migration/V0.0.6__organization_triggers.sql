delimiter $$
create trigger create_organization_trigger
    after insert
    on organization
    for each row
begin
    insert into event(parent_id, attributes)
    values (new.id,
            concat('{',
                   '"type": "CREATE",',
                   '"description": "Created organization ', new.name, '"'
                       '}'));
end
$$
delimiter ;