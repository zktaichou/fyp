package com.google.gwt.sample.stockwatcher.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class RPCQueue{
//	//the request queue
//			private List queuedRequests = new ArrayList(); 
//
//			//tracking to provide serial behavior 
//			private boolean insideRequest = false;
//			
//			private List args = new ArrayList();
//			private String method;
//			private String service;
//			private AsyncCallback callback;
//			
//			public void executeNextRPC() {
//			    if (insideRequest || queuedRequests.isEmpty()) {
//			        //if we're in the middle of a request or the queue is empty
//			        return;
//			    }
//			    insideRequest = true;
//			    QueuedRPCRequest request = queuedRequests.remove(queuedRequests.size() - 1);
//			    if (request.getService().equals(QueuedRPCRequest.LOGIN_SERVICE)) {
//			        executeLoginService(request);
//			    } 
//			} 
//		
//			public void addRequestToQueue(QueuedRPCRequest request) {
//			    queuedRequests.add(request);
//			    executeNextRPC();
//			} 
//		
//			private void executeLoginService(QueuedRPCRequest request) {
//			    LoginServiceGWTAsync loginService =
//			                              (LoginServiceGWTAsync) GWT.create(LoginServiceGWT.class);
//			    ((ServiceDefTarget) loginService).setRpcRequestBuilder(
//			                    new RpcRequestBuilderNowWithTimeout());
//			    if (request.getMethod().equals(QueuedRPCRequest.LOGIN)) {
//			        loginService.login((String) request.getArgs().get(0),
//			                    (String) request.getArgs().get(1), request.getCallback());
//			    }
//			}
}
