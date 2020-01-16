using System;
namespace GRGcms.Model
{
    /// <summary>
    /// </summary>
    [Serializable]
    public partial class appversion
    {
        public appversion()
        { }
        #region Model
        private int _id;
        private int _isdel=0;
        private byte[] _recsn;
        private string _apptype;
        private int _appversionno=88;
        private string _appversionname;
        private DateTime _apppublishtime=DateTime.Now;
        private string _appfeatures;
        private string _apptitle;
        private string _appdownloadurl;
        private string _createuser;
        /// <summary>
        /// 
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
        public string appType
        {
            set { _apptype = value; }
            get { return _apptype; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int appVersionNo
        {
            set { _appversionno = value; }
            get { return _appversionno; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string appVersionName
        {
            set { _appversionname = value; }
            get { return _appversionname; }
        }
        /// <summary>
        /// 
        /// </summary>
        public DateTime appPublishTime
        {
            set { _apppublishtime = value; }
            get { return _apppublishtime; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string appFeatures
        {
            set { _appfeatures = value; }
            get { return _appfeatures; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string appTitle
        {
            set { _apptitle = value; }
            get { return _apptitle; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string appDownloadUrl
        {
            set { _appdownloadurl = value; }
            get { return _appdownloadurl; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string createuser
        {
            set { _createuser = value; }
            get { return _createuser; }
        }
        #endregion Model

    }
}

