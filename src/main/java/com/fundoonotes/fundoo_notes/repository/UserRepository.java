package com.fundoonotes.fundoo_notes.repository;

import com.fundoonotes.fundoo_notes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);   //optional hme null pointer exc. se bacahata hai

    boolean existsByEmail(String email);   //Registration mein sirf check karna hai
    //    ki email exist karta hai ya nahi
    //    poora User object nahi chahiye
    //    isliye existsByEmail use karenge.
}


//User user = findByEmail("nvn@gmail.com");
//user.getName(); // ← NullPointerException agar user nahi mila!