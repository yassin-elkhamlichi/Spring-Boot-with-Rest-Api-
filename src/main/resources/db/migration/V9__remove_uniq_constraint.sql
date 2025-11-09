alter table order_items
    drop foreign key fk_order;

alter table order_items
    drop foreign key fk_product;

drop index order_id on order_items;

drop index product_id on order_items;

create index order_id
    on order_items (order_id);

create index product_id
    on order_items (product_id);
alter table order_items
    add constraint fk_product
        foreign key (product_id) references products (id);

alter table order_items
    add constraint fk_order
        foreign key (order_id) references orders (id);