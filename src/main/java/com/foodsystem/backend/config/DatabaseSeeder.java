package com.foodsystem.backend.config;

import com.foodsystem.backend.entity.*;
import com.foodsystem.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Seed Admin User
            User admin = User.builder()
                    .name("System Admin")
                    .email("admin@foodsystem.com")
                    .password(passwordEncoder.encode("adminpwd123"))
                    .phone("9876543210")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);

            // Seed Customer User
            User customer = User.builder()
                    .name("John Doe")
                    .email("john.doe@example.com")
                    .password(passwordEncoder.encode("johnpwd123"))
                    .phone("9876543222")
                    .role(Role.CUSTOMER)
                    .build();
            userRepository.save(customer);

            // Seed Restaurant
            Restaurant restaurant = Restaurant.builder()
                    .restaurantName("Galactic Gourmet")
                    .address("Sector 7, Food Space Station")
                    .contactNumber("+919876543233")
                    .openingTime("09:00")
                    .closingTime("22:00")
                    .build();
            restaurant = restaurantRepository.save(restaurant);

            // Seed Categories
            Category burgersCategory = Category.builder()
                    .categoryName("Burgers")
                    .description("Vibrant and delicious variety of burgers")
                    .build();
            burgersCategory = categoryRepository.save(burgersCategory);

            Category pizzasCategory = Category.builder()
                    .categoryName("Pizzas")
                    .description("Delicious pizzas from another dimension")
                    .build();
            pizzasCategory = categoryRepository.save(pizzasCategory);

            Category sidesCategory = Category.builder()
                    .categoryName("Sides")
                    .description("Perfect accompaniments for your space journey")
                    .build();
            sidesCategory = categoryRepository.save(sidesCategory);

            Category momosCategory = Category.builder()
                    .categoryName("Momos")
                    .description("Hot steaming space momos")
                    .build();
            momosCategory = categoryRepository.save(momosCategory);

            // Seed Menu Items
            MenuItem burger = MenuItem.builder()
                    .itemName("Neon Classic Burger")
                    .description("Flame grilled chicken patty with cheddar cheese and fresh lettuce")
                    .price(12.99)
                    .availability(true)
                    .category(burgersCategory)
                    .restaurant(restaurant)
                    .build();
            menuItemRepository.save(burger);

            MenuItem pizza = MenuItem.builder()
                    .itemName("Cyberpunk Pizza")
                    .description("Loaded with extraterrestrial cheese and synth-pepperoni")
                    .price(18.50)
                    .availability(true)
                    .category(pizzasCategory)
                    .restaurant(restaurant)
                    .build();
            menuItemRepository.save(pizza);

            MenuItem fries = MenuItem.builder()
                    .itemName("Galactic Fries")
                    .description("Golden potato wedges seasoned with stardust")
                    .price(5.99)
                    .availability(true)
                    .category(sidesCategory)
                    .restaurant(restaurant)
                    .build();
            menuItemRepository.save(fries);

            MenuItem momos = MenuItem.builder()
                    .itemName("Veg Momos")
                    .description("Fresh vegetable fillings steamed to cosmic perfection")
                    .price(7.50)
                    .availability(true)
                    .category(momosCategory)
                    .restaurant(restaurant)
                    .build();
            menuItemRepository.save(momos);

            System.out.println("Database seeded successfully with default users, restaurant, categories and menu items!");
        }
    }
}
