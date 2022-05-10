package androidsamples.java.tictactoe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.StringTokenizer;


public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private NavController mNav;
    private String email,password;
    private DatabaseReference loginDetails;
    private String UID;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        loginDetails= FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        EditText mail = view.findViewById(R.id.edit_email);
        EditText pass = view.findViewById(R.id.edit_password);
        mNav = Navigation.findNavController(view);
        view.findViewById(R.id.btn_register).setOnClickListener(v -> {
            email=mail.getText().toString();
            password=pass.getText().toString();
            if(email.equals(""))
            {
                Toast.makeText(getContext(), "email cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else if(password.equals(""))
            {
                Toast.makeText(getContext(), "password cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else
            {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                        task -> {
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                }
                                catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                    Toast.makeText(getActivity(), "incorrect email", Toast.LENGTH_SHORT).show();
                                }
                                catch (FirebaseAuthUserCollisionException existEmail) {
                                    Toast.makeText(getActivity(), "Email already exists", Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(getActivity(),"Authentication failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                PlayerInfo playerInfo = new PlayerInfo(setUserName(email));
                                UID=mAuth.getCurrentUser().getUid();
                                loginDetails.child(UID).setValue(playerInfo);
                                Toast.makeText(getActivity(), "User registered successfully", Toast.LENGTH_SHORT).show();
                                mNav.navigate(R.id.action_login_successful);
                            }
                        }
                );
            }
        });
        view.findViewById(R.id.btn_signIn).setOnClickListener(view1 -> {
            email=mail.getText().toString();
            password=pass.getText().toString();
            if(email.equals(""))
            {
                Toast.makeText(getContext(), "email cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else if(password.equals(""))
            {
                Toast.makeText(getContext(), "password cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else{
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if(!task.isSuccessful())
                    {
                        try {
                            throw task.getException();
                        }
                        catch(FirebaseAuthInvalidUserException e1){
                            Toast.makeText(getContext(), "Please register the email first", Toast.LENGTH_SHORT).show();
                        }
                        catch(FirebaseAuthInvalidCredentialsException e){
                            Toast.makeText(getContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e) {
                            Toast.makeText(getActivity(),"Authentication failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Logged In successfully", Toast.LENGTH_SHORT).show();
                        mNav.navigate(R.id.dashboardFragment);
                    }
                });
            }
        });
    }

    private String setUserName(String email) {
        StringTokenizer st = new StringTokenizer(email, "@");
        return st.nextToken();
    }
    // No options menu in login fragment.
}