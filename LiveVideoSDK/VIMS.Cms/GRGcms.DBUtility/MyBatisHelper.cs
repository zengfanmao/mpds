using System;
using System.Collections;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using IBatisNet.DataMapper;
using System.IO;
using IBatisNet.Common.Utilities;
using IBatisNet.DataMapper.Configuration;
using IBatisNet.DataMapper.SessionStore;

namespace GRGcms.DBUtility
{
    public sealed class MyBatisHelper
    {
        private static volatile ISqlMapper mapper = null;

        private static string GetConfigPath()
        {
            //string baseDir = Path.GetFullPath(@"../../");
            return ConfigurationManager.AppSettings["mybatispath"];
        }

        public static void Configure(object obj)
        {
            mapper = (ISqlMapper)obj;
            mapper.SessionStore = new HybridWebThreadSessionStore(mapper.Id);
        }

        public static void InitMapper()
        {
            string configPath = GetConfigPath();
            ConfigureHandler hanlder = new ConfigureHandler(Configure);

            DomSqlMapBuilder builder = new DomSqlMapBuilder();
            mapper = builder.ConfigureAndWatch(configPath, hanlder);
        }

        public static ISqlMapper Instance
        {
            get
            {
                try
                {
                    if (mapper == null)
                    {
                        lock (typeof(SqlMapper))
                        {
                            if (mapper == null)
                            {
                                InitMapper();
                            }
                        }
                    }
                    return mapper;
                }
                catch (Exception ex)
                {
                    throw ex;
                }
            }
        }

        public static T QueryForObject<T>(string statementName, object parameterObject)
        {
            T result = default(T);
            try
            {
                result = Instance.QueryForObject<T>(statementName, parameterObject);
                return result;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        public static IList<T> QueryForList<T>(string statementName, object parameterObject)
        {
            IList<T> result = default(IList<T>);
            try
            {
                result = Instance.QueryForList<T>(statementName, parameterObject);
                //result = Instance.QueryForObject<T>(statementName, parameterObject);
                return result;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        public static object InsertObject(string statementName, object parameterObject)
        {
            object result = null;
            try
            {
                result = Instance.Insert(statementName, parameterObject);
                return result;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        public static object DeleteObject(string statementName, object parameterObject)
        {
            object result = null;
            try
            {
                result = Instance.Delete(statementName, parameterObject);
                return result;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }

        public static object UpdateObject(string statementName, object parameterObject)
        {
            object result = null;
            try
            {
                result = Instance.Update(statementName, parameterObject);
                return result;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
    }
}