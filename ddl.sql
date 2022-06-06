drop table if exists ERS_REIMBURSEMENTS; 
drop table if exists ERS_USERS;
drop table if exists ERS_REIMBURSEMENT_STATUSES;
drop table if exists ERS_REIMBURSEMENT_TYPES;
drop table if exists ERS_USER_ROLES;



create table ERS_REIMBURSEMENTS (
	REIMB_ID varchar,
	AMOUNT int,
	SUBMITTED TIMESTAMP, 
	RESOLVED TIMESTAMP, 
	DESCRIPTION varchar,
	RECIEPT bytea,
	PAYMENT_ID varchar,
	AUTHOR_ID varchar,
	RESOLVER_ID varchar,
	STATUS_ID varchar,
	TYPE_ID varchar,
	
	constraint ERS_REIMBURSEMENTS
		primary key (REIMB_ID)
);	

create table ERS_USERS(
	USER_ID varchar,
	USERNAME varchar,
	EMAIL varchar,
	PASSWORD varchar,
	GIVEN_NAME varchar,
	SURNAME varchar,
	IS_ACTIVE boolean,
	ROLE_ID varchar,
	
	constraint ERS_USERS
		primary key (USER_ID)
);

create table ERS_REIMBURSEMENT_STATUSES(
	STATUS_ID varchar,
	STATUS varchar,

	
	constraint ERS_REIMBURSEMENT_STATUSES
		primary key (STATUS_ID)
);

create table ERS_REIMBURSEMENT_TYPES(
	TYPE_ID varchar,
	TYPE_NAME varchar,
	
	constraint ERS_REIMBURSEMENT_TYPES
		primary key (TYPE_ID)
);

create table ERS_USER_ROLES(
	ROLE_ID varchar,
	ROLE_NAME varchar,
	
	constraint ERS_USER_ROLES
		primary key(ROLE_ID)
);

alter table ERS_REIMBURSEMENTS
	add CONSTRAINT FK_AUTHOR_ID
		foreign key (AUTHOR_ID) references ERS_USERS (USER_ID),
		
	add CONSTRAINT FK_STATUS_ID
		foreign key (STATUS_ID) references ERS_REIMBURSEMENT_STATUSES (STATUS_ID),
		
	add CONSTRAINT FK_REIMBURSEMENT_TYPES_ID
		foreign key (TYPE_ID) references ERS_REIMBURSEMENT_TYPES (TYPE_ID),
	
	add CONSTRAINT FK_RESOLVER_ID
		foreign key (RESOLVER_ID) references ERS_USERS (USER_ID);
	
alter table ERS_USERS	
	add constraint FK_ROLE_ID
		foreign key (ROLE_ID) references ERS_USER_ROLES (ROLE_ID);
	