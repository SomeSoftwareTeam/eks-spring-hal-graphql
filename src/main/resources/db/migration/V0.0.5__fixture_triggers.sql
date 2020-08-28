delimiter $$
create trigger create_fixture_trigger
    after insert
    on fixture
    for each row
begin
    insert into event(parent_id, attributes)
    values (new.id, '{
      "type": "CREATE"
    }');
end
$$
delimiter ;