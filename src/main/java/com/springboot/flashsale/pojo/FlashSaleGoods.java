package com.springboot.flashsale.pojo;

import java.util.Date;

public class FlashSaleGoods {
	private Long id;
	private Long goods_id;
	private Double flash_sale_price;
	private Integer stock_count;
	private Date start_date;
	private Date end_date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Long goods_id) {
		this.goods_id = goods_id;
	}

	public Double getFlash_sale_price() {
		return flash_sale_price;
	}

	public void setFlash_sale_price(Double flash_sale_price) {
		this.flash_sale_price = flash_sale_price;
	}

	public Integer getStock_count() {
		return stock_count;
	}

	public void setStock_count(Integer stock_count) {
		this.stock_count = stock_count;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	@Override
	public String toString() {
		return "FlashSaleGoods [id=" + id + ", goods_id=" + goods_id + ", stock_count=" + stock_count + ", start_date="
				+ start_date + ", end_date=" + end_date + "]";
	}

}
