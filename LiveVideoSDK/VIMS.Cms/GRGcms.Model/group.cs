using System;
namespace GRGcms.Model
{
    /// <summary>
    /// discussiongroup:实体类(属性说明自动提取数据库字段的描述信息)
    /// </summary>
    [Serializable]
    public partial class group
    {
        public group()
        { }
        #region Model
        private int _id;
        private int _isdel=0;
        private byte[] _recsn;
        private DateTime  _createdtime=DateTime.Now;
        private string _discussioncode;
        private string _discussionname;
        private string _createdusercode;
        private string _createdusername;
        private string _cscode;
        private string _type;
        private string _relativegroup;
        private string _relativegroupid;
        private string _dept;
        private string _deptid;
        private int _status;
        private DateTime? _updatetime;
        private int _clazz;
        /// <summary>
        /// auto_increment
        /// </summary>
        public int ID
        {
            set { _id = value; }
            get { return _id; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int isDel
        {
            set { _isdel = value; }
            get { return _isdel; }
        }
        /// <summary>
        /// 
        /// </summary>
        public byte[] recSN
        {
            set { _recsn = value; }
            get { return _recsn; }
        }
        /// <summary>
        /// 
        /// </summary>
        public DateTime createdTime
        {
            set { _createdtime = value; }
            get { return _createdtime; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string discussionCode
        {
            set { _discussioncode = value; }
            get { return _discussioncode; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string discussionName
        {
            set { _discussionname = value; }
            get { return _discussionname; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string createdUserCode
        {
            set { _createdusercode = value; }
            get { return _createdusercode; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string createdUserName
        {
            set { _createdusername = value; }
            get { return _createdusername; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string csCode
        {
            set { _cscode = value; }
            get { return _cscode; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string type
        {
            set { _type = value; }
            get { return _type; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string relativegroup
        {
            set { _relativegroup = value; }
            get { return _relativegroup; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string relativegroupid
        {
            set { _relativegroupid = value; }
            get { return _relativegroupid; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string dept
        {
            set { _dept = value; }
            get { return _dept; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string deptid
        {
            set { _deptid = value; }
            get { return _deptid; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int status
        {
            set { _status = value; }
            get { return _status; }
        }
        /// <summary>
        /// 
        /// </summary>
        public DateTime? updatetime
        {
            set { _updatetime = value; }
            get { return _updatetime; }
        }

        public int clazz
        {
            set { _clazz = value; }
            get { return _clazz; }
        }
        #endregion Model

    }
}

