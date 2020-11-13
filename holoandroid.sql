create database holoandroid;
use holoandroid;
create table user (id char(10) primary key,name nvarchar(50),sodienthoai char(10),email nvarchar(50),ngaysinh char(15),password nvarchar(50),gioiTinh nvarchar(3));
create table relationship(id_user_1 char(10) references user(id),id_user_2 char(10) references user(id),id_action_user char(10) references user(id),status int,primary key(id_user_1,id_user_2),unique key (id_user_1,id_user_2));
insert into user (id,name,sodienthoai,email,ngaysinh,password,gioiTinh) values ('1','Vo Van Nghia','0001112222','asdsa@','6-6-1666','111','Nam');
insert into user (id,name,sodienthoai,email,ngaysinh,password,gioiTinh) values ('2','Vo Van A','0001112222','asdsa@','6-6-1666','111','Nam');
insert into user (id,name,sodienthoai,email,ngaysinh,password,gioiTinh) values ('3','Vo Van V','0001112222','asdsa@','6-6-1666','111','Nam');
insert into user (id,name,sodienthoai,email,ngaysinh,password,gioiTinh) values ('4','Vo Van C','0001112222','asdsa@','6-6-1666','111','Nam');
insert into user (id,name,sodienthoai,email,ngaysinh,password,gioiTinh) values ('5','Vo Van D','0001112222','asdsa@','6-6-1666','111','Nam');
insert into user (id,name,sodienthoai,email,ngaysinh,password,gioiTinh) values ('6','Vo Van E','0001112222','asdsa@','6-6-1666','111','Nam');
insert into user (id,name,sodienthoai,email,ngaysinh,password,gioiTinh) values ('7','Vo Van F','0001112222','asdsa@','6-6-1666','111','Nam');
insert into user (id,name,sodienthoai,email,ngaysinh,password,gioiTinh) values ('8','Vo Van G','0001112222','asdsa@','6-6-1666','111','Nam');
insert into user (id,name,sodienthoai,email,ngaysinh,password,gioiTinh) values ('9','Vo Van H','0001112222','asdsa@','6-6-1666','111','Nam');
insert into user (id,name,sodienthoai,email,ngaysinh,password,gioiTinh) values ('10','Vo Van J','0001112222','asdsa@','6-6-1666','111','Nam');
select * from user;
insert into relationship(id_user_1,id_user_2,id_action_user,status) values('1','2','1',1);
insert into relationship(id_user_1,id_user_2,id_action_user,status) values('1','3','3',2);
insert into relationship(id_user_1,id_user_2,id_action_user,status) values('1','4','1',1);
insert into relationship(id_user_1,id_user_2,id_action_user,status) values('1','5','5',2);
insert into relationship(id_user_1,id_user_2,id_action_user,status) values('1','6','6',1);
insert into relationship(id_user_1,id_user_2,id_action_user,status) values('1','7','2',2);
insert into relationship(id_user_1,id_user_2,id_action_user,status) values('1','8','1',1);
insert into relationship(id_user_1,id_user_2,id_action_user,status) values('1','9','9',2);
insert into relationship(id_user_1,id_user_2,id_action_user,status) values('1','10','10',1);
insert into relationship(id_user_1,id_user_2,id_action_user,status) values('10','1','10',2);
select * from relationship;
select u.id,u.name from User u join Relationship r
		on u.id = r.id_user_1 or u.id = r.id_user_2
        where ( id_user_1 = '1' or id_user_2 = '1' ) and u.id != '1' and status = 2
        group by name;
select u.id,u.name from User u join Relationship r
		on u.id = r.id_user_1 or u.id = r.id_user_2
        where ( id_user_1 = '1' or id_user_2 = '1' ) and u.id != '1' and status = 1
        group by name;
select * from User u join Relationship r
		on u.id = r.id_user_1 or u.id = r.id_user_2
        where ( id_user_1 = '1' or id_user_2 = '1' ) and u.id != '1' 
        group by name;
        