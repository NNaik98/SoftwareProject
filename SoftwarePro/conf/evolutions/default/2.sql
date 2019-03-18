# --- Sample dataset
 
# --- !Ups
-- delete from employee;
-- delete from project;


insert into address (id,street1,street2,town,post_code) values (1,'Whatley hall','Archerswood','Dublin','D15'); 
insert into address (id,street1,street2,town,post_code) values (2,'Smithfield','Smithfield','Dublin','D07'); 
insert into address (id,street1,street2,town,post_code) values (3,'BlackHall','Navan','Dublin','D13'); 
insert into address (id,street1,street2,town,post_code) values (4,'George House','Georges St','Dublin','D02'); 


insert into department (id,name) values ( 1,'Computing' );
insert into department (id,name) values ( 2,'Operating System' );
insert into department (id,name) values ( 3,'Web Development' );
insert into department (id,name) values ( 4,'Networking' );


insert into employee(id,fname,lname,department_id,aid) values (1,'Michael','Santos',1,1);
insert into employee(id,fname,lname,department_id,aid) values (2,'David','O,toole',2,2);
insert into employee(id,fname,lname,department_id,aid) values (3,'Emma','Jackson',3,3);
insert into employee(id,fname,lname,department_id,aid) values (4,'Ken','Jhonston',4,4);

insert into project(id,name) values (1,'Linux');
insert into project(id,name) values (2,'PlayFrameWork');
insert into project(id,name) values (3,'Vim');
insert into project(id,name) values (4,'PacketTracer');


insert into project_employee(project_id,employee_id) values (1,1);
insert into project_employee(project_id,employee_id) values (2,2);
insert into project_employee(project_id,employee_id) values (3,3);
insert into project_employee(project_id,employee_id) values (4,4);


-- delete from user;
  
insert into user (email,role,name,password) values ( 'manager@manager.com', 'admin', 'manager', 'manager' );
insert into user (email,role,name,password) values ( 'employee@employee.com', 'employee', 'Emma Jackson', 'employee' );
