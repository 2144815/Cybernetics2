package com.example.witsonline;

import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
public class EditProfileTest {
    //isEmpty Mocks
    private final TextInputLayout isEmptyTextLayout_empty= Mockito.mock(TextInputLayout.class);
    private final EditText isEmptyEditText_empty=Mockito.mock(EditText.class);

    private final TextInputLayout isEmptyTextLayout_notempty=Mockito.mock(TextInputLayout.class);;
    private final EditText isEmptyEditText_notempty=Mockito.mock(EditText.class);

    //ValidatePassword Mocks
    private final TextInputLayout PasswordTextLayout_validInput=Mockito.mock(TextInputLayout.class);
    private final EditText PasswordEditText_validInput=Mockito.mock(EditText.class);

    private final TextInputLayout ConfirmPasswordTextLayout_validInput=Mockito.mock(TextInputLayout.class);
    private final EditText ConfirmPasswordEditText_validInput=Mockito.mock(EditText.class);

    private final TextInputLayout PasswordTextLayout_short=Mockito.mock(TextInputLayout.class);
    private final EditText PasswordEditText_short=Mockito.mock(EditText.class);

    private final TextInputLayout ConfirmPasswordTextLayout_short=Mockito.mock(TextInputLayout.class);
    private final EditText ConfirmPasswordEditText_short=Mockito.mock(EditText.class);

    private final TextInputLayout PasswordTextLayout_notMatching=Mockito.mock(TextInputLayout.class);
    private final EditText PasswordEditText_notMatching=Mockito.mock(EditText.class);

    private final TextInputLayout ConfirmPasswordTextLayout_notMatching=Mockito.mock(TextInputLayout.class);
    private final EditText ConfirmPasswordEditText_notMatching=Mockito.mock(EditText.class);

    private final TextInputLayout ConfirmPasswordTextLayout_empty=Mockito.mock(TextInputLayout.class);
    private final EditText ConfirmPasswordEditText_empty=Mockito.mock(EditText.class);

    private final TextInputLayout ConfirmPasswordTextLayout_emptyConfirm=Mockito.mock(TextInputLayout.class);
    private final EditText ConfirmPasswordEditText_emptyConfirm=Mockito.mock(EditText.class);

    //ValidateEmail Mocks
    private final TextInputLayout EmailTextLayout_validInput=Mockito.mock(TextInputLayout.class);
    private final EditText EmailEditText_validInput=Mockito.mock(EditText.class);

    private final TextInputLayout EmailTextLayout_EmptyInput=Mockito.mock(TextInputLayout.class);
    private final EditText EmailEditText_EmptyInput=Mockito.mock(EditText.class);

    private final TextInputLayout EmailTextLayout_BadPattern=Mockito.mock(TextInputLayout.class);
    private final EditText EmailEditText_BadPattern=Mockito.mock(EditText.class);

    //Mocks for is last Item Displaying when Not empty
    RecyclerView recyclerViewTest_NotEmpty= Mockito.mock(RecyclerView.class);
    RecyclerView.Adapter adapter_NotEmpty=Mockito.mock((RecyclerView.Adapter.class));
    LinearLayoutManager linearLayoutManager_NotEmpty=Mockito.mock(LinearLayoutManager.class);

    //Mocks for is last Item Displaying when empty
    RecyclerView recyclerViewTest_Empty= Mockito.mock(RecyclerView.class);
    RecyclerView.Adapter adapter_Empty=Mockito.mock((RecyclerView.Adapter.class));

    @Before
    public void init(){
        // setUP for isEmpty tests
        Mockito.when(isEmptyTextLayout_empty.getEditText()).thenReturn(isEmptyEditText_empty);
        Mockito.when(isEmptyTextLayout_notempty.getEditText()).thenReturn(isEmptyEditText_notempty);
        Mockito.when(isEmptyEditText_empty.getText()).thenReturn(new MockEditable(""));
        Mockito.when(isEmptyEditText_notempty.getText()).thenReturn(new MockEditable("123"));

        //setUP for Validate Password Tests
        Mockito.when(PasswordTextLayout_validInput.getEditText()).thenReturn(PasswordEditText_validInput);
        Mockito.when(PasswordEditText_validInput.getText()).thenReturn(new MockEditable("TestPassword"));
        Mockito.when(ConfirmPasswordTextLayout_validInput.getEditText()).thenReturn(ConfirmPasswordEditText_validInput);
        Mockito.when(ConfirmPasswordEditText_validInput.getText()).thenReturn(new MockEditable("TestPassword"));
        Mockito.when(PasswordTextLayout_short.getEditText()).thenReturn(PasswordEditText_short);
        Mockito.when(PasswordEditText_short.getText()).thenReturn(new MockEditable("Test"));
        Mockito.when(ConfirmPasswordTextLayout_short.getEditText()).thenReturn(ConfirmPasswordEditText_short);
        Mockito.when(ConfirmPasswordEditText_short.getText()).thenReturn(new MockEditable("Test"));
        Mockito.when(PasswordTextLayout_notMatching.getEditText()).thenReturn(PasswordEditText_notMatching);
        Mockito.when(PasswordEditText_notMatching.getText()).thenReturn(new MockEditable("TestPassword"));
        Mockito.when(ConfirmPasswordTextLayout_notMatching.getEditText()).thenReturn(ConfirmPasswordEditText_notMatching);
        Mockito.when(ConfirmPasswordEditText_notMatching.getText()).thenReturn(new MockEditable("TestNotMatching"));
        Mockito.when(ConfirmPasswordTextLayout_empty.getEditText()).thenReturn(ConfirmPasswordEditText_empty);
        Mockito.when(ConfirmPasswordEditText_empty.getText()).thenReturn(new MockEditable(""));
        Mockito.when(ConfirmPasswordTextLayout_emptyConfirm.getEditText()).thenReturn(ConfirmPasswordEditText_emptyConfirm);
        Mockito.when(ConfirmPasswordEditText_emptyConfirm.getText()).thenReturn(new MockEditable(""));

        //setUp for ValidateEmail Tests
        Mockito.when(EmailTextLayout_validInput.getEditText()).thenReturn(EmailEditText_validInput);
        Mockito.when(EmailEditText_validInput.getText()).thenReturn(new MockEditable("Test@gmail.com"));
        Mockito.when(EmailTextLayout_EmptyInput.getEditText()).thenReturn(EmailEditText_EmptyInput);
        Mockito.when(EmailEditText_EmptyInput.getText()).thenReturn(new MockEditable(""));
        Mockito.when(EmailTextLayout_BadPattern.getEditText()).thenReturn(EmailEditText_BadPattern);
        Mockito.when(EmailEditText_BadPattern.getText()).thenReturn(new MockEditable("Test@@.gmail..com"));

        //set up for last item displaying when not empty
        Mockito.when(recyclerViewTest_NotEmpty.getAdapter()).thenReturn(adapter_NotEmpty);
        Mockito.when(adapter_NotEmpty.getItemCount()).thenReturn(1);
        Mockito.when(recyclerViewTest_NotEmpty.getLayoutManager()).thenReturn(linearLayoutManager_NotEmpty);


        //set up for last item displaying when not empty
        Mockito.when(recyclerViewTest_Empty.getAdapter()).thenReturn(adapter_Empty);
        Mockito.when(adapter_Empty.getItemCount()).thenReturn(0);

    }
    @Test
    public void isEmpty_EmptyInput() {
        EditProfile temp = new EditProfile();
        Boolean output=temp.isEmpty(isEmptyTextLayout_empty);
        assertEquals(true,output);
    }
    @Test
    public void isEmpty_NonEmptyInput() {
        EditProfile temp = new EditProfile();
        Boolean output=temp.isEmpty(isEmptyTextLayout_notempty);
        assertEquals(false,output);
    }

    @Test
    public void validatePassword_withValidInput() {
        EditProfile temp = new EditProfile();
        Boolean output=temp.validPassword(PasswordTextLayout_validInput,ConfirmPasswordTextLayout_validInput);
        assertEquals(true,output);
    }
    @Test
    public void validatePassword_withShortInput() {
        EditProfile temp = new EditProfile();
        Boolean output=temp.validPassword(PasswordTextLayout_short,ConfirmPasswordTextLayout_short);
        assertEquals(false,output);
    }
    @Test
    public void validatePassword_withNotMatchingInput() {
        EditProfile temp = new EditProfile();
        Boolean output=temp.validPassword(PasswordTextLayout_notMatching,ConfirmPasswordTextLayout_notMatching);
        assertEquals(false,output);
    }
    @Test
    public void validatePassword_emptyInput() {
        EditProfile temp = new EditProfile();
        Boolean output=temp.validPassword(ConfirmPasswordTextLayout_empty,ConfirmPasswordTextLayout_emptyConfirm);
        assertEquals(false,output);
    }

    @Test
    public void validatePassword_emptyConfirm() {
        EditProfile temp = new EditProfile();
        Boolean output=temp.validPassword(PasswordTextLayout_notMatching,ConfirmPasswordTextLayout_emptyConfirm);
        assertEquals(false,output);
    }

    @Test
    public void validateEmail_withValidInput() {
        EditProfile temp = new EditProfile();
        Boolean output=temp.validEmail(EmailTextLayout_validInput);
        assertEquals(true,output);
    }
    @Test
    public void validateEmail_withEmptyInput() {
        EditProfile temp = new EditProfile();
        Boolean output=temp.validEmail(EmailTextLayout_EmptyInput);
        assertEquals(false,output);
    }
    @Test
    public void validateEmail_withNonMatchingPattern() {
        EditProfile temp = new EditProfile();
        Boolean output=temp.validEmail(EmailTextLayout_BadPattern);
        assertEquals(false,output);
    }

    @Test
    public void isLastItemDisplaying_empty() {
        EditProfile temp = new EditProfile();
        Boolean output=temp.isLastItemDistplaying(recyclerViewTest_Empty);
        assertEquals(false,output);
    }
    @Test
    public void isLastItemDisplaying_Nonempty() {
        EditProfile temp = new EditProfile();
        Boolean output=temp.isLastItemDistplaying(recyclerViewTest_NotEmpty);
        assertEquals(true,output);
    }
}