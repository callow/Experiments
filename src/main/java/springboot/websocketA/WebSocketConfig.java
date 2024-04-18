package springboot.websocketA;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Configuration  
@EnableWebSocket  
public class WebSocketConfig implements WebSocketConfigurer {
	
    @Override  
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(orderHandler(),"/orderWSServer").addInterceptors(new OrderInterceptor()).setAllowedOrigins("*");
        System.out.println("Websocket example: " + "ws://127.0.0.1:5090/orderWSServer?userCode=123");
    }  
   
    @Bean  
    public TextWebSocketHandler orderHandler(){
        return new OrderHandler();  
    }  
}
