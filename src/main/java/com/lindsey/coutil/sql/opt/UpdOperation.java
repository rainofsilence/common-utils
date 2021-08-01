package com.lindsey.coutil.sql.opt;

import com.lindsey.coutil.sql.dto.OperationResultDTO;
import com.lindsey.coutil.sql.enums.ColTypeEnum;
import com.lindsey.coutil.sql.enums.KeyTypeEnum;

public class UpdOperation extends Operation {
    @Override
    public OperationResultDTO buildSql(String[] items, int lineNumber) {
        OperationResultDTO result = new OperationResultDTO();
        // 检验是否有col
        if (items.length < 4) {
            result.setErrorLineNumber(lineNumber + 2);
            return result;
        }
        StringBuilder sb = new StringBuilder();
        // append header
        sb.append("update ").append(items[1]).append(" set ");
        // append col
        for (int i = 3; i < items.length; i++) {
            String col = buildSqlCol(items[i]);
            if (col == null) {
                result.setErrorLineNumber(lineNumber + 2);
                return result;
            }
            sb.append(col);
            // remove last', '
            if (i == (items.length - 1)) sb.deleteCharAt(sb.length() - 2);
        }
        // append where
        String where = buildSqlWhere(items[2]);
        if (where == null) {
            result.setErrorLineNumber(lineNumber + 2);
            return result;
        }
        sb.append(where);
        result.setSql(sb.toString());
        return result;
    }

    /**
     * <p>生成UPDATE_SQL_COL
     *
     * @param col
     * @return
     */
    private String buildSqlCol(String col) {
        StringBuilder sb = new StringBuilder();
        String[] cols = col.split("::");
        String colType = cols[0].toUpperCase();
        if (cols.length < 3) {
            return null;
        }
        if (ColTypeEnum.STRING.getCode().equals(colType)) {
            sb.append(cols[1]).append(" = '").append(cols[2]).append("', ");
        } else if (ColTypeEnum.NUMBER.getCode().equals(cols[0])) {
            sb.append(cols[1]).append(" = ").append(cols[2]).append(", ");
        } else {
            return null;
        }
        return sb.toString();
    }

    /**
     * <p>生成UPDATE_SQL_WHERE
     *
     * @param where
     * @return
     */
    private String buildSqlWhere(String where) {
        StringBuilder sb = new StringBuilder();
        String[] wheres = where.split("::");
        String keyType = wheres[0].toUpperCase();
        if (wheres.length < 3) {
            return null;
        }
        if (KeyTypeEnum.STRING.getCode().equals(keyType)) {
            sb.append("where ").append(wheres[1]).append(" = '").append(wheres[2]).append("';\r\n");
        } else if (KeyTypeEnum.NUMBER.getCode().equals(keyType)) {
            sb.append("where ").append(wheres[1]).append(" = ").append(wheres[2]).append(";\r\n");
        } else {
            return null;
        }
        return sb.toString();
    }
}
