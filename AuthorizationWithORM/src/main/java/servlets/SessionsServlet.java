package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import com.google.gson.Gson;
import dbService.DBException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionsServlet extends HttpServlet {
    private final AccountService accountService;
    public SessionsServlet(AccountService accountService){
    this.accountService=accountService;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        UserProfile profile = accountService.getUserBySessionId(sessionId);
        if (profile == null){
            resp.setContentType("text/html;charset=utf-8");
        }else{
            Gson gson = new Gson();
            String json = gson.toJson(profile);
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println(json);
            resp.setStatus(HttpServletResponse.SC_OK);

        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String pass = req.getParameter("password");

        if (login == null || pass == null){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        UserProfile profile= null;
        try {
            profile = accountService.getUserByLogin(login);

        if (profile == null|| !profile.getPassword().equals(pass)){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

            accountService.addSession(req.getSession().getId(),profile);
        } catch (DBException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        String json = gson.toJson(profile);
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(json);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        UserProfile profile = accountService.getUserBySessionId(sessionId);
        if (profile==null){
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            accountService.deleteSession(sessionId);
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println("Googbye! ");
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
