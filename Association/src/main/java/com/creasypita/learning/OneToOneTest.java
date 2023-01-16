package com.creasypita.learning;

import com.creasypita.learning.Dao.ArticleDao;
import com.creasypita.learning.model.Article;
import com.creasypita.learning.model.Author;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lujq on 1/16/2023.
 */
public class OneToOneTest {
    public static void main(String[] args) {
        String resource = "com/creasypita/learning/mybatis-config.xml";
        InputStream inputStream = null;
        SqlSessionFactory sqlSessionFactory = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);

            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSession session = sqlSessionFactory.openSession();
        try {
            ArticleDao articleDao = session.getMapper(ArticleDao.class);
            Article article = articleDao.findOne(1);

            Author author = article.getAuthor();
            article.setAuthor(null);

            System.out.println("\narticles info:");
            System.out.println(article);

            System.out.println("\nauthor info:");
            System.out.println(author);
        } finally {
            session.close();
        }
    }
}
