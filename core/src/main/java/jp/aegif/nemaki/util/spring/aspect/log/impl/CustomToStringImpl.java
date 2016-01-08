package jp.aegif.nemaki.util.spring.aspect.log.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ObjectData;
import org.apache.chemistry.opencmis.commons.data.ObjectInFolderContainer;
import org.apache.chemistry.opencmis.commons.data.ObjectInFolderData;
import org.apache.chemistry.opencmis.commons.data.ObjectInFolderList;
import org.apache.chemistry.opencmis.commons.data.ObjectParentData;
import org.apache.chemistry.opencmis.commons.data.Properties;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ObjectInFolderListImpl;
import org.apache.chemistry.opencmis.commons.server.CallContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CustomToStringImpl {
	public String parse(Object obj){
		if(obj == null){
			return "null";
		}else if(obj instanceof ObjectData){
			return parseObjectData((ObjectData)obj);
		}else if(obj instanceof ObjectInFolderData){
			return parseObjectInFolderData((ObjectInFolderData)obj);
		}else if(obj instanceof ObjectInFolderContainer){
			return parseObjectInFolderContainer((ObjectInFolderContainer)obj);
		}else if(obj instanceof ObjectInFolderList){
			return parseObjectInFolderList((ObjectInFolderList)obj);
		}else if(obj instanceof ObjectParentData){
			return parseObjectParentData((ObjectParentData)obj);
		}else if(obj instanceof Properties){
				return parseProperties((Properties)obj);
		}else{
			return obj.toString();
		}
	}
	
	private String parseObjectData(ObjectData od){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = mapper.createObjectNode();
		json.put(PropertyIds.OBJECT_ID, od.getId());
		
		Map<String, PropertyData<?>>properties = od.getProperties().getProperties();
		convetPropertyToJson(json, PropertyIds.NAME, properties);
		convetPropertyToJson(json, PropertyIds.OBJECT_TYPE_ID, properties);
		
		return json.toString();
	}
	
	private String parseObjectInFolderData(ObjectInFolderData oifd){
		ObjectData od = ((ObjectInFolderData)oifd).getObject();
		return parseObjectData(od);
	}
	
	private String parseObjectInFolderContainer(ObjectInFolderContainer oifc){
		ObjectData od = ((ObjectInFolderContainer)oifc).getObject().getObject();
		return parseObjectData(od);
	}
	
	private String parseObjectInFolderList(ObjectInFolderList list){
		List<String> result = new ArrayList<String>();
		Iterator<ObjectInFolderData> itr = list.getObjects().iterator();
		while(itr.hasNext()){
			result.add(parseObjectInFolderData(itr.next()));
		}
		return result.toString();
	}
	
	private String parseObjectParentData(ObjectParentData opd){
		ObjectData od = ((ObjectParentData)opd).getObject();
		return parseObjectData(od);
	}
	
	private String parseProperties(Properties properties){
		Map<String, PropertyData<?>> map = properties.getProperties();
		Map<String, Object>_map = new HashMap<String, Object>();
		if(MapUtils.isNotEmpty(map)){
			for(Entry<String, PropertyData<?>> e : map.entrySet()){
				Object values = e.getValue().getValues();
				_map.put(e.getKey(), values);
			}
		}
		return _map.toString();
	}
	
	public String parseList(Object[] list){
		List<String>result = new ArrayList<String>();
		if(list == null){
			return null;
		}else{
			for(int i=0; i<list.length; i++){
				//Omit CallContext argument
				if(list[i] instanceof CallContext){
					continue;
				}
				result.add(this.parse(list[i]));
			}
			return result.toString();
		}
	}
	
	private void convetPropertyToJson(ObjectNode json, String key, Map<String, PropertyData<?>> properties){
		PropertyData<?> pd = properties.get(key);
		if(pd != null && pd.getFirstValue() != null){
			json.put(key, pd.getFirstValue().toString());
		}
	}
}