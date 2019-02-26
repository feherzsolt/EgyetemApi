package com.logmein.testing;

public class FoodResponse<T> {
	
	private String content;
	private int httpCode;
	private T result;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getHttpCode() {
		return httpCode;
	}
	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}		
}
