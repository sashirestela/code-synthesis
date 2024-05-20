DROP TABLE IF EXISTS alert; 

DROP TABLE IF EXISTS item;

CREATE TABLE IF NOT EXISTS item (
    id INT AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    description VARCHAR(60),
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS alert (
    id INT AUTO_INCREMENT,
    item_id INT,
    message VARCHAR(100),
    status VARCHAR(20) CHECK (status IN ('acknowledged', 'pending')),
    PRIMARY KEY (id),
    FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE
);
