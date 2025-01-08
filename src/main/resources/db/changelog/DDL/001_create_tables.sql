
CREATE TABLE role
(
    id      VARCHAR(64) PRIMARY KEY,
    payload jsonb
);

CREATE TABLE IF NOT EXISTS  USERS
(
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR(256),
    password VARCHAR(256) NOT NULL,
    msisdn   VARCHAR(50)  NOT NULL
);

CREATE TABLE user_role
(
    user_id BIGINT       NOT NULL REFERENCES users,
    role_id VARCHAR(256) NOT NULL REFERENCES role,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS TODO
(
    ID                 BIGSERIAL PRIMARY KEY,
    CREATED            timestamp not null,
    DATE_OF_COMPLETION timestamp,
    USER_ID            BIGINT,
    COMMENTS           varchar(4000),
    CONSTRAINT USERS_FK FOREIGN KEY (USER_ID) REFERENCES USERS (ID)
);


COMMENT ON TABLE TODO IS 'Список дел или желаний';
COMMENT ON COLUMN TODO.ID IS 'ID желания';

INSERT INTO role (id, payload) VALUES ('ADMIN', '["VIEW", "MODIFY", "ADD"]')
    ON CONFLICT (id) DO UPDATE SET payload = EXCLUDED.payload;

INSERT INTO role (id, payload) VALUES ('USER', '["VIEW", "MODIFY"]')
    ON CONFLICT (id) DO UPDATE SET payload = EXCLUDED.payload;

insert into users (email, password, msisdn) VALUES ('test1@domain.org', '$2a$10$NuhVAagRra06oG92xUxH6OH/STNWE6wQH2jrEhI7NyXESlXsxepu2', '79885853788');

INSERT INTO public.user_role (user_id, role_id)
VALUES (1, 'ADMIN');