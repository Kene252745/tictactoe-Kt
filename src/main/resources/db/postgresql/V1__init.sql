CREATE SEQUENCE player_id_seq INCREMENT BY 50;

CREATE TABLE players (
    id  BIGINT PRIMARY KEY DEFAULT NEXTVAL('player_id_seq'),
    sub TEXT UNIQUE NOT NULL
);

CREATE TYPE WINNER AS ENUM ('NONE', 'PLAYER_1', 'PLAYER_2', 'TIE');

CREATE SEQUENCE game_id_seq INCREMENT BY 50;

CREATE TABLE games (
    id             BIGINT PRIMARY KEY DEFAULT NEXTVAL('game_id_seq'),
    player_1_id    BIGINT REFERENCES players (id) NOT NULL,
    player_2_id    BIGINT REFERENCES players (id) NOT NULL,
    size           INT                            NOT NULL,
    winning_number INT                            NOT NULL,
    winner         WINNER                         NOT NULL
);

CREATE SEQUENCE move_id_seq INCREMENT BY 50;

CREATE TABLE moves (
    id          BIGINT PRIMARY KEY DEFAULT NEXTVAL('move_id_seq'),
    game_id     BIGINT REFERENCES games (id)   NOT NULL,
    move_number INT                            NOT NULL,
    x           INT                            NOT NULL,
    y           INT                            NOT NULL,
    player_id   BIGINT REFERENCES players (id) NOT NULL
);
