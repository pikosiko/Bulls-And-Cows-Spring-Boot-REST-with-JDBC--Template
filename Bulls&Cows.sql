DROP DATABASE IF EXISTS BullsCows;
CREATE DATABASE BullsCows;

use BullsCows;



CREATE TABLE game(
game_id INT PRIMARY KEY AUTO_INCREMENT,
numberOfGuesses int, 
answer varchar(4) default null,
isWon BOOLEAN default false);

use BullsCows;

select * from game;

