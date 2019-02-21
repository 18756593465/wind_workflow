package com.bcx.wind.workflow.core.flow;


import com.bcx.wind.workflow.WorkflowEngine;
import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.constant.NodeType;
import com.bcx.wind.workflow.core.pojo.*;
import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.executor.handler.Handler;
import com.bcx.wind.workflow.executor.handler.RouterHandler;
import com.bcx.wind.workflow.executor.handler.ScribeTaskHandler;
import com.bcx.wind.workflow.executor.handler.TaskHandler;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.message.MessageHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.bcx.wind.workflow.core.constant.NodeAttr.*;
import static com.bcx.wind.workflow.core.constant.NodeName.*;
import static com.bcx.wind.workflow.core.constant.WorkflowOperateConstant.REJECT;
import static com.bcx.wind.workflow.message.MsgConstant.w018;

/**
 * @author zhanglei
 */
public abstract class BaseNode extends Node implements NodeModel {

    /**
     * 父节点
     */
    protected NodeModel parentNode;


    /**
     * 节点类型
     */
    protected NodeType nodeType;

    /**
     * dom元素
     */
    protected Element element;

    /**
     * 当前元素
     */
    protected Element now;

    /**
     * 当前任务相关数据
     */
    protected   Task task;



    /**
     * 路线L
     */
    protected List<Path> paths = new LinkedList<>();


    /**
     * 前面的节点
     */
    protected List<NodeModel> preNodes = new LinkedList<>();


    /**
     * 后续节点
     */
    protected List<NodeModel> nextNodes = new LinkedList<>();

    /**
     * 后续任务
     */
    protected List<NodeModel>  nextTasks = new LinkedList<>();

    /**
     * 前面任务节点
     */
    protected List<NodeModel>  preTasks = new LinkedList<>();

    /**
     * 是否属于子流程
     */
    protected  boolean inRouter;

    /**
     * 执行数据
     */
    protected Actuator actuator;


    public BaseNode(String name, String displayName){
        super();
        this.name = name;
        this.displayName = displayName;
    }


    public BaseNode(String name,String displayName,NodeType nodeType){
        super();
        this.name = name;
        this.displayName = displayName;
        this.nodeType = nodeType;
    }

    public abstract void executor();


    @Override
    public void execute() {
        executor();
    }

    @Override
    public void task(Task task) {
        this.task  = task;
    }

    @Override
    public void next(String submitLine) {
        List<NodeModel> nextModels = null;
        if(!ObjectHelper.isEmpty(submitLine)){
            nextModels = getSubmitLineNodes(submitLine);
        }else {
            nextModels = nextNodes();
        }
        Assert.notEmpty(MessageHelper.getMsg(w018,this.name),nextModels);

        for(NodeModel next : nextModels){
            next.actuator(this.actuator);
            if(next instanceof TaskNode ){
                this.handler(new TaskHandler(this.actuator,next,this.task));
            }else if( next instanceof RouterNode){
                this.handler(new RouterHandler(this.actuator,next,this.task));
            }else if(next instanceof ScribeTaskNode){
                this.handler(new ScribeTaskHandler(this.actuator,next,this.task));
            }else{
                next.task(this.task);
                next.execute();
            }
        }
    }


    protected   void  handler(Handler handler){
        handler.handler();
    }


    protected String operate(){
        return this.actuator.getOperate().value();
    }

    @Override
    public void actuator(Actuator actuator) {
        this.actuator = actuator;
    }

    protected WorkflowEngine engine(){
        return this.actuator.getEngine();
    }


    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String displayName() {
        return this.displayName;
    }


    @Override
    public NodeType nodeType() {
        return this.nodeType;
    }

    @Override
    public List<NodeModel> nextNodes() {
        return nextNodes;
    }

    @Override
    public List<NodeModel> lastNodes() {
        return preNodes;
    }


    @Override
    public List<NodeModel> nextTaskNodes() {
        return nextTasks;
    }

    @Override
    public List<NodeModel> lastTaskNodes() {
        return preTasks;
    }

    @Override
    public List<Path> path() {
        return this.paths;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void setParentNode(NodeModel parentNode) {
        this.parentNode = parentNode;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    protected void setDefaultPro(Element now , Element element){
        this.now = now;
        this.element = element;
    }

    private NodeModel buildNodeModel(String nodeName,String name,String displayName,Element now){
        if(NodeCache.getInstance().contain(name)){
            return NodeCache.getInstance().get(name);
        }
        switch (nodeName){
            case TASK:
                TaskNode taskNode =  new TaskNode(name,displayName);
                taskNode.setDefaultPro(now,element);
                taskNode.setParentNode(this.parentNode);
                AndCache.getInstance().inAnd(this instanceof AndNode ? 1 : 0);
                OrCache.getInstance().inOr(this instanceof OrNode ? 1 : 0);

                if(this instanceof TaskNode){
                    if(((TaskNode) this).isInAnd())
                        AndCache.getInstance().inAnd(1);
                    if(((TaskNode) this).isInOr()){
                        OrCache.getInstance().inOr(1);
                    }
                }

                taskNode.setInAnd(AndCache.getInstance().inAnd());
                taskNode.setInOr(OrCache.getInstance().inOr());

                taskNode.build();
                NodeCache.getInstance().put(name,taskNode);
                return taskNode;
            case END:
                EndNode endNode =  new EndNode(name,displayName);
                endNode.setDefaultPro(now,element);
                endNode.setParentNode(parentNode);
                endNode.build();
                NodeCache.getInstance().put(name,endNode);
                return endNode;
            case AND:
                AndNode andNode =  new AndNode(name,displayName);
                andNode.setDefaultPro(now,element);
                andNode.setParentNode(parentNode);

                andNode.build();
                NodeCache.getInstance().put(name,andNode);
                return andNode;
            case OR:
                OrNode orNode =  new OrNode(name,displayName);
                orNode.setDefaultPro(now,element);
                orNode.setParentNode(parentNode);
                orNode.build();
                NodeCache.getInstance().put(name,orNode);
                return orNode;
            case AND_JOIN:
                AndJoinNode andJoinNode =  new AndJoinNode(name,displayName);
                andJoinNode.setDefaultPro(now,element);
                andJoinNode.setParentNode(parentNode);
                AndCache.getInstance().inAnd(0);
                andJoinNode.build();
                NodeCache.getInstance().put(name,andJoinNode);
                return andJoinNode;
            case OR_JOIN:
                OrJoinNode orJoinNode =  new OrJoinNode(name,displayName);
                orJoinNode.setDefaultPro(now,element);
                orJoinNode.setParentNode(parentNode);
                OrCache.getInstance().inOr(0);
                orJoinNode.build();
                NodeCache.getInstance().put(name,orJoinNode);
                return orJoinNode;
            case ROUTER:
                RouterNode routerNode =  new RouterNode(name,displayName);
                routerNode.setDefaultPro(now,element);
                routerNode.setParentNode(parentNode);
                routerNode.build();
                NodeCache.getInstance().put(name,routerNode);
                return routerNode;
            case DISC:
                DiscNode discNode =  new DiscNode(name,displayName);
                discNode.setDefaultPro(now,element);
                discNode.setParentNode(parentNode);
                discNode.build();
                NodeCache.getInstance().put(name,discNode);
                return discNode;
            case SCRIBE:
                ScribeNode scribeNode = new ScribeNode(name,displayName);
                scribeNode.setDefaultPro(now,element);
                scribeNode.setParentNode(parentNode);
                scribeNode.build();
                NodeCache.getInstance().put(name,scribeNode);
                return scribeNode;
            case SCRIBE_TASK:
                ScribeTaskNode scribeTaskNode = new ScribeTaskNode(name,displayName);
                scribeTaskNode.setDefaultPro(now,element);
                scribeTaskNode.setParentNode(parentNode);
                scribeTaskNode.build();
                NodeCache.getInstance().put(name,scribeTaskNode);
                return scribeTaskNode;
            case CUSTOM:
                CustomNode customNode = new CustomNode(name,displayName);
                customNode.setDefaultPro(now,element);
                customNode.setParentNode(parentNode);
                customNode.build();
                NodeCache.getInstance().put(name,customNode);
                return customNode;
                default:
                    throw new WorkflowException("nodeName:"+nodeName+" , is not found, please update process!");

        }
    }


    /**'
     *构建提交路线
     * @param now  当前节点
     */
    protected void buildPaths(Element now) {
        NodeList ps = now.getChildNodes();
        for (int i = 0; i < ps.getLength(); i++) {
            org.w3c.dom.Node node = ps.item(i);
            if (node instanceof Element && PATH.equals(node.getNodeName())) {
                String to = ((Element) node).getAttribute(TO);
                Assert.notEmpty("path node is lack 'to' property! build process fail!", to);

                String name = ((Element) node).getAttribute(NAME);
                String displayName = ((Element) node).getAttribute(DISPLAY_NAME);
                String expr = ((Element) node).getAttribute(EXPR);
                Path path = new Path(name, displayName);
                path.setTo(to);
                path.setExpr(expr);
                path.setPreNode(this);

                //查询后续节点
                NodeList childNodes = element.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    org.w3c.dom.Node child = childNodes.item(j);

                    if (child instanceof Element) {
                        Element next = (Element) child;
                        String nodeName = next.getAttribute(NAME);

                        if (to.equals(nodeName)) {
                            String type = next.getNodeName();
                            String nextNodeName = next.getAttribute(NAME);
                            String nextDisplayName = next.getAttribute(DISPLAY_NAME);
                            NodeModel nodeModel = buildNodeModel(type, nextNodeName, nextDisplayName,next);
                            //添加到缓存

                            nodeModel.lastNodes().add(this);
                            nodeModel.inRouter(ROUTER.equals(this.parentNode.nodeType().value()));

                            if (TASK.equals(nodeModel.nodeType().value())) {
                                TaskNode task = pathToTask(nodeModel,next,childNodes,child);
                                path.setNextTaskNode(task);
                            }else if(SCRIBE_TASK.equals(nodeModel.nodeType().value())){
                                String  interceptor = next.getAttribute(INTERCEPTOR);
                                ((ScribeTaskNode)nodeModel).setInterceptor(ObjectHelper.isEmpty(interceptor) && TRUE.equals(interceptor));
                                path.setNextTaskNode((ScribeTaskNode)nodeModel);
                            }else if(CUSTOM.equals(nodeModel.nodeType().value())){
                                String  interceptor = next.getAttribute(INTERCEPTOR);
                                ((CustomNode)nodeModel).setInterceptor(ObjectHelper.isEmpty(interceptor) && TRUE.equals(interceptor));
                            }

                            path.setNextNode(nodeModel);
                        }
                    }
                }

                this.paths.add(path);
            }
        }
    }



    private TaskNode  pathToTask(NodeModel nodeModel,Element next,NodeList childNodes,org.w3c.dom.Node child){
        TaskNode task = (TaskNode) nodeModel;
        //会签属性
        String  jointly = next.getAttribute(JOINTLY);
        String  interceptor = next.getAttribute(INTERCEPTOR);
        String  assignee = next.getAttribute(ASSIGNEE_USER);
        task.setJointly(!ObjectHelper.isEmpty(jointly) && TRUE.equals(jointly));
        task.setInterceptor(!ObjectHelper.isEmpty(interceptor) && TRUE.equals(interceptor));

        if(!ObjectHelper.isEmpty(assignee)){
            NodeList childs = element.getChildNodes();
            for (int k = 0; k < childs.getLength(); k++) {
                org.w3c.dom.Node c = childNodes.item(k);

                if (c instanceof Element) {
                    Element ele = (Element) c;
                    String assigneeName = ele.getNodeName();
                    if(ASSIGNEE.equals(assigneeName)){
                        String assigneeId = ele.getAttribute(ID);
                        if(assigneeId.equals(assignee)){
                            NodeList propertys = ele.getChildNodes();

                            List<DefaultUser> users = new LinkedList<>();
                            for (int n = 0; n < propertys.getLength(); n++) {
                                org.w3c.dom.Node property = propertys.item(n);
                                if (property instanceof Element) {
                                    Element pro = (Element) property;
                                    String userId = pro.getAttribute(USER_ID);
                                    String userName = pro.getAttribute(USER_NAME);
                                    DefaultUser user = new DefaultUser();
                                    user.setUserId(userId);
                                    user.setUserName(userName);
                                    users.add(user);
                                }
                            }
                            task.setApproveUsers(users);
                        }
                    }
                }
            }
        }

        return task;
    }



    @Override
    public List<NodeModel> getSubmitLineNodes(String submitLine) {
        List<Path> ps = this.path();
        for(Path path : ps){
            if(submitLine.equals(path.getName())){
                return Collections.singletonList(path.getNextNode());
            }
        }
        return Collections.emptyList();
    }

    protected void createNextNodes(){
        for(Path path : paths){
            NodeModel nodeModel = path.getNextNode();

            this.nextNodes.add(nodeModel);
        }
    }


    protected void createNextTaskNodes(NodeModel node){
        List<Path> ps = node.path();
        for(Path path : ps){
            TaskModel taskNode = path.getNextTaskNode();
            NodeModel nodeModel = path.getNextNode();

            //如果是任务，直接添加
            if(!ObjectHelper.isEmpty(taskNode)) {
                if(!nextTasks.contains(node)) {
                    this.nextTasks.add(taskNode);
                }
            }else if(!ObjectHelper.isEmpty(nodeModel)){
                //不是任务 迭代操作
                if(!TASK.equals(nodeModel.nodeType().value())){
                    createNextTaskNodes(nodeModel);
                }
            }
        }
    }


    protected void createPreTaskNodes(NodeModel nodeModel,NodeModel act){
        List<NodeModel> lastNodes = nodeModel.lastNodes();
        for(NodeModel node : lastNodes){

            if(node instanceof TaskNode){
                if(!act.lastTaskNodes().contains(node)) {
                    act.lastTaskNodes().add(node);
                }
            }else{
                createPreTaskNodes(node,act);
            }
        }
    }


    /**
     * 构建流程模型节点指针
     */
    protected void createNodePointer(){
        createNextNodes();
        createNextTaskNodes(this);
    }

    public List<NodeModel> getPreNodes() {
        return preNodes;
    }

    public void setPreNodes(List<NodeModel> preNodes) {
        this.preNodes = preNodes;
    }

    public boolean inRouter(){
        return this.inRouter;
    }

    public NodeModel getParentNode() {
        return parentNode;
    }

    @Override
    public void inRouter(boolean inRouter){
        this.inRouter = inRouter;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


    protected NodeConfig nodeConfig(){
        Configuration configuration = task.getTaskConfig();
        if(!ObjectHelper.isEmpty(configuration)) {
            return configuration.getNodeConfig();
        }

        return new NodeConfig();
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NodeModel){
            return this.name.equals(((NodeModel) obj).name());
        }
        return super.equals(obj);
    }


    protected ProcessModel processModel(){
        return this.actuator.getProcessModel();
    }


    protected Workflow workflow(){
        return this.actuator.getWorkflow();
    }


    protected WorkflowVariable variable(){
        return workflow().getVariable();
    }

    @Override
    public NodeModel parentNode(){
        return this.parentNode;
    }


    public NodeModel getTaskModel(String nodeId){
        List<NodeModel> taskModels = this.processModel().getAllTaskNodes();
        for(NodeModel node : taskModels){
            String name = node.name();
            if(nodeId.equals(name)){
                return node;
            }
        }
        return null;
    }


    public NodeModel getNodeModel(String nodeId){
        List<NodeModel> taskModels = this.processModel().getAllNodes();
        for(NodeModel node : taskModels){
            String name = node.name();
            if(nodeId.equals(name)){
                return node;
            }
        }
        return null;
    }

}
