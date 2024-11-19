package com.dangquocdat.FoodOrdering.service.impl;

import com.dangquocdat.FoodOrdering.dto.restaurant.RestaurantFavoriteDto;
import com.dangquocdat.FoodOrdering.dto.restaurant.request.RestaurantCreationRequest;
import com.dangquocdat.FoodOrdering.dto.restaurant.response.RestaurantResponse;
import com.dangquocdat.FoodOrdering.dto.user.UserDto;
import com.dangquocdat.FoodOrdering.entity.Address;
import com.dangquocdat.FoodOrdering.entity.Restaurant;
import com.dangquocdat.FoodOrdering.entity.User;
import com.dangquocdat.FoodOrdering.exception.ResourceNotFoundException;
import com.dangquocdat.FoodOrdering.repository.AddressRepository;
import com.dangquocdat.FoodOrdering.repository.RestaurantRepository;
import com.dangquocdat.FoodOrdering.repository.UserRepository;
import com.dangquocdat.FoodOrdering.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
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

    @Override
    public String deleteRestaurant(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given id:" +restaurantId));

        restaurantRepository.delete(restaurant);

        return "Delete restaurant successfully!";
    }

    @Override
    public List<RestaurantResponse> getAllRestaurants() {

        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants.stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantResponse> searchRestaurant(String keyword) {

        List<Restaurant> restaurants = restaurantRepository.searchByKeyword(keyword);

        return restaurants.stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantResponse getRestaurantById(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given id: "+restaurantId));

        return modelMapper.map(restaurant, RestaurantResponse.class);
    }

    @Override
    public RestaurantResponse getRestaurantByOwnerId(Long ownerId) {

        Restaurant restaurant = restaurantRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given owner id: "+ownerId));

        return modelMapper.map(restaurant, RestaurantResponse.class);
    }

    // Add Restaurant to List favorites restaurants of User
    @Override
    public RestaurantFavoriteDto addToFavorites(Long restaurantId, UserDto userDto) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given id: "+restaurantId));

        RestaurantFavoriteDto restaurantFavoriteDto = new RestaurantFavoriteDto();
        restaurantFavoriteDto.setId(restaurant.getId());
        restaurantFavoriteDto.setName(restaurant.getName());
        restaurantFavoriteDto.setDescription(restaurant.getDescription());
        restaurantFavoriteDto.setImages(restaurant.getImages());
        restaurantFavoriteDto.setOpen(restaurant.isOpen());

        // FE side will click toggle btn to remove or add restaurant to favorite list
        if(userDto.getFavorites().contains(restaurantFavoriteDto))
            userDto.getFavorites().remove(restaurantFavoriteDto);
        else
            userDto.getFavorites().add(restaurantFavoriteDto);

        // convert UserDto to User entity:
        User user = convertUserDtoToUser(userDto);

        // save user
        userRepository.save(user);

        return restaurantFavoriteDto;
    }

    private User convertUserDtoToUser(UserDto userDto) {
        User userInDB = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User is not exists with given id: "+userDto.getId()));
        String userPasswordInDB = userInDB.getPassword();

        User user = modelMapper.map(userDto, User.class);
        user.setPassword(userPasswordInDB);

        return user;
    }

    // update restaurant status : open / close
    @Override
    public RestaurantResponse updateRestaurantStatus(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not exists with given id: "+restaurantId));

        // toggle
        restaurant.setOpen(!restaurant.isOpen());

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        return modelMapper.map(updatedRestaurant, RestaurantResponse.class);

    }


}
