package com.hltc.common;
/**
 * 公用的分页类
 * @author Hengbin Chen
 */
public class Pager {
	private Integer totalRows; // 总行数
	private Integer pageSize = 10; // 每页显示的行数
	private Integer currentPage=1; // 当前页号
	private Integer totalPages; // 总页数
	private Integer startRow=0; // 当前页在数据库中的起始行

	public Pager() {}

	public Pager(int _totalRows) {
		totalRows = _totalRows;
		totalPages = totalRows % pageSize == 0?totalRows/pageSize:totalRows/pageSize+1;
		currentPage = 1;
		startRow = 0;
	}

	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public void first() {
		currentPage = 1;
		startRow = 0;
	}

	public void previous() {
		if (currentPage == 1) {
			return;
		}
		currentPage--;
		startRow = (currentPage - 1) * pageSize;
	}

	public void next() {
		if (currentPage < totalPages) {
			currentPage++;
		}
		startRow = (currentPage - 1) * pageSize;
	}

	public void last() {
		currentPage = totalPages;
		startRow = (currentPage - 1) * pageSize;
	}

	public void refresh(Integer _currentPage) {
		currentPage = _currentPage;
		startRow = (currentPage - 1) * pageSize;
		if (currentPage > totalPages) {
			last();
		}
	}
}
