package com.shinkai.generators;

import com.github.javafaker.Faker;
import com.shinkai.api.models.CreateItemRequestDto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ItemFactory {
    static final Faker faker = new Faker();
    private static final String PHOTO_DIRECTORY = "src/test/resources/photos/";
    private static final String[] categories = new String[]{"Категория 1", "＼(〇_ｏ)／", "Test"};
    private static final String[] colors = new String[]{"RED", "ORANGE", "BLUE", "GREEN"};
    private static final String[] sizes = new String[]{"42-44", "┐(￣∀￣)┌", "1-2г", "20"};


    private static String getRandomPhoto() {
        List<String> photos = Arrays.asList("cat_princess.jpg", "cat_sweater.jpg", "dog.jpg", "possum_hb.jpg");
        return faker.options().nextElement(photos);
    }

    public static String getBase64FileString(String photoName) {
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

    public CreateItemRequestDto getRandomItem() {
        return CreateItemRequestDto.builder()
                .name(faker.commerce().productName())
                .section(faker.options().nextElement(categories))
                .description(faker.harryPotter().quote())
                .color(faker.options().nextElement(colors))
                .size(faker.options().nextElement(sizes))
                .price(Double.parseDouble(faker.commerce().price(10.00, 1000.00)))
                .params(faker.harryPotter().spell())
                .photo(getBase64FileString(getRandomPhoto()))
                .build();
    }

    ;
}
