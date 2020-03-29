package com.wushiyii.mvc;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wgq
 * @date 2020/3/29 11:08 下午
 */
@Setter
@Getter
public class ModelAndView {

    private String view;

    private Map<String, Object> model = new HashMap<>();

    public ModelAndView addObject(String attributeName, Object object) {
        model.put(attributeName, object);
        return this;
    }

    public ModelAndView addAllObjects(Map<String, ?> modelMap) {
        model.putAll(modelMap);
        return this;
    }

}
