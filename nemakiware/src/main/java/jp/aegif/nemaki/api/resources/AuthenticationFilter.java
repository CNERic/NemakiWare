package jp.aegif.nemaki.api.resources;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jp.aegif.nemaki.model.User;
import jp.aegif.nemaki.service.node.PrincipalService;
import jp.aegif.nemaki.util.PasswordHasher;

public class AuthenticationFilter implements Filter {

	private PrincipalService principalService;

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		principalService = (PrincipalService) context
				.getBean("principalService");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) req;
		HttpServletResponse hres = (HttpServletResponse) res;

		HttpSession session = hreq.getSession();

		if (session.getAttribute("USER_INFO") == null) {
			String auth = hreq.getHeader("Authorization");
			if (auth == null) {
				requireAuth(hres);
				return;
			} else {
				try {
					String decoded = decodeAuthHeader(auth);

					int pos = decoded.indexOf(":");
					String username = decoded.substring(0, pos);
					String password = decoded.substring(pos + 1);

					UserInfo user = authenticateUser(username, password);

					if (user.userId == null || user.userId.equals("")) {
						requireAuth(hres);
						return;

					} else {
						session.setAttribute("USER_INFO", user);
					}

				} catch (Exception ex) {
					requireAuth(hres);
					return;

				}
			}

		}

		chain.doFilter(req, res);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	private void requireAuth(HttpServletResponse hres) throws IOException {
		hres.setHeader("WWW-Authenticate",
				"BASIC realm=\"Authentication Test\"");
		hres.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

	@SuppressWarnings("restriction")
	private String decodeAuthHeader(String header) {
		String ret = "";

		try {
			String encStr = header.substring(6);
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			byte[] dec = decoder.decodeBuffer(encStr);
			ret = new String(dec);
		} catch (Exception ex) {
			ret = "";
		}
		return ret;
	}

	private UserInfo authenticateUser(String username, String password) {
		UserInfo u = new UserInfo();

		User user = principalService.getUserById(username);
		Boolean match = PasswordHasher.isCompared(password,
				user.getPasswordHash());
		if (match) {
			u.userId = username;
			u.password = password;
			u.roles = new String[] { "Users" };
		}
		return u;
	}

	
	public class UserInfo {
		public String userId;
		public String password;
		public String[] roles;

		public UserInfo() {
			userId = null;
			password = null;
			roles = null;
		}

		public boolean isInRole(String role) {
			for (int i = 0; i < roles.length; i++) {
				if (roles[i].equals(role)) {
					return true;
				}
			}
			return false;
		}
	}
}