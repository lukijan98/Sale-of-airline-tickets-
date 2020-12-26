package com.lal.userservice.repository;

import com.lal.userservice.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard,String> {
    CreditCard findByCardNumber(String cardNumber);
}
