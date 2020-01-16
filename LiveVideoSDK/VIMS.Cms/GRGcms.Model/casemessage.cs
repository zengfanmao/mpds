using System;
namespace GRGcms.Model
{
    /// <summary>
    /// casemessage:实体类(属性说明自动提取数据库字段的描述信息)
    /// </summary>
    [Serializable]
    public partial class casemessage
    {
        public casemessage()
        { }
        #region Model
        private int _id;
        private int? _isdel;
        private byte[] _recsn;
        private string _cscode;
        private string _msgtype;
        private string _msgabstract;
        private string _msgfile;
        private string _ucode;
        private DateTime _msgtime = DateTime.Now;
        private decimal? _ulatitude;
        private decimal? _ulongitude;
        private string _upositionname;
        private string _msgfromtype;
        private string _msgtocode;
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
        public int? isDel
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
        public string csCode
        {
            set { _cscode = value; }
            get { return _cscode; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string msgType
        {
            set { _msgtype = value; }
            get { return _msgtype; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string msgAbstract
        {
            set { _msgabstract = value; }
            get { return _msgabstract; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string msgFile
        {
            set { _msgfile = value; }
            get { return _msgfile; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string uCode
        {
            set { _ucode = value; }
            get { return _ucode; }
        }
        /// <summary>
        /// 
        /// </summary>
        public DateTime msgTime
        {
            set { _msgtime = value; }
            get { return _msgtime; }
        }
        /// <summary>
        /// 
        /// </summary>
        public decimal? uLatitude
        {
            set { _ulatitude = value; }
            get { return _ulatitude; }
        }
        /// <summary>
        /// 
        /// </summary>
        public decimal? uLongitude
        {
            set { _ulongitude = value; }
            get { return _ulongitude; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string uPositionName
        {
            set { _upositionname = value; }
            get { return _upositionname; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string msgFromType
        {
            set { _msgfromtype = value; }
            get { return _msgfromtype; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string msgToCode
        {
            set { _msgtocode = value; }
            get { return _msgtocode; }
        }
        #endregion Model

    }
}

