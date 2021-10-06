package entities;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SchoolTest {
    private Student paul;
    private School system;
    @BeforeEach
    public void setUp(){
        paul = new Student();
        paul.setFirstName("paul");
        paul.setLastName("ajegunle");
        paul.setGender("male");
        paul.setDateOfBirth("10/10/2010");

    }
    @AfterEach
    public void tearDown(){

    }
    @Test
    public void schoolHasStudent(){
        assertNotNull(paul);

    }
}