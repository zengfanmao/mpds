using System;

namespace GRGcms.Model
{
    /// <summary>
    /// 管理员信息表
    /// </summary>
    [Serializable]
    public partial class user
    {
        public user()
        { }
        #region Model
        private int _id;
        private int? _isdel;
        private byte[] _recsn;
        private string _ucode;
        private string _upassword;
        private string _usalt;
        private string _ubelong;
        private int? _uisactive;
        private string _rname;
        private string _pcnum;
        private string _uname;
        private string _usex;
        private string _uduty;
        private string _dcode;
        private string _utel;
        private string _ushortnum;
        private string _uheadportrait;
        private string _dname;
        private string _lycid;
        private int? _loginfailtimes;
        private DateTime? _lastlogintime;
        private string _uremarks;
        private DateTime? _createtime;
        private string _udepartment;
        private string _accounttype;
        private string _uemployeenum;
        private int? _uishistory;
        private int? _uisunilt;
        private int? _uisaccontion;
        private string _uunitcode;
        private int _roleid;
        private int _roletype;
        private string _groupid;
        private string _groupname;
        private int? _deviceid;
        private string _deviceesn;
        private string _devicetype;
        private string _status;
        private string _purpose;
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
        public string uCode
        {
            set { _ucode = value; }
            get { return _ucode; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string uPassword
        {
            set { _upassword = value; }
            get { return _upassword; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string uSalt
        {
            set { _usalt = value; }
            get { return _usalt; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string uBelong
        {
            set { _ubelong = value; }
            get { return _ubelong; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int? uIsActive
        {
            set { _uisactive = value; }
            get { return _uisactive; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string rName
        {
            set { _rname = value; }
            get { return _rname; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string pcNum
        {
            set { _pcnum = value; }
            get { return _pcnum; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string uName
        {
            set { _uname = value; }
            get { return _uname; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string uSex
        {
            set { _usex = value; }
            get { return _usex; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string uDuty
        {
            set { _uduty = value; }
            get { return _uduty; }
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
        public string uTel
        {
            set { _utel = value; }
            get { return _utel; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string uShortNum
        {
            set { _ushortnum = value; }
            get { return _ushortnum; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string uHeadPortrait
        {
            set { _uheadportrait = value; }
            get { return _uheadportrait; }
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
        public string LYCID
        {
            set { _lycid = value; }
            get { return _lycid; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int? loginFailTimes
        {
            set { _loginfailtimes = value; }
            get { return _loginfailtimes; }
        }
        /// <summary>
        /// 
        /// </summary>
        public DateTime? lastLoginTime
        {
            set { _lastlogintime = value; }
            get { return _lastlogintime; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string uRemarks
        {
            set { _uremarks = value; }
            get { return _uremarks; }
        }
        /// <summary>
        /// 
        /// </summary>
        public DateTime? Createtime
        {
            set { _createtime = value; }
            get { return _createtime; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string uDepartment
        {
            set { _udepartment = value; }
            get { return _udepartment; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string accountType
        {
            set { _accounttype = value; }
            get { return _accounttype; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string uEmployeenum
        {
            set { _uemployeenum = value; }
            get { return _uemployeenum; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int? uIshistory
        {
            set { _uishistory = value; }
            get { return _uishistory; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int? uIsUnilt
        {
            set { _uisunilt = value; }
            get { return _uisunilt; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int? uIsAccontion
        {
            set { _uisaccontion = value; }
            get { return _uisaccontion; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string uUnitCode
        {
            set { _uunitcode = value; }
            get { return _uunitcode; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int roleid
        {
            set { _roleid = value; }
            get { return _roleid; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int roleType
        {
            set { _roletype = value; }
            get { return _roletype; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string groupid
        {
            set { _groupid = value; }
            get { return _groupid; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string groupName
        {
            set { _groupname = value; }
            get { return _groupname; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int? deviceid
        {
            set { _deviceid = value; }
            get { return _deviceid; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string deviceESN
        {
            set { _deviceesn = value; }
            get { return _deviceesn; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string devicetype
        {
            set { _devicetype = value; }
            get { return _devicetype; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string status
        {
            set { _status = value; }
            get { return _status; }
        }

        public string purpose
        {
            set { _purpose = value; }
            get { return _purpose; }
        }
        #endregion Model
    }
}