package com.andy.activiti.helloworld;

import com.google.common.collect.Maps;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @projectName: springstudy  com.com.activiti.helloworld
 * @desc:
 * @author: xiangdan
 * @time : 2019-01-25 Friday 19:50
 */
public class DemoMain {
	private static Logger logger = LoggerFactory.getLogger(DemoMain.class);
	
	public static void main(String[] args){
		logger.debug("启动我们的程序1");
		logger.debug("第1步：创建流程引擎 \n");
		ProcessEngine processEngine = getProcessEngine();

		logger.debug("第2步：部署流程定义文件\n");
		ProcessDefinition processDefinition = getProcessDefinition(processEngine);


		logger.debug("第3步：启动运行流程\n");
		ProcessInstance processInstance = getProcessInstance(processEngine, processDefinition);

		logger.debug("第4步：处理流程任务\n");
		proccessTask(processEngine, processInstance);


		logger.debug("\n结束我们的程序");

	}

	private static void proccessTask(ProcessEngine processEngine, ProcessInstance processInstance) {
		Scanner scanner = new Scanner(System.in);
		while (processInstance!=null && !processInstance.isEnded()){
			TaskService taskService = processEngine.getTaskService();
			List<Task> list = taskService.createTaskQuery().list();
			for (Task task:list) {
				logger.debug("待处理的任务:{}" , task.getName());
				Map<String, Object> variables = getMap(processEngine, scanner, task);
				logger.debug("提交任务:{} ,变量:{}",task.getId(),variables);
				taskService.complete(task.getId(),variables);

				processInstance = processEngine.getRuntimeService()
						.createProcessInstanceQuery()
						.processInstanceId(processInstance.getId())
						.singleResult();
			}
			logger.debug("待处理的任务数量: {}",list.size());
		}
		scanner.close();
	}

	private static Map<String, Object> getMap(ProcessEngine processEngine, Scanner scanner, Task task) {
		Map<String,Object> variables = Maps.newHashMap();
		FormService formService = processEngine.getFormService();
		TaskFormData taskFormData = formService.getTaskFormData(task.getId());
		List<FormProperty> formProperties = taskFormData.getFormProperties();
		for (FormProperty fp : formProperties) {
			String fpId = fp.getId();
			String fpName = fp.getName();
			FormType fpType = fp.getType();
			String fpValue = fp.getValue();
			boolean fpReadable = fp.isReadable();
			boolean fpRequired = fp.isRequired();
			boolean fpWritable = fp.isWritable();
			logger.debug("请输入 fpId:{},fpName:{},fpType:{} ,fpValue:{},fpReadable:{},fpRequired:{},fpWritable:{}   ?"
					,fpId,fpName,fpType.getName(),fpValue,fpReadable,fpRequired,fpWritable);
			String line = scanner.nextLine();
			if(StringFormType.class.isInstance(fpType)){
				variables.put(fpId,line);
			}else if(DateFormType.class.isInstance(fpType)){
				try {
					variables.put(fpId,new SimpleDateFormat("yyyy-MM-dd").parse(line));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else{
				logger.warn("暂时不支持的类型:{} ",fpType);
			}
		}
		return variables;
	}

	private static ProcessInstance getProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition) {
		RuntimeService runtimeService = processEngine.getRuntimeService();
		return runtimeService.startProcessInstanceById(processDefinition.getId());
	}

	private static ProcessDefinition getProcessDefinition(ProcessEngine processEngine) {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		deploymentBuilder.addClasspathResource("second_approve.bpmn20.xml");
		Deployment deployment = deploymentBuilder.deploy();
		String deploymentId = deployment.getId();

		return repositoryService
				.createProcessDefinitionQuery()
				.deploymentId(deploymentId)
				.singleResult();
	}

	private static ProcessEngine getProcessEngine() {
		ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
		ProcessEngine processEngine = cfg.buildProcessEngine();
		String processEngineName = processEngine.getName();
		String version = ProcessEngine.VERSION;
		logger.debug("流程引擎名称:{} 版本:{}",processEngineName,version);
		return processEngine;
	}

}
