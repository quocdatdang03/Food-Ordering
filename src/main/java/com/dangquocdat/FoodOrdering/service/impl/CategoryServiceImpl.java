package com.dangquocdat.FoodOrdering.service.impl;

import com.dangquocdat.FoodOrdering.dto.category.request.CategoryCreationRequest;
import com.dangquocdat.FoodOrdering.dto.category.CategoryDto;
import com.dangquocdat.FoodOrdering.entity.Category;
import com.dangquocdat.FoodOrdering.entity.Restaurant;
import com.dangquocdat.FoodOrdering.exception.ResourceNotFoundException;
import com.dangquocdat.FoodOrdering.repository.CategoryRepository;
import com.dangquocdat.FoodOrdering.repository.RestaurantRepository;
import com.dangquocdat.FoodOrdering.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryCreationRequest categoryCreationRequest, Long ownerId) {

        // get restaurant of owner
        Restaurant restaurant = restaurantRepository.findByOwnerId(ownerId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given owner id: "+ownerId));

        Category category = new Category();
        category.setName(categoryCreationRequest.getName());
        category.setRestaurant(restaurant);

        Category savedCategory = categoryRepository.save(category);

        CategoryDto categoryDto = modelMapper.map(savedCategory, CategoryDto.class);
        categoryDto.setRestaurantId(restaurant.getId());

        return categoryDto;
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                                .orElseThrow(() -> new ResourceNotFoundException("Category is not exists with given id: "+categoryId));

        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        categoryDto.setRestaurantId(category.getId());

        return categoryDto;
    }

    @Override
    public List<CategoryDto> getCategoriesByRestaurantId(Long restaurantId) {

        List<Category> categories = categoryRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Category is not exists with given restaurant id: "+restaurantId));

        return categories.stream().map(category -> {

            CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
            categoryDto.setRestaurantId(restaurantId);

            return categoryDto;
        }).collect(Collectors.toList());
    }
}
