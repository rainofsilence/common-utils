package com.lindsey.coutil.sql;


import com.lindsey.coutil.sql.dto.OperationResultDTO;
import com.lindsey.coutil.sql.enums.OptTypeEnum;
import com.lindsey.coutil.sql.opt.DelOperation;
import com.lindsey.coutil.sql.opt.InsOperation;
import com.lindsey.coutil.sql.opt.Operation;
import com.lindsey.coutil.sql.opt.UpdOperation;
import com.lindsey.coutil.sql.res.BuildingResult;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>批量生成SQL脚本工具类</p>
 *
 * @author LindseyCheung
 * @version V.1.0.0
 */
public class SqlBatchBuildUtil {

//    private static final Logger logger = LoggerFactory.getLogger(SqlBatchBuildUtil.class);

    private static final String FILE_SUFFIX_CSV_UP = "CSV";
    public static final String DEFAULT_FILE_PATH_DATA = "src/main/resources/data_source.csv";
    public static final String DEFAULT_FILE_PATH_SQL = "src/main/resources/sql_result.sql";

    /**
     * <p>默认创建方法
     * <p>dataFilePath: src/main/resources/data_source.CSV
     * <p>sqlFilePath: src/main/resources/sql_result.sql
     *
     * @return BuildingResult<Object>
     */
    public static BuildingResult<Object> defaultBuilding() {
        return building(DEFAULT_FILE_PATH_DATA, DEFAULT_FILE_PATH_SQL);
    }

    /**
     * @param dataFilePath String 源数据文件路径
     * @param sqlFilePath  String SQL文件路径
     * @see com.lindsey.coutil.sql.enums.OptTypeEnum 操作类型
     * @see com.lindsey.coutil.sql.enums.KeyTypeEnum 主键类型
     * @see com.lindsey.coutil.sql.enums.ColTypeEnum 字段类型
     */
    public static BuildingResult<Object> building(String dataFilePath, String sqlFilePath) {
        // file_path cannot be empty
        if (StringUtils.isAnyEmpty(dataFilePath, sqlFilePath))
            return BuildingResult.failed("DataFilePath or SqlFilePath is empty!");
        // data_file default is .CSV
        if (!isCSV(dataFilePath)) return BuildingResult.failed("DataFilePath must be .CSV!");
        // verify data_file authenticity
        File originFile = new File(dataFilePath);
        if (!originFile.exists()) {
            return BuildingResult.failed("dataFile is not exist.");
        }

        // 读取数据
        List<String> originList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(originFile));
            // 行内容
            String line = "";
            StringBuilder resultSql = new StringBuilder();
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                // 第一行不操作
                if (lineNumber == 0) {
                    lineNumber++;
                    continue;
                }
                originList.add(line);
                lineNumber++;
            }
            // SQL结果写入文件
        } catch (IOException e) {
            return BuildingResult.failed(e.toString());
        }

        // TODO 2021.08.01 修改为多线程
        // 数据操作
        StringBuffer resultSql = new StringBuffer();
        List<String> errorLineNumbers = new ArrayList<>();
        for (int i = 0; i < originList.size(); i++) {

            // s*:替换大部分空白字符，不限于空格
            String[] items = originList.get(i).replaceAll("\\s*", "").split(",");
            if (items.length < 3) {
                errorLineNumbers.add(String.valueOf(i + 2));
                continue;
            }
            String optType = items[0].toUpperCase();
            if (OptTypeEnum.INSERT.getCode().equals(optType)) {
                Operation ins = new InsOperation();
                OperationResultDTO iOrDTO = ins.buildSql(items, i);
                if (iOrDTO.getErrorLineNumber() != null)
                    errorLineNumbers.add(String.valueOf(iOrDTO.getErrorLineNumber()));
                if (iOrDTO.getSql() != null) resultSql.append(iOrDTO.getSql());
            } else if (OptTypeEnum.UPDATE.getCode().equals(optType)) {
                Operation upd = new UpdOperation();
                OperationResultDTO uOrDTO = upd.buildSql(items, i);
                if (uOrDTO.getErrorLineNumber() != null)
                    errorLineNumbers.add(String.valueOf(uOrDTO.getErrorLineNumber()));
                if (uOrDTO.getSql() != null) resultSql.append(uOrDTO.getSql());
            } else if (OptTypeEnum.DELETE.getCode().equals(optType)) {
                Operation del = new DelOperation();
                OperationResultDTO dOrDTO = del.buildSql(items, i);
                if (dOrDTO.getErrorLineNumber() != null)
                    errorLineNumbers.add(String.valueOf(dOrDTO.getErrorLineNumber()));
                if (dOrDTO.getSql() != null)
                    resultSql.append(dOrDTO.getSql());
            }
        }

        // 写入数据
        FileWriter fw = null;
        try {

            fw = new FileWriter(sqlFilePath);
            fw.write(resultSql.toString());
            fw.close();
        } catch (IOException e) {
            return BuildingResult.failed(e.toString());
        }

        // 生成错误行信息
        if (errorLineNumbers.size() > 0) {
            String linesStr = StringUtils.join(errorLineNumbers, ",");
            String sbErr = "These lines is error, [" +
                    linesStr + "].";
            System.out.println(sbErr);
        }
        return BuildingResult.success();
    }

    /**
     * <p>判断输入数据文件是否为CSV
     *
     * @param dataFilePath
     * @return
     */
    private static boolean isCSV(String dataFilePath) {
        String[] fileSuffixs = dataFilePath.split("\\.");
        String fileSuffix = fileSuffixs[fileSuffixs.length - 1].toUpperCase();
        return FILE_SUFFIX_CSV_UP.equals(fileSuffix);
    }
}
