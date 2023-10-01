insert into CardSet(id, name) values (1, 'CardSet A');
insert into CardSet(id, name) values (2, 'CardSet B');

insert into Topic(id, name, cardset_id) values (1, 'Topic A1', 1);
insert into Topic(id, name, cardset_id) values (2, 'Topic A2', 1);
insert into Topic(id, name, cardset_id) values (3, 'Topic B1', 2);
insert into Topic(id, name, cardset_id) values (4, 'Topic B2', 2);

insert into Card(id, topic_id, question) values (1, 1, 'Q1');
insert into Card(id, topic_id, question) values (2, 1, 'Q2');
insert into Card(id, topic_id, question) values (3, 2, 'Q3');
insert into Card(id, topic_id, question) values (4, 2, 'Q4');
insert into Card(id, topic_id, question) values (5, 3, 'Q5');
insert into Card(id, topic_id, question) values (6, 3, 'Q6');
insert into Card(id, topic_id, question) values (7, 4, 'Q7');
insert into Card(id, topic_id, question) values (8, 4, 'Q8');