package jp.aegif.nemaki.model.couch;

import java.util.GregorianCalendar;
import java.util.List;

import jp.aegif.nemaki.model.Acl;
import jp.aegif.nemaki.model.Change;

import org.apache.chemistry.opencmis.commons.enums.ChangeType;

public class CouchChange extends CouchNodeBase{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3016760183200314355L;

	private String name;
	private String baseType;
	private String objectType;
	private String versionSeriesId;
	private String versionLabel;
	private List<String> policyIds;
	private Acl acl;
	private String paretnId;
	
	private String objectId;
	private int changeToken;
	private ChangeType changeType;
	private GregorianCalendar time;
	private boolean latest;
	
	public CouchChange(){
		super();
	}
	
	public CouchChange(Change c){
		super(c);
		setObjectId(c.getObjectId());
		setChangeToken(c.getChangeToken());
		setChangeType(c.getChangeType());
		setTime(c.getCreated());
		setLatest(c.isLatest());
		setType(c.getType());
		setName(c.getName());
		setBaseType(c.getBaseType());
		setObjectType(c.getObjectType());
		setVersionSeriesId(c.getVersionSeriesId());
		setVersionLabel(c.getVersionLabel());
		setPolicyIds(c.getPolicyIds());
		setAcl(c.getAcl());
	}
	
	
	
	public String getParetnId() {
		return paretnId;
	}

	public void setParetnId(String paretnId) {
		this.paretnId = paretnId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBaseType() {
		return baseType;
	}

	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getVersionSeriesId() {
		return versionSeriesId;
	}

	public void setVersionSeriesId(String versionSeriesId) {
		this.versionSeriesId = versionSeriesId;
	}

	public String getVersionLabel() {
		return versionLabel;
	}

	public void setVersionLabel(String versionLabel) {
		this.versionLabel = versionLabel;
	}

	public List<String> getPolicyIds() {
		return policyIds;
	}

	public void setPolicyIds(List<String> policyIds) {
		this.policyIds = policyIds;
	}

	public Acl getAcl() {
		return acl;
	}

	public void setAcl(Acl acl) {
		this.acl = acl;
	}

	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	public int getChangeToken() {
		return changeToken;
	}
	public void setChangeToken(int changeToken) {
		this.changeToken = changeToken;
	}
	
	
	

	public ChangeType getChangeType() {
		return changeType;
	}
	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}
	public GregorianCalendar getTime() {
		return time;
	}
	
	public void setTime(GregorianCalendar time) {
		this.time = time;
	}
	
	public boolean isLatest() {
		return latest;
	}

	public void setLatest(boolean latest) {
		this.latest = latest;
	}

	

	public Change convert(){
		Change change = new Change(super.convert());
		change.setChangeType(getChangeType());
		change.setTime(getTime());
		change.setObjectId(getObjectId());
		change.setChangeToken(getChangeToken());
		change.setLatest(isLatest());
		change.setType(getType());
		
		change.setName(getName());
		change.setBaseType(getBaseType());
		change.setObjectType(getObjectType());
		change.setVersionSeriesId(getVersionSeriesId());
		change.setVersionLabel(getVersionLabel());
		change.setPolicyIds(getPolicyIds());
		change.setAcl(getAcl());
		return change;
	}
}