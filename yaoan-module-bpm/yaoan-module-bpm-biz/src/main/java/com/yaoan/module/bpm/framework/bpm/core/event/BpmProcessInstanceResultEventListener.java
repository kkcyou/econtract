package com.yaoan.module.bpm.framework.bpm.core.event;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

/**
 * {@link BpmProcessInstanceResultEvent} 的监听器
 *
 * @author 芋道源码
 */
@Slf4j
public abstract class BpmProcessInstanceResultEventListener
        implements ApplicationListener<BpmProcessInstanceResultEvent> {

    @Override
    public final void onApplicationEvent(BpmProcessInstanceResultEvent event) {
        if (!StrUtil.equals(event.getProcessDefinitionKey(), getProcessDefinitionKey())) {
            return;
        }
        try {
            onEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("缓存文件删除异常。");
        }
    }

    /**
     * @return 返回监听的流程定义 Key
     */
    protected abstract String getProcessDefinitionKey();

    /**
     * 处理事件
     *
     * @param event 事件
     */
    protected abstract void onEvent(BpmProcessInstanceResultEvent event) throws Exception;

}
