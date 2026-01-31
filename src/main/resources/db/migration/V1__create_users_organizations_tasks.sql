CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE organizations (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner_id UUID,
    FOREIGN KEY(owner_id) REFERENCES users(id)
);

CREATE TABLE tasks (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    desciption TEXT,
    creator_id UUID,
    FOREIGN KEY(creator_id) REFERENCES users(id)
);

CREATE TABLE user_tasks (
    user_id UUID,
    task_id UUID,
    PRIMARY KEY(user_id, task_id)
);
