-- rolesテーブル
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_GENERAL');

-- usersテーブル
INSERT IGNORE INTO users (id, name, email, password, role_id, enabled) VALUES (1, 'suzuki ichiro', 'suzuki.ichiro@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 1, true);
INSERT IGNORE INTO users (id, name, email, password, role_id, enabled) VALUES (2, 'tanaka taro', 'tanaka.taro@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 1, true);
INSERT IGNORE INTO users (id, name, email, password, role_id, enabled) VALUES (3, 'sato jiro', 'sato.jiro@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 1, true);

-- boardsテーブル
INSERT IGNORE INTO boards (id, user_id, title, created_at, updated_at) VALUES (1, 1, 'プログラミング学習', '2024-11-01 00:00:00', '2024-11-01 00:00:00');
INSERT IGNORE INTO boards (id, user_id, title, created_at, updated_at) VALUES (2, 2, '雑談', '2024-11-03 00:00:00', '2024-11-03 00:00:00');

-- postsテーブル
INSERT IGNORE INTO posts (id, board_id, user_id, content, created_at, updated_at) VALUES (1, 1, 1, 'Javaの学習をしているけど、for文とwhile文の違いがよくわからない…', '2024-11-01 00:00:00', '2024-11-01 00:00:00');
INSERT IGNORE INTO posts (id, board_id, user_id, content, created_at, updated_at) VALUES (2, 1, 3, '繰り返し回数が事前に決まっていればfor文、決まっていなければwhile文と覚えると分かりやすいですよ！', '2024-11-02 00:00:00', '2024-11-02 00:00:00');
INSERT IGNORE INTO posts (id, board_id, user_id, content, created_at, updated_at) VALUES (3, 2, 2, 'マンガを読みたいんだけど、みんなのおすすめを教えて！', '2024-11-03 00:00:00', '2024-11-03 00:00:00');
INSERT IGNORE INTO posts (id, board_id, user_id, content, created_at, updated_at) VALUES (4, 2, 1, 'やっぱりONE PIECEが面白いよ！', '2024-11-04 00:00:00', '2024-11-04 00:00:00');
INSERT IGNORE INTO posts (id, board_id, user_id, content, created_at, updated_at) VALUES (5, 2, 3, '呪術廻戦もおすすめだよ！', '2024-11-05 00:00:00', '2024-11-05 00:00:00');