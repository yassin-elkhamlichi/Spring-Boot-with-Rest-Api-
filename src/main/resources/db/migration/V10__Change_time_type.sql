alter table orders
    modify created_at TIMESTAMP default CURRENT_TIMESTAMP not null;