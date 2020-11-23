SP_RENAME CODE_TEMPLATE, OLD_CODE_TEMPLATE

CREATE TABLE CODE_TEMPLATE_LIBRARY (
	ID NVARCHAR(255) NOT NULL PRIMARY KEY,
	NAME NVARCHAR(255) NOT NULL UNIQUE,
	REVISION INTEGER,
	LIBRARY NVARCHAR(MAX)
)

CREATE TABLE CODE_TEMPLATE (
	ID NVARCHAR(255) NOT NULL PRIMARY KEY,
	NAME NVARCHAR(255) NOT NULL,
	REVISION INTEGER,
	CODE_TEMPLATE NVARCHAR(MAX)
)