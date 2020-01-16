using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Model
{
    public class ImageModel :BaseFile
    {
        /// <summary>
        ///  图片地址
        /// </summary>
        public string ImageUrl { get; set; }
        public string FileId { get; set; }
    }
}
