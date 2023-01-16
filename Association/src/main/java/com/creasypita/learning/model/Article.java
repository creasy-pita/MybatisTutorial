package com.creasypita.learning.model;

import java.util.Date;

/**
 * Created by lujq on 1/16/2023.
 */
public class Article {
    private Integer id;
    private String title;
    // 一对一关系
    private Author author;
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

// 省略 getter/setter
}
