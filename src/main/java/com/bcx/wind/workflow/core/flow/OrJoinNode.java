package com.bcx.wind.workflow.core.flow;

import com.bcx.wind.workflow.core.constant.NodeType;
import org.w3c.dom.Element;

/**
 * 或者合并节点
 *
 * @author zhanglei
 */
public class OrJoinNode extends BaseNode {


    public OrJoinNode(String name, String displayName) {
        super(name, displayName);
        this.nodeType = NodeType.OR_JOIN;
    }


    @Override
    public void executor() {
        next(null);
    }

    public void build(){
        //构建路线
        buildPaths(now);
        //构建节点指针
        createNodePointer();
    }

    public Element getNow() {
        return now;
    }

    public void setNow(Element now) {
        this.now = now;
    }
}
