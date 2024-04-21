package xyz.magicraft.longshort.ssf.base;

import java.util.List;

public class Pagination<T> {
	private int page;
	private int size;
	private int pages;
	private List<T> items;

	public Pagination(int page,int size,int pages,List<T> items) {
		this.page = page;
		this.size = size;
		this.pages = pages;
		this.items = items;
	}

	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}




}
