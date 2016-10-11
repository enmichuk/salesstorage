package ru.salesstorage.entities

import java.util.Date

case class Sale(shop_id: Int, sale_id: Int, sale_date: Date, product_id: Int, product_count: Int, price: Double, category_id: Int, vendor_id: Int)