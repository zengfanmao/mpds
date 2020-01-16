using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Text;

namespace GRGcms.Model
{
    [Serializable]
    public class user_gps
    {
        public int id
        {
            get;
            set;
        }

        public string account
        {
            get;
            set;
        }

        public string type
        {
            get;
            set;
        }

        public string name
        {
            get;
            set;
        }

        public float longtitude
        {
            get;
            set;
        }

        public float latitude
        {
            get;
            set;
        }
        private DateTime _updatetime = DateTime.Now;

        public DateTime updatetime
        {
            get;
            set;
        }

        public string group
        {
            get;
            set;
        }

        public string groupid
        {
            get;
            set;
        }

        public string dept
        {
            get;
            set;
        }

        public string devicetype
        {
            get;
            set;
        }

        public string status
        {
            get;
            set;
        }

        public int page
        {
            get;
            set;
        }

        public int pageSize
        {
            get;
            set;
        }
    }
}
