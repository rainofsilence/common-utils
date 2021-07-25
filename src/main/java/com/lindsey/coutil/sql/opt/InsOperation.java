package com.lindsey.coutil.sql.opt;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.lindsey.coutil.sql.dto.InsColResultDTO;
import com.lindsey.coutil.sql.dto.OperationResultDTO;
import com.lindsey.coutil.sql.enums.ColTypeEnum;
import com.lindsey.coutil.sql.enums.KeyTypeEnum;

/**
 * @author LindseyCheung
 * @version 1.0.1
 */
public class InsOperation extends Operation {

    private static final SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Override
    public OperationResultDTO buildSql(String[] items, int lineNumber) {
        OperationResultDTO result = new OperationResultDTO();
        // 检验是否有col
        if (items.length < 4) {
            result.setErrorLineNumber(lineNumber + 1);
            return result;
        }
        StringBuilder sb = new StringBuilder();
        // append header
        sb.append("insert into ").append(items[1]);
        // append col
        StringBuilder sbColName = new StringBuilder().append(" (");
        StringBuilder sbColValue = new StringBuilder().append(" (");
        // generate keyId
        InsColResultDTO keyResult = buildSqlKey(items[2]);
        if (keyResult.getColName() == null
                && !KeyTypeEnum.AUTO_INCREMENT.getCode().equals(keyResult.getColType())) {
            result.setErrorLineNumber(lineNumber + 1);
            return result;
        }
        if (!KeyTypeEnum.AUTO_INCREMENT.getCode().equals(keyResult.getColType())) {
            sbColName.append(keyResult.getColName()).append(", ");
            sbColValue.append(keyResult.getColValue()).append(", ");
        }
        // generate col
        for (int i = 3; i < items.length; i++) {
            InsColResultDTO colResult = buildSqlCol(items[i]);
            if (colResult.getColName() == null) {
                result.setErrorLineNumber(lineNumber + 1);
                return result;
            }
            sbColName.append(colResult.getColName()).append(", ");
            sbColValue.append(colResult.getColValue()).append(", ");
            if (i == (items.length - 1)) {
                sbColName.deleteCharAt(sbColName.length() - 1);
                sbColName.deleteCharAt(sbColName.length() - 1);
                sbColValue.deleteCharAt(sbColValue.length() - 1);
                sbColValue.deleteCharAt(sbColValue.length() - 1);
                sbColName.append(") ");
                sbColValue.append(")");
            }
        }
        // 拼接
        sb.append(sbColName).append("values").append(sbColValue).append(";\r\n");
        result.setSql(sb.toString());
        return result;
    }

    private InsColResultDTO buildSqlKey(String key) {
        InsColResultDTO result = new InsColResultDTO();
        String[] keys = key.split("::");
        if (keys.length < 2) {
            return result;
        }
        String keyType = keys[0].toUpperCase();
        if (KeyTypeEnum.AUTO_INCREMENT.getCode().equals(keyType)) {
            result.setColType(KeyTypeEnum.AUTO_INCREMENT.getCode());
        } else if (KeyTypeEnum.SNOWFLAKE.getCode().equals(keyType)) {
            result.setColType(KeyTypeEnum.SNOWFLAKE.getCode());
            result.setColName(keys[1]);
            result.setColValue("'" + snowflakeGenerator.next() + "'");
        }
        return result;
    }

    private InsColResultDTO buildSqlCol(String col) {
        InsColResultDTO result = new InsColResultDTO();
        String[] cols = col.split("::");
        if (cols.length < 3) {
            return result;
        }
        String colType = cols[0].toUpperCase();
        if (ColTypeEnum.STRING.getCode().equals(colType)) {
            result.setColType(ColTypeEnum.STRING.getCode());
            result.setColName(cols[1]);
            result.setColValue("'" + cols[2] + "'");
        } else if (ColTypeEnum.NUMBER.getCode().equals(colType)) {
            result.setColType(ColTypeEnum.NUMBER.getCode());
            result.setColName(cols[1]);
            result.setColValue(cols[2]);
        }
        return result;
    }
}
