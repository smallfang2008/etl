package com.zbiti.common.pageQuery;

import java.util.List;

public class PageQueryResult<T>
{
	public PageQueryResult(List<T> rows, int total)
	{
		this.rows = rows;
		this.results = total;
	}

	List<T> rows;

	public List<T> getRows()
	{
		return rows;
	}

	public void setRows(List<T> rows)
	{
		this.rows = rows;
	}

	int results;

	public int getResults() {
		return results;
	}

	public void setResults(int results) {
		this.results = results;
	}
}
