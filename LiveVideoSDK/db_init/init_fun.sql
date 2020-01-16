

go
--初始化一个单位
create procedure addUnit(@unitCode varchar(36),@unitName varchar(36),@userCode varchar(36))
as
begin

insert into tbUnits (id,unitCode,unitName,maxUserNumber,maxVideoUserNumber,unitLoginName) values(
newid(),@unitCode,@unitName,100,100,@userCode
)

end
go



go
----初始化一个案件
create procedure addCase(@caseCode varchar(36),@caseName varchar(36),@caseAddr varchar(36),@userCode varchar(36))
as
begin

insert into tbCases(csCode,csName,csAddress,csStatus,uCode) values(
@caseCode,@caseName,@caseAddr,'已立案',@userCode
)
end
go


go
----初始化用户1
create procedure addUser(@userCode varchar(36),@userNmae varchar(36),@unitCode varchar(36),@userPwd varchar(36))
as
begin

 insert into tbUsers (uCode,uName,accountType,uPassword,uUnitCode,dCode) values(
@userCode,@userNmae,1,@userPwd,@unitCode,@unitCode
)
end
go


go
----初始化案件部署
create procedure addCaseDeploy(@userCode varchar(36),@caseCode varchar(36))

as
begin

insert into tbCaseDeploys(uCode,csCode) values(
@userCode,@caseCode
)

end
go

