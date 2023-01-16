package com.creasypita.learning.Dao;

import com.creasypita.learning.model.Article;
import com.creasypita.learning.model.Author;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lujq on 1/16/2023.
 */
public interface ArticleDao {
    Article findOne(@Param("id") int id);
    Author findAuthor(@Param("id") int authorId);
}
