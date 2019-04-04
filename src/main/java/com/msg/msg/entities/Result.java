package com.msg.msg.entities;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Result<T> {

	private int count;

	private List<T> results;

	public Result() {
	}

	public Result(int count, List<T> results) {
		this.count = count;
		this.results = results;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}
	
	public static void validateIndexes(int index1, int index2) {
		if((index1<0)||(index2<0)||(index2-index1>50)){
			throw new ResponseStatusException(
			           HttpStatus.BAD_REQUEST, "Invalid index");
		}
	}

	@Override
	public String toString() {
		return "Result [count=" + count + ", results=" + results + "]";
	}

}
