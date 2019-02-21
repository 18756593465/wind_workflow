package com.bcx.wind.workflow.core.flow;

import com.bcx.wind.workflow.io.Resources;
import com.bcx.wind.workflow.parser.ProcessBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ProcessModelTest  {

    @Test
    public void buildModel(){
        try {
            InputStream stream = Resources.getResourceAsStream("process/Context.xml");

            ProcessModel model = ProcessBuilder.getInstance().setInputStream(stream).build("12345");

            TaskModel taskNode = model.getTaskModel("monitor");
            List<NodeModel> next = taskNode.getSubmitLineNodes("monitorPath");
            TaskNode minister = model.getNodeModel("minister",TaskNode.class);

            taskNode.nextTaskNodes();
            taskNode.nextNodes();

            List<NodeModel> nexts = minister.getSubmitLineNodes("disc_path1");

            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
