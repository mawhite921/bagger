package gov.loc.repository.serviceBroker.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import gov.loc.repository.serviceBroker.RequestingServiceBroker;
import gov.loc.repository.serviceBroker.ServiceRequest;
import gov.loc.repository.serviceBroker.dao.ServiceRequestDAO;

public class RequestingServiceBrokerImpl implements RequestingServiceBroker {

	private ServiceRequestDAO dao;
	private String requester;
	
	public RequestingServiceBrokerImpl(ServiceRequestDAO dao, String requester) {
		this.dao = dao;
		this.requester = requester;
	}
	
	@Override
	@Transactional
	public ServiceRequest findAndAcknowledgeNextServiceRequestWithResponse() {
		ServiceRequest req = dao.findNextServiceRequestWithResponse(this.requester);
		if (req != null)
		{
			req.acknowledgeResponse();
			dao.save(req);
		}
		return req;
	}

	@Override
	public void sendRequest(ServiceRequest req) {
		req.request(this.requester);
		this.dao.save(req);
	}
	
	@Override
	public String getRequester() {
		return this.requester;
	}
	
	@Override
	@Transactional
	public void resume(String correlationKey) {
		List<ServiceRequest> requests = this.dao.findServiceRequests(this.requester, correlationKey);
		for(ServiceRequest req : requests)
		{
			if (req.isSuspended())
			{
				req.resume();
				this.dao.save(req);
			}
		}		
	}
	
	@Override
	@Transactional
	public void suspend(String correlationKey) {
		List<ServiceRequest> requests = this.dao.findServiceRequests(this.requester, correlationKey);
		for(ServiceRequest req : requests)
		{
			if (! req.isSuspended())
			{
				req.suspend();
				this.dao.save(req);
			}
		}

	}
	
	@Override
	@Transactional
	public List<ServiceRequest> findServiceRequests(String correlationKey) {
		return this.dao.findServiceRequests(this.requester, correlationKey);
	}
}
