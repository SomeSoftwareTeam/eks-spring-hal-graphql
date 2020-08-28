delimiter $$
create trigger create_document_trigger
    after insert
    on document
    for each row
begin
    insert into event(parent_id, attributes)
    values (new.id,
            concat('{',
                   '"type": "CREATE",',
                   '"description": "Created document ', new.name, '"'
                       '}'));
end
$$
delimiter ;