using System;
using System.Collections.Generic;

namespace GRGcms.Model
{
    /// <summary>
    /// 管理角色表
    /// </summary>
    [Serializable]
    public partial class role
    {
        public role()
        { }
        #region Model
        private int _id;
        private string _role_name = string.Empty;
        private int _role_type = 0;
        private int _is_sys = 0;
        private string _memo = string.Empty;
        private int _role_level = 1;
        /// <summary>
        /// 自增ID
        /// </summary>
        public int id
        {
            set { _id = value; }
            get { return _id; }
        }
        /// <summary>
        /// 角色名称
        /// </summary>
        public string role_name
        {
            set { _role_name = value; }
            get { return _role_name; }
        }
        /// <summary>
        /// 角色类型
        /// </summary>
        public int role_type
        {
            set { _role_type = value; }
            get { return _role_type; }
        }
        /// <summary>
        /// 角色级别
        /// </summary>
        public int role_level
        {
            set { _role_level = value; }
            get { return _role_level; }
        }
        /// <summary>
        /// 角色描述
        /// </summary>
        public string memo
        {
            set { _memo = value; }
            get { return _memo; }
        }
        /// <summary>
        /// 是否系统默认0否1是
        /// </summary>
        public int is_sys
        {
            set { _is_sys = value; }
            get { return _is_sys; }
        }

        private List<role_value> _manager_role_values;
        /// <summary>
        /// 权限子类 
        /// </summary>
        public List<role_value> manager_role_values
        {
            set { _manager_role_values = value; }
            get { return _manager_role_values; }
        }
        #endregion
    }
}