package sbs.repository;
// http://www.codesenior.com/en/tutorial/Spring-Generic-DAO-and-Generic-Service-Implementation
import java.util.List;

public interface JdbcAdrOptimaRepository  {
    public List<String> findAllUsers();
}
