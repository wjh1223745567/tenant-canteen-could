package com.iotinall.canteen.common.configuration;

import com.alibaba.nacos.common.utils.ExceptionUtil;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.netflix.client.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;

/**
 * restController 统一异常处理
 *
 * @author xin-bing
 * @date 10/17/2019 15:40
 */
@Slf4j
@RestControllerAdvice(basePackages = {"com.iotinall.canteen"})
public class RestControllerExceptionHandler {
    // 处理业务业务异常
    @ResponseBody
    @ExceptionHandler({BizException.class})
    public <T> ResultDTO<T> handleBusinessException(HttpServletRequest request, BizException be) {
        return ResultDTO.failed(be.getCode(), be.getMsg());
    }

    // 处理jsr303参数校验异常
    @ResponseBody
    @ExceptionHandler({BindException.class})
    public <T> ResultDTO<T> handleBindException(HttpServletRequest request, BindException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().get(0);
        return ResultDTO.failed("400", objectError.getDefaultMessage());
    }

    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public <T> ResultDTO<T> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().get(0);
        return ResultDTO.failed("400", objectError.getDefaultMessage());
    }

    // 处理spring security 异常
    @ResponseBody
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException exception) {
        log.debug("spring security exception:", exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    // 处理其它exception
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleException(HttpServletRequest request, Exception e) {
        if(e.getCause() instanceof ClientException){
            ClientException clientException = (ClientException)e.getCause();
            if(clientException.getCause() instanceof BizException){
                BizException bizException = (BizException) clientException.getCause();
                return ResultDTO.failed(bizException.getCode(), bizException.getMessage());
            }
        }
        logError(request, e);
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>("访问服务器失败", status);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

    private void logError(HttpServletRequest request, Exception e) {
        String requestString = request.getQueryString();
        if (requestString == null || requestString.isEmpty()) {
            requestString = "";
        } else {
            requestString = "?" + requestString;
        }
        if (request instanceof ContentCachingRequestWrapper) { // request被wrap过，可重复读取数据
            if ("POST".equals(request.getMethod()) || "PUT".equals(request.getMethod())) {
                ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
                String body = StringUtils.toEncodedString(wrapper.getContentAsByteArray(), Charset.forName(wrapper.getCharacterEncoding()));
                body = body.replaceAll("[\\r\\n\\t]", "");
                log.error("invocation {} error, request body:\n{}\n, error: {}", request.getRequestURL() + "?" + request.getQueryString(), body, ExceptionUtil.getStackTrace(e));
            }
            log.error("invocation {} error", request.getRequestURL() + requestString, e);
        } else {
            log.error("invocation {} error", request.getRequestURL() + requestString, e);
        }
    }
}
