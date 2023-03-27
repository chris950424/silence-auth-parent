package com.silence.auth.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.silence.auth.util.EncryptUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * SecurityGlobalFilter
 *
 * @author leo
 * @version 1.1.0
 * @date 2021/12/24
 */
@Slf4j
public class SecurityGlobalFilter implements GlobalFilter, Ordered {

    private final List<String> noCheckList;
    private final String baseUrl;

    public SecurityGlobalFilter(List<String> noCheckList, String baseUrl) {
        this.noCheckList = noCheckList;
        this.baseUrl = baseUrl;
    }

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("time:" + new Date() + "\t 执行了自定义的全局过滤器: " + "MyLogGateWayFilter" + "hello");

        String requestUrl = exchange.getRequest().getPath().value();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        // 1 auth服务所有放行
        for (String a : noCheckList) {
            if (pathMatcher.match(a, requestUrl)) {
                return chain.filter(exchange);
            }
        }

        String tokenStr = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StrUtil.isBlank(tokenStr)) {
            return null;
        }

        String token = tokenStr.split(" ")[1];
        if (StrUtil.isBlank(token)) {
            return null;
        }
        // 解析JWT获取jti，以jti为key判断redis的黑名单列表是否存在，存在则拦截访问
        HttpEntity<?> entity = new HttpEntity<>(exchange.getRequest().getHeaders());

        ParameterizedTypeReference<String> myBean = new ParameterizedTypeReference<String>() {
        };

        RestTemplate restTemplate = new RestTemplate();

        // 调用zswyAuth-8850的check_token,检查token
        ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl + "/silence-auth/oauth/check_token"
                + "?token=" + token, HttpMethod.POST, entity, myBean);

        // 日志得到返回的数据
        String body = responseEntity.getBody();

        // 把String转为HashMap类型
        Map<String, List<String>> jsonObject = JSON.parseObject(body, HashMap.class);
        System.out.println(jsonObject);
        //获取凭证
        Object principal = jsonObject.get("user_name");
        // 获取用户权限
        List<String> authorities = jsonObject.get("authorities");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("principal", principal);
        jsonObject2.put("authorities", authorities);
        // 转为base64编码
        String base64 = EncryptUtil.encodeUTF8StringBase64(jsonObject2.toString());
        // 把base64加入请求头中
        ServerHttpRequest tokenRequest = exchange.getRequest().mutate().header("json-token", base64).build();
        exchange.mutate().request(tokenRequest).build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
