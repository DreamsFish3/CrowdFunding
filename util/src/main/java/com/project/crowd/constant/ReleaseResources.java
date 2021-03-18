package com.project.crowd.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * @description:该类为指定所有放行资源路径
 */
public class ReleaseResources {

    // 放行请求路径
    public static final Set<String> PASS_REQUEST_SET = new HashSet<>();

    static {
        PASS_REQUEST_SET.add("/");
        PASS_REQUEST_SET.add("/auth/member/to/reg/page");
        PASS_REQUEST_SET.add("/auth/do/member/register");
        PASS_REQUEST_SET.add("/auth/member/to/login/page");
        PASS_REQUEST_SET.add("/auth/member/do/login");
        PASS_REQUEST_SET.add("/auth/member/logout");
        PASS_REQUEST_SET.add("/auth/member/send/short/message.josn");
    }

    // 放行静态资源名
    private static final Set<String> STATIC_RESOURCES_SET = new HashSet<>();

    static {
        STATIC_RESOURCES_SET.add("bootstrap");
        STATIC_RESOURCES_SET.add("css");
        STATIC_RESOURCES_SET.add("fonts");
        STATIC_RESOURCES_SET.add("img");
        STATIC_RESOURCES_SET.add("jquery");
        STATIC_RESOURCES_SET.add("layer");
        STATIC_RESOURCES_SET.add("script");
        STATIC_RESOURCES_SET.add("ztree");
    }

    /**
     * 用于判断某个ServletPath值是否对应一个静态资源
     * （根据传过来的资源路径，判断路径的开头的名称是否属于放行资源名）
     * @param servletPath
     * @return
     */
    public static boolean judgeCurrentServletPathWhetherStaticResource(String servletPath) {

        // 排除字符串无效的情况
        if (servletPath == null || servletPath.length() == 0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        // 把资源路径按斜杠拆分为字符串数组（["/","aaa","/","bbb","/","bbb"]）
        String[] split = servletPath.split("/");
        // 数组的1索引就是静态资源名
        String firstLevelPath = split[1];
        // 返回判断是否包含有该静态资源的结果
        return STATIC_RESOURCES_SET.contains(firstLevelPath);
    }
}
