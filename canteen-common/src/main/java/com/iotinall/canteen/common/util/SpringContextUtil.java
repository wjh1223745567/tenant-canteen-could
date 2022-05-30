package com.iotinall.canteen.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author ckinghan
 * @title: SpringContextUtil
 * @projectName platform
 * @description: 获取springcontext中的bean
 * @date 2019/10/1811:23
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

	public static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringContextUtil.context = context;
	}

	/**
	 * 获取容器中的实例
	 * @param clazz 根据class获取Spring容器中对应的Bean类
	 */
	public static <T> T getBean( Class<T> clazz){
		try {
			return context.getBean(clazz);
		} catch (BeansException e) {
			return null;
		}
	}

	public static ApplicationContext getContext(){
		return context;
	}
}

