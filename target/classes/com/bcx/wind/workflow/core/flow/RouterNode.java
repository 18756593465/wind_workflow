package com.bcx.wind.workflow.core.flow;

import com.bcx.wind.workflow.core.constant.NodeType;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bcx.wind.workflow.core.constant.NodeAttr.DISPLAY_NAME;
import static com.bcx.wind.workflow.core.constant.NodeAttr.NAME;
import static com.bcx.wind.workflow.core.constant.NodeName.END;
import static com.bcx.wind.workflow.core.constant.NodeName.TASK;

/**
 * 子流程节点
 *
 * @author zhanglei
 */
public class RouterNode extends BaseNode {

    /**
     * 子流程起始
     */
    private NodeModel childNodeModel;

    /**
     * 所有任务节点
     */
    private List<NodeModel> allTaskNodes = new LinkedList<>();

    /**
     * 所有节点
     */
    protected List<NodeModel>  allNodes = new LinkedList<>();

    /**
     * 第一个任务节点
     */
    private NodeModel firstTaskNode;


    /**
     * 最后一个任务节点
     */
    private List<NodeModel>  lastTaskNode = new LinkedList<>();

    public RouterNode(String name, String displayName) {
        super(name, displayName);
        this.nodeType = NodeType.ROUTER;
    }

    @Override
    public void executor() {
        //No code
    }

    public NodeModel getNodeModel() {
        return childNodeModel;
    }

    public NodeModel getNodeModel(String nodeName){
        for(NodeModel nodeModel : allNodes){
            if(nodeName.equals(nodeModel.name())){
                return nodeModel;
            }
        }
        return null;
    }

    public TaskNode getTaskNode(String nodeName){
        for(NodeModel nodeModel : allTaskNodes){
            if(nodeName.equals(nodeModel.name())){
                return (TaskNode) nodeModel;
            }
        }
        return null;
    }

    public <T>T getNodeModel(String nodeName,Class<T> clazz){
        NodeModel nodeModel = getNodeModel(nodeName);
        if(!ObjectHelper.isEmpty(nodeModel)){
            return (T)nodeModel;
        }
        return null;
    }


    public void build(){
        //构建路线
        buildPaths(now);
        //构建节点指针
        createNodePointer();

        //构建子流程中的流程数据！
        buildRouter();
    }

    /**
     * 构建子流程  创建childNodeModel
     * @return  this
     */
    private RouterNode buildRouter(){
        StartNode startNode = null;
        NodeList nodeList = now.getChildNodes();
        for(int i=0 ; i<nodeList.getLength() ; i++){
            Node node = nodeList.item(i);
            if(node instanceof Element && node.getNodeName().equals(NodeType.START.value())){
                String name = ((Element) node).getAttribute(NAME);
                String displayName = ((Element) node).getAttribute(DISPLAY_NAME);
                startNode = new StartNode(name,displayName);
                startNode.setElement(now);
                startNode.setParentNode(this);
                startNode.inRouter(true);
                startNode.setNow((Element) node);
                startNode.build();
                break;
            }
        }

        this.childNodeModel = startNode;
        enhanceProcessModel();
        return this;
    }

    public Element getNow() {
        return now;
    }

    public void setNow(Element now) {
        this.now = now;
    }

    public NodeModel getChildNodeModel() {
        return childNodeModel;
    }

    public void setChildNodeModel(NodeModel childNodeModel) {
        this.childNodeModel = childNodeModel;
    }

    public List<NodeModel> getAllTaskNodes() {
        return allTaskNodes;
    }

    public void setAllTaskNodes(List<NodeModel> allTaskNodes) {
        this.allTaskNodes = allTaskNodes;
    }

    public List<NodeModel> getAllNodes() {
        return allNodes;
    }

    public void setAllNodes(List<NodeModel> allNodes) {
        this.allNodes = allNodes;
    }

    public NodeModel getFirstTaskNode() {
        return firstTaskNode;
    }

    public void setFirstTaskNode(NodeModel firstTaskNode) {
        this.firstTaskNode = firstTaskNode;
    }

    public List<NodeModel> getLastTaskNode() {
        return lastTaskNode;
    }

    public void setLastTaskNode(List<NodeModel> lastTaskNode) {
        this.lastTaskNode = lastTaskNode;
    }

    /**
     * 增强模型
     */
    public void enhanceProcessModel(){
        buildAllNodes(this.childNodeModel);
        buildAllTaskNodes(this.childNodeModel);
        buildFirstTaskNode();
        //设置前置任务节点
        for(NodeModel node : this.allNodes){
            createPreTaskNodes(node,node);
        }
        buildLastTaskNodes();
    }

    private void buildAllNodes(NodeModel nodeModel){
        if(!this.allNodes.contains(nodeModel)){
            this.allNodes.add(nodeModel);
        }
        List<NodeModel> nextNodes = nodeModel.nextNodes();
        if(!ObjectHelper.isEmpty(nextNodes)){
            for(NodeModel node : nextNodes){
                buildAllNodes(node);
            }
        }
    }


    private void buildAllTaskNodes(NodeModel nodeModel){
        if(TASK.equals(nodeModel.nodeType().value())){
            if(!allTaskNodes.contains(nodeModel)) {
                this.allTaskNodes.add(nodeModel);
            }
        }
        List<NodeModel> nextNodes = nodeModel.nextTaskNodes();
        if(!ObjectHelper.isEmpty(nextNodes)){
            for(NodeModel node : nextNodes){
                buildAllTaskNodes(node);
            }
        }
    }


    private void buildFirstTaskNode(){
        this.firstTaskNode = childNodeModel.nextTaskNodes().get(0);
    }


    private void buildLastTaskNodes(){
        if(!ObjectHelper.isEmpty(this.allNodes)){
            List<NodeModel> nodeModels = this.allNodes.stream().filter(
                    node->END.equals(node.nodeType().value())).collect(Collectors.toList());
            if(!ObjectHelper.isEmpty(nodeModels)) {
                this.lastTaskNode = nodeModels.get(0).lastTaskNodes();
                return;
            }
        }
        buildLastTaskNodes(this.childNodeModel);
    }


    private void  buildLastTaskNodes(NodeModel nodeModel){
        List<NodeModel> nextNodes = nodeModel.nextNodes();

        if(!ObjectHelper.isEmpty(nextNodes)){
            for(NodeModel node : nextNodes){
                if(END.equals(node.nodeType().value())){
                    this.lastTaskNode = node.lastTaskNodes();
                    return;
                }else{
                    buildLastTaskNodes(node);
                }
            }
        }
    }
}
