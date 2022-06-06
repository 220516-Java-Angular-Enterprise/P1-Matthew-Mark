drop table if exists ers_reimbursements; 
drop table if exists ers_users;
drop table if exists ers_reimbursement_statuses;
drop table if exists ers_reimbursement_types;
drop table if exists ers_user_roles;



create table ers_reimbursements (
	reimb_id varchar,
	amount int,
	submitted TIMESTAMP, 
	resolved TIMESTAMP, 
	description varchar,
	receipt bytea,
	payment_id varchar,
	author_id varchar,
	resolver_id varchar,
	status_id varchar,
	type_id varchar,
	
	constraint pk_ers_reimbursements
		primary key (reimb_id)
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
	
	constraint PK_ERS_USERS
		primary key (USER_ID)
);

create table ERS_REIMBURSEMENT_STATUSES(
	STATUS_ID varchar,
	STATUS varchar,

	
	constraint PK_ERS_REIMBURSEMENT_STATUSES
		primary key (STATUS_ID)
);

create table ERS_REIMBURSEMENT_TYPES(
	TYPE_ID varchar,
	TYPE_NAME varchar,
	
	constraint PK_ERS_REIMBURSEMENT_TYPES
		primary key (TYPE_ID)
);

create table ERS_USER_ROLES(
	ROLE_ID varchar,
	ROLE_NAME varchar,
	
	constraint PK_ERS_USER_ROLES
		primary key(ROLE_ID)
);

alter table ers_reimbursements 
	add CONSTRAINT FK_AUTHOR_ID
		foreign key (author_id) references ERS_USERS (USER_ID),
		
	add CONSTRAINT FK_STATUS_ID
		foreign key (status_id) references ERS_REIMBURSEMENT_STATUSES (STATUS_ID),
		
	add CONSTRAINT FK_REIMBURSEMENT_TYPES_ID
		foreign key (type_id) references ERS_REIMBURSEMENT_TYPES (TYPE_ID),
	
	add CONSTRAINT FK_RESOLVER_ID
		foreign key (resolver_id) references ERS_USERS (USER_ID);
	
alter table ERS_USERS	
	add constraint FK_ROLE_ID
		foreign key (ROLE_ID) references ERS_USER_ROLES (ROLE_ID);
	