package com.bcx.wind.workflow.core.flow;

import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.constant.NodeType;
import com.bcx.wind.workflow.core.constant.OrderType;
import com.bcx.wind.workflow.entity.ActiveHistory;
import com.bcx.wind.workflow.entity.OrderHistoryInstance;
import com.bcx.wind.workflow.entity.OrderInstance;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.w3c.dom.Element;

import java.util.List;
import java.util.stream.Collectors;

import static com.bcx.wind.workflow.core.constant.NodeName.ROUTER;

/**
 * 结束节点
 *
 * @author zhanglei
 */
public class EndNode extends BaseNode {

    public EndNode(String name, String displayName) {
        super(name, displayName);
        this.nodeType = NodeType.END;
    }

    public void build(){
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
        //如果是子流程 继续递归
        if(ROUTER.equals(this.parentNode.nodeType().value())){
            RouterNode node = (RouterNode) this.parentNode;
            node.actuator(this.actuator);
            node.next(null);
            //删除子流程实例
            removeChildOrderInstance();
            return;
        }

        //结束
        addCompleteHistory();
    }


    /**
     * 删除子流程实例
     */
    private void removeChildOrderInstance(){
        String orderId = workflow().getOrderId();
        engine().runtimeService().orderService().deleteByParentId(orderId);
    }


    private  void   addCompleteHistory(){
        String orderId = workflow().getOrderId();
        QueryFilter filter = new QueryFilter().setOrderId(orderId);
        List<ActiveHistory> activeHistoryList = engine().historyService().activeHistoryService().queryList(filter);

        if(!ObjectHelper.isEmpty(activeHistoryList)){
            //新增执行完毕履历
            engine().historyService().completeHistoryService().insertList(activeHistoryList);

            List<String> ids = activeHistoryList.stream().map(ActiveHistory::getId).collect(Collectors.toList());
            //删除执行履历
            engine().historyService().activeHistoryService().deleteByIds(ids);

            //新增流程实例历史
            addHistoryOrderInstance(orderId);
        }
    }


    /**
     * 更新历史流程实例
     * @param orderId  流程实例号
     */
    private void  addHistoryOrderInstance(String orderId){
        OrderInstance orderInstance = engine().runtimeService().orderService().queryOne(orderId);
        createHistoryOrderInstance(orderInstance);
    }


    private void createHistoryOrderInstance(OrderInstance orderInstance){
        OrderHistoryInstance instance =  new OrderHistoryInstance()
                .setId(orderInstance.getId())
                .setProcessId(orderInstance.getProcessId())
                .setStatus(OrderType.COMPLETE)
                .setCreateTime(orderInstance.getCreateTime())
                .setExpireTime(orderInstance.getExpireTime())
                .setParentId(orderInstance.getParentId())
                .setVersion(orderInstance.getVersion())
                .setVariable(orderInstance.getVariable())
                .setData(orderInstance.getData())
                .setSystem(orderInstance.getSystem());

        engine().historyService().orderHistoryService().insert(instance);
        engine().runtimeService().orderService().deleteById(orderInstance.getId());
    }
}
