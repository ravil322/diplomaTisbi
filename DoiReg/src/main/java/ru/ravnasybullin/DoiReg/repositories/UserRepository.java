package ru.ravnasybullin.DoiReg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ravnasybullin.DoiReg.models.Article;
import ru.ravnasybullin.DoiReg.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
}
