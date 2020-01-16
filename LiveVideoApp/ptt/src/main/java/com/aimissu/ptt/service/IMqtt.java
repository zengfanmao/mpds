package com.aimissu.ptt.service;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;

/**

 */
public interface IMqtt {

    String getServiceUrl();

    String getDeviceId();

    IMqttAsyncClient getMqttClient();

    void connect();

    void subscribeTopics();

    boolean isLogin();

    String getLoginName();

}
