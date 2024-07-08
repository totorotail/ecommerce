package com.zerobase.userapi.config.filter;

import com.zerobase.domain.config.JwtAuthenticationProvider;
import com.zerobase.domain.domain.common.UserVo;
import com.zerobase.userapi.service.customer.CustomerService;
import com.zerobase.userapi.service.seller.SellerService;
import lombok.RequiredArgsConstructor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = "/seller/*")
@RequiredArgsConstructor
public class SellerFilter implements Filter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final SellerService sellerService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("X-AUTH-TOKEN");

        if (!jwtAuthenticationProvider.validateToken(token)) {
            throw new ServletException("Invalid Access");
        }

        UserVo vo = jwtAuthenticationProvider.getUserVo(token);
        sellerService.findByIdAndEmail(vo.getId(), vo.getEmail())
                .orElseThrow(() -> new ServletException("Invalid access"));

        chain.doFilter(request, response);
    }
}
