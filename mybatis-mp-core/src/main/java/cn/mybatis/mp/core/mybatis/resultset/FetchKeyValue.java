package cn.mybatis.mp.core.mybatis.resultset;

import cn.mybatis.mp.db.annotations.ResultEntity;
import cn.mybatis.mp.db.annotations.ResultField;
import lombok.Data;

@Data
@ResultEntity(Void.class)
public class FetchKeyValue {

    @ResultField
    private Object key;

    @ResultField
    private Object value;
}
