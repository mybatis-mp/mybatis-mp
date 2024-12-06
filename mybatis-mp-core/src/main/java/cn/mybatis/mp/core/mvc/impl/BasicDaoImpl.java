/*
 *  Copyright (c) 2024-2024, Ai东 (abc-127@live.cn).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License").
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package cn.mybatis.mp.core.mvc.impl;

import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;

/**
 * 专门给不想写Mapper的人用
 *
 * @param <T>
 * @param <ID>
 */
public class BasicDaoImpl<T, ID> extends BaseDaoImpl<BasicMapper, T, ID> {

    public BasicDaoImpl() {

    }

    public BasicDaoImpl(BasicMapper mapper) {
        this.setMapper(mapper);
    }

    @Override
    protected BasicMapper getBasicMapper() {
        return getMapper();
    }
}
