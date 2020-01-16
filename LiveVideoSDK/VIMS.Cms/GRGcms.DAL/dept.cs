using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Reflection;
using System.Text;
using GRGcms.DBUtility;
using GRGcms.Common;
using MySql.Data.MySqlClient;

namespace GRGcms.DAL
{
    /// <summary>
    /// 数据访问类:组织机构管理
    /// </summary>
    public partial class dept
    {
        private string databaseprefix;//数据库表名前缀
        public dept(string _databaseprefix)
        {
            databaseprefix = _databaseprefix;
        }

        #region 基本方法================================
        /// <summary>
        /// 是否存在该记录
        /// </summary>
        public bool Exists(int id)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("select count(1) from  " + databaseprefix + "departments");
            strSql.Append(" where id=@id  ");
            MySqlParameter[] parameters = {
					new MySqlParameter("@id", MySqlDbType.Int32,4)};
            parameters[0].Value = id;

            return DbHelperMySql.Exists(strSql.ToString(), parameters);
        }

        /// <summary>
        /// 增加一条数据
        /// </summary>
        public int Add(Model.dept model)
        {
            StringBuilder strSql = new StringBuilder();
            StringBuilder str1 = new StringBuilder();//数据字段
            StringBuilder str2 = new StringBuilder();//数据参数
            //利用反射获得属性的所有公共属性
            PropertyInfo[] pros = model.GetType().GetProperties();
            List<MySqlParameter> paras = new List<MySqlParameter>();
            strSql.Append("insert into  " + databaseprefix + "departments(");
            foreach (PropertyInfo pi in pros)
            {
                //如果不是主键则追加sql字符串
                if (!pi.Name.Equals("id"))
                {
                    //判断属性值是否为空
                    if (pi.GetValue(model, null) != null)
                    {
                        str1.Append(pi.Name + ",");//拼接字段
                        str2.Append("@" + pi.Name + ",");//声明参数
                        paras.Add(new MySqlParameter("@" + pi.Name, pi.GetValue(model, null)));//对参数赋值
                    }
                }
            }
            strSql.Append(str1.ToString().Trim(','));
            strSql.Append(") values (");
            strSql.Append(str2.ToString().Trim(','));
            strSql.Append(") ");
            strSql.Append(";select @@IDENTITY;");
            object obj = DbHelperMySql.GetSingle(strSql.ToString(), paras.ToArray());
            if (obj == null)
            {
                return 0;
            }
            else
            {
                return Convert.ToInt32(obj);
            }
        }

        /// <summary>
        /// 更新一条数据
        /// </summary>
        public bool Update(Model.dept model)
        {
            StringBuilder strSql = new StringBuilder();
            StringBuilder str1 = new StringBuilder();
            //利用反射获得属性的所有公共属性
            PropertyInfo[] pros = model.GetType().GetProperties();
            List<MySqlParameter> paras = new List<MySqlParameter>();
            strSql.Append("update  " + databaseprefix + "departments set ");
            foreach (PropertyInfo pi in pros)
            {
                //如果不是主键则追加sql字符串
                if (!pi.Name.Equals("ID"))
                {
                    //判断属性值是否为空
                    if (pi.GetValue(model, null) != null)
                    {
                        str1.Append(pi.Name + "=@" + pi.Name + ",");//声明参数
                        paras.Add(new MySqlParameter("@" + pi.Name, pi.GetValue(model, null)));//对参数赋值
                    }
                }
            }
            strSql.Append(str1.ToString().Trim(','));
            strSql.Append(" where ID=@id ");
            paras.Add(new MySqlParameter("@id", model.ID));
            return DbHelperMySql.ExecuteSql(strSql.ToString(), paras.ToArray()) > 0;
        }

        /// <summary>
        /// 删除一条数据
        /// </summary>
        public bool Delete(int id)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("delete from " + databaseprefix + "departments ");
            strSql.Append(" where ID=@id");
            MySqlParameter[] parameters = {
					new MySqlParameter("@id", MySqlDbType.Int32,4)};
            parameters[0].Value = id;

            return DbHelperMySql.ExecuteSql(strSql.ToString(), parameters) > 0;
        }

        /// <summary>
        /// 得到一个对象实体
        /// </summary>
        public Model.dept GetModel(int id)
        {
            StringBuilder strSql = new StringBuilder();
            StringBuilder str1 = new StringBuilder();
            Model.dept model = new Model.dept();
            //利用反射获得属性的所有公共属性
            PropertyInfo[] pros = model.GetType().GetProperties();
            foreach (PropertyInfo p in pros)
            {
                str1.Append(p.Name + ",");//拼接字段
            }
            strSql.Append("select " + str1.ToString().Trim(','));
            strSql.Append(" from " + databaseprefix + "departments");
            strSql.Append(" where ID=@id");
            strSql.Append(" limit 1");
            MySqlParameter[] parameters = {
					new MySqlParameter("@id", MySqlDbType.Int32,4)};
            parameters[0].Value = id;
            DataTable dt = DbHelperMySql.Query(strSql.ToString(), parameters).Tables[0];

            if (dt.Rows.Count > 0)
            {
                return DataRowToModel(dt.Rows[0]);
            }
            else
            {
                return null;
            }
        }

        /// <summary>
        /// 获得前几行数据
        /// </summary>
        public DataSet GetList(int Top, string strWhere, string filedOrder)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("select ");
            
            strSql.Append(" * ");
            strSql.Append(" FROM  " + databaseprefix + "dept ");
            if (strWhere.Trim() != "")
            {
                strSql.Append(" where " + strWhere);
            }
            strSql.Append(" order by " + filedOrder);
            if (Top > 0)
            {
                strSql.Append(" limit " + Top.ToString());
            }
            return DbHelperMySql.Query(strSql.ToString());
        }

        /// <summary>
        /// 获得查询分页数据
        /// </summary>
        public DataSet GetList(int pageSize, int pageIndex, string strWhere, string filedOrder, out int recordCount)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("select * FROM " + databaseprefix + "departments");
            if (strWhere.Trim() != "")
            {
                strSql.Append(" where " + strWhere);
            }
            recordCount = Convert.ToInt32(DbHelperMySql.GetSingle(PagingHelper.CreateCountingSql(strSql.ToString())));
            return DbHelperMySql.Query(PagingHelper.CreatePagingSql(recordCount, pageSize, pageIndex, strSql.ToString(), filedOrder));
        }

        #endregion

        #region 扩展方法================================
        /// <summary>
        /// 查询是否存在该记录
        /// </summary>
        public bool Exists(string nav_name)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("select count(1) from " + databaseprefix + "departments");
            strSql.Append(" where name=@nav_name ");
            MySqlParameter[] parameters = {
					new MySqlParameter("@nav_name", MySqlDbType.VarChar,50)};
            parameters[0].Value = nav_name;

            return DbHelperMySql.Exists(strSql.ToString(), parameters);
        }

       

        /// <summary>
        /// 获取父类下的所有子类ID(含自己本身)
        /// </summary>
        public string GetAllIds(string parent_id)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("select dCode,dFather from " + databaseprefix + "departments");
            DataSet ds = DbHelperMySql.Query(strSql.ToString());
            string ids = parent_id.ToString() + ",";
            GetChildIds(ds.Tables[0], parent_id, ref ids);
            return ids.TrimEnd(',');
        }

        /// <summary>
        /// 获取父类下的所有子类ID(含自己本身)
        /// </summary>
        public string GetIds(string nav_name)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("select dCode from " + databaseprefix + "departments");
            strSql.Append(" where name=@nav_name");
            strSql.Append(" limit 1");
            MySqlParameter[] parameters = {
					new MySqlParameter("@nav_name", MySqlDbType.VarChar,50)};
            parameters[0].Value = nav_name;
            object obj = DbHelperMySql.GetSingle(strSql.ToString(), parameters);
            if (obj != null)
            {
                return GetAllIds(obj.ToString());
            }
            return string.Empty;
        }

        /// <summary>
        /// 修改一列数据
        /// </summary>
        public bool UpdateField(int id, string strValue)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("update " + databaseprefix + "departments set " + strValue);
            strSql.Append(" where id=" + id);
            return DbHelperMySql.ExecuteSql(strSql.ToString()) > 0;
        }

        /// <summary>
        /// 修改一列数据
        /// </summary>
        public bool UpdateField(string name, string strValue)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("update " + databaseprefix + "departments set " + strValue);
            strSql.Append(" where name='" + name + "'");
            return DbHelperMySql.ExecuteSql(strSql.ToString()) > 0;
        }

        /// <summary>
        /// 修改一条记录，带事务
        /// </summary>
        public bool Update(MySqlConnection conn, MySqlTransaction trans, string old_name, string new_name)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("update " + databaseprefix + "departments set name=@new_name");
            strSql.Append(" where name=@old_name");
            MySqlParameter[] parameters = {
					new MySqlParameter("@new_name", MySqlDbType.VarChar,50),
					new MySqlParameter("@old_name", MySqlDbType.VarChar,50)};
            parameters[0].Value = new_name;
            parameters[1].Value = old_name;
            return DbHelperMySql.ExecuteSql(conn, trans, strSql.ToString(), parameters) > 0;
        }

        /// <summary>
        /// 修改一条记录，带事务
        /// </summary>
        public bool Update(MySqlConnection conn, MySqlTransaction trans, string old_name, string new_name, string title, int sort_id)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("update " + databaseprefix + "departments set");
            strSql.Append(" name=@new_name,");
            strSql.Append(" title=@title,");
            strSql.Append(" sort_id=@sort_id");
            strSql.Append(" where name=@old_name");
            MySqlParameter[] parameters = {
					new MySqlParameter("@new_name", MySqlDbType.VarChar,50),
                    new MySqlParameter("@title", MySqlDbType.VarChar,100),
                    new MySqlParameter("@sort_id", MySqlDbType.Int32,4),
                    new MySqlParameter("@old_name", MySqlDbType.VarChar,50)};
            parameters[0].Value = new_name;
            parameters[1].Value = title;
            parameters[2].Value = sort_id;
            parameters[3].Value = old_name;
            return DbHelperMySql.ExecuteSql(conn, trans, strSql.ToString(), parameters) > 0;
        }

        /// <summary>
        /// 修改一条记录，带事务
        /// </summary>
        public bool Update(MySqlConnection conn, MySqlTransaction trans, string old_name, int parent_id, string nav_name, string title, int sort_id)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("update " + databaseprefix + "departments set");
            strSql.Append(" parent_id=@parent_id,");
            strSql.Append(" name=@name,");
            strSql.Append(" title=@title,");
            strSql.Append(" sort_id=@sort_id");
            strSql.Append(" where name=@old_name");
            MySqlParameter[] parameters = {
					new MySqlParameter("@parent_id", MySqlDbType.Int32,4),
					new MySqlParameter("@name", MySqlDbType.VarChar,50),
                    new MySqlParameter("@title", MySqlDbType.VarChar,100),
                    new MySqlParameter("@sort_id", MySqlDbType.Int32,4),
                    new MySqlParameter("@old_name", MySqlDbType.VarChar,50)};
            parameters[0].Value = parent_id;
            parameters[1].Value = nav_name;
            parameters[2].Value = title;
            parameters[3].Value = sort_id;
            parameters[4].Value = old_name;
            return DbHelperMySql.ExecuteSql(conn, trans, strSql.ToString(), parameters) > 0;
        }

        /// <summary>
        /// 快捷添加系统默认导航
        /// </summary>
        public int Add(string parent_name, string nav_name, int sort_id, int status)
        {
            //先根据名称查询该父ID
            StringBuilder strSql1 = new StringBuilder();
            strSql1.Append("select id from " + databaseprefix + "departments");
            strSql1.Append(" where name=@parent_name");
            strSql1.Append(" limit 1");
            MySqlParameter[] parameters1 = {
					new MySqlParameter("@parent_name", MySqlDbType.VarChar,50)};
            parameters1[0].Value = parent_name;
            object obj1 = DbHelperMySql.GetSingle(strSql1.ToString(), parameters1);
            if (obj1 == null)
            {
                return 0;
            }
            int parent_id = Convert.ToInt32(obj1);

            StringBuilder strSql = new StringBuilder();
            strSql.Append("insert into " + databaseprefix + "departments(");
            strSql.Append("name,parent_id,sort_id,add_time,status)");
            strSql.Append(" values (");
            strSql.Append("@name,@parent_id,@sort_id,@add_time,@statys)");
            strSql.Append(";select @@IDENTITY");
            MySqlParameter[] parameters = {
                    new MySqlParameter("@name", MySqlDbType.VarChar,50),
					new MySqlParameter("@parent_id", MySqlDbType.Int32,4),
                    new MySqlParameter("@sort_id", MySqlDbType.Int32,4),
					new MySqlParameter("@add_time", MySqlDbType.DateTime),
					new MySqlParameter("@status", MySqlDbType.Int32,4)};
            parameters[0].Value = nav_name;
            parameters[1].Value = parent_id;
            parameters[2].Value = sort_id;
            parameters[3].Value = DateTime.Now;
            parameters[4].Value = status;
            object obj2 = DbHelperMySql.GetSingle(strSql.ToString(), parameters);
            return Convert.ToInt32(obj2);
        }


       

        /// <summary>
        /// 根据父节点ID获取导航列表
        /// </summary>
        public DataTable GetList(string parent_id)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("select * FROM " + databaseprefix + "departments");
            strSql.Append(" order by sort_id asc,id desc");
            DataSet ds = DbHelperMySql.Query(strSql.ToString());
            //重组列表
            DataTable oldData = ds.Tables[0] as DataTable;
            if (oldData == null)
            {
                return null;
            }
            //创建一个新的DataTable增加一个深度字段
            DataTable newData = oldData.Clone();
            newData.Columns.Add("class_layer", typeof(int));
            //调用迭代组合成DAGATABLE
            GetChilds(oldData, newData, parent_id, 0);
            return newData;
        }

        /// <summary>
        /// 将对象转换实体
        /// </summary>
        public Model.dept DataRowToModel(DataRow row)
        {
            Model.dept model = new Model.dept();
            if (row != null)
            {
                //利用反射获得属性的所有公共属性
                Type modelType = model.GetType();
                for (int i = 0; i < row.Table.Columns.Count; i++)
                {
                    PropertyInfo proInfo = modelType.GetProperty(row.Table.Columns[i].ColumnName);
                    if (proInfo != null && row[i] != DBNull.Value)
                    {
                        proInfo.SetValue(model, row[i], null);//用索引值设置属性值
                    }
                }
            }
            return model;
        }
        #endregion

        #region 私有方法================================
        /// <summary>
        /// 从内存中取得所有下级类别列表（自身迭代）
        /// </summary>
        private void GetChilds(DataTable oldData, DataTable newData, string parent_id, int class_layer)
        {
            class_layer++;
            DataRow[] dr = oldData.Select("dFather='" + parent_id+"'");
            for (int i = 0; i < dr.Length; i++)
            {
                DataRow row = newData.NewRow();//创建新行
                //循环查找列数量赋值
                for (int j = 0; j < dr[i].Table.Columns.Count; j++)
                {
                    row[dr[i].Table.Columns[j].ColumnName] = dr[i][dr[i].Table.Columns[j].ColumnName];
                }
                row["class_layer"] = class_layer;//赋值深度字段
                newData.Rows.Add(row);//添加新行
                //调用自身迭代
                this.GetChilds(oldData, newData, dr[i]["dCode"].ToString(), class_layer);
            }
        }

        /// <summary>
        /// 获取父类下的所有子类ID
        /// </summary>
        private void GetChildIds(DataTable dt, string parent_id, ref string ids)
        {
            DataRow[] dr = dt.Select("dFather=" + parent_id);
            for (int i = 0; i < dr.Length; i++)
            {
                ids += dr[i]["dCode"].ToString() + ",";
                //调用自身迭代
                this.GetChildIds(dt, dr[i]["dCode"].ToString(), ref ids);
            }
        }
        #endregion
    }
}