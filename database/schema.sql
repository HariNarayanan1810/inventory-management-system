CREATE TABLE IF NOT EXISTS suppliers (
    supplier_id SERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    contact_name VARCHAR(120),
    email VARCHAR(160) UNIQUE,
    address TEXT,
    rating NUMERIC(3, 2) DEFAULT 4.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS warehouses (
    warehouse_id SERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    location TEXT
);

CREATE TABLE IF NOT EXISTS products (
    product_id SERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    description TEXT,
    sku VARCHAR(80) NOT NULL UNIQUE,
    category VARCHAR(100) NOT NULL,
    unit_price NUMERIC(12, 2) NOT NULL DEFAULT 0,
    supplier_id INTEGER REFERENCES suppliers(supplier_id),
    product_rating NUMERIC(3, 2) DEFAULT 4.00,
    reorder_point INTEGER NOT NULL DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    default_selling_price NUMERIC(12, 2) NOT NULL DEFAULT 0,
    image_url TEXT,
    is_archived BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS product_stock (
    stock_id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES products(product_id),
    warehouse_id INTEGER NOT NULL REFERENCES warehouses(warehouse_id),
    quantity INTEGER NOT NULL DEFAULT 0,
    low_stock_threshold INTEGER NOT NULL DEFAULT 10,
    UNIQUE (product_id, warehouse_id)
);

CREATE TABLE IF NOT EXISTS sales (
    sale_id SERIAL PRIMARY KEY,
    customer_name VARCHAR(140) NOT NULL,
    product_id INTEGER NOT NULL REFERENCES products(product_id),
    warehouse_id INTEGER NOT NULL REFERENCES warehouses(warehouse_id),
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    payment_status VARCHAR(40) NOT NULL DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS purchase_orders (
    purchase_id SERIAL PRIMARY KEY,
    supplier_id INTEGER NOT NULL REFERENCES suppliers(supplier_id),
    warehouse_id INTEGER NOT NULL REFERENCES warehouses(warehouse_id),
    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_status VARCHAR(40) NOT NULL DEFAULT 'unpaid',
    delivery_status VARCHAR(60) NOT NULL DEFAULT 'on the way',
    total_amount NUMERIC(12, 2) NOT NULL DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sales_returns (
    return_id SERIAL PRIMARY KEY,
    sale_id INTEGER REFERENCES sales(sale_id),
    product_id INTEGER REFERENCES products(product_id),
    quantity_returned INTEGER NOT NULL CHECK (quantity_returned > 0),
    reason TEXT NOT NULL,
    refund_amount NUMERIC(12, 2) NOT NULL DEFAULT 0,
    return_status VARCHAR(40) NOT NULL DEFAULT 'pending',
    return_date DATE NOT NULL DEFAULT CURRENT_DATE,
    warehouse_id INTEGER REFERENCES warehouses(warehouse_id)
);

CREATE TABLE IF NOT EXISTS purchase_returns (
    return_id SERIAL PRIMARY KEY,
    purchase_id INTEGER REFERENCES purchase_orders(purchase_id),
    product_id INTEGER REFERENCES products(product_id),
    quantity_returned INTEGER NOT NULL CHECK (quantity_returned > 0),
    reason TEXT NOT NULL,
    return_status VARCHAR(40) NOT NULL DEFAULT 'Pending',
    return_date DATE NOT NULL DEFAULT CURRENT_DATE
);

ALTER TABLE suppliers ADD COLUMN IF NOT EXISTS contact_name VARCHAR(120);
ALTER TABLE suppliers ADD COLUMN IF NOT EXISTS email VARCHAR(160);
ALTER TABLE suppliers ADD COLUMN IF NOT EXISTS address TEXT;
ALTER TABLE suppliers ADD COLUMN IF NOT EXISTS rating NUMERIC(3, 2) DEFAULT 4.00;
ALTER TABLE suppliers ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE warehouses ADD COLUMN IF NOT EXISTS name VARCHAR(120);
ALTER TABLE warehouses ADD COLUMN IF NOT EXISTS location TEXT;

ALTER TABLE products ADD COLUMN IF NOT EXISTS description TEXT;
ALTER TABLE products ADD COLUMN IF NOT EXISTS sku VARCHAR(80);
ALTER TABLE products ADD COLUMN IF NOT EXISTS category VARCHAR(100);
ALTER TABLE products ADD COLUMN IF NOT EXISTS unit_price NUMERIC(12, 2) DEFAULT 0;
ALTER TABLE products ADD COLUMN IF NOT EXISTS supplier_id INTEGER;
ALTER TABLE products ADD COLUMN IF NOT EXISTS product_rating NUMERIC(3, 2) DEFAULT 4.00;
ALTER TABLE products ADD COLUMN IF NOT EXISTS reorder_point INTEGER DEFAULT 10;
ALTER TABLE products ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE products ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE products ADD COLUMN IF NOT EXISTS default_selling_price NUMERIC(12, 2) DEFAULT 0;
ALTER TABLE products ADD COLUMN IF NOT EXISTS image_url TEXT;
ALTER TABLE products ADD COLUMN IF NOT EXISTS is_archived BOOLEAN DEFAULT FALSE;

ALTER TABLE product_stock ADD COLUMN IF NOT EXISTS low_stock_threshold INTEGER DEFAULT 10;

ALTER TABLE sales ADD COLUMN IF NOT EXISTS customer_name VARCHAR(140);
ALTER TABLE sales ADD COLUMN IF NOT EXISTS product_id INTEGER;
ALTER TABLE sales ADD COLUMN IF NOT EXISTS warehouse_id INTEGER;
ALTER TABLE sales ADD COLUMN IF NOT EXISTS quantity INTEGER DEFAULT 1;
ALTER TABLE sales ADD COLUMN IF NOT EXISTS payment_status VARCHAR(40) DEFAULT 'Pending';
ALTER TABLE sales ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE purchase_orders ADD COLUMN IF NOT EXISTS purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE purchase_orders ADD COLUMN IF NOT EXISTS payment_status VARCHAR(40) DEFAULT 'unpaid';
ALTER TABLE purchase_orders ADD COLUMN IF NOT EXISTS delivery_status VARCHAR(60) DEFAULT 'on the way';
ALTER TABLE purchase_orders ADD COLUMN IF NOT EXISTS total_amount NUMERIC(12, 2) DEFAULT 0;
ALTER TABLE purchase_orders ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE sales_returns ADD COLUMN IF NOT EXISTS refund_amount NUMERIC(12, 2) DEFAULT 0;
ALTER TABLE sales_returns ADD COLUMN IF NOT EXISTS return_status VARCHAR(40) DEFAULT 'pending';
ALTER TABLE sales_returns ADD COLUMN IF NOT EXISTS return_date DATE DEFAULT CURRENT_DATE;
ALTER TABLE sales_returns ADD COLUMN IF NOT EXISTS warehouse_id INTEGER;

ALTER TABLE purchase_returns ADD COLUMN IF NOT EXISTS refund_amount NUMERIC(12, 2) DEFAULT 0;
ALTER TABLE purchase_returns ALTER COLUMN refund_amount SET DEFAULT 0;
UPDATE purchase_returns SET refund_amount = 0 WHERE refund_amount IS NULL;

INSERT INTO suppliers (supplier_id, name, contact_name, email, address, rating)
VALUES
    (1, 'Pro Sports Manufacturing', 'Arun Kumar', 'sales@prosports.example', 'Coimbatore', 4.50),
    (2, 'Ace Warehouse Supply', 'Meena Raj', 'contact@acewarehouse.example', 'Chennai', 4.20)
ON CONFLICT (supplier_id) DO NOTHING;

INSERT INTO warehouses (warehouse_id, name, location)
VALUES
    (1, 'Main Warehouse', 'Coimbatore')
ON CONFLICT (warehouse_id) DO NOTHING;

INSERT INTO products (product_id, name, description, sku, category, unit_price, supplier_id, product_rating, reorder_point, default_selling_price)
VALUES
    (1, 'Cricket Bat Pro', 'Premium willow cricket bat', 'BAT-PRO-001', 'Cricket', 1200.00, 1, 4.70, 10, 1800.00),
    (2, 'Leather Ball Pack', 'Pack of six leather cricket balls', 'BALL-LTH-006', 'Cricket', 450.00, 2, 4.30, 20, 700.00),
    (3, 'Batting Gloves', 'Right handed batting gloves', 'GLV-BAT-001', 'Accessories', 350.00, 1, 4.10, 15, 550.00)
ON CONFLICT (product_id) DO NOTHING;

INSERT INTO product_stock (product_id, warehouse_id, quantity, low_stock_threshold)
VALUES
    (1, 1, 35, 10),
    (2, 1, 12, 20),
    (3, 1, 42, 15)
ON CONFLICT (product_id, warehouse_id) DO NOTHING;

INSERT INTO sales (customer_name, product_id, warehouse_id, quantity, payment_status)
VALUES
    ('Retailer A', 1, 1, 3, 'Paid'),
    ('Retailer B', 2, 1, 8, 'Pending')
ON CONFLICT DO NOTHING;

INSERT INTO purchase_orders (supplier_id, warehouse_id, payment_status, delivery_status, total_amount)
VALUES
    (1, 1, 'paid', 'delivered', 24000.00),
    (2, 1, 'unpaid', 'on the way', 9000.00)
ON CONFLICT DO NOTHING;
