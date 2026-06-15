INSERT INTO roles (name) VALUES ('ADMIN'), ('CUSTOMER'), ('SUPPORT') ON CONFLICT DO NOTHING;

INSERT INTO inventory (id, sku, product_name, available_quantity, reserved_quantity)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'SKU-LAPTOP-14', 'Business Laptop 14 Inch', 50, 0),
    ('22222222-2222-2222-2222-222222222222', 'SKU-HEADSET-PRO', 'Noise Cancelling Headset', 120, 0),
    ('33333333-3333-3333-3333-333333333333', 'SKU-DOCK-USBC', 'USB-C Docking Station', 75, 0)
ON CONFLICT DO NOTHING;
