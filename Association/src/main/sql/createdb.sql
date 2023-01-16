CREATE TABLE author (
                        id                INT ,
                        name          VARCHAR(255) NOT NULL,
                        age                INT,
                        sex                INT,
                        email             VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
);

CREATE TABLE Article (
                         id                INT ,
                         title          VARCHAR(255) NOT NULL,
                         author_id                INT,
                         content             VARCHAR(255) NOT NULL,
                         PRIMARY KEY (id)
);