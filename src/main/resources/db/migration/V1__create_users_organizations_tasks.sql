CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE
);

CREATE TABLE organizations (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    owner_id UUID,
    FOREIGN KEY(owner_id) REFERENCES users(id)
);

CREATE TABLE tasks (
    id UUID PRIMARY KEY,
    title VARCHAR(255),
    desciption TEXT,
    master_id UUID,
    FOREIGN KEY(master_id) REFERENCES users(id)
);

CREATE TABLE user_tasks (
    user_id UUID,
    task_id UUID,
    PRIMARY KEY(user_id, task_id)
);
