package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import dbService.DBException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String pass = req.getParameter("password");
      //  String email = req.getParameter("email");
        if ((login.isEmpty()) || (pass.isEmpty())) {
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("User writed empty login or password");

        } else {
            try {
                if (!checkCredo(login, pass, accountService)) {
                    accountService.addNewUser(new UserProfile(login, pass,"testMail@.com"));
                    resp.setContentType("text/html;charset=utf-8");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().println("You are registered");
                } else {
                    resp.setContentType("text/html;charset=utf-8");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().println("User with login " + login + " already exits");
                }
            } catch (DBException e) {
                e.printStackTrace();
            }
        }

    }

    private static boolean checkCredo(String login, String pass, AccountService accountService) throws DBException {
        UserProfile userProfile = accountService.getUserByLogin(login);
        if (userProfile == null) return false;
        return pass.equals(userProfile.getPassword());
    }
}
