package com.yuntu.mybatis.hilo;

import org.apache.ibatis.jdbc.SqlRunner;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 高地位算法 id 生成器
 * Created by  on 2015/3/5.
 */
public class SequenceHiLoGenerator {

    private DataSource dataSource;
    //表名
    private String table;
    //值的列名
    private String column;
    //增量
    private long increment;

    //当前值
    private AtomicLong value;
    //最大值
    private Long max;

    public SequenceHiLoGenerator(){

    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public long getIncrement() {
        return increment;
    }

    public void setIncrement(long increment) {
        this.increment = increment;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long next() throws SQLException {
        if( null == value || null == max || value.addAndGet(1) > max) {
            init();
        }
        return value.get();
    }

    public synchronized void init() throws SQLException {

        SqlRunner sqlRunner = new SqlRunner(dataSource.getConnection());
        try {
            //sql 执行者
            sqlRunner=new SqlRunner(dataSource.getConnection());
            //开始事务代码块
            sqlRunner.run("BEGIN");



            //获取当前ID表中的数值, 并且锁表,对 SELECT 可见 ,SELECT FOR UPDATE 阻塞,直到本事物提交
            String queryForUpdate=MessageFormat.format("SELECT * FROM {0} FOR UPDATE",this.table);
            Map<String,Object> objectMap=sqlRunner.selectOne(queryForUpdate);
            //当前ID表中的数值
            this.value =new AtomicLong(((Number)objectMap.get(this.column)).longValue());

            //计算加上步长后的ID数值
            this.max = this.value.get() + this.getIncrement();
            //当前ID表中数值加1
            this.value.addAndGet(1);
            //更新
            String update=MessageFormat.format("UPDATE {0} SET {1} = ?",this.table,this.column);
            if( sqlRunner.update(update,this.max) <1 ){
                throw new RuntimeException("更新错误!");
            };



            //提交
            sqlRunner.run("COMMIT");
        }catch (Exception e){
            e.printStackTrace();
            //回滚
            sqlRunner.run("rollback");
        }finally {
            if( null != sqlRunner ){//关闭连接
                sqlRunner.closeConnection();
            }
        }
    }
}
