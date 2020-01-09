DROP TABLE IF EXISTS organizations, employees,employees_organizations,employees_employees,organizations_organizations;

CREATE TABLE employees (
id             serial PRIMARY KEY,
name     VARCHAR(50)
);

CREATE TABLE organizations (
id             serial PRIMARY KEY,
name          VARCHAR(100) NOT NULL
);


CREATE TABLE employees_organizations (
--author_id      INT          NOT NULL,
--book_id        INT          NOT NULL,
organization_id INT NOT NULL,
employee_id INT NOT NULL,

--PRIMARY KEY (author_id, book_id),
PRIMARY KEY (organization_id, employee_id),
--CONSTRAINT fk_ab_author     FOREIGN KEY (author_id)  REFERENCES author (id)
CONSTRAINT fk_oe_o    FOREIGN KEY (organization_id)  REFERENCES organizations (id)
ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT fk_oe_e      FOREIGN KEY (employee_id)    REFERENCES employees   (id)
ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE employees_employees (
--author_id      INT          NOT NULL,
--book_id        INT          NOT NULL,
supervisor_id INT,
employee_id INT NOT NULL,

--PRIMARY KEY (author_id, book_id),
PRIMARY KEY (supervisor_id, employee_id),
CONSTRAINT fk_ee_e      FOREIGN KEY (employee_id)    REFERENCES employees   (id)
ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE organizations_organizations (
--author_id      INT          NOT NULL,
--book_id        INT          NOT NULL,
parent_id INT,
organization_id INT NOT NULL,

--PRIMARY KEY (author_id, book_id),
PRIMARY KEY (parent_id, organization_id),
CONSTRAINT fk_ee_e      FOREIGN KEY (organization_id)    REFERENCES organizations   (id)
ON UPDATE CASCADE ON DELETE CASCADE
);
