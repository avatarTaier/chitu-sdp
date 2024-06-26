/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chitu.bigdata.sdp.service.validate;

import com.chitu.bigdata.sdp.service.validate.util.Asserts;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SystemConfiguration {

    private static volatile SystemConfiguration systemConfiguration = new SystemConfiguration();
    public static SystemConfiguration getInstances() {
        return systemConfiguration;
    }
    private static final List<Configuration> CONFIGURATION_LIST = new ArrayList<Configuration>(){{
        add(systemConfiguration.sqlSubmitJarPath);
        add(systemConfiguration.sqlSubmitJarParas);
        add(systemConfiguration.sqlSubmitJarMainAppClass);
        add(systemConfiguration.useRestAPI);
        add(systemConfiguration.sqlSeparator);
    }};

    private Configuration sqlSubmitJarPath = new Configuration(
            "sqlSubmitJarPath",
            "FlinkSQL提交Jar路径",
            ValueType.STRING,
            "hdfs:///dlink/jar/dlink-app.jar",
            "用于指定Applcation模式提交FlinkSQL的Jar的路径"
    );
    private Configuration sqlSubmitJarParas = new Configuration(
            "sqlSubmitJarParas",
            "FlinkSQL提交Jar参数",
            ValueType.STRING,
            "",
            "用于指定Applcation模式提交FlinkSQL的Jar的参数"
    );
    private Configuration sqlSubmitJarMainAppClass = new Configuration(
            "sqlSubmitJarMainAppClass",
            "FlinkSQL提交Jar主类",
            ValueType.STRING,
            "com.dlink.app.MainApp",
            "用于指定Applcation模式提交FlinkSQL的Jar的主类"
    );
    private Configuration useRestAPI = new Configuration(
            "useRestAPI",
            "使用 RestAPI",
            ValueType.BOOLEAN,
            true,
            "在运维 Flink 任务时是否使用 RestAPI"
    );
    private Configuration sqlSeparator = new Configuration(
            "sqlSeparator",
            "FlinkSQL语句分割符",
            ValueType.STRING,
            ";",
            "Flink SQL 的语句分割符"
    );

    public void setConfiguration(JsonNode jsonNode){
        for(Configuration item : CONFIGURATION_LIST){
            if(!jsonNode.has(item.getName())){
                continue;
            }
            switch (item.getType()){
                case BOOLEAN:
                    item.setValue(jsonNode.get(item.getName()).asBoolean());
                    break;
                case INT:
                    item.setValue(jsonNode.get(item.getName()).asInt());
                    break;
                default:
                    item.setValue(jsonNode.get(item.getName()).asText());
            }
        }
    }

    public void addConfiguration(Map<String,Object> map){
        for(Configuration item : CONFIGURATION_LIST){
            if(map.containsKey(item.getName())&&item.getType().equals(ValueType.BOOLEAN)){
                map.put(item.getName(), Asserts.isEqualsIgnoreCase("true",map.get(item.getName()).toString()));
            }
            if(!map.containsKey(item.getName())) {
                map.put(item.getName(), item.getValue());
            }
        }
    }

    public String getSqlSubmitJarParas() {
        return sqlSubmitJarParas.getValue().toString();
    }

    public void setSqlSubmitJarParas(String sqlSubmitJarParas) {
        this.sqlSubmitJarParas.setValue(sqlSubmitJarParas);
    }

    public String getSqlSubmitJarPath() {
        return sqlSubmitJarPath.getValue().toString();
    }

    public void setSqlSubmitJarPath(String sqlSubmitJarPath) {
        this.sqlSubmitJarPath.setValue(sqlSubmitJarPath);
    }

    public String getSqlSubmitJarMainAppClass() {
        return sqlSubmitJarMainAppClass.getValue().toString();
    }

    public void setSqlSubmitJarMainAppClass(String sqlSubmitJarMainAppClass) {
        this.sqlSubmitJarMainAppClass.setValue(sqlSubmitJarMainAppClass);
    }

    public boolean isUseRestAPI() {
        return (boolean) useRestAPI.getValue();
    }

    public void setUseRestAPI(boolean useRestAPI) {
        this.useRestAPI.setValue(useRestAPI);
    }

    public String getSqlSeparator() {
        return sqlSeparator.getValue().toString();
    }

    public void setSqlSeparator(String sqlSeparator) {
        this.sqlSeparator.setValue(sqlSeparator);
    }

    enum ValueType{
        STRING,INT,DOUBLE,FLOAT,BOOLEAN,DATE
    }
}
