
--公司1
declare  @unitCode1 varchar(36)
set @unitCode1=NEWID()
declare  @unitName1 varchar(36)
set @unitName1='测试公司'
-- 案件1
declare  @caseCode1 varchar(36)
set @caseCode1=1
declare  @caseName1 varchar(36)
set @caseName1='测试案件'
declare  @caseAddr1 varchar(36)
set @caseAddr1='广州天河正佳广场'
--用户1
declare  @userCode1 varchar(36)
set @userCode1='test1'
declare  @userNmae1 varchar(36)
set @userNmae1='测试账号1'
--用户2
declare  @userCode2 varchar(36)
set @userCode2='test2'
declare  @userNmae2 varchar(36)
set @userNmae2='测试账号2'
--用户3
declare  @userCode3 varchar(36)
set @userCode3='test3'
declare  @userNmae3 varchar(36)
set @userNmae3='测试账号3'


declare  @userPwd varchar(36)
set @userPwd='E10ADC3949BA59ABBE56E057F20F883E'

--添加单位
exec addUnit @unitCode1,@unitName1,@userCode1
--添加案件
exec addCase @caseCode1,@caseName1,@caseAddr1,@userCode1
--添加用户
exec addUser @userCode1,@userNmae1,@unitCode1,@userPwd
exec addUser @userCode2,@userNmae2,@unitCode1,@userPwd
exec addUser @userCode3,@userNmae3,@unitCode1,@userPwd
--为案件添加用户
exec addCaseDeploy @userCode1,@caseCode1

exec addCaseDeploy @userCode2,@caseCode1

exec addCaseDeploy @userCode3,@caseCode1


-- 清空数据
--delete from tbUsers
--delete from tbCases
--delete from tbCaseDeploys
--delete from tbUnits