

go
--��ʼ��һ����λ
create procedure addUnit(@unitCode varchar(36),@unitName varchar(36),@userCode varchar(36))
as
begin

insert into tbUnits (id,unitCode,unitName,maxUserNumber,maxVideoUserNumber,unitLoginName) values(
newid(),@unitCode,@unitName,100,100,@userCode
)

end
go



go
----��ʼ��һ������
create procedure addCase(@caseCode varchar(36),@caseName varchar(36),@caseAddr varchar(36),@userCode varchar(36))
as
begin

insert into tbCases(csCode,csName,csAddress,csStatus,uCode) values(
@caseCode,@caseName,@caseAddr,'������',@userCode
)
end
go


go
----��ʼ���û�1
create procedure addUser(@userCode varchar(36),@userNmae varchar(36),@unitCode varchar(36),@userPwd varchar(36))
as
begin

 insert into tbUsers (uCode,uName,accountType,uPassword,uUnitCode,dCode) values(
@userCode,@userNmae,1,@userPwd,@unitCode,@unitCode
)
end
go


go
----��ʼ����������
create procedure addCaseDeploy(@userCode varchar(36),@caseCode varchar(36))

as
begin

insert into tbCaseDeploys(uCode,csCode) values(
@userCode,@caseCode
)

end
go

