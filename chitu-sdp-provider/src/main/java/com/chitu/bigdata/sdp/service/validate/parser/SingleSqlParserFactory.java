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
package com.chitu.bigdata.sdp.service.validate.parser;

import com.chitu.bigdata.sdp.api.enums.ResponseCode;
import com.chitu.cloud.exception.ApplicationException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SingleSqlParserFactory {

    public static Map<String,List<String>> generateParser(String sql) {
        BaseSingleSqlParser tmp = null;
        sql = sql.replace("\r\n"," ").replace("\n"," ") +" ENDOFSQL";
        if (contains(sql, "(insert\\s+into)(.+)(select)(.+)(from)(.+)")) {
            tmp = new InsertSelectSqlParser(sql);
        } else if (contains(sql, "(create\\s+aggtable)(.+)(as\\s+select)(.+)")) {
            tmp = new CreateAggTableSelectSqlParser(sql);
        } else if (contains(sql, "(select)(.+)(from)(.+)")) {
            tmp = new SelectSqlParser(sql);
        } else if (contains(sql, "(delete\\s+from)(.+)")) {
            tmp = new DeleteSqlParser(sql);
        } else if (contains(sql, "(update)(.+)(set)(.+)")) {
            tmp = new UpdateSqlParser(sql);
        } else if (contains(sql, "(insert\\s+into)(.+)(values)(.+)")) {
            tmp = new InsertSqlParser(sql);
        } else if (contains(sql, "(create\\s+table)(.+)")) {
        } else if (contains(sql, "(create\\s+database)(.+)")) {
        } else if (contains(sql, "(show\\s+databases)")) {
        } else if (contains(sql, "(use)(.+)")) {
        } else if (contains(sql, "(set)(.+)")) {
            tmp = new SetSqlParser(sql);
        } else {
        }
        if (Objects.isNull(tmp)){
            throw new ApplicationException(ResponseCode.ERROR.getCode(),"sql有误或者不支持该类型语句");
        }
        return tmp.splitSql2Segment();
    }

    /**
     * 看word是否在lineText中存在，支持正则表达式
     *
     * @param sql:要解析的sql语句
     * @param regExp:正则表达式
     * @return
     **/
    private static boolean contains(String sql, String regExp) {
        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        return matcher.find();
    }
}

