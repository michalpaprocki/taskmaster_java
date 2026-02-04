CREATE TABLE organization_tasks (
    organization_id UNIQUEIDENTIFIER NOT NULL,
    task_id UNIQUEIDENTIFIER NOT NULL,
    PRIMARY KEY(task_id, organization_id),
    CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES tasks(id),
    CONSTRAINT fk_organization FOREIGN KEY (organization_id) REFERENCES organizations(id)
);