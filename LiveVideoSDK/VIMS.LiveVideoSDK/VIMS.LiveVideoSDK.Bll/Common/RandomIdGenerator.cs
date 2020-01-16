using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace VIMS.LiveVideoSDK.Bll.Common
{


    public static class RandomIdGenerator
    {
        
        //44
        //down vote
        //accepted
        //Base 62 is used by tinyurl and bit.ly for the abbreviated URLs. It's a well-understood method for creating "unique", human-readable IDs. Of course you will have to store the created IDs and check for duplicates on creation to ensure uniqueness. (See code at bottom of answer)

        //Base 62 uniqueness metrics

        //5 chars in base 62 will give you 62^5 unique IDs = 916,132,832 (~1 billion) At 10k IDs per day you will be ok for 91k+ days

        //6 chars in base 62 will give you 62^6 unique IDs = 56,800,235,584 (56+ billion) At 10k IDs per day you will be ok for 5+ million days

        //Base 36 uniqueness metrics

        //6 chars will give you 36^6 unique IDs = 2,176,782,336 (2+ billion)

        //7 chars will give you 36^7 unique IDs = 78,364,164,096 (78+ billion)

        private static char[] _base62chars =
        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        .ToCharArray();

        private static Random _random = new Random();

        public static string GetBase62(int length)
        {
            var sb = new StringBuilder(length);

            for (int i = 0; i < length; i++)
                sb.Append(_base62chars[_random.Next(62)]);

            return sb.ToString();
        }

        public static string GetBase36(int length)
        {
            var sb = new StringBuilder(length);

            for (int i = 0; i < length; i++)
                sb.Append(_base62chars[_random.Next(36)]);

            return sb.ToString();
        }
    }
}
