drop database if exists AM_DB_25_03;
create database AM_DB_25_03;
use AM_DB_25_03;

create table article(
                        id int(10) unsigned not null primary key auto_increment,
                        regDate datetime not null,
                        updateDate datetime not null,
                        title char(100) not null,
                        `body` text not null
);

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목1',
`body` = '내용1';

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목2',
`body` = '내용2';

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목3',
`body` = '내용3';

select *
from article
order by id desc;

select now();

select '제목1';

select concat('제목',' 1');

select substring(RAND() * 1000 from 1 for 2);


insert into article
set regDate = now(),
updateDate = now(),
title = concat('제목',substring(RAND() * 1000 from 1 for 2)),
`body` = concat('내용',substring(RAND() * 1000 from 1 for 2));

update article
set updateDate = now(),
    title = 'title1',
    `body` = 'body1'
where id = 3;

update article
set updateDate = now(),
    `body` = 'body1'
where id = 1;

select * from article;

select count(*)
from article
where id = 5;

UPDATE article
SET updateDate = NOW(),
    title = 'title1',
    `body` = 'body1'
WHERE id = 5;

USE AM_db_25_03;

SHOW TABLES;

CREATE TABLE `member`(
    id INT(10) UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(100) NOT NULL UNIQUE,
    loginPw CHAR(100) NOT NULL,
    `name` CHAR(100) NOT NULL
);

SELECT *
FROM `member`;