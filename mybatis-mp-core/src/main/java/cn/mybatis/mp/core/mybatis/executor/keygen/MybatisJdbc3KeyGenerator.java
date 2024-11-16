package cn.mybatis.mp.core.mybatis.executor.keygen;

import cn.mybatis.mp.core.mybatis.mapper.context.SQLCmdInsertContext;
import cn.mybatis.mp.core.mybatis.mapper.context.SetIdMethod;
import db.sql.api.DbType;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class MybatisJdbc3KeyGenerator extends Jdbc3KeyGenerator {

    public static final MybatisJdbc3KeyGenerator INSTANCE = new MybatisJdbc3KeyGenerator();

    @Override
    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        if (parameter instanceof SetIdMethod) {
            final String[] keyProperties = ms.getKeyProperties();
            if (keyProperties == null || keyProperties.length == 0) {
                return;
            }

            SetIdMethod setIdMethod = (SetIdMethod) parameter;
            if (setIdMethod.idHasValue()) {
                return;
            }
            final Configuration configuration = ms.getConfiguration();
            if (setIdMethod.getInsertSize() > 1) {
                SQLCmdInsertContext insertContext = (SQLCmdInsertContext) parameter;
                if (insertContext.getDbType() == DbType.SQL_SERVER && insertContext.sql(insertContext.getDbType()).contains("OUTPUT INSERTED")) {
                    try (ResultSet rs = stmt.getResultSet()) {
                        if (rs != null) {
                            this.assignSQLServerKeys(configuration, rs, setIdMethod);
                            return;
                        }
                    } catch (Exception e) {
                        throw new ExecutorException("Error getting generated key or setting result to parameter object. Cause: " + e, e);
                    }
                }
            }
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                final ResultSetMetaData rsmd = rs.getMetaData();

                if (rsmd.getColumnCount() < keyProperties.length) {
                    // Error?
                } else {
                    this.assignKeys(configuration, rs, setIdMethod);
                }
            } catch (Exception e) {
                throw new ExecutorException("Error getting generated key or setting result to parameter object. Cause: " + e, e);
            }
            return;
        }
        super.processAfter(executor, ms, stmt, parameter);
    }

    private void assignSQLServerKeys(Configuration configuration, ResultSet rs, SetIdMethod setIdMethod) throws SQLException {
        int insertSize = setIdMethod.getInsertSize();
        for (int i = 0; i < insertSize; i++) {
            rs.next();
            setIdMethod.setId(setIdMethod.getIdTypeHandler(configuration).getResult(rs, setIdMethod.getIdColumnName()), i);
        }
    }

    private void assignKeys(Configuration configuration, ResultSet rs, SetIdMethod setIdMethod) throws SQLException {
        int insertSize = setIdMethod.getInsertSize();
        for (int i = 0; i < insertSize; i++) {
            rs.next();
            setIdMethod.setId(setIdMethod.getIdTypeHandler(configuration).getResult(rs, 1), i);
        }
    }
}
