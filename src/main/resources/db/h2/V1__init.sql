CREATE SEQUENCE player_id_seq INCREMENT BY 50;

CREATE TABLE players (
    id  BIGINT DEFAULT player_id_seq.nextval PRIMARY KEY,
    sub VARCHAR(255) NOT NULL
);

CREATE SEQUENCE game_id_seq INCREMENT BY 50;

CREATE TABLE games (
    id             BIGINT DEFAULT game_id_seq.nextval PRIMARY KEY,
    player_1_id    BIGINT       NOT NULL,
    player_2_id    BIGINT       NOT NULL,
    size           INT          NOT NULL,
    winning_number INT          NOT NULL,
    winner         VARCHAR(255) NOT NULL,
    FOREIGN KEY (player_1_id) REFERENCES players (id),
    FOREIGN KEY (player_2_id) REFERENCES players (id)
);

CREATE SEQUENCE move_id_seq INCREMENT BY 50;

CREATE TABLE moves (
    id          BIGINT DEFAULT move_id_seq.nextval PRIMARY KEY,
    game_id     BIGINT NOT NULL,
    move_number INT    NOT NULL,
    x           INT    NOT NULL,
    y           INT    NOT NULL,
    player_id   BIGINT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES games (id),
    FOREIGN KEY (player_id) REFERENCES players (id)
);
