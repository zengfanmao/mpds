using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class IncomingStream
    {
         public string ApplicationInstance{get;set;}
         public string Name{get;set;}
         public string SourceIp{get;set;}
         public string IsRecordingSet{get;set;}
         public string IsStreamManagerStream{get;set;}
         public string IsPublishedToVOD{get;set;}
         public string IsConnected{get;set;}
         public string IsPTZEnabled{get;set;}
         public string PtzPollingInterval{get;set;}
    }
}
