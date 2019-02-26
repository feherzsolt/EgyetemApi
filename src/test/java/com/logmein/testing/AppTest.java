package com.logmein.testing;

import java.io.InputStream;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;

public class AppTest 
{
	private String baseUrl ;
	private Gson g = new Gson();
	
	/* get all
	 * get
	 * post
	 * delete
	 * put
	 * samename policy
	 * missing parameter
	 * text in decimal
	 */
	
	@Before
	public void Before() {
		Properties p = new Properties();
    	InputStream inputStream = ClassLoader.getSystemResourceAsStream("setting.properties");
    	try {
    		p.load(inputStream);
    	} catch(Exception e) {
    		Assert.fail();
    	}
    	
    	baseUrl = p.getProperty("url");
	}

    @Test
	public void a(){
		Assert.assertEquals(1,1);
	}

    @Test
	@Ignore
    public void proba() throws Exception{
    	Content content = Request.Get(baseUrl + "/api/food")
    			.execute().returnContent();
    	
    	System.out.println(content.asString());
    	    	
    	Food[] foodArray = g.fromJson(content.asString(), Food[].class);
    	
    	System.out.println(foodArray[0].name);
    }
    
    @Test
	@Ignore
    public void canGet() throws Exception{
    	HttpResponse r = Request.Get(baseUrl + "/api/food").execute().returnResponse();
    	Assert.assertEquals(200 , r.getStatusLine().getStatusCode());
    	
    	Food[] foodArray = g.fromJson(EntityUtils.toString(r.getEntity()), Food[].class);
    	
    	int id = foodArray[0].id;
    	
    	HttpResponse r2 = Request.Get(baseUrl + "/api/food/" + id).execute().returnResponse();
    	Assert.assertEquals(200, r2.getStatusLine().getStatusCode());
    	Food f = g.fromJson(EntityUtils.toString(r2.getEntity()), Food.class);
    	
    	Assert.assertEquals(foodArray[0].id, f.id);
    	Assert.assertEquals(foodArray[0].name, f.name);
    }
    
	  @Test
	  @Ignore
	  public void canPostAndDelete() throws Exception {
		  
	  }
}
