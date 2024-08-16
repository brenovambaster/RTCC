INSERT INTO course (id, name, code_of_course)
VALUES
    ('a4d2f1f3-75e0-4e9a-b0d3-2a7262cf564f', 'Ciência da Computação', 'CC01'),
    ('e564e6b4-7c92-4d3e-b0e2-d2c6b9a16c76', 'Engenharia Elétrica', 'EE01'),
    ('f7b7b3a0-3b68-4f69-a5c2-3b5c9825d451', 'Engenharia Química', 'EQ01');


INSERT INTO coordinator (id, name, email, username, password, course)
VALUES
    ('6c4f8d6e-1d2e-4b6c-b8b5-58b13ed6a6d1', 'João Silva', 'joao.silva@ifnmg.com.br', 'joao', '$2a$10$K6S6KiRtJ66uSFI8ewtYc.ihA4x/fMI3ErYr/ZG9PFAcAlrCqRxG.', 'a4d2f1f3-75e0-4e9a-b0d3-2a7262cf564f'),
    ('b287c44b-5aaf-45e3-8e14-0cfcff5b686b', 'Mariana Costa', 'mariana.costa@ifnmg.com.br', 'mariana', '$2a$10$C7h7EFiFJ8JkG8z.E/5u6OXZj/cX0BThw/8cz7Tr3ocMy7GFuLp2K', 'e564e6b4-7c92-4d3e-b0e2-d2c6b9a16c76'),
    ('c847d22f-8c7d-4e62-8321-e606d6caa0a4', 'Pedro Lima', 'pedro.lima@ifnmg.com.br', 'pedro', '$2a$10$K9VfDRouOB.tV9g6m9RlyOJ2.rffN1mdf88gyS2bMa/Uo1d6fTceO', 'f7b7b3a0-3b68-4f69-a5c2-3b5c9825d451');


INSERT INTO professor (id, name, email, research_area, title, location_of_work)
VALUES
    ('d5a4b637-6299-4f87-900d-89b23a6b7c8a', 'Ana Beatriz Silva', 'ana.silva@ifnmg.com.br', 'Matematica', 'Doutor', 'Interno'),
    ('f6b74d68-d382-4b42-87bc-237f7cb2ea11', 'Carlos Pereira', 'carlos.pereira@ifnmg.com.br', 'Álgebra Abstrata', 'Mestre', 'Externo'),
    ('a2e6b28b-012f-4b92-a344-2b5b5d603f33', 'Juliana Souza', 'juliana.souza@ifnmg.com.br', 'Teoria da Medida', 'Doutor', 'Interno'),
    ('5c9e9796-6b76-4b69-80f2-3e2e5f5825b2', 'Mariana Oliveira', 'mariana.oliveira@ifnmg.com.br', 'Espaços Métricos', 'Mestre', 'Interno'),
    ('c9a3833e-d90f-4c35-89d7-405c4c5cb535', 'Fernando Lima', 'fernando.lima@ifnmg.com.br', 'Teoria Quântica de Campos', 'Licenciatura', 'Externo'),
    ('a5e2b139-5390-4a52-9d99-f9e5b40cb37b', 'Fernanda Costa', 'fernanda.costa@ifnmg.com.br', 'Geometria Diferencial', 'Doutor', 'Interno'),
    ('b1a1cdd4-d2a5-4de6-91f0-d035a1f3b8ee', 'Lucas Santos', 'lucas.santos@ifnmg.com.br', 'Química', 'Mestre', 'Externo'),
    ('fd2735b3-e953-4a61-8490-4cf5b731f02f', 'Camila Rodrigues', 'camila.rodrigues@ifnmg.com.br', 'Física', 'Doutor', 'Interno'),
    ('dcd7e012-86a6-437c-b579-59e6b5fdf54b', 'Gabriel Almeida', 'gabriel.almeida@ifnmg.com.br', 'Física Teórica', 'Mestre', 'Externo'),
    ('e5c4177b-d9c0-44b2-b8e2-724e3b91a462', 'Sofia Mendes', 'sofia.mendes@ifnmg.com.br', 'Otimização Combinatoria', 'Licenciatura', 'Interno');


