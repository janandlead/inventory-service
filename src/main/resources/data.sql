-- Run after the application has started once (so Hibernate has created the table),
-- or execute these statements manually in inventorydb.
INSERT INTO inventory (product_code, product_name, quantity)
VALUES ('P1004', 'Shoes', 10)
ON CONFLICT (product_code) DO NOTHING;

INSERT INTO inventory (product_code, product_name, quantity)
VALUES ('P1005', 'Tshirts', 20)
ON CONFLICT (product_code) DO NOTHING;

INSERT INTO inventory (product_code, product_name, quantity)
VALUES ('P1006', 'Electronics', 5)
ON CONFLICT (product_code) DO NOTHING;
