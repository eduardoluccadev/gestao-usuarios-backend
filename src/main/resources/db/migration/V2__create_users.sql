-- V2: criar tabelas de usuários e papéis (roles)
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[users]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[users] (
        id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        email VARCHAR(255) NOT NULL,
        full_name VARCHAR(255) NOT NULL,
        is_active BIT NOT NULL,
        password_hash VARCHAR(255) NOT NULL
    );
END;

-- Único por email
IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes i
    JOIN sys.objects o ON i.object_id = o.object_id
    WHERE o.name = 'users' AND i.name = 'UK_users_email'
)
BEGIN
    CREATE UNIQUE INDEX UK_users_email ON [dbo].[users](email);
END;

-- Tabela de papéis do usuário
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[user_roles]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[user_roles] (
        user_id BIGINT NOT NULL,
        role    VARCHAR(255) NOT NULL
    );
END;

-- FK user_roles -> users
IF NOT EXISTS (
    SELECT 1
    FROM sys.foreign_keys
    WHERE name = 'FK_user_roles_users'
)
BEGIN
    ALTER TABLE [dbo].[user_roles]
      ADD CONSTRAINT FK_user_roles_users
      FOREIGN KEY (user_id) REFERENCES [dbo].[users](id);
END;
