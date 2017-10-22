package com.example.ion.restclient.models;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Response {
public static final String SUCCESSFUL_FIELD="successful";
public static final String MESSAGE_FIELD="message";
public static final String LIMIT_FIELD="limit";
public static final String RESULT_FIELD="result";

private Boolean successful;
private String message;
private Integer limit;

private List<Long>  needToDelete;
private List<Article> result;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();


/**
* No args constructor for use in serialization
*/
public Response() {
}


public Response(Boolean successful, String message, Integer limit, List<Article> result) {
super();
this.successful = successful;
this.message = message;
this.limit = limit;
this.result = result;

}


public Boolean getSuccessful() {
return successful;
}

public void setSuccessful(Boolean successful) {
this.successful = successful;
}

public String getMessage() {
return message;
}


public void setMessage(String message) {
this.message = message;
}


public Integer getLimit() {
return limit;
}


public void setLimit(Integer limit) {
this.limit = limit;
}

public List<Article> getResult() {
return result;
}


public void setResult(List<Article> result) {
this.result = result;
}
public List<Long> getNeedToDelete() {
		return needToDelete;
}

public void setNeedToDelete(List<Long> needToDelete) {
		this.needToDelete = needToDelete;
}

public Long[] getNeedToDeletePrim(){
	if(needToDelete!=null) {
		Long[] result = new Long[needToDelete.size()];
		int i = 0;
		for (Long l : needToDelete) {
			result[i++] = l;
		}
		return result;
	}else{
		return null;
	}
}




@Override
public String toString() {
	return "Response [successful=" + successful + ", message=" + message + ", limit=" + limit + ", result=" + result
			+  ", additionalProperties=" + additionalProperties + "]";
}


public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}


public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}


}