package com.example.demo.Repository;
import com.example.demo.Entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

   List<Item> findAllByCategory(String Category);
   List<Item> findAllByAvailable(boolean available);

}
