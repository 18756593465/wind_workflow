package com.bcx.wind.workflow.core.flow;

import com.bcx.wind.workflow.core.constant.NodeType;
import org.w3c.dom.Element;

/**
 * 或者分支
 *
 * @author zhanglei
 */
public class OrNode extends BaseNode {

    public OrNode(String name, String displayName) {
        super(name, displayName);
        this.nodeType = NodeType.OR;
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
