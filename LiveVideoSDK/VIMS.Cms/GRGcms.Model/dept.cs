using System;
namespace GRGcms.Model
{
    /// <summary>
    /// dept:实体类(属性说明自动提取数据库字段的描述信息)
    /// </summary>
    [Serializable]
    public partial class dept
    {
        public dept()
        { }
        #region Model
        private int _id;
        private int _isdel=0;
        private byte[] _recsn;
        private string _dcode;
        private string _dname;
        private string _dfather;
        private string _ddesc;
        private int? _sort_id;
        private DateTime _createtime=DateTime.Now;
        private DateTime _updatetime=DateTime.Now;
        private string _farther;
        private string _dabbr;
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
        public string dCode
        {
            set { _dcode = value; }
            get { return _dcode; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string dName
        {
            set { _dname = value; }
            get { return _dname; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string dFather
        {
            set { _dfather = value; }
            get { return _dfather; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string dDesc
        {
            set { _ddesc = value; }
            get { return _ddesc; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int? sort_id
        {
            set { _sort_id = value; }
            get { return _sort_id; }
        }
        /// <summary>
        /// 
        /// </summary>
        public DateTime createtime
        {
            set { _createtime = value; }
            get { return _createtime; }
        }
        /// <summary>
        /// 
        /// </summary>
        public DateTime updatetime
        {
            set { _updatetime = value; }
            get { return _updatetime; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string farther
        {
            set { _farther = value; }
            get { return _farther; }
        }

        public string dabbr
        {
            set { _dabbr = value; }
            get { return _dabbr; }
        }
        #endregion Model

    }
}

