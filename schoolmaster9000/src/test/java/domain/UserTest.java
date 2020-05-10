/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import ollikkala.schoolmaster9000.domain.User;
import org.junit.Test;

/**
 *
 * @author anttiollikkala
 */
public class UserTest {

    @Test
    public void testUserToStringIsCorrect() {
        User user = new User(1,"a","b","c");
        assert(user.getId() == 1);
        assert(user.toString().equals("a b"));
    }
    
}
