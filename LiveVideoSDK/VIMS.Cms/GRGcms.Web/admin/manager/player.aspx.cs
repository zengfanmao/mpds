using GRGcms.Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace GRGcms.Web.admin.manager
{
    public partial class player : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            string uuid = HttpContext.Current.Request.QueryString["uuid"].ToString();
            if (String.IsNullOrEmpty(uuid))
            {
                return;
            }
            if (!Page.IsPostBack)
            {
                RptBind(" a.conference_uuid = '" + uuid + "'", "a.id, a.event_time ");
            }
        }

        #region 数据绑定=================================
        private void RptBind(string _strWhere, string _orderby)
        {
            BLL.casemessage bll = new BLL.casemessage();
            this.rptList.DataSource = bll.GetAudioMsgList(_strWhere, _orderby);
            this.rptList.DataBind();
        }
        #endregion

        protected string getEventName(string eventName)
        {
            switch(eventName)
            {
                case "conference_create":
                    return "创建会话";
                case "conference_ptt_PDT_1":
                    return "按下讲话";
                case "conference_ptt_PDT_0":
                    return "松开";
                case "conference_ptt_APP_1":
                    return "按下讲话";
                case "conference_ptt_APP_0":
                    return "松开";
                case "conference_destroy":
                    return "结束会话";
            }
            return "";
        }
    }
}