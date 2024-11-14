create database db_sistema_bancario;

use db_sistema_bancario;

create table tb_cliente (
    id int not null primary key auto_increment, # não precisa ser unique pq é primary key e auto_increment
    nome varchar(150),
    cpf char(11) not null unique # cpf unico
);

create table tb_conta(
	numero int not null primary key,
    id_cliente int not null unique,
    saldo double not null,
    limite double not null,
    foreign key (id_cliente) references tb_cliente(id)
);

create table tb_historico(
	remetente int not null,
    destinatario int not null,
    valor double not null,
    data_transacao timestamp not null,
    foreign key (remetente) references tb_conta(numero),
    foreign key (destinatario) references tb_conta(numero)
);
