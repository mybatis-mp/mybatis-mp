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

package db.sql.api.impl.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.executor.ISubQuery;
import db.sql.api.cmd.struct.Joins;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.basic.DatasetField;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.cmd.condition.Exists;
import db.sql.api.impl.cmd.condition.In;
import db.sql.api.impl.cmd.struct.*;
import db.sql.api.impl.cmd.struct.query.*;
import db.sql.api.impl.tookit.Objects;
import db.sql.api.impl.tookit.SqlConst;

public abstract class AbstractSubQuery<SELF extends AbstractSubQuery<SELF, CMD_FACTORY>, CMD_FACTORY extends CmdFactory>
        extends AbstractQuery<SELF, CMD_FACTORY>

        implements ISubQuery<SELF,
        Table,
        TableField,
        DatasetField,
        Cmd,
        Object,
        CMD_FACTORY,
        ConditionChain,
        With,
        Select,
        From,
        Join,
        On,
        Joins<Join>,
        Where,
        GroupBy,
        Having,
        OrderBy,
        Limit,
        ForUpdate,
        Union
        >,
        db.sql.api.cmd.basic.IDataset<SELF, DatasetField> {

    protected String alias;

    public AbstractSubQuery(CMD_FACTORY $) {
        super($);
    }

    public AbstractSubQuery(Where where) {
        super(where);
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public DatasetField $(String columnName) {
        return new DatasetField(this, columnName);
    }

    @Override
    protected <E, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> DATASET_FIELD $(IDataset<DATASET, DATASET_FIELD> dataset, Getter<E> getter) {
        return super.$(dataset, getter);
    }

    @Override
    protected <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> DATASET_FIELD $(IDataset<DATASET, DATASET_FIELD> dataset, String columnName) {
        return super.$(dataset, columnName);
    }

    public DatasetField $outerField(String columnName) {
        return super.$(this, columnName);
    }

    public <E> DatasetField $outerField(Getter<E> getter) {
        return super.$(this, getter);
    }

    protected IDatasetField $refField(AbstractSubQuery root, AbstractSubQuery subQuery, TableField tableField) {
        Select select = subQuery.getSelect();
        for (Cmd cmd : select.getSelectField()) {
            if (cmd instanceof IDatasetField) {
                IDatasetField df = (IDatasetField) cmd;
                if (df.getName().equals(tableField.getName())) {
                    if (df.getTable() == tableField.getTable()) {
                        //同个对象
                        return super.$(this, df.getAlias() == null || df.getAlias().isEmpty() ? df.getName() : df.getAlias());
                    } else if (df instanceof TableField && ((Table) df.getTable()).getName().equals(tableField.getTable().getName())) {
                        //同个表名 列名
                        return super.$(this, df.getAlias() == null || df.getAlias().isEmpty() ? df.getName() : df.getAlias());
                    }
                }
                //如果是子查询字段，则从深度继续查找
                if (df.getTable() instanceof AbstractSubQuery) {
                    AbstractSubQuery subQuery2 = (AbstractSubQuery) df.getTable();
                    IDatasetField datasetField = this.$refField(root, subQuery2, tableField);
                    if (Objects.nonNull(datasetField)) {
                        return datasetField;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 引用字段 会自动深层查找；只能针对那些没有包装过的字段
     *
     * @param getter
     * @param <E>
     * @return
     */
    public <E> IDatasetField $refField(Getter<E> getter) {
        TableField tableField = this.$(getter);
        IDatasetField datasetField = this.$refField(this, this, tableField);
        if (Objects.nonNull(datasetField)) {
            return datasetField;
        }
        throw new RuntimeException("cannot find dataset field");
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (parent instanceof In || parent instanceof Exists || parent instanceof With) {
            return super.sql(module, this, context, sqlBuilder);
        }
        sqlBuilder.append(SqlConst.BRACKET_LEFT);
        super.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        if (this.getAlias() != null) {
            sqlBuilder.append(SqlConst.AS(context.getDbType())).append(this.getAlias());
        }

        return sqlBuilder;
    }


}

