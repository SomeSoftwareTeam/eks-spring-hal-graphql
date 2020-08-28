delimiter $$
create trigger create_property_trigger
    after insert
    on property
    for each row
begin
    insert into event(parent_id, attributes)
    values (new.id, '{
      "type": "CREATE"
    }');
end
$$
delimiter ;