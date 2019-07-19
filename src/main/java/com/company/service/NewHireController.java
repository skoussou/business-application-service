package com.company.service;

import io.swagger.annotations.*;

import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.UserTaskService;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.server.api.model.ReleaseId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "New Hire Additional Endpoints", produces = MediaType.APPLICATION_JSON)
//@Path("pam")
public class NewHireController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewHireController.class);


    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private ProcessService processService;
    
    private String containerId = "com.redhat:business-app-kjar:1.0.0-SNAPSHOT";


    
    @ApiOperation(value = "Start a process")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Process started")
    })
    @PostMapping("/processes/{processId}")
    public Response startProcess(
            @ApiParam(value = "process id", required = true) @PathVariable("processId") String processId,
            @RequestBody String requestID, @RequestBody Boolean managerApprovalRequired) {

    	HashMap<String, Object> processVariables = new HashMap<String, Object>();
    	processVariables.put("pRequestID", requestID);
    	processVariables.put("pManagerApprovalRequired", managerApprovalRequired);

        //String containerId = "com.poalim:business-application-kjar:1.0.4-SNAPSHOT";
        LOGGER.info("Process  with container id: {} and process id: {} and processVariables {} is starting", containerId, processId, processVariables);
        processService.startProcess(containerId, processId, processVariables);
        LOGGER.info("Process  with container id: {} and process id: {} started", containerId, processId);

        return Response.ok().build();
    }

    @ApiOperation(value = "Complete a task in Ready or Reserved state")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Task completed")
    })
    @PostMapping("/tasks/{taskId}/{actor}")
    public Response autoCompleteTask(
            @ApiParam(value = "task id", required = true) @PathVariable("taskId") Long taskId,
            @ApiParam(value = "name of the actor", required = true) @PathVariable("actor") String actor) {
        Task task = userTaskService.getTask(taskId);
        if(task != null) {
            LOGGER.info("Task {} status {}", task.getId(), task.getTaskData().getStatus());
            if(task.getTaskData().getStatus() == Status.Reserved) {
                userTaskService.start(task.getId(), actor);
                userTaskService.complete(task.getId(), actor, null);
            }
            else if(task.getTaskData().getStatus() == Status.Ready) {
                userTaskService.claim(task.getId(), actor);
                userTaskService.start(task.getId(), actor);
                userTaskService.complete(task.getId(), actor, null);
            }
        }

        return Response.ok().build();
        //comment1211
    }

    @ApiOperation(value = "Run Rules")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Process started")
    })
    @PostMapping("/rules/{value}")
    public Response runRules(
            @ApiParam(value = "value", required = false) @PathVariable("value") String value) {

//        LOGGER.info("Runninbg rules with value: {} ", value);
//        
//        RuntimeManager targetRuntimeManager = RuntimeManagerRegistry.get().getManager(containerId);
//
//        targetRuntimeManager.getRuntimeEngine(EmptyContext.get()).getKieSession().execute(arg0);
//        
//        KieServices kieServices = KieServices.Factory.get();
//        //KieContainer kContainer = kieServices.getKieClasspathContainer();
//        KieContainer kContainer =
//                KieBase kBase = kContainer.getKieBase();
//        
//        KieSession kieSession = kContainer.newKieSession();
//
//        kieSession.insert(new String(value));
//        kieSession.fireAllRules();
//
//        Object boolobje;
//        return ((Boolean)kcontext.getVariable("pManagerApprovalRequired")).equals(Boolean.TRUE);
        
        // ((Boolean)kcontext.getVariable("pManagerApprovalRequired"));
        
        return Response.ok().build();
    }
    
    
//    @ApiOperation(value = "Run Rules")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Rules Run successfully")
//    })
//    @POST
//    @Path(value = "/rules/{name}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response startRules(
//            @ApiParam(value = "name", required = false) @PathParam("name") String name) {
//        
//    	RuntimeManager targetRuntimeManager = RuntimeManagerRegistry.get().getManager(containerId);
//
////    	Specific Session
////    	-----------------
////    	KieServices kieServices = KieServices.Factory.get();
////    	KieContainer kContainer = kieServices.getKieClasspathContainer();
////
////    	KieBase kBase1 = kContainer.getKieBase("KBase1");
////    	KieSession kieSession1 = kContainer.newKieSession("KSession1_1"),
////    	    kieSession2 = kContainer.newKieSession("KSession1_2");
////
////    	KieBase kBase2 = kContainer.getKieBase("KBase2");
////    	StatelessKieSession kieSession3 = kContainer.newStatelessKieSession("KSession2_1")
////
////
////    	Default Session
////    	-----------------
//    	
//    	// Classpath KJAR (Will not work since it is already in the KIE SERVER of the SB Kie Container)
////    	KieServices kieServices = KieServices.Factory.get();
////    	//ReleaseId releaseId = KieServices.get()..newReleaseId( "com.redhat.poc.banking", "SLAMgmtRules", "LATEST" );
////    	KieContainer kContainer = KieServices.get()..newKieContainer(arg0);
////    	KieContainer kContainer = kieServices.getKieClasspathContainer();
////
////    	KieBase kBase1 = kContainer.getKieBase();
////    	KieSession kieSession1 = kContainer.newKieSession(),
////    	kieSession2 = kContainer.newKieSession();
//
////    	KieBase kBase2 = kContainer.getKieBase();
////    	StatelessKieSession kieSession3 = kContainer.newStatelessKieSession(
//
////    	kieSession1.insert(new String(name));
////    	kieSession1.fireAllRules();
//    	
//
//
//        return Response.ok().build();
//    }
//    
//    
//    @ApiOperation(value = "Start Process")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Task completed")
//    })
//    @POST
//    @Path(value = "/processes/{processId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response startProcess(
//            @ApiParam(value = "process id", required = true) @PathParam("processId") String processId) {
//        
//
//            LOGGER.info("Process {} to be started", processId);
//
//
//
//        return Response.ok().build();
//    }
//
//
//
//    @ApiOperation(value = "Complete a task in Ready or Reserved state")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Task completed")
//    })
//    @POST
//    @Path(value = "/tasks/{taskId}/{actor}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response autoCompleteTask(
//            @ApiParam(value = "task id", required = true) @PathParam("taskId") Long taskId,
//            @ApiParam(value = "name of the actor", required = true) @PathParam("actor") String actor) {
//        Task task = userTaskService.getTask(taskId);
//        if(task != null) {
//            LOGGER.info("Task {} status {}", task.getId(), task.getTaskData().getStatus());
//            if(task.getTaskData().getStatus() == Status.Reserved) {
//                userTaskService.start(task.getId(), actor);
//                userTaskService.complete(task.getId(), actor, null);
//            }
//            else if(task.getTaskData().getStatus() == Status.Ready) {
//                userTaskService.claim(task.getId(), actor);
//                userTaskService.start(task.getId(), actor);
//                userTaskService.complete(task.getId(), actor, null);
//            }
//        }
//
//        return Response.ok().build();
//    }



}

