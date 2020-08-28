delimiter $$
create trigger create_record_trigger
    after insert
    on record
    for each row
begin
    insert into event(parent_id, attributes)
    values (new.id, '{
      "type": "CREATE"
    }');
end
$$
delimiter ;