CREATE TABLE memberships (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    user_id UNIQUEIDENTIFIER NOT NULL,
    organization_id UNIQUEIDENTIFIER NOT NULL,
    role VARCHAR(20) NOT NULL,
    joined_at DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME(),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_org FOREIGN KEY (organization_id) REFERENCES organizations(id),
    CONSTRAINT uc_user_org UNIQUE (user_id, organization_id)
);

