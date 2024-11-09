package com.dangquocdat.FoodOrdering.service.impl;

import com.dangquocdat.FoodOrdering.dto.food.request.FoodCreationRequest;
import com.dangquocdat.FoodOrdering.dto.food.request.FoodResponse;
import com.dangquocdat.FoodOrdering.entity.Food;
import com.dangquocdat.FoodOrdering.entity.Restaurant;
import com.dangquocdat.FoodOrdering.exception.ResourceNotFoundException;
import com.dangquocdat.FoodOrdering.repository.FoodRepository;
import com.dangquocdat.FoodOrdering.repository.RestaurantRepository;
import com.dangquocdat.FoodOrdering.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    @Override
    public FoodResponse createFood(FoodCreationRequest foodCreationRequest) {

        Restaurant restaurant = restaurantRepository.findById(foodCreationRequest.getRestaurantId())
                                .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given id: "+foodCreationRequest.getRestaurantId()));

        Food food = new Food();
        food.setName(foodCreationRequest.getName());
        food.setDescription(foodCreationRequest.getDescription());
        food.setPrice(foodCreationRequest.getPrice());
        food.setImages(foodCreationRequest.getImages());
        food.setAvailable(foodCreationRequest.isAvailable());
        food.setVegetarian(food.isVegetarian());
        food.setSeasonal(food.isSeasonal());
        food.setCategory(foodCreationRequest.getCategory());
        food.setRestaurant(restaurant);
        food.setIngredientsItems(food.getIngredientsItems());
        food.setCreationDate(new Date());

        Food savedFood = foodRepository.save(food);

        FoodResponse foodResponse = modelMapper.map(savedFood, FoodResponse.class);
        foodResponse.setRestaurantId(foodCreationRequest.getRestaurantId());

        return foodResponse;
    }

    // This food will appear in DB but depend on any restaurants
    @Override
    public String deleteFoodFromRestaurant(Long foodId) {

        Food food = foodRepository.findById(foodId)
                        .orElseThrow(() -> new ResourceNotFoundException("Food is not exists with given id: "+foodId));

        food.setRestaurant(null);

        foodRepository.save(food);

        return "Delete food from restaurant successfully!";
    }

    @Override
    public List<FoodResponse> filterRestaurantsFood(
            Long restaurantId,
            boolean isVegetarian,
            boolean isNonVegetarian,
            boolean isSeasonal,
            String foodCategory) {

        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);

        if(isVegetarian)
            foods = filterByVegetarian(isVegetarian, foods);

        if(isNonVegetarian)
            foods = filterByNonVegetarian(foods);

        if(isSeasonal)
            foods = filterBySeasonal(isSeasonal, foods);

        if(foodCategory!=null && !foodCategory.isEmpty())
            foods = filterByFoodCategory(foodCategory, foods);

        return foods.stream().map(food -> {
            FoodResponse foodResponse = modelMapper.map(food, FoodResponse.class);
            foodResponse.setRestaurantId(food.getRestaurant().getId());

            return foodResponse;
        }).collect(Collectors.toList());
    }


    private List<Food> filterByVegetarian(boolean isVegetarian, List<Food> foods) {

        return foods.stream().filter(food -> food.isVegetarian()==isVegetarian)
                .collect(Collectors.toList());
    }

    private List<Food> filterByNonVegetarian(List<Food> foods) {

        return foods.stream().filter(food -> !food.isVegetarian())
                .collect(Collectors.toList());
    }

    private List<Food> filterBySeasonal(boolean isSeasonal, List<Food> foods) {

        return foods.stream().filter(food -> food.isSeasonal()==isSeasonal)
                .collect(Collectors.toList());
    }

    private List<Food> filterByFoodCategory(String foodCategory, List<Food> foods) {

        return foods.stream().filter(food -> food.getCategory().getName().equals(foodCategory))
                .collect(Collectors.toList());
    }


    @Override
    public List<FoodResponse> searchFood(String keyword) {

        List<Food> foods = foodRepository.searchFoodByKeyword(keyword);

        return foods.stream().map(food -> {
            FoodResponse foodResponse = modelMapper.map(food, FoodResponse.class);
            foodResponse.setRestaurantId(food.getRestaurant().getId());

            return foodResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public FoodResponse findFoodById(Long foodId) {

        Food food = foodRepository.findById(foodId)
                        .orElseThrow(() -> new ResourceNotFoundException("Food is not exists with given id: "+foodId));

        FoodResponse foodResponse = modelMapper.map(food, FoodResponse.class);
        foodResponse.setRestaurantId(food.getRestaurant().getId());

        return foodResponse;
    }

    @Override
    public FoodResponse updateAvailableStatus(Long foodId) {

        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Food is not exists with given id: "+foodId));

        // toggle
        food.setAvailable(!food.isAvailable());

        Food savedFood = foodRepository.save(food);

        FoodResponse foodResponse = modelMapper.map(savedFood, FoodResponse.class);
        foodResponse.setRestaurantId(savedFood.getRestaurant().getId());

        return foodResponse;
    }


}
