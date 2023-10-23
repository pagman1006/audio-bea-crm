package com.audiobea.crm.app.controller;

import static com.audiobea.crm.app.utils.Constants.XS_NAMESPACE;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.jvnet.ws.wadl.Application;
import org.jvnet.ws.wadl.Doc;
import org.jvnet.ws.wadl.Param;
import org.jvnet.ws.wadl.ParamStyle;
import org.jvnet.ws.wadl.Representation;
import org.jvnet.ws.wadl.Request;
import org.jvnet.ws.wadl.Resource;
import org.jvnet.ws.wadl.Resources;
import org.jvnet.ws.wadl.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Controller
@RequestMapping("/v1/audio-bea/wadl")
public class WADLController {
	@Autowired
	private RequestMappingHandlerMapping handlerMapping;
	@Autowired
	private WebApplicationContext webApplicationContext;

	@GetMapping(produces = { "application/xml" })
	public @ResponseBody Application generateWadl(HttpServletRequest request) {
		Application result = new Application();
		Doc doc = new Doc();
		doc.setTitle("API REST AUDIO-BEA WADL");
		result.getDoc().add(doc);
		Resources wadResources = new Resources();
		wadResources.setBase(getBaseUrl(request));
		Map<RequestMappingInfo, HandlerMethod> handledMethods = handlerMapping.getHandlerMethods();
		wadResources = setupWadResourcesByHandleMethods(wadResources, handledMethods);
		wadResources.getResource().sort((a, b) -> a.getPath().compareTo(b.getPath()));
		result.getResources().add(wadResources);
		return result;
	}

	private Resources setupWadResourcesByHandleMethods(Resources wadResources, Map<RequestMappingInfo, HandlerMethod> handledMethods) {
		Set<String> pattern = new HashSet<>();
		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handledMethods.entrySet()) {
			HandlerMethod handlerMethod = entry.getValue();
			Object object = handlerMethod.getBean();
			Object bean = webApplicationContext.getBean(object.toString());

			boolean isRestContoller = bean.getClass().isAnnotationPresent(RestController.class);
			if (!isRestContoller) {
				continue;
			}
			RequestMappingInfo mappingInfo = entry.getKey();

			@SuppressWarnings("null")
			String uri = mappingInfo.getPathPatternsCondition().getPatterns().toString();
			pattern.add(uri);
			Set<RequestMethod> httpMethods = mappingInfo.getMethodsCondition().getMethods();
			ProducesRequestCondition producesRequestCondition = mappingInfo.getProducesCondition();
			Set<MediaType> mediaTypes = producesRequestCondition.getProducibleMediaTypes();
			Resource wadlResource = null;

			for (RequestMethod httpMethod : httpMethods) {
				wadlResource = createOrFind(uri, wadResources);
				Method javaMethod = handlerMethod.getMethod();
				org.jvnet.ws.wadl.Method wadlMethod = setWadlMethod(httpMethod, javaMethod);
				// Request
				Request wadlRequest = setWadlRequest(javaMethod.getParameterAnnotations(), javaMethod.getParameterTypes());
				if (!wadlRequest.getParam().isEmpty()) {
					wadlMethod.setRequest(wadlRequest);
				}
				// Response
				if (!mediaTypes.isEmpty()) {
					wadlMethod.getResponse().add(setWadlResponse(handlerMethod, mediaTypes));
				}
				wadlResource.getMethodOrResource().add(wadlMethod);
			}
		}
		return wadResources;
	}

	private org.jvnet.ws.wadl.Method setWadlMethod(RequestMethod httpMethod, Method javaMethod) {
		org.jvnet.ws.wadl.Method wadlMethod = new org.jvnet.ws.wadl.Method();
		wadlMethod.setName(httpMethod.name());
		wadlMethod.setId(javaMethod.getName());

		Doc wadlDocMethod = new Doc();
		wadlDocMethod.setTitle(javaMethod.getDeclaringClass().getSimpleName() + "." + javaMethod.getName());

		wadlMethod.getDoc().add(wadlDocMethod);
		return wadlMethod;
	}

	private Request setWadlRequest(Annotation[][] annotations, Class<?>[] paramTypes) {
		Request wadlRequest = new Request();
		int i = 0;
		for (Annotation[] annotation : annotations) {
			Class<?> paramType = paramTypes[i];
			i++;
			for (Annotation annotation2 : annotation) {
				if (annotation2 instanceof RequestParam) {
					wadlRequest.getParam().add(setRequestParam(annotation2, paramType));
				} else if (annotation2 instanceof PathVariable) {
					wadlRequest.getParam().add(setPathVariable(annotation2, paramType));
				}
			}
		}
		return wadlRequest;
	}

	private Param setPathVariable(Annotation annotation, Class<?> paramType) {
		Param waldParam = new Param();
		PathVariable param = (PathVariable) annotation;
		QName nm = convertJavaToXMLType(paramType);

		waldParam.setName(param.value());
		waldParam.setStyle(ParamStyle.TEMPLATE);
		waldParam.setRequired(true);
		waldParam.setType(nm);
		return waldParam;
	}

	private Param setRequestParam(Annotation annotation, Class<?> paramType) {
		Param waldParam = new Param();
		RequestParam param = (RequestParam) annotation;

		QName nm = convertJavaToXMLType(paramType);
		waldParam.setName(param.name());
		waldParam.setStyle(ParamStyle.QUERY);
		waldParam.setRequired(param.required());
		String defaultValue = cleanDefault(param.defaultValue());
		if (StringUtils.isNotBlank(defaultValue)) {
			waldParam.setDefault(defaultValue);
		}
		waldParam.setType(nm);
		return waldParam;
	}

	private Response setWadlResponse(HandlerMethod handlerMethod, Set<MediaType> mediaTypes) {
		Response wadlResponse = new Response();
		ResponseStatus status = handlerMethod.getMethodAnnotation(ResponseStatus.class);
		if (status == null) {
			wadlResponse.getStatus().add((long) (HttpStatus.OK.value()));
		} else {
			HttpStatus httpcode = status.value();
			wadlResponse.getStatus().add((long) httpcode.value());
		}
		wadlResponse.getRepresentation().addAll(setRepresentations(mediaTypes));
		return wadlResponse;
	}

	private List<Representation> setRepresentations(Set<MediaType> mediaTypes) {
		if (mediaTypes.isEmpty()) {
			return Collections.emptyList();
		}
		List<Representation> representation = new ArrayList<>();
		for (MediaType mediaType : mediaTypes) {
			Representation wadlRepresentation = new Representation();
			wadlRepresentation.setMediaType(mediaType.toString());
			representation.add(wadlRepresentation);
		}
		return representation;
	}

	private QName convertJavaToXMLType(Class<?> type) {
		QName nm = new QName("");
		String classname = type.toString();
		if (classname.indexOf("String") >= 0) {
			nm = new QName(XS_NAMESPACE, "string", "xs");

		} else if (classname.indexOf("Integer") >= 0) {
			nm = new QName(XS_NAMESPACE, "int", "xs");
		}
		return nm;
	}

	private Resource createOrFind(String uri, Resources wadResources) {
		List<Resource> current = wadResources.getResource();
		for (Resource resource : current) {
			if (resource.getPath().equalsIgnoreCase(uri)) {
				return resource;
			}
		}
		Resource wadlResource = new Resource();
		wadlResource.setPath(uri);
		current.add(wadlResource);
		return wadlResource;
	}

	private String getBaseUrl(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + requestUri;
	}

	private String cleanDefault(String value) {
		value = value.replace("\t", "");
		value = value.replace("\n", "");
		return value;
	}

}
