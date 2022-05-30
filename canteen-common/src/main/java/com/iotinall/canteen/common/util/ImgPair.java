package com.iotinall.canteen.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * 用法
 * \@JsonSerialize(using = ImgPair.ImgSerializer.class)
 * \@JsonDeserialize(using = ImgPair.ImgDeserializer.class)
 *
 * @author xin-bing
 * @date 10/31/2019 13:54
 */
@Component
@RefreshScope
public class ImgPair {
    private static String _fileServer = "";

    public static String getFileServer() {
        return _fileServer;
    }

    @Value("${filemanage.file_url}")
    private String alyFileUrl;

    @Value("${fastdfs.file_url}")
    private String fdfsFileUrl;

    @Resource
    private FileHandler fileHandler;

    @PostConstruct
    public void init(){
        if(fileHandler instanceof FileHandlerAly){
            _fileServer = alyFileUrl;
        }else if(fileHandler instanceof FileHandlerFast){
            _fileServer = fdfsFileUrl;
        }
    }

    /**
     * 自定义的json解码器
     */
    public static class ImgDeserializer extends JsonDeserializer<String> {
        public ImgDeserializer() {
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String text = p.getText();
            if (text.startsWith(_fileServer) && !StringUtils.isBlank(text)) {
                return text.substring(_fileServer.length());
            }
            return text;
        }
    }

    /**
     * 自定义的json序列化器
     */
    public static class ImgSerializer extends JsonSerializer<String> {
        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (!StringUtils.isBlank(value)) {
                gen.writeString(_fileServer + value);
            } else {
                gen.writeString(value);
            }
        }
    }
}
