package org.example.simpleonlinestore.config;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Slf4j
@Component
public class RateLimitingFilter implements Filter {
    private final Bucket bucket;

    @Autowired
    public RateLimitingFilter(Bucket bucket){
       this.bucket=bucket;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
      //  Filter.super.init(filterConfig);
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Details "+request.getLocalAddr()+" "+request.getProtocol()+" "+request.getRemoteAddr());
        if(bucket.tryConsume(1)){
                chain.doFilter(request,response);
            }
            else{
                ((HttpServletResponse)response).setStatus(429);
            }
    }
    @Override
    public void destroy() {
       // Filter.super.destroy();
    }
}