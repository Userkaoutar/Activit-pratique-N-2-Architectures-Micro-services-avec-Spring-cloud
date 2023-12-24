package com.example.billingservice.repository;

import com.example.billingservice.entities.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


import java.util.List;

@RepositoryRestResource
public interface ProductItemRepository extends
        JpaRepository<ProductItem,Long> {

  public   List<ProductItem> findByBillId(Long billID);

}
