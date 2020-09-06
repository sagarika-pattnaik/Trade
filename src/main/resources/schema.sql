create table trade(
id INT AUTO_INCREMENT  PRIMARY KEY,
tradeid varchar(10),
version INT,
counterpartyid varchar(10),
bookid varchar(10),
maturitydate timestamp(6),
createdate timestamp(6),
expired char(1)
);

