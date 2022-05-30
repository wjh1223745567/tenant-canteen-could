package com.iotinall.canteen.common.interceptor;

import com.iotinall.canteen.common.constant.OperationLogType;
import com.iotinall.canteen.common.entity.OperationLog;
import com.iotinall.canteen.common.thread.OperationLogThread;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.Executor;

@Slf4j
@Component
public class JpaLogInterceptor extends EmptyInterceptor {

    private static Executor executor;

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if(!(entity instanceof OperationLog)){
            try {
                OperationLog log = new OperationLog()
                        .setClassName(entity.getClass().getName())
                        .setData(id.toString())
                        .setType(OperationLogType.add.getCode());
                executor.execute(new OperationLogThread(log));
            } catch (Exception e) {
                log.error("保存日志记录异常：{}", e.getMessage());
            }
        }
        return super.onSave(entity, id, state, propertyNames, types);
    }


    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if(!(entity instanceof OperationLog)) {
            try {
                OperationLog log = new OperationLog()
                        .setClassName(entity.getClass().getName())
                        .setData(id.toString())
                        .setType(OperationLogType.deleted.getCode());
                executor.execute(new OperationLogThread(log));
            } catch (Exception e) {
                log.error("保存日志记录异常：{}", e.getMessage());
            }
        }
        super.onDelete(entity, id, state, propertyNames, types);
    }

    @Resource(name = "operationLogExecutor")
    public void setExecutor(Executor executor) {
        JpaLogInterceptor.executor = executor;
    }
}
