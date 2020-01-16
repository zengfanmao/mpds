using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using VIMS.LiveVideoSDK.Core;
using VIMS.LiveVideoSDK.Core.Messages;

namespace VIMS.LiveVideoSDK.Bll.Common
{
    public class MqttClientHelper
    {
        private readonly static object locker = new Object();
        private static MqttClientHelper helper;
        public static MqttClientHelper getInstance()
        {
            lock(locker)
            {
                if (helper == null)
                {
                    helper = new MqttClientHelper();
                }
                return helper;
            }
        }

        private BlockingCollection<MqttClient> connections = new BlockingCollection<MqttClient>();
        private readonly int maxConnections = Convert.ToInt32(Bll.Common.Utils.GetAppSetting("mqtt_client_pool_size"));
        private int count = 0;

        public MqttClient getConnection()
        {
            MqttClient client = null;
            while (client == null)
            {
                connections.TryTake(out client, 0);
                if (client != null)
                {
                    LogHelper.WriteInfo(typeof(MqttClientHelper), "Get Mqtt Connection" + count);
                    if (!client.IsConnected)
                    {
                        string clientId = Guid.NewGuid().ToString();
                        client.Connect(clientId);
                    }
                    return client;
                }

                if (count < maxConnections)
                {
                    if (Interlocked.Increment(ref count) <= maxConnections)
                    {
                        LogHelper.WriteInfo(typeof(MqttClientHelper), "Create Mqtt Connection" + count);
                        client = new MqttClient(IPAddress.Parse(Bll.Common.Utils.GetAppSetting("LiveVideoSDK_IM_Ip")));
                        client.MqttMsgPublishReceived += publishReceiveEvent;
                        string clientId = Guid.NewGuid().ToString();
                        client.Connect(clientId);
                        return client;
                    }
                    else
                    {
                        Interlocked.Decrement(ref count);
                    }
                }

                bool isTake = connections.TryTake(out client, 10000);
                if (!isTake || !client.IsConnected)
                {
                    LogHelper.WriteInfo(typeof(MqttClientHelper), "Mqtt Connection Timeout");
                    throw new Exception("Mqtt Connection Timeout...");
                }
            }
            return client;
        }

        public void release(MqttClient client)
        {
            LogHelper.WriteInfo(typeof(MqttClientHelper), "Release Mqtt Connection");
            if (client == null)
            {
                return;
            }
            if (!client.IsConnected)
            {
                Interlocked.Decrement(ref count);
                return;
            }
            connections.Add(client);
            Interlocked.Decrement(ref count);
        }

        private void publishReceiveEvent(object sender, MqttMsgPublishEventArgs e)
        {
            LogHelper.WriteInfo(typeof(MqttClientHelper), "Publish Received : " + e.Message.ToString());
        }
    }
}
