IF OBJECT_ID(N'[dbo].[password_reset_tokens]', N'U') IS NULL
BEGIN
    CREATE TABLE [dbo].[password_reset_tokens] (
        [id] UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID(),
        [user_id] BIGINT NOT NULL,
        [token] NVARCHAR(120) NOT NULL,
        [expires_at] DATETIME2 NOT NULL,
        [used_at] DATETIME2 NULL,
        [created_at] DATETIME2 NOT NULL CONSTRAINT [DF_prt_created_at] DEFAULT (SYSUTCDATETIME()),
        CONSTRAINT [PK_password_reset_tokens] PRIMARY KEY ([id]),
        CONSTRAINT [UQ_password_reset_tokens_token] UNIQUE ([token]),
        CONSTRAINT [FK_prt_user] FOREIGN KEY ([user_id]) REFERENCES [dbo].[users]([id]) ON DELETE CASCADE
    );
    CREATE INDEX [IX_prt_user_expires] ON [dbo].[password_reset_tokens]([user_id], [expires_at]);
END;

DELETE FROM [dbo].[password_reset_tokens]
WHERE [expires_at] < SYSUTCDATETIME() OR [used_at] IS NOT NULL;

