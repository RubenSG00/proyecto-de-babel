-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO authorities(id,authority) VALUES (1,'ADMIN');
INSERT INTO appusers(id,username,password,authority) VALUES (1,'admin1','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS',1);

-- Ten player users, named player1 with passwor 0wn3r
INSERT INTO authorities(id,authority) VALUES (2,'PLAYER');
INSERT INTO appusers(id,username,password,authority,rating) VALUES (4,'player1','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,1500);
INSERT INTO appusers(id,username,password,authority,rating) VALUES (5,'player2','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,1550);
INSERT INTO appusers(id,username,password,authority,rating) VALUES (6,'player3','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,1600);
INSERT INTO appusers(id,username,password,authority,rating) VALUES (7,'player4','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,1450);
INSERT INTO appusers(id,username,password,authority,rating) VALUES (8,'player5','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,1700);
INSERT INTO appusers(id,username,password,authority,rating) VALUES (9,'player6','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,1750);
INSERT INTO appusers(id,username,password,authority,rating) VALUES (10,'player7','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,1800);
INSERT INTO appusers(id,username,password,authority,rating) VALUES (11,'player8','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,1400);
INSERT INTO appusers(id,username,password,authority,rating) VALUES (12,'player9','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,1650);
INSERT INTO appusers(id,username,password,authority,rating) VALUES (13,'player10','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,1900);




-- Match 15: partida ya existente, usada de ejemplo y por el desafío 1 (finalizada, gana el blanco)
INSERT INTO chess_board (id, white_turn, jaque, current_turn_start) VALUES (15, 0, 0, null);
INSERT INTO chess_match(id, name, white_id, black_id, winner_id, board_id, type, start, finish, turn_duration) VALUES
                        (15, 'new', 4, 5, 4, 15, 0, '2026-07-01 09:00:00', '2026-07-01 09:20:00', 600);

INSERT INTO Position(id, x, y) VALUES
    (1, 1, 1),
    (2, 2, 1),
    (3, 3, 1),
    (4, 4, 1),
    (5, 5, 1),
    (6, 6, 1),
    (7, 7, 1),
    (8, 8, 1),
    (9, 1, 2),
    (10, 2, 2),
    (11, 3, 2),
    (12, 4, 2),
    (13, 5, 2),
    (14, 6, 2),
    (15, 7, 2),
    (16, 8, 2),
    (17, 1, 3),
    (18, 2, 3),
    (19, 3, 3),
    (20, 4, 3),
    (21, 5, 3),
    (22, 6, 3),
    (23, 7, 3),
    (24, 8, 3),
    (25, 1, 4),
    (26, 2, 4),
    (27, 3, 4),
    (28, 4, 4),
    (29, 5, 4),
    (30, 6, 4),
    (31, 7, 4),
    (32, 8, 4),
    (33, 1, 5),
    (34, 2, 5),
    (35, 3, 5),
    (36, 4, 5),
    (37, 5, 5),
    (38, 6, 5),
    (39, 7, 5),
    (40, 8, 5),
    (41, 1, 6),
    (42, 2, 6),
    (43, 3, 6),
    (44, 4, 6),
    (45, 5, 6),
    (46, 6, 6),
    (47, 7, 6),
    (48, 8, 6),
    (49, 1, 7),
    (50, 2, 7),
    (51, 3, 7),
    (52, 4, 7),
    (53, 5, 7),
    (54, 6, 7),
    (55, 7, 7),
    (56, 8, 7),
    (57, 1, 8),
    (58, 2, 8),
    (59, 3, 8),
    (60, 4, 8),
    (61, 5, 8),
    (62, 6, 8),
    (63, 7, 8),
    (64, 8, 8);

INSERT INTO piece_type (name, points) VALUES
  ('KING',   1000),
  ('QUEEN',  9),
  ('ROOK',   5),
  ('BISHOP', 3),
  ('KNIGHT', 3),
  ('PAWN',   1);

INSERT INTO Piece(id, type_name, color, position_id, board_id) VALUES
    (123, 'ROOK', 'WHITE', 1, 15),
    (124, 'KNIGHT', 'WHITE', 9, 15),
    (125, 'BISHOP', 'WHITE', 17, 15),
    (126, 'QUEEN', 'WHITE', 25, 15),
    (127, 'KING', 'WHITE', 33, 15),
    (128, 'BISHOP', 'WHITE', 41, 15),
    (129, 'KNIGHT', 'WHITE', 49, 15),
    (130, 'ROOK', 'WHITE', 57, 15),
    (131, 'PAWN', 'WHITE', 2, 15),
    (132, 'PAWN', 'WHITE', 10, 15),
    (133, 'PAWN', 'WHITE', 18, 15),
    (134, 'PAWN', 'WHITE', 26, 15),
    (135, 'PAWN', 'WHITE', 34, 15),
    (136, 'PAWN', 'WHITE', 42, 15),
    (137, 'PAWN', 'WHITE', 50, 15),
    (138, 'PAWN', 'WHITE', 58, 15),
    (139, 'ROOK', 'BLACK', 8, 15),
    (140, 'KNIGHT', 'BLACK', 16, 15),
    (141, 'BISHOP', 'BLACK', 24, 15),
    (142, 'QUEEN', 'BLACK', 32, 15),
    (143, 'KING', 'BLACK', 40, 15),
    (144, 'BISHOP', 'BLACK', 48, 15),
    (145, 'KNIGHT', 'BLACK', 56, 15),
    (146, 'ROOK', 'BLACK', 64, 15),
    (147, 'PAWN', 'BLACK', 7, 15),
    (148, 'PAWN', 'BLACK', 15, 15),
    (149, 'PAWN', 'BLACK', 23, 15),
    (150, 'PAWN', 'BLACK', 31, 15),
    (151, 'PAWN', 'BLACK', 39, 15),
    (152, 'PAWN', 'BLACK', 47, 15),
    (153, 'PAWN', 'BLACK', 55, 15),
    (154, 'PAWN', 'BLACK', 63, 15);

-- Match 20: segunda partida del desafío 1, finalizada en tablas (sin winner)
INSERT INTO chess_board (id, white_turn, current_turn_start, jaque) VALUES (20, true, CURRENT_TIMESTAMP(), false);
INSERT INTO chess_match (id, name, white_id, black_id, winner_id, board_id, type, start, finish, turn_duration)
VALUES (20, 'Revancha', 4, 5, NULL, 20, 0, '2026-07-05 09:00:00', '2026-07-05 09:25:00', 600);

-- Match 21: partida del desafío 2, todavía sin finalizar
INSERT INTO chess_board (id, white_turn, current_turn_start, jaque) VALUES (21, true, CURRENT_TIMESTAMP(), false);
INSERT INTO chess_match (id, name, white_id, black_id, winner_id, board_id, type, start, finish, turn_duration)
VALUES (21, 'Primera partida de agosto', 5, 7, NULL, 21, 0, '2026-08-03 10:00:00', NULL, 600);

-- Matches 22-24: usadas solo por el desafío 3, para cubrir en el Test 6 varias
-- casuísticas de una vez (victoria+empate acumulados, derrota explícita a 0
-- puntos, y una partida sin finalizar que no debe contar):
INSERT INTO chess_board (id, white_turn, current_turn_start, jaque) VALUES (22, true, CURRENT_TIMESTAMP(), false);
INSERT INTO chess_match (id, name, white_id, black_id, winner_id, board_id, type, start, finish, turn_duration)
VALUES (22, 'Desafio3 - partida 1', 4, 6, 4, 22, 0, '2026-07-10 09:00:00', '2026-07-10 09:20:00', 600);

INSERT INTO chess_board (id, white_turn, current_turn_start, jaque) VALUES (23, true, CURRENT_TIMESTAMP(), false);
INSERT INTO chess_match (id, name, white_id, black_id, winner_id, board_id, type, start, finish, turn_duration)
VALUES (23, 'Desafio3 - partida 2', 4, 7, NULL, 23, 0, '2026-07-11 09:00:00', '2026-07-11 09:25:00', 600);

INSERT INTO chess_board (id, white_turn, current_turn_start, jaque) VALUES (24, true, CURRENT_TIMESTAMP(), false);
INSERT INTO chess_match (id, name, white_id, black_id, winner_id, board_id, type, start, finish, turn_duration)
VALUES (24, 'Desafio3 - partida 3 (sin acabar)', 6, 7, NULL, 24, 0, '2026-07-12 09:00:00', NULL, 600);

-- ==========================================================
-- FriendlyChallenge: ya viene mapeada y con datos de ejemplo
-- ==========================================================
INSERT INTO friendly_challenge (id, start_date, end_date, total_matches, completed_matches) VALUES
    (1, '01/07/2026', '31/07/2026', 5, 2);
INSERT INTO friendly_challenge (id, start_date, end_date, total_matches, completed_matches) VALUES
    (2, '01/08/2026', '31/08/2026', 3, 0);
INSERT INTO friendly_challenge (id, start_date, end_date, total_matches, completed_matches) VALUES
    (3, '10/07/2026', '20/07/2026', 3, 2);

INSERT INTO friendly_challenge_matches (friendly_challenge_id, chess_match_id) VALUES
    (1, 15),
    (1, 20),
    (2, 21),
    (3, 22),
    (3, 23),
    (3, 24);

INSERT INTO friendly_challenge_participants (friendly_challenge_id, user_id) VALUES
    (1, 4),
    (1, 5),
    (2, 5),
    (2, 6),
    (2, 7),
    (3, 4),
    (3, 6),
    (3, 7);

-- Nótese que, deliberadamente, no se insertan filas de ChallengeAnnouncement,
-- AnnouncementResponse ni de su tabla intermedia de recipients: esa es la parte
-- del script de inicialización que corresponde completar en los Tests 3 y 4.



-- ==========================================================
-- Block 0 (mock exam), Test 3: announcements and responses.
-- Inserted after the challenges and users they reference.
-- ==========================================================
INSERT INTO challenge_announcement (id, message, status, sent_at, challenge_id) VALUES
    (1, 'Remember to submit your match results by Friday', 'SENT', '2026-07-01 10:00:00', 1),
    (2, 'New format proposal for next season', 'DRAFT', NULL, 1),
    (3, 'Please confirm your availability for August', 'SENT', '2026-08-02 09:30:00', 2);

INSERT INTO announcement_response (id, message, status, responded_at, announcement_id, recipient_id) VALUES
    (1, 'Got it, will submit by Friday', 'ACCEPTED', '2026-07-01 11:00:00', 1, 4),
    (2, 'Sounds good to me', 'ACCEPTED', '2026-08-02 10:00:00', 3, 5),
    (3, 'I can''t make it in August', 'DECLINED', '2026-08-02 12:00:00', 3, 6);

-- Block 0, Test 4: recipients. Default join table of the unidirectional @ManyToMany
-- "recipients": <owner table>_<attribute>(<owner table>_id, <attribute>_id).
INSERT INTO challenge_announcement_recipients (challenge_announcement_id, recipients_id) VALUES
    (1, 4),
    (1, 5),
    (2, 4),
    (2, 5),
    (3, 5),
    (3, 6),
    (3, 7);

-- ==========================================================
-- Block A (gym peer-training programs): given data.
-- Sessions 1-8 and programs 1-3. Users: 4..11 (player1..player8).
-- ==========================================================
INSERT INTO training_session (id, start, finish, duration_minutes, goal_achieved, coach_id, trainee_id) VALUES
    (1, '2026-09-01 08:00:00', '2026-09-01 09:00:00', 60, true, 6, 4),
    (2, '2026-09-02 08:00:00', '2026-09-02 08:45:00', 45, true, 4, 5),
    (3, '2026-09-03 08:00:00', '2026-09-03 09:00:00', 60, false, 5, 6),
    (4, '2026-09-04 08:00:00', NULL, 60, true, 7, 4),
    (5, '2026-09-05 10:00:00', NULL, 30, NULL, 5, 8),
    (6, '2026-10-01 18:00:00', '2026-10-01 19:00:00', 60, true, 10, 11),
    (7, '2026-10-02 18:00:00', '2026-10-02 18:50:00', 50, false, 11, 9),
    (8, '2026-10-03 18:00:00', NULL, 60, true, 9, 4);

INSERT INTO training_program (id, name, start_date, end_date, planned_sessions) VALUES
    (1, 'Strength basics', '2026-09-01', '2026-09-30', 4),
    (2, 'Mobility', '2026-09-05', '2026-10-05', 3),
    (3, 'Endurance', '2026-10-01', '2026-10-31', 3);

INSERT INTO training_program_sessions (training_program_id, training_session_id) VALUES
    (1, 1), (1, 2), (1, 3), (1, 4),
    (2, 5),
    (3, 6), (3, 7), (3, 8);

INSERT INTO training_program_members (training_program_id, user_id) VALUES
    (1, 4), (1, 5), (1, 6), (1, 7),
    (2, 5), (2, 8),
    (3, 4), (3, 9), (3, 10), (3, 11);

-- Block A - A3: notices and acknowledgements (after the programs and users they reference)
INSERT INTO program_notice (id, text, priority, published_at, read_count, program_id) VALUES
    (1, 'Bring your own towel from Monday', 'NORMAL', '2026-09-01 08:00:00', 12, 1),
    (2, 'Leg day moved to Thursday', 'URGENT', '2026-09-03 18:30:00', 30, 1),
    (3, 'Draft: new stretching routine', 'LOW', NULL, 0, 2);

INSERT INTO notice_acknowledgement (id, comment, status, acknowledged_at, notice_id, member_id, delegate_id) VALUES
    (1, 'Thanks, noted', 'CONFIRMED', '2026-09-01 09:15:00', 1, 4, NULL),
    (2, NULL, 'SEEN', '2026-09-03 19:00:00', 2, 5, NULL),
    (3, 'I can''t make it on Thursday', 'REJECTED', '2026-09-03 20:45:00', 2, 6, 4);

-- Block A - A4: targets of the notices (default join table of the @ManyToMany "targets")
INSERT INTO program_notice_targets (program_notice_id, targets_id) VALUES
    (1, 4), (1, 5),
    (2, 4), (2, 5), (2, 6),
    (3, 5), (3, 8);

-- ==========================================================
-- Block X - given data of the starting state (X03-X08).
-- ==========================================================
INSERT INTO concert (id, title, city, date, price, sold_out, genre) VALUES
    (1, 'Rock Night', 'Seville', '2026-11-05', 30, false, 'ROCK'),
    (2, 'Jazz at Dawn', 'Madrid', '2026-11-10', 45, true, 'JAZZ'),
    (3, 'Pop Parade', 'Seville', '2026-12-01', 25, false, 'POP'),
    (4, 'Classical Evenings', 'Valencia', '2026-12-15', 60, false, 'CLASSICAL'),
    (5, 'Rock Legends', 'Madrid', '2027-01-20', 80, true, 'ROCK'),
    (6, 'Smooth Jazz', 'Seville', '2027-02-14', 40, false, 'JAZZ'),
    (7, 'Pop Hits Live', 'Bilbao', '2026-10-30', 20, false, 'POP'),
    (8, 'The Rock Opera', 'Seville', '2026-11-05', 50, true, 'CLASSICAL');

INSERT INTO band (id, name, city, founded_year) VALUES
    (1, 'The Riffs', 'Seville', 1998),
    (2, 'Blue Notes', 'Seville', 2005),
    (3, 'Night Owls', 'Madrid', 2010),
    (4, 'Paper Planes', 'Valencia', 2018);

INSERT INTO musician (id, name, instrument, salary, agent) VALUES
    (1, 'Ana Torres', 'guitar', 2000, 'Agency One'),
    (2, 'Bruno Diaz', 'drums', 1500, NULL),
    (3, 'Carla Ruiz', 'guitar', 3000, 'Star Agents'),
    (4, 'Diego Lopez', 'bass', 1500, NULL),
    (5, 'Elena Gil', 'piano', 4000, 'Agency One'),
    (6, 'Ana Belen', 'voice', 2500, NULL),
    (7, 'Fabio Moreno', 'guitar', 1000, NULL);

INSERT INTO musician_bands (musician_id, bands_id) VALUES
    (1, 1),
    (2, 1), (2, 3),
    (3, 1), (3, 2),
    (4, 3),
    (5, 2);

INSERT INTO workshop (id, name, capacity, enrolled) VALUES
    (1, 'Opening theory', 2, 1),
    (2, 'Endgame clinic', 1, 1),
    (3, 'Blitz tactics', 3, 0);

-- ==========================================================
-- Block B (library reading clubs): given data.
-- Loans 1-6 and clubs 1-2. Users: 4..13 (player1..player10).
-- ==========================================================
INSERT INTO book_loan (id, book_title, loan_date, due_date, return_date, borrower_id, recommender_id) VALUES
    (1, 'The Name of the Wind', '2026-09-01', '2026-09-15', '2026-09-20', 4, 5),
    (2, 'Dune', '2026-09-10', '2026-10-05', NULL, 4, NULL),
    (3, 'Middlemarch', '2026-09-05', '2026-09-25', NULL, 5, NULL),
    (4, 'Beloved', '2026-09-01', '2026-09-20', '2026-09-20', 6, 4),
    (5, 'Ulysses', '2026-09-01', '2026-09-10', NULL, 9, NULL),
    (6, 'Emma', '2026-09-10', '2026-09-28', NULL, 13, NULL);

INSERT INTO reading_club (id, name, founded_on, max_members) VALUES
    (1, 'Classics Circle', '2025-01-15', 10),
    (2, 'Sci-fi Nights', '2025-06-01', 6);

INSERT INTO reading_club_members (reading_club_id, user_id) VALUES
    (1, 4), (1, 5), (1, 6), (1, 7), (1, 13),
    (2, 8), (2, 9);

INSERT INTO reading_club_loans (reading_club_id, book_loan_id) VALUES
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6);

-- Block B - B3: reviews and reactions (after the clubs and users they reference)
INSERT INTO book_review (id, content, stars, visibility, published_on, club_id, author_id) VALUES
    (1, 'A gripping story about memory and loss', 5, 'PUBLIC', '2026-09-10', 1, 4),
    (2, 'Too slow in the middle chapters, sadly', 2, 'MEMBERS_ONLY', '2026-09-12', 1, 5),
    (3, 'Personal notes, will finish them later', 3, 'PRIVATE', NULL, 2, 8);

INSERT INTO review_reaction (id, kind, reacted_at, note, review_id, reactor_id) VALUES
    (1, 'LIKE', '2026-09-10 20:00:00', NULL, 1, 5),
    (2, 'LOVE', '2026-09-11 08:30:00', 'It''s my favourite book!', 1, 6),
    (3, 'DISLIKE', '2026-09-12 22:15:00', NULL, 2, 4),
    (4, 'LIKE', '2026-09-13 10:00:00', 'Agreed', 2, 7);

-- Block B - B4: members mentioned in the reviews (review 3 mentions nobody: no rows)
INSERT INTO book_review_mentioned_members (book_review_id, mentioned_members_id) VALUES
    (1, 5), (1, 6),
    (2, 4);

-- ==========================================================
-- Block C (airline crew bases): given data.
-- Flights 1-8 and bases 1-2. Users: 4..13 (player1..player10).
-- ==========================================================
INSERT INTO flight (id, code, departure, arrival, distance_km, diverted, captain_id, first_officer_id) VALUES
    (1, 'IB1001', '2026-07-02 08:00:00', '2026-07-02 14:00:00', 5800, false, 4, 5),
    (2, 'IB1002', '2026-07-05 06:00:00', '2026-07-05 08:00:00', 900, false, 5, 6),
    (3, 'IB1003', '2026-07-06 10:00:00', '2026-07-06 12:30:00', 1200, false, 4, 10),
    (4, 'IB1004', '2026-07-08 07:00:00', '2026-07-08 15:00:00', 6100, false, 13, 6),
    (5, 'IB1005', '2026-07-09 09:00:00', '2026-07-09 10:00:00', 400, false, 10, 6),
    (6, 'IB1006', '2026-07-10 09:00:00', '2026-07-10 13:00:00', 3500, true, 7, 4),
    (7, 'IB1007', '2026-07-11 09:00:00', NULL, 3000, false, 13, 7),
    (8, 'IB1008', '2026-07-12 16:00:00', '2026-07-12 18:00:00', 800, true, 8, 9);

INSERT INTO crew_base (id, name, city) VALUES
    (1, 'Madrid hub', 'Madrid'),
    (2, 'Canary base', 'Las Palmas');

INSERT INTO crew_base_pilots (crew_base_id, user_id) VALUES
    (1, 4), (1, 5), (1, 6), (1, 7), (1, 10), (1, 13),
    (2, 8), (2, 9);

INSERT INTO crew_base_flights (crew_base_id, flight_id) VALUES
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7),
    (2, 8);
