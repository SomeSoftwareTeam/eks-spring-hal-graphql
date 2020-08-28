delimiter $$
create trigger create_organization_trigger
    after insert
    on organization
    for each row
begin
    insert into event(parent_id, attributes)
    values (new.id, '{
      "type": "CREATE"
    }');
end
$$
delimiter ;