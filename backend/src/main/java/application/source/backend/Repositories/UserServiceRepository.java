package application.source.backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import application.source.backend.Entities.UserEntity;

@Repository
public interface UserServiceRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username);
}
