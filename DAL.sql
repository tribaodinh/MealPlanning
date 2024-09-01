use mealplanning;
#username: root
#password: [YOURPASSWORD]

DROP PROCEDURE IF EXISTS getCookBooks;
DELIMITER $$
CREATE PROCEDURE getCookBooks ()
	BEGIN
		SELECT CookbookName FROM CookBook;
	END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS makeWeek;
DELIMITER $$
CREATE PROCEDURE makeWeek()
BEGIN
	CREATE TABLE IF NOT EXISTS TheWeek
    (
		days char(100) ,
		thefood varchar(100),
        primary key (days)
    );
    INSERT INTO TheWeek(days ,thefood) VALUE ('monday', 'none');
    INSERT INTO TheWeek(days ,thefood) VALUE ('tuesday', 'none');
    INSERT INTO TheWeek(days ,thefood) VALUE ('wednesday', 'none');
    INSERT INTO TheWeek(days ,thefood) VALUE ('thursday', 'none');
    INSERT INTO TheWeek(days ,thefood) VALUE ('friday', 'none');
    INSERT INTO TheWeek(days ,thefood) VALUE ('saturday', 'none');
    INSERT INTO TheWeek(days ,thefood) VALUE ('sunday', 'none');
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS RemoveWeek;
DELIMITER $$
CREATE PROCEDURE removeWeek()
BEGIN
	DROP TABLE IF EXISTS TheWeek;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS showWeek;
DELIMITER $$
CREATE PROCEDURE showWeek()
BEGIN
	select * 
    from theweek
order by
	CASE
		WHEN Days = 'Monday' THEN 1
		WHEN Days = 'Tuesday' THEN 2
		WHEN Days = 'Wednesday' THEN 3
		WHEN Days = 'Thursday' THEN 4
		WHEN Days = 'Friday' THEN 5
		WHEN Days = 'Saturday' THEN 6
        WHEN Days = 'Sunday' THEN 7
        END ASC;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS FoodToDay;
DELIMITER $$
CREATE PROCEDURE FoodToDay(IN DayOfWeek char(100),IN food varchar(100))
	begin
        SET @a = concat("UPDATE TheWeek Set thefood =  '",food,"' WHERE days = '", DayOfWeek,"'");
        prepare stmt FROM @a;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
	END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS getRecipesFromBook;
DELIMITER $$
CREATE PROCEDURE getRecipesFromBook(in book varchar(200)) 
	BEGIN
		SELECT RecipeName FROM recipe WHERE CookBookName = book;
    END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS getIngredientsFromRecipe;
DELIMITER $$
CREATE PROCEDURE getIngredientsFromRecipe(in myRecipe varchar(100))
BEGIN
	SELECT i.IngredientName
	From meal as m
	INNER JOIN ingredients as i on m.IngredientId = i.Id
	Where m.RecipeName = myRecipe;
END $$
DELIMITER ;


