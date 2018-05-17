package util;

import javax.servlet.*;
import java.io.IOException;


/**
 * Created by Administrator on 2017/9/18.
 */
public class ChromeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String cmd="C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe http://localhost:8080/FDR";
        Runtime run =Runtime.getRuntime();
        try{
            run.exec(cmd);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
