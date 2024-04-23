package springboot.Interceptor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import springboot.utility.JWTUtils;

public class JWTInterceptor implements HandlerInterceptor {
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Map<String,Object> map = new HashMap<>();
		try {
			DecodedJWT decoded = JWTUtils.verify(request.getParameter("token"));
			String userId = decoded.getClaim("userId").asString();
			request.setAttribute("admin", userId == "admin");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "token verification failed");
		}
		
		map.put("state", false);
		String json = new ObjectMapper().writeValueAsString(map);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().println(json);
		return false;
	}

}
