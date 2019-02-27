package com.bcx.wind.workflow.core.flow;

import com.bcx.wind.workflow.helper.ObjectHelper;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bcx.wind.workflow.core.constant.NodeName.*;

/**
 * 流程定义模型
 *
 * @author zhanglei
 */
public class ProcessModel extends BaseNode {

    private String processId;

    private NodeModel nodeModel;

    /**
     * 所有任务节点
     */
    private List<NodeModel>  allTaskNodes = new LinkedList<>();

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

    //最起始节点
    private NodeModel startNode;



    @Override
    public void executor() {

    }


    public ProcessModel(String name, String displayName) {
        super(name, displayName);
    }

    public List<NodeModel> getAllTaskNodes() {
        return allTaskNodes;
    }

    public void setAllTaskNodes(List<NodeModel> allTaskNodes) {
        this.allTaskNodes = allTaskNodes;
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

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public NodeModel getNodeModel() {
        return nodeModel;
    }

    public void setNodeModel(NodeModel nodeModel) {
        this.nodeModel = nodeModel;
        enhanceProcessModel();
    }

    public NodeModel getNodeModel(String nodeName){
        for(NodeModel nodeModel : allNodes){
            if(nodeName.equals(nodeModel.name())){
                return nodeModel;
            }
        }
        return null;
    }

    public TaskModel getTaskModel(String nodeName){
        for(NodeModel nodeModel : allTaskNodes){
            if(nodeName.equals(nodeModel.name())){
                return (TaskModel) nodeModel;
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


    /**
     * 增强模型
     */
    public void enhanceProcessModel(){
        buildAllNodes(this.nodeModel);
        buildAllTaskNodes(this.nodeModel);
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
        if(isTaskModel(nodeModel)){
            if(!allTaskNodes.contains(nodeModel)) {
                this.allTaskNodes.add(nodeModel);
            }
        }
        List<NodeModel> nextNodes = nodeModel.nextTaskNodes();
        List<NodeModel> nexts = nodeModel.nextNodes();
        if(!ObjectHelper.isEmpty(nexts)){
            if(nexts.get(0) instanceof RouterNode){
                RouterNode routerNode = (RouterNode)nexts.get(0);
                this.allTaskNodes.addAll(routerNode.getAllTaskNodes());
            }
        }
        if(!ObjectHelper.isEmpty(nextNodes)){
            for(NodeModel node : nextNodes){
                buildAllTaskNodes(node);
            }
        }
    }


    private boolean isTaskModel(NodeModel nodeModel){
        String type = nodeModel.nodeType().value();
        return TASK.equals(type) || SCRIBE_TASK.equals(type);
    }


    private void buildFirstTaskNode(){
        this.firstTaskNode = nodeModel.nextTaskNodes().get(0);
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
        buildLastTaskNodes(this.nodeModel);
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


    public List<RouterNode>  getRouterNodes(){
        List<RouterNode> routerNodes = new LinkedList<>();

        for(NodeModel nodeModel : this.allNodes){
            if(ROUTER.equals(nodeModel.nodeType().value())){
                routerNodes.add((RouterNode) nodeModel);
            }
        }

        return routerNodes;
    }


    public List<TaskModel> getNextTasks(String curNodeId){
        NodeModel curNode = getNodeModel(curNodeId);
        if(ObjectHelper.isEmpty(curNode)){
            return Collections.emptyList();
        }

        List<TaskModel> taskModels = new LinkedList<>();
        addNextTasks(taskModels,curNode);
        return taskModels;
    }


    private void  addNextTasks(List<TaskModel> taskModels,NodeModel curNode){
        List<NodeModel> nextModels = curNode.nextNodes();
        addRouterTasksToTaskModels(taskModels,nextModels);

        for(NodeModel task : nextModels){
            if(task instanceof TaskModel) {
                if (!taskModels.contains(task)) {
                    taskModels.add((TaskModel) task);
                }
                addNextTasks(taskModels,task);
            }
        }
    }


    public List<TaskModel> getLastTasks(String curNodeId){
        NodeModel curNode = getNodeModel(curNodeId);
        if(ObjectHelper.isEmpty(curNode)){
            curNode = getTaskModel(curNodeId);
            if(ObjectHelper.isEmpty(curNode)) {
                return Collections.emptyList();
            }
        }

        List<TaskModel> taskModels = new LinkedList<>();
        addLastTasks(taskModels,curNode);
        return taskModels;
    }




    public void addLastTasks(List<TaskModel> taskModels,NodeModel curNode){
        List<NodeModel> lastModels = curNode.lastNodes();
        addRouterTasksToTaskModels(taskModels,lastModels);

        for(NodeModel task : lastModels){
            if(task instanceof TaskModel) {
                if (!taskModels.contains(task)) {
                    taskModels.add((TaskModel) task);
                }
            }
            addLastTasks(taskModels,task);
        }
    }



    private  void  addRouterTasksToTaskModels(List<TaskModel> taskModels,List<NodeModel> nodeModels){
        for(NodeModel node  : nodeModels){
            if(node instanceof RouterNode){
                List<NodeModel> routerTasks = ((RouterNode) node).getAllTaskNodes();
                for(NodeModel routerTask : routerTasks){
                    if(!taskModels.contains(routerTask)){
                        taskModels.add((TaskModel) routerTask);
                    }
                }
            }
        }
    }



    /**
     * 判断任务节点是否在指定节点后面
     * @param curTask    指定任务节点
     * @param nextTask   判断的任务节点
     * @return           boolean
     */
    public boolean isNextTask(String curTask,String nextTask){
        List<TaskModel> nextTasks = getNextTasks(curTask);
        for(TaskModel taskModel : nextTasks){
            if(taskModel.name().equals(nextTask)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断任务节点是否在指定节点前面
     * @param curTask    指定任务节点
     * @param lastTask   判断的任务节点
     * @return           boolean
     */
    public boolean isLastTask(String curTask,String lastTask){
        List<TaskModel> lastTasks = getLastTasks(curTask);
        for(TaskModel taskModel : lastTasks){
            if(taskModel.name().equals(lastTask)){
                return true;
            }
        }
        return false;
    }



    public List<NodeModel> getAllNodes() {
        return allNodes;
    }

    public void setAllNodes(List<NodeModel> allNodes) {
        this.allNodes = allNodes;
    }

    public NodeModel getStartNode() {
        return startNode;
    }

    public void setStartNode(NodeModel startNode) {
        this.startNode = startNode;
    }
}
