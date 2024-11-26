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

package com.mybatis.mp.core.test.model;

import cn.mybatis.mp.db.Model;
import cn.mybatis.mp.db.annotations.ForeignKey;
import com.mybatis.mp.core.test.DO.BaseIDSysUser;
import com.mybatis.mp.core.test.DO.BaseId;
import com.mybatis.mp.core.test.DO.SysRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseIDSysUserModel extends BaseId<Long> implements Model<BaseIDSysUser> {

    private String userName;

    private String password;

    @ForeignKey(SysRole.class)
    private Integer role_id;

    private LocalDateTime create_time;
}
