/* V4__unicode_fix.sql
   - Converte full_name/email para NVARCHAR(200)
   - Remove temporariamente constraints/índices únicos em email
   - Recria unicidade em email
*/

---------------------------------------------
-- Helper: Drop UQ ou índice único de 'email'
---------------------------------------------
CREATE OR ALTER PROCEDURE dbo.__drop_unique_on_email
    @table SYSNAME
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @objId INT = OBJECT_ID(@table);
    IF @objId IS NULL RETURN;

    -- 1) Dropar UNIQUE CONSTRAINT (se existir) que inclua 'email'
    DECLARE @uq SYSNAME;
    SELECT TOP (1) @uq = kc.name
    FROM sys.key_constraints kc
    WHERE kc.parent_object_id = @objId
      AND kc.type = 'UQ'
      AND EXISTS (
            SELECT 1
            FROM sys.index_columns ic
            JOIN sys.columns c
              ON c.object_id = ic.object_id AND c.column_id = ic.column_id
            WHERE ic.object_id = kc.parent_object_id
              AND ic.index_id  = kc.unique_index_id
              AND c.name = 'email'
      );

    IF @uq IS NOT NULL
    BEGIN
        EXEC('ALTER TABLE ' + @table + ' DROP CONSTRAINT [' + @uq + ']');
    END

    -- 2) Dropar ÍNDICE ÚNICO (se existir) que inclua 'email'
    DECLARE @idx SYSNAME;
    SELECT TOP (1) @idx = i.name
    FROM sys.indexes i
    JOIN sys.index_columns ic ON ic.object_id = i.object_id AND ic.index_id = i.index_id
    JOIN sys.columns c ON c.object_id = ic.object_id AND c.column_id = ic.column_id
    WHERE i.object_id = @objId
      AND i.is_unique = 1
      AND c.name = 'email';

    IF @idx IS NOT NULL
    BEGIN
        EXEC('DROP INDEX [' + @idx + '] ON ' + @table);
    END
END
GO

---------------------------------------------
-- users (minúsculo)
---------------------------------------------
IF OBJECT_ID('dbo.users','U') IS NOT NULL
BEGIN
    EXEC dbo.__drop_unique_on_email 'dbo.users';

    -- altera para NVARCHAR
    IF COL_LENGTH('dbo.users', 'full_name') IS NOT NULL
        ALTER TABLE dbo.users ALTER COLUMN full_name NVARCHAR(200) NOT NULL;
    IF COL_LENGTH('dbo.users', 'email') IS NOT NULL
        ALTER TABLE dbo.users ALTER COLUMN email NVARCHAR(200) NOT NULL;

    -- recria unicidade
    IF NOT EXISTS (
        SELECT 1 FROM sys.key_constraints
        WHERE parent_object_id = OBJECT_ID('dbo.users') AND name = 'UQ_users_email'
    )
        ALTER TABLE dbo.users ADD CONSTRAINT UQ_users_email UNIQUE (email);
END
GO

---------------------------------------------
-- Users (maiúsculo)
---------------------------------------------
IF OBJECT_ID('dbo.Users','U') IS NOT NULL
BEGIN
    EXEC dbo.__drop_unique_on_email 'dbo.Users';

    -- altera para NVARCHAR
    IF COL_LENGTH('dbo.Users', 'full_name') IS NOT NULL
        ALTER TABLE dbo.Users ALTER COLUMN full_name NVARCHAR(200) NOT NULL;
    IF COL_LENGTH('dbo.Users', 'email') IS NOT NULL
        ALTER TABLE dbo.Users ALTER COLUMN email NVARCHAR(200) NOT NULL;

    -- recria unicidade
    IF NOT EXISTS (
        SELECT 1 FROM sys.key_constraints
        WHERE parent_object_id = OBJECT_ID('dbo.Users') AND name = 'UQ_Users_email'
    )
        ALTER TABLE dbo.Users ADD CONSTRAINT UQ_Users_email UNIQUE (email);
END
GO

-- limpa o helper
DROP PROCEDURE IF EXISTS dbo.__drop_unique_on_email;
GO
