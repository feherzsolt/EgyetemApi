package com.logmein.testing;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class FoodClient {

	private final String baseUrl;
	private final Gson g;

	public FoodClient(String baseUrl) {
		this.baseUrl = baseUrl;
		g = new Gson();
	}
	
	public com.logmein.testing.FoodResponse<Food[]> getAll() throws Exception {
		
		Request request = Request.Get(baseUrl + "/api/food");
	
		return doRequest(request, Food[].class);
	}
	
	public FoodResponse<Food> get(int id) throws Exception {
		Request request = Request.Get(baseUrl + "/api/food/"+ id);
		
		return doRequest(request, Food.class);
	}

	public FoodResponse<Object> post(Food createdFood) throws Exception{
		Request request = Request.Post(baseUrl + "/api/food/").bodyString(g.toJson(createdFood), ContentType.APPLICATION_JSON);
		
		return doRequest(request, Object.class);
	}
	
	public FoodResponse<Object> delete(int id) throws Exception{
		Request request = Request.Delete(baseUrl + "/api/food/" + id);
		
		return doRequest(request, Object.class);
	}	

	private <T> FoodResponse<T> doRequest(Request request, Class<T> classOfT) throws IOException, ClientProtocolException {
		
		HttpResponse httpResponse = request.execute().returnResponse();
		String content = null;
		HttpEntity e = httpResponse.getEntity();
		if (e != null)
				content = EntityUtils.toString(e);
		
		int httpCode = httpResponse.getStatusLine().getStatusCode();
		
		FoodResponse<T> foodResponse = new FoodResponse<T>();
		foodResponse.setContent(content);
		foodResponse.setHttpCode(httpCode);
		if (httpCode / 100 == 2 && content != null)
			foodResponse.setResult(g.fromJson(content, classOfT));
		
		return foodResponse;
	}
}
