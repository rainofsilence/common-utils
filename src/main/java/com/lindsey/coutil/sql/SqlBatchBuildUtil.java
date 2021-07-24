package com.lindsey.coutil.sql;


import com.lindsey.coutil.sql.dto.OperationResultDTO;
import com.lindsey.coutil.sql.opt.DelOperation;
import com.lindsey.coutil.sql.opt.InsOperation;
import com.lindsey.coutil.sql.opt.Operation;
import com.lindsey.coutil.sql.opt.UpdOperation;
import com.lindsey.coutil.sql.res.BuildingResult;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <p>批量生成SQL脚本工具类</p>
 *
 * @author LindseyCheung
 * @version V.1.0.0
 */
public class SqlBatchBuildUtil {

//    private static final Logger logger = LoggerFactory.getLogger(SqlBatchBuildUtil.class);

    private static final String FILE_SUFFIX_CSV_UP = "CSV";

    private static final String OPT_TYPE_INSERT = "INS";
    private static final String OPT_TYPE_UPDATE = "UPD";
    private static final String OPT_TYPE_DELETE = "DEL";

    private static final String COL_TYPE_STRING = "S";
    private static final String COL_TYPE_NUMBER = "N";
    private static final String COL_TYPE_DATETIME = "DT";

    /**
     * 主键自增
     */
    private static final String KEY_TYPE_AUTO = "AI";
    /**
     * 雪花
     */
    private static final String KEY_TYPE_SNOWFLAKES = "SF";
    private static final String KEY_TYPE_STRING = "S";
    private static final String KEY_TYPE_NUMBER = "N";


    /**
     * @param dataFilePath String 源数据文件路径
     * @param sqlFilePath  String SQL文件路径
     * @see com.lindsey.coutil.sql.enums.OptTypeEnum 操作类型
     * @see com.lindsey.coutil.sql.enums.KeyTypeEnum 主键类型
     * @see com.lindsey.coutil.sql.enums.ColTypeEnum 字段类型
     */
    public static BuildingResult<Object> building(String dataFilePath, String sqlFilePath) {
        if (StringUtils.isAnyEmpty(dataFilePath, sqlFilePath) || dataFilePath.length() < 5) {
            return BuildingResult.failed("DataFilePath or SqlFilePath is bad path.");
        }
        // 默认CSV文件
        String[] fileSuffixs = dataFilePath.split("\\.");
        String fileSuffix = fileSuffixs[fileSuffixs.length - 1].toUpperCase(Locale.ROOT);
        if (!FILE_SUFFIX_CSV_UP.equals(fileSuffix)) {
            return BuildingResult.failed("DataFilePath must be .CSV.");
        }
        // 判断数据文件是否存在
        File originFile = new File(dataFilePath);
        if (!originFile.exists()) {
            return BuildingResult.failed("dataFile is not exist.");
        }
        // 读取数据
        try {
            BufferedReader br = new BufferedReader(new FileReader(originFile));
            // 行内容
            String line = "";
            StringBuilder resultSql = new StringBuilder();
            int lineNumber = 0;
            // 错误行数
            List<String> errorLineNumbers = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                // 第一行不操作
                if (lineNumber == 0) {
                    lineNumber++;
                    continue;
                }
                // s*:替换大部分空白字符，不限于空格
                String[] items = line.replaceAll("\\s*", "").split(",");
                if (items.length < 3) {
                    errorLineNumbers.add(Integer.toString(lineNumber + 1));
                    continue;
                }
                String optType = items[0].toUpperCase();
                switch (optType) {
                    case OPT_TYPE_INSERT:
                        Operation ins = new InsOperation();
                        OperationResultDTO iOrDTO = ins.buildSql(items, lineNumber);
                        if (iOrDTO.getErrorLineNumber() != null)
                            errorLineNumbers.add(String.valueOf(iOrDTO.getErrorLineNumber()));
                        resultSql.append(iOrDTO.getSql());
                        break;
                    case OPT_TYPE_UPDATE:
                        Operation upd = new UpdOperation();
                        OperationResultDTO uOrDTO = upd.buildSql(items, lineNumber);
                        if (uOrDTO.getErrorLineNumber() != null)
                            errorLineNumbers.add(String.valueOf(uOrDTO.getErrorLineNumber()));
                        resultSql.append(uOrDTO.getSql());
                        break;
                    case OPT_TYPE_DELETE:
                        Operation del = new DelOperation();
                        OperationResultDTO dOrDTO = del.buildSql(items, lineNumber);
                        if (dOrDTO.getErrorLineNumber() != null)
                            errorLineNumbers.add(String.valueOf(dOrDTO.getErrorLineNumber()));
                        resultSql.append(dOrDTO.getSql());
                        break;
                    default:
                        break;
                }
            }
            // SQL结果写入文件
            FileWriter fw = null;
            fw = new FileWriter(sqlFilePath);
            fw.write(resultSql.toString());
            fw.close();

            // 生成错误行信息
            if (errorLineNumbers.size() > 0) {
                String linesStr = StringUtils.join(errorLineNumbers, ",");
                String sbErr = "These lines is error, [" +
                        linesStr + "].";
                System.out.println(sbErr);
            }
        } catch (IOException e) {
            return BuildingResult.failed(e.toString());
        }
        return BuildingResult.success();
    }

    public static void main(String[] args) {
        SqlBatchBuildUtil.building("src/main/resources/data-demo.CSV", "src/main/resources/result.sql");
    }
}
