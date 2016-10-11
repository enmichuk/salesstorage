Cassandra:

```
create keyspace dev with replication = {'class':'SimpleStrategy','replication_factor':1};

CREATE TABLE sales (
  shop_id int, 
  sale_id int,
  sale_date timestamp,
  product_id int,
  product_count int,
  price double,
  category_id int,
  vendor_id int,
  PRIMARY KEY (shop_id, sale_date, sale_id)
)
WITH CLUSTERING ORDER BY (sale_date DESC);
```
Запросы:

Выбираем продажи за заданный период
```
POST http://localhost:8080/test/get-sales-by-period
{
	"from": "2016-01-01T00:00:00",
  	"to": "2017-01-01T00:00:00"
}
```

Выбираем продажи за период в списке магазинов
```
POST http://localhost:8080/test/get-sales-by-shop
{
	"shop": [],
  	"from": "2016-01-01T00:00:00",
  	"to": "2017-01-01T00:00:00"
}
```
