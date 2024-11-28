package com.dangquocdat.FoodOrdering.repository;

import com.dangquocdat.FoodOrdering.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
