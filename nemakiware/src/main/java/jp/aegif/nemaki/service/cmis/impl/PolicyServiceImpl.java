package jp.aegif.nemaki.service.cmis.impl;

import java.util.ArrayList;
import java.util.List;

import jp.aegif.nemaki.model.Content;
import jp.aegif.nemaki.model.Policy;
import jp.aegif.nemaki.model.constant.DomainType;
import jp.aegif.nemaki.service.cmis.CompileObjectService;
import jp.aegif.nemaki.service.cmis.ExceptionService;
import jp.aegif.nemaki.service.cmis.PolicyService;
import jp.aegif.nemaki.service.node.ContentService;

import org.apache.chemistry.opencmis.commons.data.ExtensionsData;
import org.apache.chemistry.opencmis.commons.data.ObjectData;
import org.apache.chemistry.opencmis.commons.data.PermissionMapping;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.server.CallContext;

public class PolicyServiceImpl implements PolicyService {

	private ContentService contentService;
	private CompileObjectService compileObjectService;
	private ExceptionService exceptionService;
	
	@Override
	public void applyPolicy(CallContext callContext, String policyId,
			String objectId, ExtensionsData extension) {
		// //////////////////
		// General Exception
		// //////////////////
		exceptionService.invalidArgumentRequiredString("objectId", objectId);
		exceptionService.invalidArgumentRequiredString("policyId", policyId);
		Content content = contentService.getContentAsEachBaseType(objectId);
		exceptionService.objectNotFound(DomainType.OBJECT, content, objectId);
		exceptionService.permissionDenied(callContext,
				PermissionMapping.CAN_ADD_POLICY_OBJECT, content);
		Policy policy = contentService.getPolicy(policyId);
		exceptionService.objectNotFound(DomainType.OBJECT, policy, policyId);
		exceptionService.permissionDenied(callContext,
				PermissionMapping.CAN_ADD_POLICY_POLICY, policy);
		
		// //////////////////
		// Specific Exception
		// //////////////////		
		TypeDefinition td = contentService.getTypeDefinition(content);
		if(!td.isControllablePolicy()) exceptionService.constraint(objectId, "appyPolicy cannot be performed on the object whose controllablePolicy = false");
		
		// //////////////////
		// Body of the method
		// //////////////////
		contentService.applyPolicy(callContext, policyId, objectId, extension);
	}

	@Override
	public void removePolicy(CallContext callContext, String policyId,
			String objectId, ExtensionsData extension) {
		// //////////////////
		// General Exception
		// //////////////////
		exceptionService.invalidArgumentRequiredString("objectId", objectId);		
		exceptionService.invalidArgumentRequiredString("policyId", policyId);
		Content content = contentService.getContentAsEachBaseType(objectId);
		exceptionService.objectNotFound(DomainType.OBJECT, content, objectId);
		exceptionService.permissionDenied(callContext,
				PermissionMapping.CAN_REMOVE_POLICY_OBJECT, content);
		Policy policy = contentService.getPolicy(policyId);
		exceptionService.objectNotFound(DomainType.OBJECT, policy, policyId);
		exceptionService.permissionDenied(callContext,
				PermissionMapping.CAN_REMOVE_POLICY_POLICY, policy);
		
		// //////////////////
		// Body of the method
		// //////////////////
		contentService.removePolicy(callContext, policyId, objectId, extension);
	}

	@Override
	public List<ObjectData> getAppliedPolicies(CallContext callContext, String objectId,
			String filter, ExtensionsData extension) {
		// //////////////////
		// General Exception
		// //////////////////
		exceptionService.invalidArgumentRequiredString("objectId", objectId);
		Content content = contentService.getContentAsEachBaseType(objectId);
		exceptionService.objectNotFound(DomainType.OBJECT, content, objectId);
		exceptionService.permissionDenied(callContext, PermissionMapping.CAN_GET_APPLIED_POLICIES_OBJECT, content);
		
		// //////////////////
		// Body of the method
		// //////////////////
		List<Policy> policies = contentService.getAppliedPolicies(objectId, extension);
		List<ObjectData> objects = new ArrayList<ObjectData>();
		for(Policy policy : policies){
			objects.add(compileObjectService.compileObjectData(callContext, policy, filter, true, true));
		}
		return objects;
	}

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

	public void setCompileObjectService(CompileObjectService compileObjectService) {
		this.compileObjectService = compileObjectService;
	}

	public ExceptionService getExceptionService() {
		return exceptionService;
	}

	public void setExceptionService(ExceptionService exceptionService) {
		this.exceptionService = exceptionService;
	}
	
}