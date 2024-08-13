INSERT INTO professor (id, name, email, research_area, title, location_of_work)
VALUES ('0753b2bc-175a-4ff4-9b8b-7580a2826c2f', 'Rafael Marques Loiola', 'rafa@gmail.com', 'Matematica', 'Licenciatura',
        'Interno'),
       ('b6e7bcbf-a309-4869-a4f0-80df1262a7db', 'Breno Vambaster Cardoso', 'brenovambaster5@gmail.com', 'Computacao',
        'Doutor', 'Interno'),
       ('da4934e6-6326-4d1f-8f29-27949aa528c4', 'Lucas Matos Dias', 'lucas@gmail.com', 'quimica', 'Mestre', 'Externo');



INSERT INTO coordinator (id, name, email, username, password, course)
VALUES ('7ab5ea9f-fa3d-48eb-a274-7ce35a341c04', 'Danilo', 'breno.cardoso@gail.com', 'danilo',
        '$2a$10$D41bhmUnVTTTB9OjmuJ1iOwAMt7rPZTS2pUtGc4RZFqgVB1l8rrby', 'Ciência da Computação'),
       ('7b2344ae-8374-425d-aa80-7ca88add98cc', '34', 'brenovambaster5@gmail.com', 'breno',
        '$2a$10$h0L6LSgjc/RVGfva1C.vquvhht/n1Clx81tcBHgvq4merr2neORKO', 'Ciência da Computação'),
       ('e29a3908-f411-405e-9764-e86680dbf13d', 'adm polls', 'breno@breno.com', 'admin',
        '$2a$10$X6r88iB5c1fckSL9NDnY6unM7PPHFLh1DefS/NxtPekv4wHTf3hKq', 'Ciência da Computação');

INSERT INTO course (id, name, code_of_course)
VALUES ('571c2a36-a191-455a-b675-296768e80887', 'Matematica', '432'),
       ('d03e6fbd-5215-4a80-9d62-a507997dc6f9', 'POrtugues', '4234');

INSERT INTO tcc (id, title, author, course, defense_date, advisor, summary, abstract, keywords, language, path_file)
VALUES ('21c85274-9fc7-484d-8c1e-b68b82c45058', 'Title', 'Lucas Azevedo', '571c2a36-a191-455a-b675-296768e80887',
        '2024-01-01', 'b6e7bcbf-a309-4869-a4f0-80df1262a7db', 'This is a summary of the TCC.',
        'This is an abstract of the TCC.', 'batata, pera, teste', 'EN',
        'C:\\Users\\breno\\Documents\\github\\RTCC\\uploads\\70e39af0-b35b-4e1f-8a6a-9d09ae559c49.pdf');


INSERT INTO tcc_committee_members (tcc_id, professor_id)
VALUES ('21c85274-9fc7-484d-8c1e-b68b82c45058', '0753b2bc-175a-4ff4-9b8b-7580a2826c2f'),
       ('21c85274-9fc7-484d-8c1e-b68b82c45058', 'da4934e6-6326-4d1f-8f29-27949aa528c4');
