create database db_sistema_bancario;

use db_sistema_bancario;

create table tb_conta(
	numero int not null primary key,
    titular varchar(150) not null,
    saldo double not null,
    limite double not null
);

create table tb_historico(
	remetente int not null,
    destinatario int not null,
    valor double not null,
    data_transacao timestamp not null,
    foreign key (remetente) references tb_conta(numero),
    foreign key (destinatario) references tb_conta(numero)
);
