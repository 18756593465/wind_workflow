package com.bcx.wind.workflow.core.flow;

/**
 * 自定义节点
 *
 * @author zhanglei
 */
public class CustomNode extends BaseNode implements TaskModel{

    /**
     * 是否拦截
     */
    private boolean interceptor;

    public CustomNode(String name, String displayName) {
        super(name, displayName);
    }

    @Override
    public void executor() {
        //这里暂且直接跳过
        next(null);
    }

    public void build(){
        buildPaths(now);
        //构建节点指针
        createNodePointer();
    }

    public boolean isInterceptor() {
        return interceptor;
    }

    public void setInterceptor(boolean interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public NodeModel parentNode() {
        return this.parentNode;
    }
}
