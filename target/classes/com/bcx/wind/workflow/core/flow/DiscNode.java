package com.bcx.wind.workflow.core.flow;

import com.bcx.wind.workflow.core.constant.NodeType;
import com.bcx.wind.workflow.core.pojo.NodeConfig;
import com.bcx.wind.workflow.core.pojo.Task;
import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.message.MessageHelper;
import org.w3c.dom.Element;

import java.util.Map;

import static com.bcx.wind.workflow.message.MsgConstant.w018;

/**
 * 决策路由节点
 *
 * @author zhanglei
 */
public class DiscNode extends BaseNode {


    public DiscNode(String name, String displayName) {
        super(name, displayName);
        this.nodeType = NodeType.DISC;
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

    @Override
    public void executor() {
        //查询提交路线
        String path = getSubmitLine();

        Assert.notEmpty(MessageHelper.getMsg(w018,task.getTaskInstance().getTaskName()),path);
        next(path);
    }


    private  String  getSubmitLine(){
        //参数提交路线
        Map<String,String> lines  = workflow().getVariable().getPath();
        if(!ObjectHelper.isEmpty(lines)){
            String path = lines.get(this.task.getTaskInstance().getTaskName());
            if(!ObjectHelper.isEmpty(path)){
               return path;
            }
        }

        String submitLine = nodeConfig().getSubmitLine();
        if(!ObjectHelper.isEmpty(submitLine)){
            return submitLine;
        }

        return null;
    }


}
