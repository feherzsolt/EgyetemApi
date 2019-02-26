package com.logmein.testing;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class FoodTest {

	private FoodClient foodClient;

	@Before
	public void Before() {
		Properties p = new Properties();
		InputStream inputStream = ClassLoader.getSystemResourceAsStream("setting.properties");
		try {
			p.load(inputStream);
		} catch (Exception e) {
			Assert.fail();
		}

		String baseUrl = p.getProperty("url");
		foodClient = new FoodClient(baseUrl);
	}

	@Test
	@Ignore
	public void canGet() throws Exception {

		FoodResponse<Food[]> allFood = foodClient.getAll();

		Assert.assertEquals(200, allFood.getHttpCode());

		int id = allFood.getResult()[0].id;

		FoodResponse<Food> f = foodClient.get(id);
		Assert.assertEquals(200, f.getHttpCode());

		assertSame(allFood.getResult()[0], f.getResult());
	}

	@Test
	@Ignore
	public void canPostAndDelete() throws Exception {
		final Food createdFood = new Food();
		createdFood.name = "testFood";
		createdFood.difficulty = 1;
		createdFood.foodType = 4;

		FoodResponse<Food[]> allExistingFood = foodClient.getAll();
		Food existingFood = Arrays.stream(allExistingFood.getResult()).filter(new Predicate<Food>() {
			@Override
			public boolean test(Food t) {
				return t.name.equals(createdFood.name);
			}
		}).findFirst().orElse(null);
		if (existingFood != null) {
			foodClient.delete(existingFood.id);
		}

		FoodResponse<?> post = foodClient.post(createdFood);

		Assert.assertEquals(204, post.getHttpCode());

		FoodResponse<Food[]> allFood = foodClient.getAll();
		final Optional<Food> optionalFood = Arrays.stream(allFood.getResult()).filter(new Predicate<Food>() {
			@Override
			public boolean test(Food f) {
				return f.name != null && f.name.equals(createdFood.name);
			}
		}).findFirst();

		Assert.assertTrue(optionalFood.isPresent());
		assertEqual(createdFood, optionalFood.get());

		FoodResponse<?> deleteResponse = foodClient.delete(optionalFood.get().id);
		Assert.assertEquals(200, deleteResponse.getHttpCode());

		FoodResponse<Food[]> remainingFood = foodClient.getAll();
		boolean deleted = Arrays.stream(remainingFood.getResult()).allMatch(new Predicate<Food>() {

			@Override
			public boolean test(Food t) {
				return t.id != optionalFood.get().id;
			}

		});

		Assert.assertTrue(deleted);
	}
	
	@Test
	public void testMissingName() throws Exception{
		String name = null;
		
		nameValidation(name);
	}

	private void nameValidation(String name) throws Exception {
		final Food createdFood = new Food();
		createdFood.name = name;
		createdFood.difficulty = 1;
		createdFood.foodType = 1;
		
		FoodResponse<?> postResponse = foodClient.post(createdFood);
		
		Assert.assertEquals(400, postResponse.getHttpCode());
	}
	
	@Test
	public void testLongName () throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		for(int i  = 0; i< 256;i++)
			stringBuilder.append('a');
	
		nameValidation(stringBuilder.toString());		
	}

	private void assertEqual(Food f1, Food f2) {
		Assert.assertEquals(f1.name, f2.name);
		Assert.assertEquals(f1.difficulty, f2.difficulty);
		Assert.assertEquals(f1.foodType, f2.foodType);
	}

	private void assertSame(Food f1, Food f2) {
		Assert.assertEquals(f1.id, f2.id);
		assertEqual(f1, f2);
	}
}
