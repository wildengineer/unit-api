
-- TODO: Add user and dates to tables

-- Products table
CREATE TABLE IF NOT EXISTS Product (id BIGINT IDENTITY PRIMARY KEY, name VARCHAR(50) NOT NULL);

-- Create Unit Definition Table
CREATE TABLE IF NOT EXISTS UnitDefinition (id BIGINT IDENTITY PRIMARY KEY, name VARCHAR(50) NOT NULL, publishDate DATETIME);

-- Create Attribute Definition Table
CREATE TABLE IF NOT EXISTS UnitAttributeDefinition (id BIGINT IDENTITY PRIMARY KEY, name VARCHAR(50) NOT NULL, valueType VARCHAR(20) NOT NULL, unitDefinitionId BIGINT NOT NULL, FOREIGN KEY (unitDefinitionId) REFERENCES UnitDefinition(id));

CREATE TABLE IF NOT EXISTS UnitAttributeConstraintDefinition (id BIGINT IDENTITY PRIMARY KEY, unitAttributeDefinitionId BIGINT NOT NULL, constraintType INT NOT NULL, upper NUMERIC, lower NUMERIC, maxLength INT, FOREIGN KEY (unitAttributeDefinitionId) REFERENCES UnitAttributeDefinition(id));
CREATE TABLE IF NOT EXISTS UnitAttributeOption (id BIGINT IDENTITY PRIMARY KEY, unitAttributeConstraintDefinitionId BIGINT NOT NULL, value VARCHAR(250), FOREIGN KEY (unitAttributeConstraintDefinitionId) REFERENCES UnitAttributeConstraintDefinition(id));

CREATE TABLE IF NOT EXISTS Unit (id BIGINT IDENTITY PRIMARY KEY, name VARCHAR(50) NOT NULL, unitDefinitionId BIGINT NOT NULL, createdDate DATETIME, FOREIGN KEY (unitDefinitionId) REFERENCES UnitDefinition(id));

CREATE TABLE IF NOT EXISTS UnitAttribute (id BIGINT IDENTITY PRIMARY KEY, name VARCHAR(50) NOT NULL, unitId BIGINT NOT NULL, valueType INT NOT NULL, numericValue DECIMAL, textValue VARCHAR(250), dateValue DATETIME, productId BIGINT, FOREIGN KEY (unitId) REFERENCES Unit(id), FOREIGN KEY (productId) REFERENCES Product(id));

CREATE TABLE IF NOT EXISTS LocationEvent (id BIGINT IDENTITY PRIMARY KEY, longitude DECIMAL NOT NULL, latitude DECIMAL NOT NULL, unitId BIGINT NOT NULL, createdDate DATETIME,  FOREIGN KEY (unitId) REFERENCES Unit(id));


