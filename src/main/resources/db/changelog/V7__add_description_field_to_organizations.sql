ALTER TABLE organizations
ADD description NVARCHAR(255) DEFAULT ''
ALTER TABLE organizations
ADD created_at DATETIME2(6) NOT NULL CONSTRAINT df_organizations_created_at DEFAULT SYSUTCDATETIME()
ALTER TABLE organizations
ADD updated_at DATETIME2(6) NOT NULL CONSTRAINT df_your_table_updated_at DEFAULT SYSUTCDATETIME()