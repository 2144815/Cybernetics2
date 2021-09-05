package com.example.witsonline;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
public class LoginActivityTest {
    //Mocks for ValidateInstructorUserName
    TextInputLayout InstructorTextLayout_userDoesNotExist= Mockito.mock(TextInputLayout.class);
    private final EditText InstructorEditText_userDoesNotExist=Mockito.mock(EditText.class);
    private final TextInputLayout InstructorTextLayout_empty=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorEditText_empty=Mockito.mock(EditText.class);
    private final TextInputLayout InstructorTextLayout_tooLong=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorEditText_tooLong=Mockito.mock(EditText.class);
    private final TextInputLayout InstructorTextLayout_valid=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorEditText_valid=Mockito.mock(EditText.class);

    //Mocks for ValidateInstructorPassword
    private final TextInputLayout InstructorPasswordTextLayout_empty=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorPasswordEditText_empty=Mockito.mock(EditText.class);
    private final TextInputLayout InstructorUserTextLayout_empty=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorUserEditText_empty=Mockito.mock(EditText.class);
    private final TextInputLayout InstructorPasswordTextLayout_valid=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorPasswordEditText_valid=Mockito.mock(EditText.class);
    private final TextInputLayout InstructorUserTextLayout_valid=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorUserEditText_valid=Mockito.mock(EditText.class);
    private final TextInputLayout InstructorPasswordTextLayout_wrong=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorPasswordEditText_wrong=Mockito.mock(EditText.class);
    private final TextInputLayout InstructorUserTextLayout_wrong=Mockito.mock(TextInputLayout.class);
    private final EditText InstructorUserEditText_wrong=Mockito.mock(EditText.class);

    //Mocks for ValidateStudentUserName
    private final TextInputLayout StudentTextLayout_userDoesNotExist=Mockito.mock(TextInputLayout.class);
    private final EditText StudentEditText_userDoesNotExist=Mockito.mock(EditText.class);
    private final TextInputLayout StudentTextLayout_empty=Mockito.mock(TextInputLayout.class);
    private final EditText StudentEditText_empty=Mockito.mock(EditText.class);
    private final TextInputLayout StudentTextLayout_tooLong=Mockito.mock(TextInputLayout.class);
    private final EditText StudentEditText_tooLong=Mockito.mock(EditText.class);
    private final TextInputLayout StudentTextLayout_valid=Mockito.mock(TextInputLayout.class);
    private final EditText StudentEditText_valid=Mockito.mock(EditText.class);

    //Mocks for ValidateStudentPassword
    private final TextInputLayout StudentPasswordTextLayout_empty=Mockito.mock(TextInputLayout.class);
    private final EditText StudentPasswordEditText_empty=Mockito.mock(EditText.class);
    private final TextInputLayout StudentUserTextLayout_empty=Mockito.mock(TextInputLayout.class);
    private final EditText StudentUserEditText_empty=Mockito.mock(EditText.class);
    private final TextInputLayout StudentPasswordTextLayout_valid=Mockito.mock(TextInputLayout.class);
    private final EditText StudentPasswordEditText_valid=Mockito.mock(EditText.class);
    private final TextInputLayout StudentUserTextLayout_valid=Mockito.mock(TextInputLayout.class);
    private final EditText StudentUserEditText_valid=Mockito.mock(EditText.class);
    private final TextInputLayout StudentPasswordTextLayout_wrong=Mockito.mock(TextInputLayout.class);
    private final EditText StudentPasswordEditText_wrong=Mockito.mock(EditText.class);
    private final TextInputLayout StudentUserTextLayout_wrong=Mockito.mock(TextInputLayout.class);
    private final EditText StudentUserEditText_wrong=Mockito.mock(EditText.class);



    @Before
    public void init(){
        //SetUP for ValidateInstructorUserName
        Mockito.when(InstructorTextLayout_userDoesNotExist.getEditText()).thenReturn(InstructorEditText_userDoesNotExist);
        Mockito.when(InstructorEditText_userDoesNotExist.getText()).thenReturn(new MockEditable("test"));
        Mockito.when(InstructorTextLayout_empty.getEditText()).thenReturn(InstructorEditText_empty);
        Mockito.when(InstructorEditText_empty.getText()).thenReturn(new MockEditable(""));
        Mockito.when(InstructorTextLayout_tooLong.getEditText()).thenReturn(InstructorEditText_tooLong);
        Mockito.when(InstructorEditText_tooLong.getText()).thenReturn(new MockEditable("012345678901234567890"));
        Mockito.when(InstructorTextLayout_valid.getEditText()).thenReturn(InstructorEditText_valid);
        Mockito.when(InstructorEditText_valid.getText()).thenReturn(new MockEditable("test"));

        //SetUP for ValidateInstructorPassword
        Mockito.when(InstructorPasswordTextLayout_empty.getEditText()).thenReturn(InstructorPasswordEditText_empty);
        Mockito.when(InstructorPasswordEditText_empty.getText()).thenReturn(new MockEditable(""));
        Mockito.when(InstructorUserTextLayout_empty.getEditText()).thenReturn(InstructorUserEditText_empty);
        Mockito.when(InstructorUserEditText_empty.getText()).thenReturn(new MockEditable("test"));
        Mockito.when(InstructorPasswordTextLayout_valid.getEditText()).thenReturn(InstructorPasswordEditText_valid);
        Mockito.when(InstructorPasswordEditText_valid.getText()).thenReturn(new MockEditable("password"));
        Mockito.when(InstructorUserTextLayout_valid.getEditText()).thenReturn(InstructorUserEditText_valid);
        Mockito.when(InstructorUserEditText_valid.getText()).thenReturn(new MockEditable("test"));
        Mockito.when(InstructorPasswordTextLayout_wrong.getEditText()).thenReturn(InstructorPasswordEditText_wrong);
        Mockito.when(InstructorPasswordEditText_wrong.getText()).thenReturn(new MockEditable("password2"));
        Mockito.when(InstructorUserTextLayout_wrong.getEditText()).thenReturn(InstructorUserEditText_wrong);
        Mockito.when(InstructorUserEditText_wrong.getText()).thenReturn(new MockEditable("test"));

        //SetUP for ValidateStudentUserName
        Mockito.when(StudentTextLayout_userDoesNotExist.getEditText()).thenReturn(StudentEditText_userDoesNotExist);
        Mockito.when(StudentEditText_userDoesNotExist.getText()).thenReturn(new MockEditable("test"));
        Mockito.when(StudentTextLayout_empty.getEditText()).thenReturn(StudentEditText_empty);
        Mockito.when(StudentEditText_empty.getText()).thenReturn(new MockEditable(""));
        Mockito.when(StudentTextLayout_tooLong.getEditText()).thenReturn(StudentEditText_tooLong);
        Mockito.when(StudentEditText_tooLong.getText()).thenReturn(new MockEditable("012345678901234567890"));
        Mockito.when(StudentTextLayout_valid.getEditText()).thenReturn(StudentEditText_valid);
        Mockito.when(StudentEditText_valid.getText()).thenReturn(new MockEditable("test"));

        Mockito.when(StudentPasswordTextLayout_empty.getEditText()).thenReturn(StudentPasswordEditText_empty);
        Mockito.when(StudentPasswordEditText_empty.getText()).thenReturn(new MockEditable(""));
        Mockito.when(StudentUserTextLayout_empty.getEditText()).thenReturn(StudentUserEditText_empty);
        Mockito.when(StudentUserEditText_empty.getText()).thenReturn(new MockEditable("test"));
        Mockito.when(StudentPasswordTextLayout_valid.getEditText()).thenReturn(StudentPasswordEditText_valid);
        Mockito.when(StudentPasswordEditText_valid.getText()).thenReturn(new MockEditable("password"));
        Mockito.when(StudentUserTextLayout_valid.getEditText()).thenReturn(StudentUserEditText_valid);
        Mockito.when(StudentUserEditText_valid.getText()).thenReturn(new MockEditable("test"));
        Mockito.when(StudentPasswordTextLayout_wrong.getEditText()).thenReturn(StudentPasswordEditText_wrong);
        Mockito.when(StudentPasswordEditText_wrong.getText()).thenReturn(new MockEditable("password2"));
        Mockito.when(StudentUserTextLayout_wrong.getEditText()).thenReturn(StudentUserEditText_wrong);
        Mockito.when(StudentUserEditText_wrong.getText()).thenReturn(new MockEditable("test"));





    }


    @Test
    public void validateInstructorUsername_valid() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> instDetails = new HashMap<>();
        instDetails.put("test","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateInstructorUsername(InstructorTextLayout_valid,instDetails);
        assertEquals(true,output);
    }
    @Test
    public void validateInstructorUsername_empty() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> instDetails = new HashMap<>();
        instDetails.put("test","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateInstructorUsername(InstructorTextLayout_empty,instDetails);
        assertEquals(false,output);
    }
    @Test
    public void validateInstructorUsername_userExists() {
        LoginActivity temp = new LoginActivity();
        ArrayList<ArrayList<String>> instructor = new ArrayList<>();
        HashMap<String, String> instDetails = new HashMap<>();
        instDetails.put("test2","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateInstructorUsername(InstructorTextLayout_userDoesNotExist,instDetails);
        assertEquals(false,output);
    }
    @Test
    public void validateInstructorUsername_tooLong() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> instDetails = new HashMap<>();
        instDetails.put("test","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateInstructorUsername(InstructorTextLayout_tooLong,instDetails);
        assertEquals(false,output);
    }


    @Test
    public void validateInstructorPassword_valid() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> instDetails = new HashMap<>();
        instDetails.put("test","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateInstructorPassword(InstructorPasswordTextLayout_valid,InstructorUserTextLayout_valid,instDetails);
        assertEquals(true,output);
    }
    @Test
    public void validateInstructorPassword_empty() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> instDetails = new HashMap<>();
        instDetails.put("test","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateInstructorPassword(InstructorPasswordTextLayout_empty,InstructorUserTextLayout_empty,instDetails);
        assertEquals(false,output);
    }
    @Test
    public void validateInstructorPassword_WrongPassword() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> instDetails = new HashMap<>();
        instDetails.put("test","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateInstructorPassword(InstructorPasswordTextLayout_wrong,InstructorUserTextLayout_wrong,instDetails);
        assertEquals(false,output);
    }
    @Test
    public void validateInstructorPassword_wrongIndex() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> instDetails = new HashMap<>();
        instDetails.put("test1","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateInstructorPassword(InstructorPasswordTextLayout_valid,InstructorUserTextLayout_valid,instDetails);
        assertEquals(false,output);
    }


    @Test
    public void validateStudentUsername_valid() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> studentDetails = new HashMap<>();
        studentDetails.put("test","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateStudentUsername(StudentTextLayout_valid,studentDetails);
        assertEquals(true,output);
    }
    @Test
    public void validateStudentUsername_empty() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> studentDetails = new HashMap<>();
        studentDetails.put("test","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateStudentUsername(StudentTextLayout_empty,studentDetails);
        assertEquals(false,output);
    }
    @Test
    public void validateStudentUsername_userExists() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> studentDetails = new HashMap<>();
        studentDetails.put("test2","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateStudentUsername(StudentTextLayout_userDoesNotExist,studentDetails);
        assertEquals(false,output);
    }
    @Test
    public void validateStudentUsername_tooLong() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> studentDetails = new HashMap<>();
        studentDetails.put("test","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateStudentUsername(StudentTextLayout_tooLong,studentDetails);
        assertEquals(false,output);
    }


    @Test
    public void validateStudentPassword_valid() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> studentDetails = new HashMap<>();
        studentDetails.put("test","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateStudentPassword(StudentPasswordTextLayout_valid,StudentUserTextLayout_valid,studentDetails);
        assertEquals(true,output);
    }
    @Test
    public void validateStudentPassword_empty() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> studentDetails = new HashMap<>();
        studentDetails.put("test","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateStudentPassword(StudentPasswordTextLayout_empty,StudentUserTextLayout_empty,studentDetails);
        assertEquals(false,output);
    }
    @Test
    public void validateStudentPassword_WrongPassword() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> studentDetails = new HashMap<>();
        studentDetails.put("test","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateStudentPassword(StudentPasswordTextLayout_wrong,StudentUserTextLayout_wrong,studentDetails);
        assertEquals(false,output);
    }
    @Test
    public void validateStudentPassword_wrongIndex() {
        LoginActivity temp = new LoginActivity();
        HashMap<String, String> studentDetails = new HashMap<>();
        studentDetails.put("test2","5f4dcc3b5aa765d61d8327deb882cf99");
        Boolean output=temp.validateStudentPassword(StudentPasswordTextLayout_valid,StudentUserTextLayout_valid,studentDetails);
        assertEquals(false,output);
    }


    @Test
    public void TestGetSrudentLogin(){
        LoginActivity login = Mockito.spy(new LoginActivity());
        String json="[{Student_Number:test,Student_Password:test}]";
        ArrayList<String> temp;
        login.getStudentLogin(json);
        temp=login.getStudentNumbers();
        assertNotNull(temp);
    }

    @Test
    public void TestGetInstructorLogin(){
        LoginActivity login = Mockito.spy(new LoginActivity());
        String json="[{Instructor_Username:test,Instructor_Password:test}]";
        ArrayList<String> temp;
        login.getInstructorLogin(json);
        temp=login.getInstructorUsernames();
        assertNotNull(temp);
    }

}