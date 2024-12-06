package com.dangquocdat.FoodOrdering.service.impl;

import com.dangquocdat.FoodOrdering.dto.food.request.FoodCreationRequest;
import com.dangquocdat.FoodOrdering.dto.food.response.FoodResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.entity.Category;
import com.dangquocdat.FoodOrdering.entity.Food;
import com.dangquocdat.FoodOrdering.entity.IngredientsItem;
import com.dangquocdat.FoodOrdering.entity.Restaurant;
import com.dangquocdat.FoodOrdering.exception.ApiException;
import com.dangquocdat.FoodOrdering.exception.ResourceNotFoundException;
import com.dangquocdat.FoodOrdering.repository.CategoryRepository;
import com.dangquocdat.FoodOrdering.repository.FoodRepository;
import com.dangquocdat.FoodOrdering.repository.RestaurantRepository;
import com.dangquocdat.FoodOrdering.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public FoodResponse createFood(FoodCreationRequest foodCreationRequest) {

        Restaurant restaurant = restaurantRepository.findById(foodCreationRequest.getRestaurantId())
                                .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given id: "+foodCreationRequest.getRestaurantId()));

        // food Category
        Category category = categoryRepository.findById(foodCreationRequest.getCategoryId())
                                .orElseThrow(() -> new ResourceNotFoundException("Category is not exists with given id: "+foodCreationRequest.getCategoryId()));

        Food food = new Food();
        food.setName(foodCreationRequest.getName());
        food.setDescription(foodCreationRequest.getDescription());
        food.setPrice(foodCreationRequest.getPrice());
        food.setImages(foodCreationRequest.getImages());
        food.setAvailable(foodCreationRequest.isAvailable());
        food.setVegetarian(foodCreationRequest.isVegetarian());
        food.setSeasonal(foodCreationRequest.isSeasonal());
        food.setCategory(category);
        food.setRestaurant(restaurant);

        List<IngredientsItem> ingredientsItems = foodCreationRequest
                                                    .getIngredients()
                                                        .stream().map((item) -> modelMapper.map(item, IngredientsItem.class))
                                                            .collect(Collectors.toList());

        food.setIngredientsItems(ingredientsItems);
        food.setCreationDate(new Date());

        Food savedFood = foodRepository.save(food);

        // restaurant.getFoods().add(savedFood);

        FoodResponse foodResponse = modelMapper.map(savedFood, FoodResponse.class);
        foodResponse.setRestaurantId(foodCreationRequest.getRestaurantId());
        foodResponse.setRestaurantName(food.getRestaurant().getName());

        return foodResponse;
    }

    @Override
    public FoodResponse updateFood(Long foodId, FoodCreationRequest foodCreationRequest) {

        Food foodFromDB = foodRepository.findById(foodId)
                                .orElseThrow(() -> new ResourceNotFoundException("Food is not exists with given id: "+foodId));

        if(foodCreationRequest.getName()!=null)
            foodFromDB.setName(foodCreationRequest.getName());

        if(foodCreationRequest.getDescription()!=null)
            foodFromDB.setDescription(foodCreationRequest.getDescription());

        if(foodCreationRequest.getPrice()>0)
            foodFromDB.setPrice(foodCreationRequest.getPrice());

        if(foodCreationRequest.getImages()!=null)
            foodFromDB.setImages(foodCreationRequest.getImages());

        if(foodCreationRequest.getRestaurantId()!=null){
            Restaurant restaurant = restaurantRepository.findById(foodCreationRequest.getRestaurantId())
                                            .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given id: "+foodCreationRequest.getRestaurantId()));

            foodFromDB.setRestaurant(restaurant);
        }

        if(foodCreationRequest.getCategoryId()!=null)
        {
            Category category = categoryRepository.findById(foodCreationRequest.getCategoryId())
                                        .orElseThrow(() -> new ResourceNotFoundException("Category is not exists with given id:" +foodCreationRequest.getCategoryId()));

            foodFromDB.setCategory(category);
        }

        if(foodCreationRequest.getIngredients()!=null){
            List<IngredientsItem> ingredientsItems = foodCreationRequest.getIngredients().stream()
                                                            .map((item) -> modelMapper.map(item, IngredientsItem.class))
                                                                .collect(Collectors.toList());

            foodFromDB.setIngredientsItems(ingredientsItems);
        }

        foodFromDB.setAvailable(foodCreationRequest.isAvailable());
        foodFromDB.setVegetarian(foodCreationRequest.isVegetarian());
        foodFromDB.setSeasonal(foodCreationRequest.isSeasonal());

        // save to DB
        Food updatedFood = foodRepository.save(foodFromDB);

        FoodResponse foodResponse = modelMapper.map(updatedFood, FoodResponse.class);
        foodResponse.setRestaurantId(updatedFood.getRestaurant().getId());
        foodResponse.setRestaurantName(updatedFood.getRestaurant().getName());

        return foodResponse;
    }

    // This food will appear in DB but depend on any restaurants
    @Override
    public String  deleteFoodFromRestaurant(Long foodId) {

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

        if(foodCategory!=null && !foodCategory.isEmpty() && !foodCategory.equals("all"))
            foods = filterByFoodCategory(foodCategory, foods);

        return foods.stream().map(food -> {
            FoodResponse foodResponse = modelMapper.map(food, FoodResponse.class);
            foodResponse.setRestaurantId(food.getRestaurant().getId());
            foodResponse.setRestaurantName(food.getRestaurant().getName());

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

        if(keyword.isEmpty())
            return new ArrayList<FoodResponse>();

        List<Food> foods = foodRepository.searchFoodByKeyword(keyword);

        return foods.stream().map(food -> {
            FoodResponse foodResponse = modelMapper.map(food, FoodResponse.class);
            foodResponse.setRestaurantId(food.getRestaurant().getId());
            foodResponse.setRestaurantName(food.getRestaurant().getName());

            return foodResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public FoodResponse findFoodOfCurrentRestaurantById(Long foodId, UserDto userDto) {

        Restaurant restaurant = restaurantRepository.findByOwnerId(userDto.getId())
                                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given owner id: "+userDto.getId()));

        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Food is not exists with given id: "+foodId));

        if(restaurant.getFoods().contains(food))
        {
            FoodResponse foodResponse = modelMapper.map(food, FoodResponse.class);
            foodResponse.setRestaurantId(food.getRestaurant().getId());
            foodResponse.setRestaurantName(food.getRestaurant().getName());

            return foodResponse;
        }
        else
            throw new ApiException(HttpStatus.BAD_REQUEST, "There are not any food with given id: "+foodId);
    }

    @Override
    public FoodResponse findFoodById(Long foodId) {

        Food food = foodRepository.findById(foodId)
                        .orElseThrow(() -> new ResourceNotFoundException("Food is not exists with given id: "+foodId));

        FoodResponse foodResponse = modelMapper.map(food, FoodResponse.class);
        foodResponse.setRestaurantId(food.getRestaurant().getId());
        foodResponse.setRestaurantName(food.getRestaurant().getName());

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
        foodResponse.setRestaurantName(food.getRestaurant().getName());

        return foodResponse;
    }


}
