CREATE TABLE IF NOT EXISTS "product-demo".product (
	product_id serial4 NOT NULL,
	product_name varchar(30) NOT NULL,
	product_group varchar(10) NOT NULL,
	CONSTRAINT product_demo_ppkey PRIMARY KEY (product_id)
);