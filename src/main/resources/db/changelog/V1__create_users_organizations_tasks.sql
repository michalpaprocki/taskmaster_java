CREATE TABLE users (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE organizations (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner_id UNIQUEIDENTIFIER,
    FOREIGN KEY(owner_id) REFERENCES users(id)
);

CREATE TABLE tasks (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    creator_id UNIQUEIDENTIFIER,
    FOREIGN KEY(creator_id) REFERENCES users(id)
);

CREATE TABLE user_tasks (
    user_id UNIQUEIDENTIFIER,
    task_id UNIQUEIDENTIFIER,
    PRIMARY KEY(user_id, task_id)
);
