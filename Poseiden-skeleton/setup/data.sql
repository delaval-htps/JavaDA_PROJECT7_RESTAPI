USE demo;

CREATE TABLE bid_list (
  bid_list_id tinyint NOT NULL AUTO_INCREMENT,
  account VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  bid_quantity DOUBLE,
  ask_quantity DOUBLE,
  bid DOUBLE ,
  ask DOUBLE,
  benchmark VARCHAR(125),
  bid_list_date TIMESTAMP,
  commentary VARCHAR(125),
  security VARCHAR(125),
  status VARCHAR(10),
  trader VARCHAR(125),
  book VARCHAR(125),
  creation_name VARCHAR(125),
  creation_Date TIMESTAMP ,
  revision_name VARCHAR(125),
  revision_date TIMESTAMP ,
  deal_name VARCHAR(125),
  deal_type VARCHAR(125),
  source_list_id VARCHAR(125),
  side VARCHAR(125),

  PRIMARY KEY (bid_list_id)
);

CREATE TABLE trade (
  trade_id tinyint NOT NULL AUTO_INCREMENT,
  account VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  buy_quantity DOUBLE,
  sell_quantity DOUBLE,
  buy_price DOUBLE ,
  sell_price DOUBLE,
  trade_date TIMESTAMP,
  security VARCHAR(125),
  status VARCHAR(10),
  trader VARCHAR(125),
  benchmark VARCHAR(125),
  book VARCHAR(125),
  creation_name VARCHAR(125),
  creation_date TIMESTAMP ,
  revision_name VARCHAR(125),
  revision_date TIMESTAMP ,
  deal_name VARCHAR(125),
  deal_type VARCHAR(125),
  source_list_id VARCHAR(125),
  side VARCHAR(125),

  PRIMARY KEY (trade_id)
);

CREATE TABLE curve_point (
  id tinyint NOT NULL AUTO_INCREMENT,
  curve_id tinyint,
  as_of_date TIMESTAMP,
  term DOUBLE ,
  value DOUBLE ,
  creation_date TIMESTAMP ,

  PRIMARY KEY (id)
);

CREATE TABLE rating (
  id tinyint NOT NULL AUTO_INCREMENT,
  moodys_rating VARCHAR(125),
  sandp_rating VARCHAR(125),
  fitch_rating VARCHAR(125),
  order_number tinyint,

  PRIMARY KEY (id)
);

CREATE TABLE rule_name (
  id tinyint NOT NULL AUTO_INCREMENT,
  name VARCHAR(125),
  description VARCHAR(125),
  json VARCHAR(125),
  template VARCHAR(512),
  sql_str VARCHAR(125),
  sql_part VARCHAR(125),

  PRIMARY KEY (id)
);

CREATE TABLE users (
  id tinyint NOT NULL AUTO_INCREMENT,
  username VARCHAR(125),
  email VARCHAR(125),
  password VARCHAR(125),
  fullname VARCHAR(125),
  role VARCHAR(125),
	authentication_provider VARCHAR(125),
id_provider INT,
  PRIMARY KEY (id)
);

insert into users(fullname, username, password, role) values("Administrator", "admin", "$2a$12$3M9nXE1KqOjXw5K4oNA9Muftgt9ppp5/foBB2h6aGC9lw4f3dMS9u", "ADMIN");
