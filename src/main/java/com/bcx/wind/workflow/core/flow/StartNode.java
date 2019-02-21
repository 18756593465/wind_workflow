package com.bcx.wind.workflow.core.flow;

import com.bcx.wind.workflow.core.constant.NodeType;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static com.bcx.wind.workflow.core.constant.NodeName.START;

/**
 * 开始节点
 *
 * @author zhanglei
 */
public class StartNode extends BaseNode {

    public StartNode(String name, String displayName) {
        super(name, displayName);
        this.nodeType = NodeType.START;
    }


    @Override
    public void executor() {
        next(null);
    }


    public void build() {
        Assert.notEmpty("DOC is null ! build process fail ",element);
        now = ObjectHelper.isEmpty(now) ? getStartNode() : now;
        Assert.notEmpty("the process context is error ! please update process!",now);

        //构建路线
        buildPaths(now);
        //构建节点指针
        createNodePointer();
    }


    private Element getStartNode(){
        NodeList nodeList = element.getChildNodes();

        for(int i=0 ; i<nodeList.getLength() ; i++){
            Node node = nodeList.item(i);
            if(node instanceof Element && node.getNodeName().equals(START)){
                now = (Element) node;
                break;
            }
        }
        return now;
    }

    public Element getNow() {
        return now;
    }

    public void setNow(Element now) {
        this.now = now;
    }
}
