package com.example.billingservice.web;

import com.example.billingservice.entities.Bill;
import com.example.billingservice.feign.CustomerRestClient;
import com.example.billingservice.feign.ProductItemRestClient;
import com.example.billingservice.repository.BillRepository;
import com.example.billingservice.repository.ProductItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class BillRestController{

    @Autowired private BillRepository billRepository;
    @Autowired private ProductItemRepository productItemRepository;
    @Autowired private CustomerRestClient customerServiceClient;
    @Autowired private ProductItemRestClient inventoryServiceClient;
    @GetMapping("/fullBill/{id}")
    public Bill getBill(@PathVariable(name = "id") Long id){
        Bill bill ;
        bill = billRepository.findById(id).get();
        bill.setCustomer(customerServiceClient.getCustomerById(bill.getCustomerID()));
        bill.getProductItems().forEach(p->{
            p.setProduct(inventoryServiceClient.getProductById(p.getProductID()));
        });
        return bill;
    }

    @GetMapping(path = "/fullCustomerBill/{id}")
    public List<Bill> getBilLCustomer(@PathVariable(name = "id") Long id){
        List<Bill> bill;
        bill=billRepository.findByCustomerID(id);
        bill.forEach(p->{
            p=getBill(p.getId());
        });
        return bill;
    }




}
