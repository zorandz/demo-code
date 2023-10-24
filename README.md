
# demo-code

This is a demo project for Ingemark company.

## API Reference

#### Get all items

```http
  GET /products
```

#### Get item

```http
  GET /products/product/{code}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `code`      | `string` |  Code of item to fetch |


#### Add new product category

In order to post a new product, product category has to be created first. It can be created on the following endpoint:

```http
  post /products/add-product-category
```

with the following JSON body:

{
  "categoryName": "Druga kategorija"
}


#### Add new product 

```http
  post /products/add-product
```

the following JSON body can be used to create a product:

{
  "name": "Proizvod 775",
  "description": "Opis",
  "priceEUR": 19.99,
  "categoryId": "1",
  "isAvailable": true
}


#### Update product 

```http
  put /products/add-product
```


#### Delete product 

```http
  delete /products/delete-product/{code}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `code`      | `string` |  Code of item to delete |





## Authors

- [@zorandz](https://github.com/zorandz)


## Badge

https://www.credly.com/badges/cb92eb62-15da-43eb-94c3-7237a535b572/public_urlc
## Features

- Create, read, update, delete the product, create product category
- Use external API to fetch EUR-USD exchange rate, and calculate the price of the product in USD based on the current exchange rate
- Validate product object upon entry of the new product




