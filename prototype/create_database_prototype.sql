use catalog;
SET FOREIGN_KEY_CHECKS = 0;
truncate table movies;
SET FOREIGN_KEY_CHECKS = 1;
insert INTO movies ( movie_name, main_actors, category, description_, time_, year_,director,price,rating, movie_link)
VALUES
( 'Moana - מואנה', 'Aulii Cravalho, Dwayne Johnson', 'Family', 'Disney animated film about a brave Polynesian girl who sets sail on a daring mission to save her people, with the help of the demigod Maui', '1:47', 2016,'Ron',40,3,'https://w27.my-cima.net/watch.php?vid=7c7d539b0'),
('Anyone But You - כל אחד מלבדך', 'Sydney Sweeney, Glen Powell', 'Romance', 'romantic comedy about two ex-lovers who must pretend to be a couple again for a wedding', '1:44', 2023,'Mias',40,9,'https://w27.my-cima.net/watch.php?vid=7c7d539b0'),
('Cruella - קרואלה', 'Emma Stone, Emma Thompson', 'Comedy', 'A stylish and rebellious origin story of Cruella de Vil, set in 1970s London amidst the punk rock revolution', '2:14', 2021,'Sami',50,6,'https://w27.my-cima.net/watch.php?vid=7c7d539b0'),
('Divergent - מפוצלים', 'Shailene Woodley, Theo James', 'Action', 'In a dystopian future, a young woman discovers she is a Divergent and must uncover the secrets behind her societys facade', '2:19', 2014,'Aisha',30,7,'https://w27.my-cima.net/watch.php?vid=7c7d539b0'),
('Spider-Man: No Way Home - ספיידרמן: אין דרך הביתה', 'Tom Holland, Zendaya, Benedict Cumberbatch', 'Action', 'Spider-Man seeks Doctor Stranges help to restore his secret identity, unleashing multiverse chaos', '2:30', 2021,'Jan',30,1, 'https://w27.my-cima.net/watch.php?vid=7c7d539b0');


truncate table worker;
INSERT INTO Worker (user_name, password, name, branch, role, is_worker_loggedIn) 
VALUES 
('MA1', 'MA1', 'John Doe', 'Sakhnin', 'Manager', 0),
('DM1', 'DM1', 'Alice Smith', 'Haifa', 'DataManager', 0),
('CS1', 'CS1', 'Bob Thompson', 'Nazareth', 'CustomerService', 0),
('DM2', 'DM2', 'Catherine Johnson', 'Nhif', 'DataManager', 0);
    
ALTER TABLE userpurchases DROP FOREIGN KEY FK5nvmah5vc4gbes38i136or0c4;

ALTER TABLE userpurchases ADD CONSTRAINT FK5nvmah5vc4gbes38i136or0c4
FOREIGN KEY (screening_id) REFERENCES screening(auto_number_screening)
ON DELETE CASCADE;

ALTER TABLE editeddetails DROP FOREIGN KEY FKaao8o7jsbr6rs8t4yeclt1nyl;

ALTER TABLE editeddetails ADD CONSTRAINT FKaao8o7jsbr6rs8t4yeclt1nyl
FOREIGN KEY (movie_id) REFERENCES movies(auto_number_movie)
ON DELETE CASCADE;
