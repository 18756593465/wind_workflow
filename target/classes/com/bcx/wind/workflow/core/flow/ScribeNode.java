package com.bcx.wind.workflow.core.flow;


import com.bcx.wind.workflow.core.constant.NodeType;

/**
 * 订阅分支节点
 *
 * @author zhanglei
 */
public class ScribeNode extends BaseNode{


    public ScribeNode(String name, String displayName) {
        super(name, displayName);
        this.nodeType = NodeType.SCRIBE;
    }


    public void build(){
        //构建路线
        buildPaths(now);
        //构建节点指针
        createNodePointer();
    }


    @Override
    public void executor() {
        next(null);
    }

}
