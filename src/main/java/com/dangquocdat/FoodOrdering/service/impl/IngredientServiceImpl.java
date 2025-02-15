package com.dangquocdat.FoodOrdering.service.impl;

import com.dangquocdat.FoodOrdering.dto.ingredient.IngredientCategoryDto;
import com.dangquocdat.FoodOrdering.dto.ingredient.IngredientItemDto;
import com.dangquocdat.FoodOrdering.dto.ingredient.request.IngredientCategoryCreationRequest;
import com.dangquocdat.FoodOrdering.dto.ingredient.request.IngredientItemCreationRequest;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.entity.IngredientCategory;
import com.dangquocdat.FoodOrdering.entity.IngredientsItem;
import com.dangquocdat.FoodOrdering.entity.Restaurant;
import com.dangquocdat.FoodOrdering.exception.ResourceNotFoundException;
import com.dangquocdat.FoodOrdering.repository.IngredientCategoryRepository;
import com.dangquocdat.FoodOrdering.repository.IngredientItemRepository;
import com.dangquocdat.FoodOrdering.repository.RestaurantRepository;
import com.dangquocdat.FoodOrdering.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final IngredientItemRepository ingredientItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;


    @Override
    public IngredientCategoryDto createIngredientCategory(IngredientCategoryCreationRequest ingredientCategoryCreationRequest) {

        Long restaurantId = ingredientCategoryCreationRequest.getRestaurantId();
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given id: "+restaurantId));

        IngredientCategory ingredientCategory = new IngredientCategory();
        ingredientCategory.setName(ingredientCategoryCreationRequest.getName());
        ingredientCategory.setRestaurant(restaurant);

        IngredientCategory savedIngredientCategory = ingredientCategoryRepository.save(ingredientCategory);

        IngredientCategoryDto ingredientCategoryDto = modelMapper.map(savedIngredientCategory, IngredientCategoryDto.class);

        return ingredientCategoryDto;
    }

    @Override
    public IngredientCategoryDto getIngredientCategoryById(Long id) {

        IngredientCategory ingredientCategory = ingredientCategoryRepository.findById(id)
                                                   .orElseThrow(() -> new ResourceNotFoundException("Ingredient Category is not exists with given id: "+id));

        return modelMapper.map(ingredientCategory, IngredientCategoryDto.class);
    }

    @Override
    public IngredientCategoryDto getIngredientCategoryByIdAndRestaurantId(Long id, UserDto userDto) {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given owner id: "+userDto.getId()));

        IngredientCategory ingredientCategory = ingredientCategoryRepository.findByIdAndRestaurantId(id, restaurant.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient Category is not exists with given id: "+id+" and restaurant id: "+restaurant.getId()));

        return modelMapper.map(ingredientCategory, IngredientCategoryDto.class);
    }


    @Override
    public IngredientItemDto getIngredientByIdAndRestaurantId(Long id, UserDto userDto) {

        Restaurant restaurant = restaurantRepository.findByOwnerId(userDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given owner id: "+userDto.getId()));

        IngredientsItem ingredientsItem = ingredientItemRepository.findByIdAndRestaurantId(id, restaurant.getId())
                                                .orElseThrow(() -> new ResourceNotFoundException("Ingredient item is not exists with given id: "+id+" and restaurant id: "+restaurant.getId()));

        return modelMapper.map(ingredientsItem, IngredientItemDto.class);
    }

    @Override
    public IngredientCategoryDto updateIngredientCategoryByIdAndRestaurantId(Long ingredientCategoryId, IngredientCategoryCreationRequest ingredientCategoryUpdateRequest, UserDto userDto) {

        Restaurant restaurant = restaurantRepository.findByOwnerId(userDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given owner id: "+userDto.getId()));

        IngredientCategory ingredientCategory = ingredientCategoryRepository.findByIdAndRestaurantId(ingredientCategoryId, restaurant.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient Category is not exists with given id: "+ingredientCategoryId+" and restaurant id: "+restaurant.getId()));


        if(ingredientCategoryUpdateRequest.getName()!=null)
            ingredientCategory.setName(ingredientCategoryUpdateRequest.getName());

        IngredientCategory updatedIngredientCategory = ingredientCategoryRepository.save(ingredientCategory);

        return modelMapper.map(ingredientCategory, IngredientCategoryDto.class);
    }

    @Override
    public IngredientItemDto updateIngredientItemByIdAndRestaurantId(Long ingredientItemId, IngredientItemCreationRequest ingredientItemUpdateRequest, UserDto userDto) {

        Restaurant restaurant = restaurantRepository.findByOwnerId(userDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given owner id: "+userDto.getId()));

        IngredientsItem ingredientsItem = ingredientItemRepository.findByIdAndRestaurantId(ingredientItemId, restaurant.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient item is not exists with given id: "+ingredientItemId+" and restaurant id: "+restaurant.getId()));

        if(ingredientItemUpdateRequest.getName()!=null)
            ingredientsItem.setName(ingredientItemUpdateRequest.getName());

        if(ingredientItemUpdateRequest.getCategoryId()!=null)
        {
            IngredientCategory ingredientCategory = ingredientCategoryRepository.findById(ingredientItemUpdateRequest.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ingredient Category is not exists with given id: "+ingredientItemUpdateRequest.getCategoryId()));

            ingredientsItem.setCategory(ingredientCategory);
        }

        IngredientsItem updatedIngredientItem = ingredientItemRepository.save(ingredientsItem);

        return modelMapper.map(updatedIngredientItem, IngredientItemDto.class);
    }

    @Override
    public List<IngredientCategoryDto> getIngredientCategoryByRestaurantId(Long restaurantId) {

        List<IngredientCategory> ingredientCategories = ingredientCategoryRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient Category is not exists with given restaurant id: "+restaurantId));

        return ingredientCategories.stream()
                .map(ingredientCategory -> modelMapper.map(ingredientCategory, IngredientCategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public IngredientItemDto createIngredientItem(IngredientItemCreationRequest ingredientItemCreationRequest) {

        Long ingredientCategoryId = ingredientItemCreationRequest.getCategoryId();
        Long restaurantId = ingredientItemCreationRequest.getRestaurantId();

        IngredientCategory ingredientCategory = ingredientCategoryRepository.findById(ingredientCategoryId)
                                                    .orElseThrow(() -> new ResourceNotFoundException("Ingredient Category is not exists with given id:"+ingredientCategoryId));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given id: "+restaurantId));


        IngredientsItem ingredientsItem = new IngredientsItem();
        ingredientsItem.setName(ingredientItemCreationRequest.getName());
        ingredientsItem.setRestaurant(restaurant);
        ingredientsItem.setCategory(ingredientCategory);

        IngredientsItem savedIngredientsItem = ingredientItemRepository.save(ingredientsItem);

        // KHI NÀO TEST BÊN FE NẾU GẶP LỖI THÌ XEM XÉT MỞ DÒNG NÀY ĐỂ TEST
        // ingredientCategory.getIngredients().add(savedIngredientsItem);

        return modelMapper.map(savedIngredientsItem, IngredientItemDto.class);
    }

    @Override
    public List<IngredientItemDto> getIngredientItemByRestaurantId(Long restaurantId) {

        List<IngredientsItem> ingredientsItems = ingredientItemRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient Item is not exists with given restaurant id: "+restaurantId));

        return ingredientsItems.stream()
                .map(ingredientsItem -> modelMapper.map(ingredientsItem, IngredientItemDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public IngredientItemDto updateStock(Long id) {

        IngredientsItem ingredientsItem = ingredientItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient is not exists with given id: "+id));

        ingredientsItem.setInStock(!ingredientsItem.isInStock());

        IngredientsItem savedIngredientsItem = ingredientItemRepository.save(ingredientsItem);

        return modelMapper.map(savedIngredientsItem, IngredientItemDto.class);
    }
}
