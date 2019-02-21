package com.bcx.wind.workflow.parser;

import com.bcx.wind.workflow.cache.Cache;
import com.bcx.wind.workflow.cache.DefaultCache;
import com.bcx.wind.workflow.cache.ScheduleClearCache;
import com.bcx.wind.workflow.core.constant.NodeType;
import com.bcx.wind.workflow.core.flow.NodeCache;
import com.bcx.wind.workflow.core.flow.ProcessModel;
import com.bcx.wind.workflow.core.flow.StartNode;
import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.XmlHelper;
import com.bcx.wind.workflow.message.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import static com.bcx.wind.workflow.core.constant.NodeAttr.DISPLAY_NAME;
import static com.bcx.wind.workflow.core.constant.NodeAttr.NAME;
import static com.bcx.wind.workflow.message.MsgConstant.w006;

/**
 * 工作流定义解析
 * @author zhanglei
 */
public class ProcessBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ProcessBuilder.class);

    //流程定义缓存  一天刷新一次
    private final Cache<String,ProcessModel>  processCache = new ScheduleClearCache<>(new DefaultCache<>(),24*60*60*1000);

    //流程定义字符串
    private ThreadLocal<String> context = new ThreadLocal<>();

    //流程定义输入流
    private ThreadLocal<InputStream> inputStream = new ThreadLocal<>();

    private ProcessBuilder(){}

    private static ProcessBuilder builder = new ProcessBuilder();

    public static ProcessBuilder getInstance(){
        return builder;
    }

    public ProcessBuilder setContext(String context){
        this.context.set(context);
        return this;
    }

    public ProcessBuilder setInputStream(InputStream inputStream){
        this.inputStream.set(inputStream);
        return this;
    }

    public String  getProcessName(){
        Document document = getDocument();
        Element element = document.getDocumentElement();
        return element.getAttribute(NAME);
    }

    private Document getDocument(){
        Document document = null;
        if(!ObjectHelper.isEmpty(context.get())){
            document = XmlHelper.buildDoc(context.get());
        }else if(!ObjectHelper.isEmpty(inputStream.get())){
            document = XmlHelper.buildDoc(inputStream.get());
        }else{
            throw new WorkflowException(MessageHelper.getMsg(w006));
        }
        return document;
    }

    /**
     *   根据processId,构建流程定义模型对象
     *
     * @param processId  流程定义ID
     * @return      流程定义模型
     */
    public ProcessModel  build(String processId){
        synchronized (processCache) {
            Assert.notEmpty("build processModel lack the processId!", processId);
            ProcessModel model = processCache.get(processId);
            if (!ObjectHelper.isEmpty(model)) {

                if (logger.isDebugEnabled()) {
                    logger.debug("get process from cache success! ");
                }
                return model;
            }

            Assert.allEmpty(" process context and  process inputStream is null ! please set context or inputStream!", this.context.get(), this.inputStream.get());

            Document document = getDocument();
            Element element = document.getDocumentElement();
            String processName = element.getAttribute(NAME);
            String processDisplayName = element.getAttribute(DISPLAY_NAME);

            ProcessModel processModel = new ProcessModel(processName,processDisplayName);
            processModel.setProcessId(processId);
            processModel.setElement(element);
            processModel.setNodeType(NodeType.PROCESS);

            //从开始节点开始构建
            StartNode startNode = null;
            NodeList nodeList = element.getChildNodes();
            for(int i=0 ; i<nodeList.getLength() ; i++){
                Node node = nodeList.item(i);
                if(node instanceof Element && node.getNodeName().equals(NodeType.START.value())){
                    String name = ((Element) node).getAttribute(NAME);
                    String displayName = ((Element) node).getAttribute(DISPLAY_NAME);
                    startNode = new StartNode(name,displayName);
                    startNode.setElement(element);
                    startNode.setParentNode(processModel);
                    startNode.setNow((Element) node);
                    startNode.build();

                    break;
                }
            }

            processModel.setNodeModel(startNode);
            //添加到缓存
            processCache.put(processId,processModel);
            //删除节点缓存
            NodeCache.getInstance().clear();
            return processModel;
        }

    }



}
