package ru.ravnasybullin.DoiReg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ravnasybullin.DoiReg.models.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
