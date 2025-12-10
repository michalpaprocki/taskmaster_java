CREATE TABLE memberships (
    id BIGINT IDENTITY PRIMARY KEY,
    user_id UUID NOT NULL,
    organization_id UUID NOT NULL,
    role VARCHAR(20) NOT NULL,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_org FOREIGN KEY (organization_id) REFERENCES organizations(id),
    CONSTRAINT uc_user_org UNIQUE (user_id, organization_id)
);

DROP TABLE user_organizations;

ALTER TABLE organizations DROP COLUMN owner_id;


EXEC sp_rename 'tasks.master', 'creator', 'COLUMN';