package com.iotinall.canteen.service;

import com.iotinall.canteen.common.util.FileHandler;
import com.iotinall.canteen.entity.BackupLibraryRecord;
import com.iotinall.canteen.repository.BackupLibraryRecordRepository;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CreateSqlFileService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private BackupLibraryRecordRepository backupLibraryRecordRepository;

    @Resource
    private FileHandler fileHandler;

    public final static String basePath = System.getProperty("user.dir");

    public final static String filePath = basePath + File.separator + "backupfile";

    @Async(value = "insertsqlExec")
    public Future<Boolean> exportInsertSql(String tableName, String fileName, int pageSize) {
        String sqlFrom = "select * from " + tableName;
        String sql = sqlFrom + " limit" + " ?," + pageSize;
        RowCountCallbackHandler rowCountCallbackHandler = new RowCountCallbackHandler();
        jdbcTemplate.query(sqlFrom + " limit 1", rowCountCallbackHandler);
        String[] columnName = rowCountCallbackHandler.getColumnNames();
        if (columnName == null) {
            return new AsyncResult<>(Boolean.TRUE);
        }
        List<String> allInsert = new ArrayList<>();

        int page = 0;
        while (true) {
            StringBuilder insertSql = new StringBuilder().append(" INSERT INTO ").append(tableName).append("( `").append(String.join("`,`", columnName)).append("` ) VALUES ");
            List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, page * pageSize);
            List<String> insertBody = new ArrayList<>();
            for (Map<String, Object> datum : data) {
                List<String> values = new ArrayList<>(datum.keySet().size());
                for (String column : columnName) {
                    if (datum.get(column) != null) {
                        values.add(typeChange(datum.get(column)));
                    } else {
                        values.add(null);
                    }
                }

                insertBody.add("(" + values.stream().map(item -> {
                    if (item == null) {
                        return "null";
                    } else {
                        return item ;
                    }
                }).collect(Collectors.joining(",")) + ")");
            }

            insertSql.append(String.join(",", insertBody));

            allInsert.add(insertSql.toString());

            if (data.size() < pageSize) {
                break;
            }
            page++;
        }

        createFile(fileName, filePath, allInsert);

        return new AsyncResult<>(Boolean.TRUE);
    }

    /**
     * 压缩成zip
     */
    @Transactional(rollbackFor = Exception.class)
    public void compressionZip(String password){
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(CompressionMethod.DEFLATE);//压缩方式
        parameters.setCompressionLevel(CompressionLevel.ULTRA); // 压缩级别 9
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(EncryptionMethod.AES);//加密方式
        parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
        try {
            ZipFile zipFile = new ZipFile(basePath + File.separator + "backupfile.zip", password.toCharArray());
            zipFile.setCharset(StandardCharsets.UTF_8);
            zipFile.addFolder(new File(filePath), parameters);
            //生成后文件
            File file = zipFile.getFile();

            String size = readableFileSize(file.length());

            String sqlFile = fileHandler.saveFile("", file);
            deleteFile(new File(filePath));
            file.delete();

            BackupLibraryRecord record = new BackupLibraryRecord()
                    .setFile(sqlFile)
                    .setSize(size)
                    .setLibraryName(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "数据备份");
            this.backupLibraryRecordRepository.save(record);

        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    private String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * 类型值转换修改
     * @return
     */
    private String typeChange(Object value){
        if(value instanceof Boolean){
            Boolean booValue = (Boolean)value;
            if(booValue){
                return "b'1'";
            }else{
                return "b'0'";
            }
        }
        return "'" + value.toString() + "'";
    }

    /**
     * 创建sql文件
     */
    public void createFile(String dataBase, String path, List<String> createTableSqls) {
        try {
            File file = new File(path + File.separator + dataBase + ".sql");
            FileOutputStream fos = new FileOutputStream(file, true);
            FileChannel channel = fos.getChannel();

            StringBuilder content = new StringBuilder();
            for (String createTableSql : createTableSqls) {
                content.append(createTableSql).append(";\r\n");
            }
            ByteBuffer buf = ByteBuffer.wrap(content.toString().getBytes());
            buf.put(content.toString().getBytes());
            buf.flip();
            channel.write(buf);
            channel.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteFile(File dirFile) {
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {
            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }
        return dirFile.delete();
    }
}
