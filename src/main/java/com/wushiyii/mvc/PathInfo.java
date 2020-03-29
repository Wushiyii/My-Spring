package com.wushiyii.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wgq
 * @date 2020/3/29 11:16 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathInfo {

    /**
     * http方法
     */
    private String requestMethod;

    /**
     * http路径
     */
    private String requestPath;

}
