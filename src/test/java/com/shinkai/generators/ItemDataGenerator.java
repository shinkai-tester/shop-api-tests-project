package com.shinkai.generators;

import com.github.javafaker.Faker;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ItemDataGenerator {
    static final Faker faker = new Faker();
    private static final String PHOTO_DIRECTORY = "src/test/resources/photos/";
    private final String[] categories = new String[]{"Категория 1", "＼(〇_ｏ)／", "Test"};
    private final String[] colors = new String[]{"RED", "ORANGE", "BLUE", "GREEN"};

    private final String[] sizes = new String[]{"42-44", "┐(￣∀￣)┌", "1-2г", "20"};

    public String getItemName() {
        return faker.commerce().productName();
    }

    public String getCategory() {
        return faker.options().nextElement(categories);
    }

    public String getDescription() {
        return faker.harryPotter().quote();
    }

    public String getColor() {
        return faker.options().nextElement(colors);
    }

    public String getSize() {
        return faker.options().nextElement(sizes);
    }

    public String getParams() {
        return faker.harryPotter().spell();
    }

    public Double getPrice() {
        return Double.parseDouble(faker.commerce().price(10.00, 1000.00));
    }

    public String getRandomPhoto() {
        List<String> photos = Arrays.asList("cat_princess.jpg", "cat_sweater.jpg", "dog.jpg", "possum_hb.jpg");
        return faker.options().nextElement(photos);
    }

    public String getBase64FileString(String photoName) {
        Path photoPath = Paths.get(PHOTO_DIRECTORY + photoName);
        try {
            byte[] photoBytes = Files.readAllBytes(photoPath);
            String base64String = Base64.getEncoder().encodeToString(photoBytes);
            return "data:image/jpeg;base64," + base64String;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
