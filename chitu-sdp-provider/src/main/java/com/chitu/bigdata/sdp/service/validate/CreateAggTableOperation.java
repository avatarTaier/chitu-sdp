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

import com.chitu.bigdata.sdp.service.validate.custom.CustomTableEnvironmentImpl;
import org.apache.flink.table.api.Table;

import java.util.List;

public class CreateAggTableOperation extends AbstractOperation implements Operation{

    private String KEY_WORD = "CREATE AGGTABLE";

    public CreateAggTableOperation() {
    }

    public CreateAggTableOperation(String statement) {
        super(statement);
    }

    @Override
    public String getHandle() {
        return KEY_WORD;
    }

    @Override
    public Operation create(String statement) {
        return new CreateAggTableOperation(statement);
    }

    @Override
    public void build(CustomTableEnvironmentImpl stEnvironment) {
        AggTable aggTable = AggTable.build(statement);
        Table source = stEnvironment.sqlQuery("select * from "+ aggTable.getTable());
        List<String> wheres = aggTable.getWheres();
        if(wheres!=null&&wheres.size()>0) {
            for (String s : wheres) {
                source = source.filter(s);
            }
        }
        Table sink = source.groupBy(aggTable.getGroupBy())
                .flatAggregate(aggTable.getAggBy())
                .select(aggTable.getColumns());
        stEnvironment.registerTable(aggTable.getName(), sink);
    }
}
