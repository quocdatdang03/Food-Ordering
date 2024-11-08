package com.dangquocdat.FoodOrdering.service.impl;

import com.dangquocdat.FoodOrdering.dto.restaurant.request.RestaurantCreationRequest;
import com.dangquocdat.FoodOrdering.dto.restaurant.response.RestaurantResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.entity.Address;
import com.dangquocdat.FoodOrdering.entity.Restaurant;
import com.dangquocdat.FoodOrdering.entity.User;
import com.dangquocdat.FoodOrdering.exception.ResourceNotFoundException;
import com.dangquocdat.FoodOrdering.repository.AddressRepository;
import com.dangquocdat.FoodOrdering.repository.RestaurantRepository;
import com.dangquocdat.FoodOrdering.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Override
    public RestaurantResponse createRestaurant(RestaurantCreationRequest restaurantCreationRequest, UserDto owner) {

        Address savedAddress = addressRepository.save(restaurantCreationRequest.getAddress());

        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantCreationRequest.getName());
        restaurant.setDescription(restaurantCreationRequest.getDescription());
        restaurant.setCuisineType(restaurantCreationRequest.getCuisineType());
        restaurant.setContactInformation(restaurantCreationRequest.getContactInformation());
        restaurant.setOpeningHours(restaurantCreationRequest.getOpeningHours());
        restaurant.setImages(restaurantCreationRequest.getImages());
        restaurant.setAddress(savedAddress);
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setOpen(false);
        restaurant.setOwner(modelMapper.map(owner, User.class));

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return modelMapper.map(savedRestaurant, RestaurantResponse.class);
    }

    @Override
    public RestaurantResponse updateRestaurant(Long restaurantId, RestaurantCreationRequest restaurantRequest) {

        Restaurant restaurantFromDB = restaurantRepository.findById(restaurantId)
                                        .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given id:" +restaurantId));

        if(restaurantRequest.getName()!=null)
            restaurantFromDB.setName(restaurantRequest.getName());

        if(restaurantRequest.getDescription()!=null)
            restaurantFromDB.setDescription(restaurantRequest.getDescription());

        if(restaurantRequest.getCuisineType()!=null)
            restaurantFromDB.setCuisineType(restaurantRequest.getCuisineType());

        if(restaurantRequest.getContactInformation()!=null)
            restaurantFromDB.setContactInformation(restaurantRequest.getContactInformation());

        if(restaurantRequest.getOpeningHours()!=null)
            restaurantFromDB.setOpeningHours(restaurantRequest.getOpeningHours());

        if(restaurantRequest.getImages()!=null)
            restaurantFromDB.setImages(restaurantRequest.getImages());

        if(restaurantRequest.getAddress()!=null)
            restaurantFromDB.setAddress(restaurantRequest.getAddress());

        Restaurant updatedRestaurant = restaurantRepository.save(restaurantFromDB);


        return modelMapper.map(updatedRestaurant, RestaurantResponse.class);
    }

}
